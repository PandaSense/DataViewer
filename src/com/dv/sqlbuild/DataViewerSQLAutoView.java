/*
 * Created by JFormDesigner on Wed Apr 24 15:09:33 CST 2013
 */

package com.dv.sqlbuild;

import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

/**
 * @author Nick
 */
public class DataViewerSQLAutoView extends JPanel {
    public DataViewerSQLAutoView() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        label1 = new JLabel();
        schemaName = new JTextField();
        label3 = new JLabel();
        tableNametextField = new JTextField();
        LoadTablebutton = new JButton();
        tableInformationMainPanel = new JPanel();

        //======== this ========

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Base Information"));

            //---- label1 ----
            label1.setText("Schema :");

            //---- label3 ----
            label3.setText("Table Name :");

            //---- LoadTablebutton ----
            LoadTablebutton.setText("Load");

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(label1, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                            .addComponent(schemaName, GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(tableNametextField, GroupLayout.PREFERRED_SIZE, 421, GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(LoadTablebutton, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
                            .addComponent(label3, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(11, Short.MAX_VALUE))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label1)
                            .addComponent(label3))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(schemaName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(LoadTablebutton)
                            .addComponent(tableNametextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(20, Short.MAX_VALUE))
            );
        }

        //======== tableInformationMainPanel ========
        {
            tableInformationMainPanel.setBorder(new TitledBorder("DataBase Table Information "));

            GroupLayout tableInformationMainPanelLayout = new GroupLayout(tableInformationMainPanel);
            tableInformationMainPanel.setLayout(tableInformationMainPanelLayout);
            tableInformationMainPanelLayout.setHorizontalGroup(
                tableInformationMainPanelLayout.createParallelGroup()
                    .addGap(0, 733, Short.MAX_VALUE)
            );
            tableInformationMainPanelLayout.setVerticalGroup(
                tableInformationMainPanelLayout.createParallelGroup()
                    .addGap(0, 486, Short.MAX_VALUE)
            );
        }

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup()
                        .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tableInformationMainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(tableInformationMainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel label1;
    private JTextField schemaName;
    private JLabel label3;
    private JTextField tableNametextField;
    private JButton LoadTablebutton;
    private JPanel tableInformationMainPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
