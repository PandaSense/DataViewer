/*
 * ClosableTabbedPane.java  2/6/13 1:04 PM
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

import com.jidesoft.swing.JideTabbedPane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

//public class ClosableTabbedPane extends JTabbedPane {

public class ClosableTabbedPane extends JideTabbedPane {


//    private TabCloseUI closeUI = new TabCloseUI(this);

//    public ClosableTabbedPane() {
//            super();
//            setOpaque(false);
//            setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
//        }

    public ClosableTabbedPane() {
        super();
        this.setShowTabButtons(true);
        this.setShowCloseButtonOnTab(true);
        this.setColorTheme(JideTabbedPane.COLOR_THEME_DEFAULT);
        this.setTabShape(JideTabbedPane.SHAPE_VSNET);
        this.setTabColorProvider(JideTabbedPane.ONENOTE_COLOR_PROVIDER);
        this.setShowCloseButtonOnSelectedTab(true);


    }

    public ClosableTabbedPane(boolean hasCloseButtonInTab) {
        super();
        this.setColorTheme(JideTabbedPane.COLOR_THEME_DEFAULT);
        this.setTabShape(JideTabbedPane.SHAPE_VSNET);
        this.setTabColorProvider(JideTabbedPane.ONENOTE_COLOR_PROVIDER);
    }

    public String getTabTitleAt(int index) {
        return super.getTitleAt(index).trim();
    }

}
