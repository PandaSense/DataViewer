/*
 * DVTableSelectAction.java  4/30/13 11:36 AM
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

package com.dv.ui.action;

import com.dv.sqlbuild.DataViewerTableInforDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.HashMap;


public class DVTableSelectAction implements ListSelectionListener {

    JTable table;
    DataViewerTableInforDialog dialog;
    public DVTableSelectAction(JTable table,DataViewerTableInforDialog dialog) {
        this.table=table;
        this.dialog=dialog;

    }

    public void valueChanged(ListSelectionEvent e) {
        int rowNumber=table.getSelectedRow();
        dialog.getRemarkArea().setText(dialog.getRemarkMap().get(rowNumber));
    }
}
