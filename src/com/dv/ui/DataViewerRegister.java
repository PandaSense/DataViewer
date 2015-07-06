/*
 * DataViewerRegister.java  2/6/13 1:04 PM
 *
 * Copyright (C) 2012-2013 Nick Ma
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.dv.ui;

import com.dv.dbinstance.DVServerInstance;
import com.dv.prop.DVPropMain;
import com.dv.ui.component.ExceptionErrorDialog;
import com.dv.ui.frequent.DVRecentRecordProcess;
import com.dv.util.DVDbUtil;
import com.dv.util.DVLOG;
import com.dv.util.DataViewerUtilities;
import com.dv.util.DvConnectFactory;
import com.dv.xml.DVDatabaseXmlSet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Nick
 */
public class DataViewerRegister extends JDialog implements ActionListener,
        ListSelectionListener {
    DVFrame parent;
    DVDatabaseXmlSet xml;
    Vector listV;
    Connection conn = null;
    DVDbUtil dvdb = new DVDbUtil();

    ExceptionErrorDialog errordialog;

    public DataViewerRegister(DVFrame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        getAllInstanceNames();
        initComponents();
        setInit(0);
    }

    public DataViewerRegister(DVFrame parent, boolean modal, int index) {
        super(parent, modal);
        this.parent = parent;
        getAllInstanceNames();
        initComponents();
        setInit(index);
    }

    public DataViewerRegister(DVFrame parent, boolean modal, String instanceName) {
        super(parent, modal);
        this.parent = parent;
        getAllInstanceNames();
        initComponents();
        setInit(getIndexByInstanceName(instanceName));
    }

    public int getIndexByInstanceName(String instanceName) {
        for (int i = 0; i < listV.size(); i++) {
            if (listV.elementAt(i).toString().trim().equals(instanceName)) {
                return i;
            }
        }
        return 0;
    }

    public void getAllInstanceNames() {
        setDatabaseInstance();
        listV = new Vector();
        listV = xml.getDVRegServerNameList();
    }

    public void setInit(int index) {
        buildInterface(index);
        Point oh = parent.getLocationOnScreen();
        setLocation((int) oh.getX() + parent.getWidth() / 2 - getWidth() / 2,
                (int) oh.getY() + parent.getHeight() / 2 - getHeight() / 2);
        setResizable(true);
        setVisible(true);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        DataViewerUtilities.scheduleGC();
    }

    public void setDatabaseInstance() {
        xml = new DVDatabaseXmlSet(DVPropMain.DV_CONFIG_FOLDER
                + "dvdatabase.xml");
        xml.setDBInstance();
    }

    public void setEncryptedPassWord() {
        if (DVPropMain.DV_ENCRYPTED_DATABASE_PASSWORD.equals("0")) {
            xml.setEncryptedPassWord();
        }
    }

    public void quit() {
        parent.setACV(xml.getDVRegServerNameList());
        this.dispose();
    }

    public void buildInterface(int index) {
        dbTypeBox.setModel(new javax.swing.DefaultComboBoxModel(
                DVPropMain.DV_DB_TYPE_ARRAY));
        serverList.setListData(listV);
        serverList.addListSelectionListener(this);
        serverList.setSelectedIndex(index);
        serverList.ensureIndexIsVisible(index);
        connectButton.addActionListener(this);
        applyButton.addActionListener(this);
        closeButton.addActionListener(this);
        removeButton.addActionListener(this);
        showPassWordButton.addActionListener(this);
        message.setForeground(Color.BLUE);
        passWordField.setEchoChar('*');
        setTitle(DVPropMain.DV_NAME + "-" + "Register");
    }

    public void setDetailFieldsNull() {
        message.setText(" ");
        hostField.setText("");
        sidField.setText("");
        userField.setText("");
        passWordField.setText("");
        schemaField.setText("");
        fName.setText("");
        portField.setText("546");
        dbTypeBox.setSelectedItem("DB2");
    }

    public void setDetailFields(DVServerInstance vlues) {
        if (vlues != null) {
            hostField.setText(vlues.getHost());
            sidField.setText(vlues.getSid());
            userField.setText(vlues.getUser());
            passWordField.setText(vlues.getPassword());
            schemaField.setText(vlues.getSchama());
            fName.setText(vlues.getInstanceName());
            portField.setText(vlues.getPort());
            dbTypeBox.setSelectedItem(vlues.getType());
        }
    }

    public void setVisibleDialog() {
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            quit();
        }
        if (e.getSource() == connectButton) {
            if (checkCanBeConnnected()) {
                if (checkIsNotServerHap()) {
                    setServerDetail();
                    setRegButton(false);
                    new SwingWorker<Long, Void>() {
                        protected Long doInBackground() {
                            Long start = System.currentTimeMillis();
                            setServerMainProp(fName.getText().trim() + "@"
                                    + hostField.getText().trim());
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                            }
                            Long end = System.currentTimeMillis();
                            return end - start;
                        }

                        protected void done() {
                            try {
                                if (get() > 0) {
                                    parent.getMainTabPanel().addTab(
                                            fName.getText().trim(),
                                            new DVMainEditView(fName.getText()
                                                    .trim()
                                                    + "@"
                                                    + hostField.getText()
                                                    .trim(), fName
                                                    .getText().trim(), parent
                                                    .getMainTabPanel()));
                                    parent.getMainTabPanel().setSelectedIndex(
                                            parent.getMainTabPanel()
                                                    .getTabCount() - 1);
                                    quit();
                                }
                            } catch (Exception e) {
                                setRegButton(true);
                                message.setText(e.getMessage());
                            }
                        }
                    }.execute();
                } else {
                    String key = fName.getText().trim() + "@"
                            + hostField.getText().trim();
                    setRegButton(false);
                    message.setText(DVMainEditView.getMainViewResource()
                            .getString("loadingText"));
                    try {
                        DVPropMain.DB_SERVER_CONNECT.put(key, conn);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                parent.getMainTabPanel().addTab(
                                        fName.getText().trim(),
                                        new DVMainEditView(fName.getText()
                                                .trim()
                                                + "@"
                                                + hostField.getText().trim(),
                                                fName.getText().trim(), parent
                                                .getMainTabPanel()));
                                parent.getMainTabPanel()
                                        .setSelectedIndex(
                                                parent.getMainTabPanel()
                                                        .getTabCount() - 1);
                            }
                        });
                        quit();
                    } catch (Exception ee) {
                        message.setText(ee.getMessage());
                        setRegButton(true);
                        DVLOG.setErrorLog(DataViewerRegister.class.getName(),
                                ee);
                    } finally {
                        DataViewerUtilities.scheduleGC();
                    }
                }
                setRecentRecordRefresh();
            } else {
                if (conn == null) {
                    DataViewerUtilities.displayErrorMessageForConnection();
                }
            }
        }
        if (e.getSource() == applyButton) {
            if (!checkField()) {
                if (checkIsNotServer()) {
                    setServerDetail();
                    serverList.setListData(xml.getDVRegServerNameList());
                    serverList.updateUI();
                } else {
                    setServerDetail();
                }
                message.setText("Save DB server [ " + fName.getText().trim()
                        + " ] successfully");

            } else {
                message.setText(DVMainEditView.getMainViewResource().getString(
                        "inputField"));
            }
        }

        if (e.getSource() == removeButton) {

            if (!serverList.getSelectedValue().toString()
                    .equals("Create new DV")) {

                removeServerDetail();
            }
        }
        if (e.getSource() == showPassWordButton) {
            if (!serverList.getSelectedValue().toString()
                    .equals("Create new DV")) {
                showPasswordAsText();
            }
        }
    }

    public void applyDataBaseSetting() {
        if (!checkField()) {
            if (checkIsNotServer()) {
                setServerDetail();
                serverList.setListData(xml.getDVRegServerNameList());
                serverList.updateUI();
            } else {
                setServerDetail();
            }
            message.setText("Save DB server [ " + fName.getText().trim()
                    + " ] successfully");

        } else {
            message.setText(DVMainEditView.getMainViewResource().getString(
                    "inputField"));
        }
    }

    public void showPasswordAsText() {
        if (!String.valueOf(passWordField.getPassword()).trim().equals("")) {
            DataViewerUtilities.displayWarningMessage(String
                    .valueOf(passWordField.getPassword()));
        }
    }

    public void setRecentRecordRefresh() {
        DVRecentRecordProcess.setRecentRecordInfo(fName.getText().trim());
        DVPropMain.DV_DVFrequentView.get("MainFrequent").setRecentRecords();
    }

    public void setServerMainProp(String key) {
        message.setText(DVMainEditView.getMainViewResource().getString(
                "loadingText"));
        DVPropMain.DB_SERVER_CONNECT.put(key, conn);
        DVPropMain.DB_SERVER_SCHEMA.put(key, schemaField.getText().trim());
        DVPropMain.DB_SERVER_CON_DETAIL.put(key, getURL());
        DVPropMain.DB_SERVER_CON_USER.put(key, userField.getText().trim());
        DVPropMain.DB_SERVER_CON_PW.put(key,
                String.valueOf(passWordField.getPassword()).trim());
    }

    public void setRegButton(boolean is) {
        connectButton.setEnabled(is);
        applyButton.setEnabled(is);
        closeButton.setEnabled(is);
        removeButton.setEnabled(is);
    }

    public boolean checkIsNotServer() {
        boolean isExit = true;
        try {
            if (DVPropMain.DV_SERVER_INSTANCE.get(fName.getText().trim()) != null) {
                isExit = false;
            }
        } catch (Exception e) {
            isExit = true;
        }
        return isExit;
    }

    public boolean checkIsNotServerHap() {
        boolean isExit = true;
        try {
            if (DVPropMain.DB_SERVER_CONNECT.get(
                    fName.getText().trim() + "@" + hostField.getText().trim())
                    .toString() != null) {
                isExit = false;
            }
        } catch (Exception e) {
            isExit = true;
        }
        return isExit;
    }

    public void setServerDetail() {

        String[] dbvalues = {hostField.getText().trim(),
                sidField.getText().trim(), userField.getText().trim(),
                String.valueOf(passWordField.getPassword()).trim(),
                schemaField.getText().trim(), fName.getText().trim(),
                portField.getText().trim(),
                dbTypeBox.getSelectedItem().toString().trim()};

        xml.updateConnection(fName.getText().trim(), dbvalues, dbTypeBox
                .getSelectedItem().toString().trim());
        DVPropMain.DV_SERVER_INSTANCE.put(fName.getText().trim(),
                new DVServerInstance(dbvalues));
    }

    public void removeServerDetail() {
        DVPropMain.DV_SERVER_INSTANCE.remove(fName.getText().trim());
        xml.removeConnection(fName.getText().trim());
        serverList.setListData(xml.getDVRegServerNameList());
        serverList.updateUI();
        message.setText("Remove DB server [ " + fName.getText().trim()
                + " ] successfully");
        DVRecentRecordProcess.removeRecentServerRecord(fName.getText().trim());

        DVPropMain.DV_DVFrequentView.get("MainFrequent").setRecentRecords();
    }

    public boolean checkField() {
        boolean isBlank = true;
        if (!hostField.getText().equals("")) {
            if (!sidField.getText().equals("")) {
                if (!userField.getText().equals("")) {
                    if (!String.valueOf(passWordField.getPassword()).equals("")) {
                        if (!fName.getText().equals("")) {
                            isBlank = false;
                        }
                    }
                }
            }
        }
        return isBlank;
    }

    public void valueChanged(ListSelectionEvent e) {
        try {
            if (serverList.getSelectedValue().toString()
                    .equals("Create new DV")) {
                setDetailFieldsNull();
            } else {
                setDetailFields(DVPropMain.DV_SERVER_INSTANCE.get(serverList.getSelectedValue()));
            }
        } catch (Exception ee) {
        }
    }

    public boolean checkCanBeConnnected() {
        boolean isCon = true;
        String USER = userField.getText().trim();
        String PASSWORD = String.valueOf(passWordField.getPassword()).trim();
        try {
            conn = DvConnectFactory.getOtherConnection(dbTypeBox
                            .getSelectedItem().toString().trim(), getURL(), USER,
                    PASSWORD);
            if (conn == null) {
                isCon = false;
            }
        } catch (Exception e) {
            DVLOG.setErrorLog(DataViewerRegister.class.getName(), e);
            isCon = false;
            errordialog = new ExceptionErrorDialog("Exception Cause By : "
                    + e.getMessage(), null, e);
        }
        return isCon;
    }

    public String getURL() {
        String url = DVPropMain.getDBURL(dbTypeBox.getSelectedItem().toString()
                .trim());

        String PORT = "[port]";
        String SID = "[sid]";
        String HOST = "[host]";

        String regex = PORT.replaceAll("\\[", "\\\\[").replaceAll("\\]",
                "\\\\]");
        url = url.replaceAll(regex, portField.getText().trim());
        regex = HOST.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        url = url.replaceAll(regex, hostField.getText().trim());
        regex = SID.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        url = url.replaceAll(regex, sidField.getText().trim());
        return url;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY
        // //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        serverList = new JList();
        panel2 = new JPanel();
        label1 = new JLabel();
        portField = new JTextField();
        Host = new JLabel();
        hostField = new JTextField();
        Host2 = new JLabel();
        sidField = new JTextField();
        Host3 = new JLabel();
        userField = new JTextField();
        Host4 = new JLabel();
        Host5 = new JLabel();
        schemaField = new JTextField();
        Host6 = new JLabel();
        fName = new JTextField();
        Host7 = new JLabel();
        dbTypeBox = new JComboBox();
        passWordField = new JPasswordField();
        showPassWordButton = new JButton();
        message = new JLabel();
        connectButton = new JButton();
        applyButton = new JButton();
        removeButton = new JButton();
        closeButton = new JButton();

        // ======== this ========
        Container contentPane = getContentPane();

        // ======== panel1 ========
        {
            panel1.setBorder(new TitledBorder(LineBorder
                    .createBlackLineBorder(), "Please Select :"));

            // ======== scrollPane1 ========
            {

                // ---- serverList ----
                serverList
                        .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                scrollPane1.setViewportView(serverList);
            }

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(panel1Layout.createParallelGroup()
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 259,
                            GroupLayout.PREFERRED_SIZE));
            panel1Layout.setVerticalGroup(panel1Layout.createParallelGroup()
                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 349,
                            Short.MAX_VALUE));
        }

        // ======== panel2 ========
        {
            panel2.setBorder(new TitledBorder("Server Detail :"));

            // ---- label1 ----
            label1.setText("Port :");

            // ---- Host ----
            Host.setText("Host :");

            // ---- Host2 ----
            Host2.setText("SID :");

            // ---- Host3 ----
            Host3.setText("User :");

            // ---- Host4 ----
            Host4.setText("PassWord :");

            // ---- Host5 ----
            Host5.setText("Schema :");

            // ---- Host6 ----
            Host6.setText("Servre Name :");

            // ---- Host7 ----
            Host7.setText("DB Type :");

            // ---- showPassWordButton ----
            showPassWordButton.setText("Show PW");
            showPassWordButton
                    .setToolTipText("Show password for selected DataBase with text");

            GroupLayout panel2Layout = new GroupLayout(panel2);
            panel2.setLayout(panel2Layout);
            panel2Layout
                    .setHorizontalGroup(panel2Layout
                            .createParallelGroup()
                            .addGroup(
                                    panel2Layout
                                            .createSequentialGroup()
                                            .addContainerGap(
                                                    GroupLayout.DEFAULT_SIZE,
                                                    Short.MAX_VALUE)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.LEADING,
                                                                    false)
                                                            .addGroup(
                                                                    panel2Layout
                                                                            .createSequentialGroup()
                                                                            .addGroup(
                                                                                    panel2Layout
                                                                                            .createParallelGroup()
                                                                                            .addComponent(
                                                                                                    Host7,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    70,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addComponent(
                                                                                                    Host2,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    67,
                                                                                                    GroupLayout.PREFERRED_SIZE))
                                                                            .addPreferredGap(
                                                                                    LayoutStyle.ComponentPlacement.RELATED,
                                                                                    19,
                                                                                    Short.MAX_VALUE)
                                                                            .addGroup(
                                                                                    panel2Layout
                                                                                            .createParallelGroup()
                                                                                            .addGroup(
                                                                                                    panel2Layout
                                                                                                            .createParallelGroup(
                                                                                                                    GroupLayout.Alignment.TRAILING,
                                                                                                                    false)
                                                                                                            .addComponent(
                                                                                                                    fName,
                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                    193,
                                                                                                                    Short.MAX_VALUE)
                                                                                                            .addComponent(
                                                                                                                    schemaField,
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                    193,
                                                                                                                    Short.MAX_VALUE)
                                                                                                            .addComponent(
                                                                                                                    passWordField,
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                    193,
                                                                                                                    Short.MAX_VALUE)
                                                                                                            .addComponent(
                                                                                                                    userField,
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                    193,
                                                                                                                    Short.MAX_VALUE)
                                                                                                            .addComponent(
                                                                                                                    sidField,
                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                    193,
                                                                                                                    Short.MAX_VALUE))
                                                                                            .addGroup(
                                                                                                    panel2Layout
                                                                                                            .createSequentialGroup()
                                                                                                            .addComponent(
                                                                                                                    dbTypeBox,
                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                    103,
                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                            .addPreferredGap(
                                                                                                                    LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                            .addComponent(
                                                                                                                    showPassWordButton,
                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                    87,
                                                                                                                    GroupLayout.PREFERRED_SIZE)))
                                                                            .addContainerGap())
                                                            .addGroup(
                                                                    panel2Layout
                                                                            .createSequentialGroup()
                                                                            .addGroup(
                                                                                    panel2Layout
                                                                                            .createParallelGroup()
                                                                                            .addGroup(
                                                                                                    panel2Layout
                                                                                                            .createSequentialGroup()
                                                                                                            .addGroup(
                                                                                                                    panel2Layout
                                                                                                                            .createParallelGroup(
                                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                                    false)
                                                                                                                            .addComponent(
                                                                                                                                    label1,
                                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                                    67,
                                                                                                                                    Short.MAX_VALUE)
                                                                                                                            .addComponent(
                                                                                                                                    Host,
                                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                                    67,
                                                                                                                                    Short.MAX_VALUE))
                                                                                                            .addGap(22,
                                                                                                                    22,
                                                                                                                    22)
                                                                                                            .addGroup(
                                                                                                                    panel2Layout
                                                                                                                            .createParallelGroup()
                                                                                                                            .addComponent(
                                                                                                                                    portField,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    138,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                                                            .addComponent(
                                                                                                                                    hostField,
                                                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                                                    193,
                                                                                                                                    GroupLayout.PREFERRED_SIZE)))
                                                                                            .addComponent(
                                                                                                    Host6)
                                                                                            .addComponent(
                                                                                                    Host3,
                                                                                                    GroupLayout.PREFERRED_SIZE,
                                                                                                    67,
                                                                                                    GroupLayout.PREFERRED_SIZE)
                                                                                            .addGroup(
                                                                                                    panel2Layout
                                                                                                            .createParallelGroup(
                                                                                                                    GroupLayout.Alignment.TRAILING,
                                                                                                                    false)
                                                                                                            .addComponent(
                                                                                                                    Host5,
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                    67,
                                                                                                                    Short.MAX_VALUE)
                                                                                                            .addComponent(
                                                                                                                    Host4,
                                                                                                                    GroupLayout.Alignment.LEADING,
                                                                                                                    GroupLayout.DEFAULT_SIZE,
                                                                                                                    67,
                                                                                                                    Short.MAX_VALUE)))
                                                                            .addGap(0,
                                                                                    15,
                                                                                    Short.MAX_VALUE)))));
            panel2Layout
                    .setVerticalGroup(panel2Layout
                            .createParallelGroup()
                            .addGroup(
                                    panel2Layout
                                            .createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(
                                                                    label1)
                                                            .addComponent(
                                                                    portField,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(Host)
                                                            .addComponent(
                                                                    hostField,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(
                                                                    sidField,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(Host2))
                                            .addGap(21, 21, 21)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(Host3)
                                                            .addComponent(
                                                                    userField,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(Host4)
                                                            .addComponent(
                                                                    passWordField,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE))
                                            .addGap(20, 20, 20)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(Host5)
                                                            .addComponent(
                                                                    schemaField,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE))
                                            .addGap(18, 18, 18)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(Host6)
                                                            .addComponent(
                                                                    fName,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE))
                                            .addGap(23, 23, 23)
                                            .addGroup(
                                                    panel2Layout
                                                            .createParallelGroup(
                                                                    GroupLayout.Alignment.BASELINE)
                                                            .addComponent(Host7)
                                                            .addComponent(
                                                                    dbTypeBox,
                                                                    GroupLayout.PREFERRED_SIZE,
                                                                    GroupLayout.DEFAULT_SIZE,
                                                                    GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(
                                                                    showPassWordButton))
                                            .addContainerGap(20,
                                                    Short.MAX_VALUE)));
        }

        // ---- message ----
        message.setText(" ");

        // ---- connectButton ----
        connectButton.setText("Connect");

        // ---- applyButton ----
        applyButton.setText("Apply");

        // ---- removeButton ----
        removeButton.setText("Remove");

        // ---- closeButton ----
        closeButton.setText("Close");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout
                .setHorizontalGroup(contentPaneLayout
                        .createParallelGroup()
                        .addGroup(
                                contentPaneLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                contentPaneLayout
                                                        .createParallelGroup()
                                                        .addComponent(
                                                                message,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE)
                                                        .addGroup(
                                                                GroupLayout.Alignment.TRAILING,
                                                                contentPaneLayout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                                contentPaneLayout
                                                                                        .createParallelGroup(
                                                                                                GroupLayout.Alignment.TRAILING)
                                                                                        .addGroup(
                                                                                                contentPaneLayout
                                                                                                        .createSequentialGroup()
                                                                                                        .addGap(0,
                                                                                                                236,
                                                                                                                Short.MAX_VALUE)
                                                                                                        .addComponent(
                                                                                                                connectButton,
                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                85,
                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                        .addPreferredGap(
                                                                                                                LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                applyButton,
                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                87,
                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                        .addPreferredGap(
                                                                                                                LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                removeButton,
                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                87,
                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                        .addPreferredGap(
                                                                                                                LayoutStyle.ComponentPlacement.RELATED)
                                                                                                        .addComponent(
                                                                                                                closeButton,
                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                87,
                                                                                                                GroupLayout.PREFERRED_SIZE))
                                                                                        .addGroup(
                                                                                                contentPaneLayout
                                                                                                        .createSequentialGroup()
                                                                                                        .addComponent(
                                                                                                                panel1,
                                                                                                                GroupLayout.PREFERRED_SIZE,
                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                GroupLayout.PREFERRED_SIZE)
                                                                                                        .addGap(18,
                                                                                                                18,
                                                                                                                18)
                                                                                                        .addComponent(
                                                                                                                panel2,
                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)))
                                                                        .addGap(7,
                                                                                7,
                                                                                7)))
                                        .addContainerGap()));
        contentPaneLayout
                .setVerticalGroup(contentPaneLayout
                        .createParallelGroup()
                        .addGroup(
                                contentPaneLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(message)
                                        .addGap(18, 18, 18)
                                        .addGroup(
                                                contentPaneLayout
                                                        .createParallelGroup()
                                                        .addComponent(
                                                                panel1,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                panel2,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))
                                        .addGap(14, 14, 14)
                                        .addGroup(
                                                contentPaneLayout
                                                        .createParallelGroup(
                                                                GroupLayout.Alignment.BASELINE)
                                                        .addComponent(
                                                                closeButton)
                                                        .addComponent(
                                                                removeButton)
                                                        .addComponent(
                                                                applyButton)
                                                        .addComponent(
                                                                connectButton))
                                        .addContainerGap()));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization
        // //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY
    // //GEN-BEGIN:variables
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JList serverList;
    private JPanel panel2;
    private JLabel label1;
    private JTextField portField;
    private JLabel Host;
    private JTextField hostField;
    private JLabel Host2;
    private JTextField sidField;
    private JLabel Host3;
    private JTextField userField;
    private JLabel Host4;
    private JLabel Host5;
    private JTextField schemaField;
    private JLabel Host6;
    private JTextField fName;
    private JLabel Host7;
    private JComboBox dbTypeBox;
    private JPasswordField passWordField;
    private JButton showPassWordButton;
    private JLabel message;
    private JButton connectButton;
    private JButton applyButton;
    private JButton removeButton;
    private JButton closeButton;
    // JFormDesigner - End of variables declaration //GEN-END:variables
}
