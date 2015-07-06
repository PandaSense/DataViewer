/*
 * DVLoadProgressView.java  2/6/13 1:04 PM
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
package com.dv.search;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * @author xyma
 */
public class DVLoadProgressView extends JWindow {

    private JLabel statusLabel = new JLabel("Connect DataBase instance, please wait...");
    private static final String MSG = "com.dv.ui.DataViewerToolBar";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);

    public JLabel getLoadingLabel() {
        return statusLabel;
    }

    public DVLoadProgressView(Frame onwer) {
        super(onwer);

        getContentPane().setLayout(new BorderLayout());
        statusLabel.setIcon(new ImageIcon(DVLoadProgressView.class.getResource(mainViewResource.getString("version"))));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setVisible(true);
        statusLabel.setOpaque(false);
        getContentPane().add(statusLabel, BorderLayout.CENTER);

        Point oh = onwer.getLocationOnScreen();
        setLocation((int) oh.getX() + onwer.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + onwer.getHeight() / 2 - getHeight() / 2);

        setAlwaysOnTop(true);
        pack();
        setVisible(true);
    }

}
