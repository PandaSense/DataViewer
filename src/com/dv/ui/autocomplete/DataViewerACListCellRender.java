/*
 * DataViewerACListCellRender.java  11/8/13 5:19 PM
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

package com.dv.ui.autocomplete;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.ImageIcon;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import java.awt.Color;


public class DataViewerACListCellRender extends DefaultListCellRenderer {
    /**
     * The renderer to fall back on if one isn't specified by a provider.
     * This is usually <tt>this</tt>.
     */
    private ListCellRenderer fallback;


    /**
     * Returns the fallback cell renderer.
     *
     * @return The fallback cell renderer.
     * @see #setFallbackCellRenderer(ListCellRenderer)
     */
    public ListCellRenderer getFallbackCellRenderer() {
        return fallback;
    }


    /**
     * {@inheritDoc}
     */
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean selected, boolean hasFocus) {
//        Completion c = (Completion)value;
//        CompletionProvider p = c.getProvider();
//        ListCellRenderer r = p.getListCellRenderer();

        String s = value.toString();
        setText(s);
        setIcon(new ImageIcon(DataViewerACListCellRender.class.getResource("/com/dv/ui/resources/icon/ac_table.png")));

        if (selected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);

		/*
        if (r!=null) {
			return r.getListCellRendererComponent(list, value, index, selected,
													hasFocus);
		}

		if (fallback==null) {
			return super.getListCellRendererComponent(list, value, index,
												selected, hasFocus);
		}
		return fallback.getListCellRendererComponent(list, value, index,
													selected, hasFocus);
													*/

        return this;

    }


    /**
     * Sets the fallback cell renderer.
     *
     * @param fallback The fallback cell renderer.  If this is
     *                 <code>null</code>, <tt>this</tt> will be used.
     * @see #getFallbackCellRenderer()
     */
    public void setFallbackCellRenderer(ListCellRenderer fallback) {
        this.fallback = fallback;
    }


    /**
     * {@inheritDoc}
     */
    public void updateUI() {
        super.updateUI();
        if ((fallback instanceof JComponent) && fallback != this) {
            ((JComponent) fallback).updateUI();
        }
    }


}
