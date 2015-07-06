/*
 * DataViewerTableInforDialog.java  4/25/13 1:57 PM
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
 * Created by JFormDesigner on Thu Apr 25 13:57:38 CST 2013
 */

package com.dv.sqlbuild;

import javax.swing.border.*;

import com.dv.prop.DVPropMain;
import com.dv.ui.action.DVTableSelectAction;

import java.awt.*;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author Nick
 */
public class DataViewerTableInforDialog extends JDialog {

    DVSqlBuildTablePanel tablePanel;

    public  HashMap<Integer, String> remarkMap = new HashMap<Integer, String>();

    public DataViewerTableInforDialog(Frame owner, String schame, String tableName, Vector cols, Vector heads) {
        super(owner, false);
        setTitle(DVPropMain.DV_NAME + "-" + DVPropMain.DV_VERSION + "-Table Information for [ " + schame + "." + tableName + " ]");
        initComponents();
        setTableInforpanel(cols, heads);

        remarkArea.setWrapStyleWord(true);
        remarkArea.setLineWrap(true);

        Point oh = owner.getLocationOnScreen();
        setLocation((int) oh.getX() + owner.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + owner.getHeight() / 2 - getHeight() / 2);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void setTableInforpanel(Vector cols, Vector heads) {
        setTableCols( cols,  heads);
    }

    public void setTableCols(Vector cols, Vector heads) {
        Vector<Vector> rows = new Vector<Vector>();
        remarkMap.clear();
        Vector singleRow = new Vector();
        for (int i = 0; i < cols.size(); i++) {
            singleRow = (Vector) cols.elementAt(i);
            remarkMap.put(i, singleRow.elementAt(singleRow.size() - 1).toString());
            singleRow.remove(singleRow.size() - 1);
            rows.addElement(singleRow);
        }
        heads.remove(heads.size() - 1);
        tablePanel = new DVSqlBuildTablePanel(rows, heads);
        tablePanel.getResultTable().getSelectionModel().addListSelectionListener(new DVTableSelectAction(tablePanel.getResultTable(),this));
        this.getMainPanel().setLayout(new BorderLayout());
        this.getMainPanel().add(tablePanel, BorderLayout.CENTER);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        splitPane1 = new JSplitPane();
        mainPanel = new JPanel();
        remarkPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        remarkArea = new JTextArea();

        //======== this ========
        Container contentPane = getContentPane();

        //======== splitPane1 ========
        {
            splitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPane1.setDividerLocation(470);

            //======== mainPanel ========
            {
                mainPanel.setBorder(null);

                GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
                mainPanel.setLayout(mainPanelLayout);
                mainPanelLayout.setHorizontalGroup(
                    mainPanelLayout.createParallelGroup()
                        .addGap(0, 597, Short.MAX_VALUE)
                );
                mainPanelLayout.setVerticalGroup(
                    mainPanelLayout.createParallelGroup()
                        .addGap(0, 469, Short.MAX_VALUE)
                );
            }
            splitPane1.setTopComponent(mainPanel);

            //======== remarkPanel ========
            {
                remarkPanel.setBorder(new TitledBorder("Remark Information"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(remarkArea);
                }

                GroupLayout remarkPanelLayout = new GroupLayout(remarkPanel);
                remarkPanel.setLayout(remarkPanelLayout);
                remarkPanelLayout.setHorizontalGroup(
                    remarkPanelLayout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                );
                remarkPanelLayout.setVerticalGroup(
                    remarkPanelLayout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                );
            }
            splitPane1.setBottomComponent(remarkPanel);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(splitPane1)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(splitPane1)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    public  HashMap<Integer, String> getRemarkMap() {
        return remarkMap;
    }
    public JTextArea getRemarkArea() {
        return remarkArea;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSplitPane splitPane1;
    private JPanel mainPanel;
    private JPanel remarkPanel;
    private JScrollPane scrollPane1;



    private JTextArea remarkArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
