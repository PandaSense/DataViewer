/*
 * DataViewerOptionView.java  2/6/13 1:04 PM
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

package com.dv.ui.config;

import com.dv.prop.DVPropMain;
import com.dv.ui.DVFrame;
import com.dv.ui.action.DVImportFromAquaAction;
import com.dv.ui.component.DVSystemTray;
import com.dv.util.DVFileUtil;
import com.dv.util.DataViewerDesEncrypter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author Nick
 */
public class DataViewerOptionView extends JDialog implements ActionListener {

    DVFrame dvframe;
    DataViewerExtraSet extraPanel;

    public DataViewerOptionView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.dvframe = (DVFrame) parent;
        initComponents();
//        buildExtraPanel();
        serverTypeCombox.setModel(new javax.swing.DefaultComboBoxModel(DVPropMain.DV_DB_TYPE_ARRAY));
        loadEditorConfig();
        buildAction();
        Point oh = parent.getLocationOnScreen();
        setLocation((int) oh.getX() + parent.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + parent.getHeight() / 2 - getHeight() / 2);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void buildExtraPanel() {
        extraPanel = new DataViewerExtraSet();
        mainTabbedPane.addTab("DataViewer Extra Database", extraPanel);
    }

    public void buildAction() {
        okButton.addActionListener(this);
        btnApply.addActionListener(this);
        cancelButton.addActionListener(this);

        importFromAqua.addActionListener(new DVImportFromAquaAction(dvframe, this));
        importFromAqua.setToolTipText("Import AquaData database setting into DataViewer");
        sytemTrayClose.setToolTipText("Minimize to system tray when you closed DataViewer");
        message.setForeground(Color.BLUE);
        sqlCodeLan.setEnabled(false);
        marginCheckBox.addActionListener(this);
        setTitle(DVPropMain.DV_NAME + "-" + DVPropMain.DV_VERSION + "-Option");
    }

    public void saveEditorConfig() {
        DVPropMain.setDvEditPropDetail("DV_FONT_SIZE", dvFontTF.getText().trim());
        DVPropMain.setDvEditPropDetail("DV_SQL_FONT_SIZE", dvSQLTF.getText().trim());
        DVPropMain.setDvEditPropDetail("DV_FILE_RECENT_PATH", fileTF.getText().trim());
        DVPropMain.setDvEditPropDetail("DV_EDITOR_MARGIN_SIZE", margibSize.getText().trim());
        DVPropMain.setDvEditPropDetail("DV_FORMAT_WORD_WIDTH", docWidthSize.getText().trim());
        DVPropMain.setDvEditPropDetail("LOOK_AND_FEEL_NAME", landkTF.getText().trim());

        DVPropMain.setDvEditPropDetail("DV_LANDK_JAR_NAME", landkJarName.getText().trim());

        DVPropMain.setDvEditPropDetail("DV_USER_DB_TYPE", serverTypeCombox.getSelectedItem().toString().trim());

        DVPropMain.setDvEditPropDetail("DV_AUTO_UPDATE_SERVER", autoServerName.getText().trim());
        DVPropMain.setDvEditPropDetail("DV_AUTO_UPDATE_SERVER_USER", autoServerUserName.getText().trim());

        if (String.valueOf(autoServerUserPassword.getPassword()).equals("")) {
            DVPropMain.setDvEditPropDetail("DV_AUTO_UPDATE_SERVER_PASSWORD", "");
        } else {
            DVPropMain.setDvEditPropDetail("DV_AUTO_UPDATE_SERVER_PASSWORD", DataViewerDesEncrypter.encrypt(String.valueOf(autoServerUserPassword.getPassword())));
        }

        if (autoCommitBox.isSelected()) {

            DVPropMain.setDvEditPropDetail("DV_AUTO_COMMIT", "1");

        } else {

            DVPropMain.setDvEditPropDetail("DV_AUTO_COMMIT", "0");
        }

        if (displayByGrid.isSelected()) {

            DVPropMain.setDvEditPropDetail("DV_DISPLAY_GRID", "1");

        } else {

            DVPropMain.setDvEditPropDetail("DV_DISPLAY_GRID", "0");
        }


        if (sqlCodeLan.isSelected()) {

            DVPropMain.setDvEditPropDetail("DV_SQLCODE_CHINESE", "1");

        } else {

            DVPropMain.setDvEditPropDetail("DV_SQLCODE_CHINESE", "0");
        }

        if (autoUpdate.isSelected()) {

            DVPropMain.setDvEditPropDetail("DV_AUTO_UPDATE", "1");

        } else {

            DVPropMain.setDvEditPropDetail("DV_AUTO_UPDATE", "0");
        }

        if (sytemTrayClose.isSelected()) {

            if (dvframe.supportTray()) {
                DVPropMain.setDvEditPropDetail("DV_SYSTEM_TRAY", "1");
                setSystemTray();
            } else {
                DVPropMain.setDvEditPropDetail("DV_SYSTEM_TRAY", "0");
                removeSystemTray();
            }
        } else {
            DVPropMain.setDvEditPropDetail("DV_SYSTEM_TRAY", "0");
            removeSystemTray();
        }

        if (encryptedPasswordCheckBox.isSelected()) {

            DVPropMain.setDvEditPropDetail("DV_ENCRYPTED_DATABASE_PASSWORD", "1");

        } else {

            DVPropMain.setDvEditPropDetail("DV_ENCRYPTED_DATABASE_PASSWORD", "0");
        }

        if (ctcWithTime.isSelected()) {

            DVPropMain.setDvEditPropDetail("DV_CTC_WITH_DATE", "1");

        } else {

            DVPropMain.setDvEditPropDetail("DV_CTC_WITH_DATE", "0");
        }

        if (marginCheckBox.isSelected()) {

            DVPropMain.setDvEditPropDetail("DV_SQL_EDITAREA_HAS_MARGIN", "1");

        } else {
            DVPropMain.setDvEditPropDetail("DV_SQL_EDITAREA_HAS_MARGIN", "0");
        }


        DVFileUtil.savePropertiesFile(DVPropMain.editorMain, DVPropMain.DV_CONFIG_FOLDER + "DV_EDITOR.properties");
        DVPropMain.resetEditorConfig();

        dvframe.getMainTabPanel().changeAllGridSQLResult();
        message.setText("Save DataViewer Normal Setting Successfully");
    }

