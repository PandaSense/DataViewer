/*
 * DataViewerUpgradeDialog.java  5/2/13 3:57 PM
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

/*
 * Created by JFormDesigner on Thu May 02 15:56:48 CST 2013
 */

package com.dv.ui.component;

import com.dv.prop.DVPropMain;
import com.dv.upgrade.DVUpgradeUtil;
import com.dv.util.FileIO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author Nick
 */
public class DataViewerUpgradeDialog extends JDialog implements ActionListener {

    private int releaseFileType;

    public DataViewerUpgradeDialog(java.awt.Frame parent, int releaseFileType) {
        super(parent);
        initComponents();
        this.releaseFileType = releaseFileType;
        this.setTitle(DVPropMain.DV_NAME + " - " + "New Version Release");
        setReleaseInfor();
        Point oh = parent.getLocationOnScreen();
        setLocation((int) oh.getX() + parent.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + parent.getHeight() / 2 - getHeight() / 2);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        okButton.addActionListener(this);
        this.setVisible(true);
    }

    public DataViewerUpgradeDialog(Dialog owner) {
        super(owner);
        initComponents();
    }
    public void setReleaseInfor() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("New version DataViewer has been downloaded in your location\n");
        buffer.append("Please restart DataViewer,and finish upgrading.\n");
        buffer.append("Release Notes : \n\n");

        if (releaseFileType==0) {
            buffer.append(FileIO.read(DVPropMain.DV_USER_HOME_UPGRADE_FOLDER + DVUpgradeUtil.getUpgradeVersionInforFile()));
        }else{
            buffer.append(FileIO.read(DVPropMain.DV_LIB_HOME+ DVUpgradeUtil.getUpgradeVersionInforFile()));
        }
        versionInforTextArea.setText(buffer.toString());
        versionInforTextArea.setCaretPosition(0);
    }

    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        versionInforTextArea = new JTextPane();
        panel2 = new JPanel();
        okButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder(null, "DataViewer Release Description ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

            //======== scrollPane1 ========
            {

                //---- versionInforTextArea ----
                versionInforTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
                scrollPane1.setViewportView(versionInforTextArea);
            }

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addComponent(scrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
            );
        }

        //======== panel2 ========
        {
            panel2.setLayout(new FlowLayout());

            //---- okButton ----
            okButton.setText("Close");
            panel2.add(okButton);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(panel2, GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
                        .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTextPane versionInforTextArea;
    private JPanel panel2;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
