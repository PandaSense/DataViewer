/*
 * NoFocusButton.java  2/6/13 1:04 PM
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

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;

/**
 * @author Cypress
 */
public class NoFocusButton extends JButton {

    private float alpha = 1.0f;

    public NoFocusButton() {
        super();
        init();
    }

    public NoFocusButton(Icon ico) {
        super(ico);
        init();
    }

    private void init() {
        setFocusPainted(false);
        setRolloverEnabled(true);
        setFocusable(false);

    }

    public boolean isFocusTraversable() {
        return false;
    }

    public void requestFocus() {
    }

    public void paint(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;
        Composite composite = g2D.getComposite();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2D.setComposite(alphaComposite);
        super.paint(g);
        g2D.setComposite(composite);
    }
}
