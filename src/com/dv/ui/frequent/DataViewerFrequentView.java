/*
 * DataViewerFrequentView.java  2/20/13 2:19 PM
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

package com.dv.ui.frequent;

import com.dv.dbinstance.DVServerInstance;
import com.dv.prop.DVPropMain;
import com.dv.ui.DVFrame;
import com.dv.ui.DataViewerRegister;
import com.dv.ui.action.DVRecentRecordAction;
import com.dv.ui.component.DVNormalButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author Nick
 */
public class DataViewerFrequentView extends JPanel implements MouseListener, ActionListener {

    String serverName = null;
    String serverHost = null;
    String serverSID = null;
    String serverSchema = null;
    String serverLastTime = null;
    String countTimes = null;
    DVServerInstance dvsi;
    DVRecentRecordAction aviaction;
    HashMap<Integer, JButton> RECENT_BUTTON = new HashMap<Integer, JButton>();

    int selectInstanceIndex = 0;

    JPopupMenu instancePopupMenu;

    JMenuItem loadInstanceItem, removeInstanceItem, editDataBaseInstance;

    public DataViewerFrequentView() {
        initComponents();
        buildButtonGroup();
        buildPopupMenu();
        buildMouseActions();
        this.setOpaque(true);
    }

    public void buildMouseActions() {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            RECENT_BUTTON.get(i + 1).setBorder(BorderFactory.createLineBorder(Color.black, 1));
            RECENT_BUTTON.get(i + 1).addMouseListener(this);
        }
    }

    public void buildButtonGroup() {
        RECENT_BUTTON.put(1, display1);
        RECENT_BUTTON.put(2, display2);
        RECENT_BUTTON.put(3, display3);
        RECENT_BUTTON.put(4, display4);
        RECENT_BUTTON.put(5, display5);
        RECENT_BUTTON.put(6, display6);
        RECENT_BUTTON.put(7, display7);
        RECENT_BUTTON.put(8, display8);
    }

    public HashMap<Integer, JButton> getRECENT_BUTTON() {
        return RECENT_BUTTON;
    }

    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                setDisplayActions(i + 1, e);
                break;
            }
        }
    }

    public String getDvrecentInstanceName(int index, HashMap<String, String> detail) {
        String propvValue;
        try {
            propvValue = detail.get(String.valueOf(index));
        } catch (Exception e) {
            return null;
        }
        return propvValue;
    }

    public void setDisplayActions(int index, MouseEvent e) {
        if (e.getClickCount() == 2) {
            loadInstance(index);
        }
    }

    public void loadInstance(int index) {
        if (getDvrecentInstanceName(index, DVPropMain.DV_RECENT_INSTANCE_NAME) != null) {
            aviaction = new DVRecentRecordAction((DVFrame) DVPropMain.DV_FRAME.get("MAIN"), DVPropMain.DV_RECENT_INSTANCE_NAME.get(String.valueOf(index)), 1);
            aviaction.setRecentDataBaseAcess();
        }
    }

    public void mouseEntered(MouseEvent e) {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                RECENT_BUTTON.get(i + 1).setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                break;
            }
        }
    }

    public void mouseExited(MouseEvent e) {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                RECENT_BUTTON.get(i + 1).setBorder(BorderFactory.createLineBorder(Color.black, 1));
                break;
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            for (int i = 0; i < RECENT_BUTTON.size(); i++) {
                if (e.getSource().equals(RECENT_BUTTON.get(i + 1))) {
                    showPopup(i + 1, e);
                    break;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void showPopup(int index, MouseEvent e) {
        if (!instancePopupMenu.isVisible()) {
            if (getDvrecentInstanceName(index, DVPropMain.DV_RECENT_INSTANCE_NAME) != null) {
                selectInstanceIndex = index;
                instancePopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }


    public void buildPopupMenu() {

        instancePopupMenu = new JPopupMenu();
        loadInstanceItem = new JMenuItem("Load Database");
        removeInstanceItem = new JMenuItem("Remove Database ");
        editDataBaseInstance = new JMenuItem("Change Database Setting");

        editDataBaseInstance.addActionListener(this);

        loadInstanceItem.addActionListener(this);
        removeInstanceItem.addActionListener(this);

        instancePopupMenu.add(loadInstanceItem);
        instancePopupMenu.add(removeInstanceItem);
        instancePopupMenu.addSeparator();
        instancePopupMenu.add(editDataBaseInstance);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeInstanceItem) {
            if (getDvrecentInstanceName(selectInstanceIndex, DVPropMain.DV_RECENT_INSTANCE_NAME) != null) {
                DVRecentRecordProcess.removeRecentServerRecord(getDvrecentInstanceName(selectInstanceIndex, DVPropMain.DV_RECENT_INSTANCE_NAME));
                this.setRecentRecords();
            }
        }

        if (e.getSource() == loadInstanceItem) {
            loadInstance(selectInstanceIndex);
        }

        if (e.getSource() == editDataBaseInstance) {
            invokeDataViewerRegister();
        }
    }

    public void invokeDataViewerRegister() {
        String instanceNme = getDvrecentInstanceName(selectInstanceIndex, DVPropMain.DV_RECENT_INSTANCE_NAME);
        if (instanceNme!= null) {
            DataViewerRegister register=new DataViewerRegister((DVFrame) (DVPropMain.DV_FRAME.get("MAIN")), true,instanceNme);
        }
    }

    public void setRecentRecords() {
        removeAllDispalys();
        int size = 0;
        if (DVPropMain.DV_RECENT_INSTANCE_NAME.size() > 8) {
            size = 8;
        } else {
            size = DVPropMain.DV_RECENT_INSTANCE_NAME.size();
        }
        if (size > 0) {
            for (int i = 1; i < size + 1; i++) {
                setDisplay(i);
            }
        }
    }

    public void removeAllDispalys() {
        for (int i = 0; i < RECENT_BUTTON.size(); i++) {
            RECENT_BUTTON.get((i + 1)).setText("");
        }
    }

    public void setDisplay(int index) {
        setDisplayDetail(index);
    }

    public void setDisplayDetail(int index) {
        serverName = DVPropMain.DV_RECENT_INSTANCE_NAME.get(String.valueOf(index));
        dvsi = DVPropMain.DV_SERVER_INSTANCE.get(serverName);
        serverHost = dvsi.getHost();
        serverSID = dvsi.getSid();
        serverSchema = dvsi.getSchama();
        serverLastTime = DVPropMain.DV_RECENT_INSTANCE_LASTTIME.get(serverName);
        countTimes = DVPropMain.DV_RECENT_INSTANCE_COUNT.get(serverName);
        String infor = "<html><p>Server Name : <font color=red face=\"DialogInput\" >" + serverName + "</font></p>"
                + "<p>Host  : <font color=blue face=\"DialogInput\" >" + serverHost + "</font></p>"
                + "<p>SID  : <font color=blue face=\"DialogInput\" >" + serverSID + "</font></p>"
                + "<p>Schema : <font color=blue face=\"DialogInput\" >" + serverSchema + "</font></p>"
                + "<p>Total Time(s) : <font color=red face=\"DialogInput\" >" + countTimes + "</font></p>"
                + "<p>Last : <font color=blue face=\"DialogInput\" >" + serverLastTime + "</font></p></html>";
        RECENT_BUTTON.get(index).setText(infor);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        frequentPanel = new JPanel();
        display1 = new JButton();
        display2 = new JButton();
        display3 = new JButton();
        display4 = new JButton();
        display5 = new JButton();
        display6 = new JButton();
        display7 = new JButton();
        display8 = new JButton();

        //======== this ========
        setOpaque(false);
        setBackground(Color.white);
        setLayout(new FlowLayout());

        //======== frequentPanel ========
        {
            frequentPanel.setOpaque(false);

            //---- display1 ----
            display1.setText(" ");
            display1.setFocusPainted(false);
            display1.setRequestFocusEnabled(false);
            display2.setOpaque(true);

            //---- display2 ----
            display2.setText(" ");
            display2.setFocusPainted(false);
            display2.setRequestFocusEnabled(false);
            display2.setOpaque(false);

            //---- display3 ----
            display3.setText(" ");
            display3.setFocusPainted(false);
            display3.setOpaque(false);
            display3.setRequestFocusEnabled(false);

            //---- display4 ----
            display4.setText(" ");
            display4.setFocusPainted(false);
            display4.setOpaque(false);
            display4.setRequestFocusEnabled(false);

            //---- display5 ----
            display5.setText(" ");
            display5.setFocusPainted(false);
            display5.setOpaque(false);
            display5.setRequestFocusEnabled(false);

            //---- display6 ----
            display6.setText(" ");
            display6.setFocusPainted(false);
            display6.setOpaque(false);
            display6.setRequestFocusEnabled(false);

            //---- display7 ----
            display7.setText(" ");
            display7.setFocusPainted(false);
            display7.setOpaque(false);
            display7.setRequestFocusEnabled(false);

            //---- display8 ----
            display8.setText(" ");
            display8.setFocusPainted(false);
            display8.setOpaque(false);
            display8.setRequestFocusEnabled(false);

            GroupLayout frequentPanelLayout = new GroupLayout(frequentPanel);
            frequentPanelLayout.setHonorsVisibility(false);
            frequentPanel.setLayout(frequentPanelLayout);
            frequentPanelLayout.setHorizontalGroup(
                    frequentPanelLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, frequentPanelLayout.createSequentialGroup()
                                    .addGap(35, 35, 35)
                                    .addGroup(frequentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addGroup(frequentPanelLayout.createSequentialGroup()
                                                    .addComponent(display5, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(display6, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(display7, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(display8, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(frequentPanelLayout.createSequentialGroup()
                                                    .addComponent(display1, GroupLayout.DEFAULT_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(display2, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(display3, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(display4, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)))
                                    .addGap(26, 26, 26))
            );
            frequentPanelLayout.setVerticalGroup(
                    frequentPanelLayout.createParallelGroup()
                            .addGroup(frequentPanelLayout.createSequentialGroup()
                                    .addGap(40, 40, 40)
                                    .addGroup(frequentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(display1, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(display3, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(display2, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(display4, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
                                    .addGap(31, 31, 31)
                                    .addGroup(frequentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(display8, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(display7, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(display6, GroupLayout.DEFAULT_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(display5, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18))
            );
        }
        add(frequentPanel);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel frequentPanel;
    private JButton display1;
    private JButton display2;
    private JButton display3;
    private JButton display4;
    private JButton display5;
    private JButton display6;
    private JButton display7;
    private JButton display8;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
