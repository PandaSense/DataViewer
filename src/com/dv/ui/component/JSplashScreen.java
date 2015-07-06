/*
 * JSplashScreen.java  2/6/13 1:04 PM
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

import com.dv.prop.DVPropMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.UIManager;

/**
 * @author xyma
 */
public class JSplashScreen extends JComponent {

    //{{{ SplashScreen constructor
    public JSplashScreen() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setBackground(Color.white);

        setFont(defaultFont);
        fm = getFontMetrics(defaultFont);

        image = getToolkit().getImage(
                getClass().getResource("/com/dv/ui/resources/icon/normal/loading.png"));

        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);

        try {
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Dimension screen = getToolkit().getScreenSize(); // sane default
        win = new JWindow();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        GraphicsDevice gd = gs[0];
        if (gd != null) {
            GraphicsConfiguration gconf = gd.getDefaultConfiguration();
            if (gconf != null) {
                Rectangle bounds = gconf.getBounds();
                screen = new Dimension(bounds.width, bounds.height);
            }
        }
        Dimension size = new Dimension(image.getWidth(this) + 2,
                image.getHeight(this) + 2 + PROGRESS_HEIGHT);
        win.setSize(size);

        win.getContentPane().add(this, BorderLayout.CENTER);

        win.setLocation((screen.width - size.width) / 2,
                (screen.height - size.height) / 2);
        win.validate();
        win.setVisible(true);
    } //}}}

    //{{{ dispose() method
    public void dispose() {
        win.dispose();
    } //}}}

    //{{{ advance() methods
    public synchronized void advance() {
        logAdvanceTime(null);
        progress++;
        repaint();

        // wait for it to be painted to ensure progress is updated
        // continuously
        try {
            wait();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public synchronized void advance(String label) {
        logAdvanceTime(label);
        progress++;
        this.label = label;
        repaint();

        // wait for it to be painted to ensure progress is updated
        // continuously
        try {
            wait();
        } catch (InterruptedException ie) {
        }
    } //}}}

    //{{{ logAdvanceTime() method
    private void logAdvanceTime(String label) {
        long currentTime = System.currentTimeMillis();
        if (lastLabel != null) {
//			Log.log(Log.DEBUG, SplashScreen.class,
//				lastLabel + ':' + (currentTime - lastAdvanceTime)
//				+ "/" + (currentTime - firstAdvanceTime) + "ms");
        }
        if (label != null) {
            lastLabel = label;
            lastAdvanceTime = currentTime;

        }
    } //}}}

    //{{{ paintComponent() method
    @Override
    public synchronized void paintComponent(Graphics g) {
        Dimension size = getSize();

        g.setColor(Color.black);
        g.drawRect(0, 0, size.width - 1, size.height - 1);

        g.drawImage(image, 1, 1, this);

        // XXX: This should not be hardcoded
        g.setColor(Color.white);
        g.fillRect(1, image.getHeight(this) + 1,
                ((win.getWidth() - 2) * progress) / PROGRESS_COUNT, PROGRESS_HEIGHT);

        g.setColor(Color.black);

        if (label != null) {
            int drawOffsetX = (getWidth() - fm.stringWidth(label)) / 2;
            int drawOffsetY = image.getHeight(this) + (PROGRESS_HEIGHT
                    + fm.getAscent() + fm.getDescent()) / 2;

            paintString(g, label, drawOffsetX, drawOffsetY);
        }

        String version = DVPropMain.DV_NAME + " " + DVPropMain.DV_VERSION;

        int drawOffsetX = (getWidth() / 2) - (fm.stringWidth(version) / 2);
        int drawOffsetY = image.getHeight(this) - fm.getDescent() - 2;

        paintString(g, version, drawOffsetX, drawOffsetY);

        notify();
    } //}}}

    //{{{ paintString() method
    private void paintString(Graphics g, String version, int drawOffsetX,
                             int drawOffsetY) {
        g.setFont(labelFont);

        g.setColor(versionColor1);
        g.drawString(version, drawOffsetX, drawOffsetY);
        // Draw a highlight effect
        g.setColor(versionColor2);
        g.drawString(version, drawOffsetX + 1, drawOffsetY + 1);
    } //}}}

    //{{{ private members
    private final FontMetrics fm;
    private final JWindow win;
    private final Image image;
    private int progress;
    private static final int PROGRESS_HEIGHT = 20;
    private static final int PROGRESS_COUNT = 28;
    private String label;
    private String lastLabel;
    private long firstAdvanceTime = System.currentTimeMillis();
    private long lastAdvanceTime = System.currentTimeMillis();
    private Font defaultFont = new Font("Dialog", Font.PLAIN, 10);
    //    private Font labelFont = UIManager.getFont("Label.font").deriveFont(9.8f);
    private Font labelFont = UIManager.getFont("Label.font");
    //    private Color versionColor1 = new Color(55, 55, 55);
    private Color versionColor2 = new Color(255, 255, 255, 50);

    private Color versionColor1 = Color.BLACK;
//    private Color versionColor2 = Color.BLACK;

    //}}}
}
