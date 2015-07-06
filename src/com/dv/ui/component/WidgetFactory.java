/*
 * WidgetFactory.java  6/7/13 2:30 PM
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

import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public final class WidgetFactory {

    public static JButton createInlineFieldButton(String text) {

        return new DefaultInlineFieldButton(text);
    }

    public static JButton createInlineFieldButton(String text, String actionCommand) {

        JButton button = new DefaultInlineFieldButton(text);
        button.setActionCommand(actionCommand);

        return button;
    }

    public static JButton createButton(String text) {

        return new DefaultButton(text);
    }

    public static JComboBox createComboBox(Vector<?> items) {

        return new DefaultComboBox(items);
    }

    public static JComboBox createComboBox(ComboBoxModel model) {

        return new DefaultComboBox(model);
    }

    public static JComboBox createComboBox(Object[] items) {

        return new DefaultComboBox(items);
    }

    public static JComboBox createComboBox() {

        return new DefaultComboBox();
    }

    public static NumberTextField createNumberTextField() {

        return new DefaultNumberTextField();
    }

    public static JTextField createTextField() {

        return new DefaultTextField();
    }

    public static JTextField createTextField(String text) {

        return new DefaultTextField(text);
    }

}