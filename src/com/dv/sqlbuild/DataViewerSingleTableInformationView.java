/*
 * DataViewerSingleTableInformationView.java  4/25/13 11:51 AM
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
 * Created by JFormDesigner on Thu Apr 25 11:51:11 CST 2013
 */

package com.dv.sqlbuild;

import com.dv.ui.gridresult.DVResultTablePanel;

import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import java.util.Vector;

/**
 * @author Nick
 */
public class DataViewerSingleTableInformationView extends JPanel {

    Vector cols, heads;
    DVResultTablePanel basicTable;

    public DataViewerSingleTableInformationView(Vector cols, Vector heads) {

        this.cols = cols;
        this.heads = heads;
        initComponents();
        basicTable=new DVResultTablePanel(cols,heads);
        this.add(basicTable);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

        //======== this ========
        setBorder(new TitledBorder("Basic Table Information"));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGap(0, 585, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGap(0, 545, Short.MAX_VALUE)
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
