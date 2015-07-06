/*
 * TopBottomLineBorder.java  7/23/13 12:44 PM
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

package com.dv.ui.datepicker;

import java.awt.*;
import javax.swing.border.*;

/**
 * <p>Title: OpenSwing</p>
 * <p>Description: TopBottomLineBorder只带上下两条线的边界Border</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author <a href="mailto:sunkingxie@hotmail.com">SunKing</a>
 * @version 1.0
 */
public class TopBottomLineBorder extends AbstractBorder{
    private Color lineColor;
    public TopBottomLineBorder(Color color){
        lineColor = color;
    }

    public void paintBorder(Component c, Graphics g, int x, int y,
                            int width, int height){
        g.setColor(lineColor);
        g.drawLine(0, 0, c.getWidth(), 0);
        g.drawLine(0, c.getHeight() - 1, c.getWidth(),
                c.getHeight() - 1);
    }
}
