/*
 * UpDownArrow.java  2/6/13 1:04 PM
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

package com.dv.sort;

import java.awt.*;

import javax.swing.*;

public class UpDownArrow implements Icon {
    /**
     * size
     * <br>
     * The size of the Direcation Icon.Due to the Icon is square,the height and
     * width of Icon is this value.
     * <p/>
     * <p/>
     */
    private int size = 12;

    /**
     * UP
     * <br>
     * Static value of direction for this class.
     * <p/>
     * <p/>
     */
    public static final int UP = 0;
    /**
     * DOWN
     * <br>
     * Static value of direction for this class.
     * <p/>
     * <p/>
     */
    public static final int DOWN = 1;
    /**
     * direction
     * <br>
     * The value of current direction of icon.
     */
    private int direction;

    public UpDownArrow(int i) {
        direction = i;
    }

    public int getIconHeight() {
        return size;
    }

    public int getIconWidth() {
        return size;
    }

    /**
     * paintIcon
     * <br> Use the Java2D to paint the icon real time.
     *
     * @param component Component
     * @param g         Graphics
     * @param i         int
     * @param j         int
     */
    public void paintIcon(Component component, Graphics g, int i, int j) {
        int k = i + size / 2;
        int l = i + 1;
        int i1 = (i + size) - 2;
        int j1 = j + 1;
        int k1 = (j + size) - 2;
        Color color = (Color) UIManager.get("controlDkShadow");
        if (direction == 0) {
            g.setColor(Color.white);
            g.drawLine(l, k1, i1, k1);
            g.drawLine(i1, k1, k, j1);
            g.setColor(color);
            g.drawLine(l, k1, k, j1);
        } else {
            g.setColor(color);
            g.drawLine(l, j1, i1, j1);
            g.drawLine(l, j1, k, k1);
            g.setColor(Color.white);
            g.drawLine(i1, j1, k, k1);
        }
    }
} 
