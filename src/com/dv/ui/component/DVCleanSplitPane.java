/*
 * DVCleanSplitPane.java  2/6/13 1:04 PM
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

import javax.swing.JSplitPane;

import org.fife.ui.CleanSplitPaneUI;

/**
 * @author xyma
 */
public class DVCleanSplitPane extends JSplitPane {

    public DVCleanSplitPane() {
        super();
        setUI(new CleanSplitPaneUI());
    }

}