    public void setSystemTray() {

        if (!dvframe.getHasTray()) {
            DVSystemTray tray = new DVSystemTray(dvframe);
            dvframe.setSystemTray(tray);
            dvframe.setHasTray(true);
        }
    }

    public void removeSystemTray() {
        if (dvframe.getHasTray()) {
            SystemTray.getSystemTray().remove(dvframe.getSystemTray().getTrayicon());
            dvframe.setSystemTray(null);
            dvframe.setHasTray(false);
        }
    }

    public void loadEditorConfig() {

        dvFontTF.setText(DVPropMain.DV_FONT_SIZE);
        dvSQLTF.setText(DVPropMain.DV_SQL_FONT_SIZE);
        docWidthSize.setText(DVPropMain.DV_FORMAT_WORD_WIDTH);
        margibSize.setText(DVPropMain.DV_EDITOR_MARGIN_SIZE);
        fileTF.setText(DVPropMain.DV_FILE_RECENT_PATH);
        landkTF.setText(DVPropMain.LOOK_AND_FEEL_NAME);
        serverTypeCombox.setSelectedItem(DVPropMain.DV_USER_DB_TYPE);
        autoServerName.setText(DVPropMain.DV_AUTO_UPDATE_SERVER);
        autoServerUserName.setText(DVPropMain.DV_AUTO_UPDATE_SERVER_USER);

        landkJarName.setText(DVPropMain.DV_LANDK_JAR_NAME);

        if (!DVPropMain.DV_AUTO_UPDATE_SERVER_PASSWORD.equals("")) {
            autoServerUserPassword.setText(DataViewerDesEncrypter.decrypt(DVPropMain.DV_AUTO_UPDATE_SERVER_PASSWORD));
        }

        if (!DVPropMain.DV_AUTO_COMMIT.equals("")) {
            if (DVPropMain.DV_AUTO_COMMIT.equals("1")) {
                autoCommitBox.setSelected(true);
            } else {
                autoCommitBox.setSelected(false);
            }

        } else {
            autoCommitBox.setSelected(false);
        }

        if (!DVPropMain.DV_DISPLAY_GRID.equals("")) {
            if (DVPropMain.DV_DISPLAY_GRID.equals("1")) {

                displayByGrid.setSelected(true);

            } else {
                displayByGrid.setSelected(false);
            }

        } else {
            displayByGrid.setSelected(false);
        }

        if (!DVPropMain.DV_SYSTEM_TRAY.equals("")) {
            if (DVPropMain.DV_SYSTEM_TRAY.equals("1")) {

                sytemTrayClose.setSelected(true);

            } else {
                sytemTrayClose.setSelected(false);
            }

        } else {
            sytemTrayClose.setSelected(false);
        }


        if (!DVPropMain.DV_SQLCODE_CHINESE.equals("")) {
            if (DVPropMain.DV_SQLCODE_CHINESE.equals("1")) {

                sqlCodeLan.setSelected(true);

            } else {
                sqlCodeLan.setSelected(false);
            }

        } else {
            sqlCodeLan.setSelected(false);
        }

        if (!DVPropMain.DV_AUTO_UPDATE.equals("")) {
            if (DVPropMain.DV_AUTO_UPDATE.equals("1")) {

                autoUpdate.setSelected(true);

            } else {
                autoUpdate.setSelected(false);
            }

        } else {
            autoUpdate.setSelected(false);
        }

        if (!DVPropMain.DV_SQL_EDITAREA_HAS_MARGIN.equals("")) {
            if (DVPropMain.DV_SQL_EDITAREA_HAS_MARGIN.equals("1")) {

                marginCheckBox.setSelected(true);
                margibSize.setEditable(true);

            } else {
                marginCheckBox.setSelected(false);
                margibSize.setEditable(false);
            }

        } else {
            marginCheckBox.setSelected(false);
        }
        if (!DVPropMain.DV_ENCRYPTED_DATABASE_PASSWORD.equals("")) {
            if (DVPropMain.DV_ENCRYPTED_DATABASE_PASSWORD.equals("1")) {

                encryptedPasswordCheckBox.setSelected(true);

            } else {
                encryptedPasswordCheckBox.setSelected(false);
            }

        } else {
            encryptedPasswordCheckBox.setSelected(false);
        }

        if (!DVPropMain.DV_CTC_WITH_DATE.equals("")) {
            if (DVPropMain.DV_CTC_WITH_DATE.equals("1")) {

                ctcWithTime.setSelected(true);

            } else {
                ctcWithTime.setSelected(false);
            }

        } else {
            ctcWithTime.setSelected(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            saveEditorConfig();
            quit();
        }
        if (e.getSource() == btnApply) {
            saveEditorConfig();
        }
        if (e.getSource() == cancelButton) {
            quit();
        }

        if (e.getSource() == marginCheckBox) {
            if (marginCheckBox.isSelected()) {
                margibSize.setEditable(true);
            } else {
                margibSize.setEditable(false);
            }
        }
    }

    public void quit() {
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        mainTabbedPane = new JTabbedPane();
        panel1 = new JPanel();
        panel2 = new JPanel();
        label1 = new JLabel();
        dvFontTF = new JTextField();
        label2 = new JLabel();
        docWidthSize = new JTextField();
        label3 = new JLabel();
        dvSQLTF = new JTextField();
        label4 = new JLabel();
        margibSize = new JTextField();
        label5 = new JLabel();
        fileTF = new JTextField();
        label6 = new JLabel();
        landkTF = new JTextField();
        marginCheckBox = new JCheckBox();
        label12 = new JLabel();
        landkJarName = new JTextField();
        panel3 = new JPanel();
        importFromAqua = new JButton();
        sqlCodeLan = new JCheckBox();
        displayByGrid = new JCheckBox();
        sytemTrayClose = new JCheckBox();
        autoUpdate = new JCheckBox();
        panel5 = new JPanel();
        label9 = new JLabel();
        label10 = new JLabel();
        label11 = new JLabel();
        autoServerName = new JTextField();
        autoServerUserName = new JTextField();
        autoServerUserPassword = new JPasswordField();
        ctcWithTime = new JCheckBox();
        superSettng = new JButton();
        panel4 = new JPanel();
        label7 = new JLabel();
        serverTypeCombox = new JComboBox();
        autoCommitBox = new JCheckBox();
        encryptedPasswordCheckBox = new JCheckBox();
        cancelButton = new JButton();
        btnApply = new JButton();
        okButton = new JButton();
        message = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== mainTabbedPane ========
        {

            //======== panel1 ========
            {

                //======== panel2 ========
                {
                    panel2.setBorder(new TitledBorder(new EtchedBorder(), "Normal Setting"));

                    //---- label1 ----
                    label1.setText("Text Font Size* :");

                    //---- label2 ----
                    label2.setText("Document Width :");

                    //---- label3 ----
                    label3.setText("SQL Font Size* :");

                    //---- label4 ----
                    label4.setText("Margin Size* :");

                    //---- label5 ----
                    label5.setText("Rencent File path :");

                    //---- label6 ----
                    label6.setText("Look And Feel* :");

                    //---- marginCheckBox ----
                    marginCheckBox.setText("Has Margin *");

                    //---- label12 ----
                    label12.setText("Source:");

                    GroupLayout panel2Layout = new GroupLayout(panel2);
                    panel2.setLayout(panel2Layout);
                    panel2Layout.setHorizontalGroup(
                        panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addComponent(label6, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(landkTF, GroupLayout.PREFERRED_SIZE, 237, GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(label12, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(landkJarName))
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addComponent(label5, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fileTF))
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dvSQLTF, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(margibSize, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(marginCheckBox, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dvFontTF, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(label2, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(docWidthSize, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(19, Short.MAX_VALUE))
                    );
                    panel2Layout.setVerticalGroup(
                        panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label1)
                                    .addComponent(dvFontTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label2)
                                    .addComponent(docWidthSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dvSQLTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label4)
                                        .addComponent(margibSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(marginCheckBox))
                                    .addComponent(label3))
                                .addGap(18, 18, 18)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label5)
                                    .addComponent(fileTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label6)
                                    .addComponent(landkTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(landkJarName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label12))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }

                //======== panel3 ========
                {
                    panel3.setBorder(new TitledBorder(new EtchedBorder(), "Extra Setting"));

                    //---- importFromAqua ----
                    importFromAqua.setText("Import From AquaData");

                    //---- sqlCodeLan ----
                    sqlCodeLan.setText("Sql Code By Chinese*");
                    sqlCodeLan.setEnabled(false);

                    //---- displayByGrid ----
                    displayByGrid.setText("Display Query Result By Grid");

                    //---- sytemTrayClose ----
                    sytemTrayClose.setText("System Tray");

                    //---- autoUpdate ----
                    autoUpdate.setText("Auto-Updated*");

                    //======== panel5 ========
                    {
                        panel5.setBorder(new TitledBorder(LineBorder.createBlackLineBorder(), "Auto Update Server :"));

                        //---- label9 ----
                        label9.setText("Server Host :");

                        //---- label10 ----
                        label10.setText("User Name :");

                        //---- label11 ----
                        label11.setText("Password :");

                        GroupLayout panel5Layout = new GroupLayout(panel5);
                        panel5.setLayout(panel5Layout);
                        panel5Layout.setHorizontalGroup(
                            panel5Layout.createParallelGroup()
                                .addGroup(panel5Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(panel5Layout.createParallelGroup()
                                        .addComponent(label9, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label11, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label10, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                                    .addGroup(panel5Layout.createParallelGroup()
                                        .addComponent(autoServerUserPassword, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(autoServerUserName, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(autoServerName, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap())
                        );
                        panel5Layout.setVerticalGroup(
                            panel5Layout.createParallelGroup()
                                .addGroup(panel5Layout.createSequentialGroup()
                                    .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label9)
                                        .addComponent(autoServerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addGap(9, 9, 9)
                                    .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(autoServerUserName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label10))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(autoServerUserPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label11))
                                    .addContainerGap(15, Short.MAX_VALUE))
                        );
                    }

                    //---- ctcWithTime ----
                    ctcWithTime.setText("CTC with Excution Date");

                    //---- superSettng ----
                    superSettng.setText("Super Setting");

                    GroupLayout panel3Layout = new GroupLayout(panel3);
                    panel3.setLayout(panel3Layout);
                    panel3Layout.setHorizontalGroup(
                        panel3Layout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addComponent(importFromAqua, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(superSettng, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addComponent(panel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)))
                                .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(sqlCodeLan, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .addComponent(autoUpdate, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .addComponent(displayByGrid, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .addComponent(sytemTrayClose, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .addComponent(ctcWithTime, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                                .addGap(33, 33, 33))
                    );
                    panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                            .addGroup(panel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(importFromAqua)
                                    .addComponent(sqlCodeLan)
                                    .addComponent(superSettng))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addComponent(displayByGrid)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(sytemTrayClose)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(autoUpdate)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ctcWithTime)
                                        .addGap(0, 17, Short.MAX_VALUE))
                                    .addComponent(panel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                    );
                }

                //======== panel4 ========
                {
                    panel4.setBorder(new TitledBorder(new EtchedBorder(), "DataBase Setting"));

                    //---- label7 ----
                    label7.setText("ServerType :");

                    //---- autoCommitBox ----
                    autoCommitBox.setText("Auto Commit");

                    //---- encryptedPasswordCheckBox ----
                    encryptedPasswordCheckBox.setText("Encrypted Password");
                    encryptedPasswordCheckBox.setSelected(true);

                    encryptedPasswordCheckBox.setEnabled(false);


                    GroupLayout panel4Layout = new GroupLayout(panel4);
                    panel4.setLayout(panel4Layout);
                    panel4Layout.setHorizontalGroup(
                        panel4Layout.createParallelGroup()
                            .addGroup(panel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label7, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverTypeCombox, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                                .addComponent(encryptedPasswordCheckBox, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(autoCommitBox, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                    );
                    panel4Layout.setVerticalGroup(
                        panel4Layout.createParallelGroup()
                            .addGroup(panel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label7)
                                    .addComponent(serverTypeCombox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(autoCommitBox)
                                    .addComponent(encryptedPasswordCheckBox))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panel4, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addComponent(panel2, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(21, 21, 21))
                );
            }
            mainTabbedPane.addTab("General", panel1);

        }

        //---- cancelButton ----
        cancelButton.setText("Cancel");

        //---- btnApply ----
        btnApply.setText("Apply");

        //---- okButton ----
        okButton.setText("OK");

        //---- message ----
        message.setText(" ");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(message, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(okButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnApply, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                        .addComponent(mainTabbedPane))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(mainTabbedPane)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelButton)
                            .addComponent(btnApply))
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(okButton)
                            .addComponent(message)))
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane mainTabbedPane;
    private JPanel panel1;
    private JPanel panel2;
    private JLabel label1;
    private JTextField dvFontTF;
    private JLabel label2;
    private JTextField docWidthSize;
    private JLabel label3;
    private JTextField dvSQLTF;
    private JLabel label4;
    private JTextField margibSize;
    private JLabel label5;
    private JTextField fileTF;
    private JLabel label6;
    private JTextField landkTF;
    private JCheckBox marginCheckBox;
    private JLabel label12;
    private JTextField landkJarName;
    private JPanel panel3;
    private JButton importFromAqua;
    private JCheckBox sqlCodeLan;
    private JCheckBox displayByGrid;
    private JCheckBox sytemTrayClose;
    private JCheckBox autoUpdate;
    private JPanel panel5;
    private JLabel label9;
    private JLabel label10;
    private JLabel label11;
    private JTextField autoServerName;
    private JTextField autoServerUserName;
    private JPasswordField autoServerUserPassword;
    private JCheckBox ctcWithTime;
    private JButton superSettng;
    private JPanel panel4;
    private JLabel label7;
    private JComboBox serverTypeCombox;
    private JCheckBox autoCommitBox;
    private JCheckBox encryptedPasswordCheckBox;
    private JButton cancelButton;
    private JButton btnApply;
    private JButton okButton;
    private JLabel message;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
