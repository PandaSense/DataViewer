/*
 * DVTableCellRenderer.java  2/6/13 1:04 PM
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

package com.dv.util;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.BorderFactory;

public class DVTableCellRenderer extends JLabel implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {


        if (row < 0) {
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(new Color(45, 145, 180));
            setForeground(Color.white);
            this.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));

        } else {

            setHorizontalAlignment(SwingConstants.LEFT);

            if (row % 2 == 0) {
                setBackground(new Color(255, 255, 205));
                setForeground(Color.blue);

            } else {
                setBackground(new Color(255, 220, 150));
                setForeground(Color.red);
            }
        }
        setText(value.toString());
        setOpaque(true);
        return this;
    }
}

