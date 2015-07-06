/*
 * RolloverButton.java  2/6/13 1:04 PM
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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dv.ui.component;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;

//}}}

/**
 * Class for buttons that implement rollovers
 * <p/>
 * If you wish to have rollovers on your buttons, use this class.
 * <p/>
 * Unlike the Swing rollover support, this class works outside of
 * <code>JToolBar</code>s, and does not require undocumented client property
 * hacks or JDK1.4-specific API calls.<p>
 * <p/>
 * Note: You should not call
 * <code>setBorder()</code> on your buttons, as they probably won't work
 * properly.
 *
 * @version $Id: RolloverButton.java 21506 2012-03-29 17:58:53Z ezust $
 */
public class RolloverButton extends JButton {
    //{{{ RolloverButton constructor

    /**
     * Setup the border (invisible initially)
     */
    public RolloverButton() {
        //setContentAreaFilled(true);
        addMouseListener(new MouseOverHandler());
    } //}}}

    public RolloverButton(String text) {
        //setContentAreaFilled(true);
        this.setText(text);
        addMouseListener(new MouseOverHandler());
    } //}}}

    //{{{ RolloverButton constructor

    /**
     * Setup the border (invisible initially)
     *
     * @param icon the icon of this button
     */
    public RolloverButton(Icon icon, String text) {
        this();


        setIcon(icon);
        this.setText(text);
    } //}}}

    public RolloverButton(String text, Icon icon) {
        this();


        setIcon(icon);
        this.setText(text);
    } //}}}

    //{{{ updateUI() method
    public void updateUI() {
        super.updateUI();
        //setBorder(originalBorder);
        setBorderPainted(false);
        setRequestFocusEnabled(false);
        setMargin(new Insets(1, 1, 1, 1));
    } //}}}

    //{{{ setEnabled() method
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setBorderPainted(false);
        repaint();
    } //}}}

    //{{{ setBorderPainted() method
    public void setBorderPainted(boolean b) {
        try {
            revalidateBlocked = true;
            super.setBorderPainted(b);
            setContentAreaFilled(b);
        } finally {
            revalidateBlocked = false;
        }
    } //}}}

    //{{{ revalidate() method

    /**
     * We block calls to revalidate() from a setBorderPainted(), for performance
     * reasons.
     */
    public void revalidate() {
        if (!revalidateBlocked) {
            super.revalidate();
        }
    } //}}}

    //{{{ paint() method
    public void paint(Graphics g) {
        if (isEnabled()) {
            super.paint(g);
        } else {
            Graphics2D g2 = (Graphics2D) g;
            g2.setComposite(c);
            super.paint(g2);
        }
    } //}}}

    //{{{ Private members
    private static final AlphaComposite c = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, 0.5f);
    private boolean revalidateBlocked;

    //{{{ MouseHandler class

    /**
     * Make the border visible/invisible on rollovers
     */
    class MouseOverHandler extends MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            setContentAreaFilled(false);
            setBorderPainted(isEnabled());
        }

        public void mouseExited(MouseEvent e) {
            setContentAreaFilled(false);
            setBorderPainted(false);
        }
    } //}}}
    //}}}
}