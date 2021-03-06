/*
 * StatusBarLabel.java  2/6/13 1:04 PM
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

package com.dv.ui.editor;

import com.dv.util.DataViewerUtilities;

import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * Status bar panel label.
 *
 * @author Takis Diakoumis
 * @version $Revision: 1460 $
 * @date $Date: 2009-01-25 11:06:46 +1100 (Sun, 25 Jan 2009) $
 */
public class StatusBarLabel extends JLabel {

    /**
     * Indicates to paint the left border
     */
    private boolean paintLeft;

    /**
     * Indicates to paint the right border
     */
    private boolean paintRight;

    /**
     * Indicates to paint the top border
     */
    private boolean paintTop;

    /**
     * Indicates to paint the bottom border
     */
    private boolean paintBottom;

    /**
     * the label height
     */
    private int height;

    public StatusBarLabel(boolean paintTop, boolean paintLeft,
                          boolean paintBottom, boolean paintRight) {
        this(paintTop, paintLeft, paintBottom, paintRight, 20);
    }

    public StatusBarLabel(boolean paintTop, boolean paintLeft,
                          boolean paintBottom, boolean paintRight, int height) {
        this.paintTop = paintTop;
        this.paintLeft = paintLeft;
        this.paintBottom = paintBottom;
        this.paintRight = paintRight;
        this.height = height;
        setVerticalAlignment(JLabel.CENTER);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(DataViewerUtilities.getDefaultBorderColour());

        int width = getWidth();
        int height = getHeight();

        if (paintTop) {
            g.drawLine(0, 0, width, 0);
        }
        if (paintBottom) {
            g.drawLine(0, height - 1, width, height - 1);
        }

        if (paintLeft) {
            g.drawLine(0, 0, 0, height);
        }
        if (paintRight) {
            g.drawLine(width - 1, 0, width - 1, height);
        }

    }

}

