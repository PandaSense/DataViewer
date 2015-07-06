/*
 * FindAndReplaceFrame.java  2/6/13 1:04 PM
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;


import com.dv.util.DataViewerUtilities;

public class FindAndReplaceFrame extends JDialog {

    public JTextComponent textComponentP;
    public FindReplaceDialog far;

    public FindAndReplaceFrame(JFrame parent, JTextComponent textComponent) {

        super(parent, false);
        textComponentP = textComponent;
        try {
            far = new FindReplaceDialog(this, 0, textComponentP);

        } catch (Exception eeee) {
            eeee.printStackTrace();
        }

        setTitle(far.TITLE);
        getContentPane().add(far, BorderLayout.CENTER);

        Dimension scrSize = parent.getSize();
        int height = (int) scrSize.getHeight();
        int width = (int) scrSize.getWidth();
        setLocation((width / 2 - getWidth() / 2), height / 2 - getHeight() / 2 - 50);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
    }

    public void dispose() {
        DataViewerUtilities.scheduleGC();
        super.dispose();
    }

    public void setTextComponet(JTextComponent textComponent) {
        textComponentP = textComponent;
        far.setTextComponet(textComponent);
    }
}
