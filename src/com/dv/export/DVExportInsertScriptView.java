/*
 * DVExportInsertScriptView.java  7/17/13 3:44 PM
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
 * Created by JFormDesigner on Wed Jul 17 15:44:25 CST 2013
 */

package com.dv.export;

import javax.swing.border.*;
import com.dv.prop.DVPropMain;
import com.dv.ui.DVMainEditView;
import com.dv.ui.component.DVFileChooser;
import com.dv.util.DataViewerUtilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author Nick
 */
public class DVExportInsertScriptView extends JDialog implements ActionListener {


    private DVMainEditView dvew;
    DVFileChooser fcd;
    private String exportFileFullpath;


    public DVExportInsertScriptView(Frame owner,DVMainEditView dvew) {
        super(owner,true);
        this.dvew=dvew;
        initComponents();
        init();
        buildAction();
        setTitle(DVPropMain.DV_NAME + "-" + DVPropMain.DV_VERSION + "-" + "Export Table Script");
        Point oh = owner.getLocationOnScreen();
        setLocation((int) oh.getX() + owner.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + owner.getHeight() / 2 - getHeight() / 2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void init(){
        connectionLabel.setText("DataViewer Database Instance : "+dvew.getSubSVKey());
        schemaField.setText(dvew.getServerSchema());
    }

    public void buildAction(){
        cancelButton.addActionListener(this);
        browseButton.addActionListener(this);
    }

    public DVExportInsertScriptView(Dialog owner) {
        super(owner);
        initComponents();
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==cancelButton){
            this.dispose();
        }
        if(e.getSource()==browseButton){
            fcd = new DVFileChooser(DataViewerUtilities.getExportFilePath());
            int i = fcd.showOpenDialog(DVPropMain.DV_FRAME.get("MAIN"));
            if (i == JFileChooser.APPROVE_OPTION) {
                exportFileFullpath = fcd.getSelectedFile().getPath().trim()+".dvbs";
                outputFileField.setText(exportFileFullpath);
            }
        }
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        connectionLabel = new JLabel();
        outputFileField = new JTextField();
        browseButton = new JButton();
        label4 = new JLabel();
        schemaField = new JTextField();
        label5 = new JLabel();
        tableNameField = new JTextField();
        label3 = new JLabel();
        executeButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBorder(new EtchedBorder());

            //---- connectionLabel ----
            connectionLabel.setText(" ");
            connectionLabel.setHorizontalAlignment(SwingConstants.LEFT);

            //---- browseButton ----
            browseButton.setText("...");

            //---- label4 ----
            label4.setText("Target Schema :");

            //---- label5 ----
            label5.setText("Table Name :");

            //---- label3 ----
            label3.setText("OutPut File :");

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(label5, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tableNameField, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(label4, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(schemaField, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(connectionLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(outputFileField, GroupLayout.PREFERRED_SIZE, 327, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(browseButton, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(connectionLabel)
                        .addGap(18, 18, 18)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addComponent(label4)
                            .addComponent(schemaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label5)
                            .addComponent(tableNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label3)
                            .addComponent(outputFileField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(browseButton))
                        .addGap(19, 19, 19))
            );
        }

        //---- executeButton ----
        executeButton.setText("Execute");

        //---- cancelButton ----
        cancelButton.setText("Cancel");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(executeButton)
                            .addGap(18, 18, 18)
                            .addComponent(cancelButton)
                            .addGap(8, 8, 8))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(cancelButton)
                        .addComponent(executeButton))
                    .addGap(9, 9, 9))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel connectionLabel;
    private JTextField outputFileField;
    private JButton browseButton;
    private JLabel label4;
    private JTextField schemaField;
    private JLabel label5;
    private JTextField tableNameField;
    private JLabel label3;
    private JButton executeButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
