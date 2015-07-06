/*
 * DVResultGridPanel.java  2/6/13 1:04 PM
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

package com.dv.ui.gridresult;

import com.jidesoft.swing.JideBoxLayout;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class DVResultGridPanel extends JPanel {

//    GridLayout layout;

    JideBoxLayout layout;

    public DVResultGridPanel() {
        super();
    }

    public void setResultTableLayout(int rows) {

//        layout = new GridLayout(rows, 1, 1, 10);
        layout=new JideBoxLayout(this,1,10);
        setLayout(layout);
    }


    public void removeAll() {
        super.removeAll();
        this.repaint();
    }

    public void addResultTable(DVResultTablePanel tablePanel) {

        this.add(tablePanel);
        this.repaint();
        this.validate();

    }
}
