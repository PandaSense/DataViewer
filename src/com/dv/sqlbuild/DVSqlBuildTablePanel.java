/*
 * DVSqlBuildTablePanel.java  4/26/13 3:03 PM
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

package com.dv.sqlbuild;

import com.dv.ui.component.LineNumberTableRowHeader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.util.Enumeration;
import java.util.Vector;

public class DVSqlBuildTablePanel  extends JScrollPane{

    JTable resultTable;
    JScrollPane resultScrollPane1;
    LineNumberTableRowHeader tableLineNumber;
    Vector cols, heads;
    DefaultTableModel modle;

    public DVSqlBuildTablePanel(Vector cols, Vector heads) {
        super();
        this.cols = cols;
        this.heads = heads;
        setData();
    }
    private void setData() {

        resultTable = new JTable();
        resultTable.setCellSelectionEnabled(true);
        resultTable.setDoubleBuffered(true);
        resultTable.setDragEnabled(true);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        modle = new DefaultTableModel(cols, heads);

        resultTable.setModel(modle);

        this.setViewportView(resultTable);

        tableLineNumber = new LineNumberTableRowHeader(this, resultTable);

        this.setRowHeaderView(tableLineNumber);
//        resultTable.setAutoCreateRowSorter(true);
        FitTableColumns(resultTable);

        this.setPreferredSize(resultTable.getPreferredSize());
    }

    public void FitTableColumns(JTable myTable) {

        JTableHeader header = myTable.getTableHeader();

        int rowCount = myTable.getRowCount();

        Enumeration columns = myTable.getColumnModel().getColumns();

        while (columns.hasMoreElements()) {

            TableColumn column = (TableColumn) columns.nextElement();

            int col = header.getColumnModel().getColumnIndex(
                    column.getIdentifier());

            int width = (int) myTable.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(myTable,
                    column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();

            for (int row = 0; row < rowCount; row++) {

                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
                        myTable.getValueAt(row, col), false, false,
                        row, col).getPreferredSize().getWidth();

                width = Math.max(width, preferedWidth)+1;

            }
            header.setResizingColumn(column);

            column.setWidth(width + myTable.getIntercellSpacing().width);

        }

    }
    public JTable getResultTable() {
        return resultTable;
    }
}
