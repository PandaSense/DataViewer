/*
 * DefaultInlineFieldButton.java  6/7/13 2:34 PM
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

import javax.swing.Action;
import javax.swing.Icon;


import com.dv.ui.eq.GUIConstants;

public class DefaultInlineFieldButton extends DefaultButton {

    public DefaultInlineFieldButton() {
        super();
    }

    public DefaultInlineFieldButton(Action a) {
        super(a);
    }

    public DefaultInlineFieldButton(Icon icon) {
        super(icon);
    }

    public DefaultInlineFieldButton(String text, Icon icon) {
        super(text, icon);
    }

    public DefaultInlineFieldButton(String text) {
        super(text);
    }

    public int getHeight() {
        return GUIConstants.DEFAULT_FIELD_HEIGHT + 1;
    }

}