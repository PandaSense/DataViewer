/*
 * DVSearchFieldView.java  2/6/13 1:04 PM
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
package com.dv.ui;

import com.dv.search.AutoCompletionField;
import com.dv.ui.action.DVInstanceConnectAction;

import java.awt.event.ActionEvent;


/**
 * @author xyma
 */
public class DVSearchFieldView extends AutoCompletionField{

    DVFrame parent;
    boolean isSearchDB = true;

    public DVSearchFieldView(DVFrame parent) {
        super();
        this.parent = parent;
        init();
    }


    public void init() {

        addActionListener(new DVInstanceConnectAction(parent, this, 0));

        requestFocus(true);
    }

    public void setIsSearchDB(boolean isOk) {

        isSearchDB = isOk;

    }

    public boolean getIsSearchDB() {
        return isSearchDB;
    }

    public void actionPerformed(ActionEvent e) {

    }
}
