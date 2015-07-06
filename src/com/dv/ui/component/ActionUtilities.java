/*
 * ActionUtilities.java  6/7/13 2:28 PM
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
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

/**
 * @author xyma
 */
public class ActionUtilities {

    private ActionUtilities() {
    }

    public static JCheckBox createCheckBox(ActionListener actionListener,
                                           String name,
                                           String command,
                                           boolean selected) {
        JCheckBox item = new JCheckBox(name, selected);
        item.setActionCommand(command);

        if (actionListener != null) {
            item.addActionListener(actionListener);
        }

        return item;
    }

    public static JCheckBox createCheckBox(ActionListener actionListener,
                                           String name,
                                           String command) {
        return createCheckBox(actionListener, name, command, false);
    }

    public static JCheckBox createCheckBox(String name,
                                           String command,
                                           boolean selected) {
        return createCheckBox(null, name, command, selected);
    }

    public static JCheckBox createCheckBox(String name,
                                           String command) {
        return createCheckBox(null, name, command, false);
    }

    public static JButton createButton(ActionListener actionListener,
                                       Icon icon,
                                       String name,
                                       String command) {
        JButton item = new DefaultButton(name);
        item.setActionCommand(command);

        if (icon != null) {
            item.setIcon(icon);
        }

        if (actionListener != null) {
            item.addActionListener(actionListener);
        }

        return item;
    }

    public static JButton createButton(ActionListener actionListener,
                                       String command,
                                       Icon icon,
                                       String toolTipText) {
        JButton item = new DefaultButton(icon);
        item.setMargin(new Insets(1, 1, 1, 1));

        item.setToolTipText(toolTipText);
        item.setActionCommand(command);

        if (actionListener != null) {
            item.addActionListener(actionListener);
        }

        return item;
    }

    public static JButton createButton(ActionListener actionListener,
                                       String name,
                                       String command) {
        JButton item = new DefaultButton(name);
        item.setActionCommand(command);
        item.addActionListener(actionListener);
        return item;
    }

    public static JButton createButton(String name, String command) {
        JButton item = new DefaultButton(name);
        item.setActionCommand(command);
        return item;
    }


}