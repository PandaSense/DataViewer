/*
 * DefaultNumberTextField.java  6/7/13 2:36 PM
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

import java.awt.Insets;

import com.dv.ui.eq.GUIConstants;

public class DefaultNumberTextField extends NumberTextField {

    public DefaultNumberTextField() {

        super();
    }

    public DefaultNumberTextField(int digits) {

        super(digits);
    }

    public Insets getMargin() {

        return GUIConstants.DEFAULT_FIELD_MARGIN;
    }

    public int getHeight() {

        return super.getHeight() < GUIConstants.DEFAULT_FIELD_HEIGHT ?
                GUIConstants.DEFAULT_FIELD_HEIGHT : super.getHeight();
    }

}
