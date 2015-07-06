
/*
 * BottomLineBorder.java  2/6/13 1:04 PM
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

package com.dv.ui.component;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;


public class BottomLineBorder extends AbstractBorder {

    private int horizInsets;

    public BottomLineBorder(int horizInsets) {
        this.horizInsets = horizInsets;
    }

    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = 0;
        insets.right = insets.left = horizInsets;
        insets.bottom = 1;
        return insets;
    }

    public void paintBorder(Component c, Graphics g, int x, int y,
                            int width, int height) {
        g.setColor(UIManager.getColor("controlDkShadow"));
        y = y + height - 1;
        g.drawLine(x, y, x + width - 1, y);
    }

}