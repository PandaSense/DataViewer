/*
 * SplashScreen.java  2/6/13 1:04 PM
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
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Toolkit;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;

/**
 * @author xyma
 */
public class SplashScreen extends JWindow {

    private static final long serialVersionUID = 1L;
    private JLabel imageLabel; // Forced repaint needed on OS X
    private ProgressBar progressBar;
    private static final int statusBarHeight =5;
    private final ImageIcon image;
    private final FontMetrics fm;
    private Font defaultFont = new Font("Dialog", Font.PLAIN, 10);
    private Font labelFont = UIManager.getFont("Label.font");
    private Color versionColor1 = Color.BLACK;
    private Color versionColor2 = new Color(255, 255, 255, 50);

    /**
     * Creates a new
     * <code>SplashScreen</code> with default color scheme.
     *
     * @param splashScreenPath Path to an image file that a
     *                         <code>ClassLoader</code> will understand.
     * @param statusText       The status text to initially display.
     */
    public SplashScreen(String splashScreenPath, String statusText) {
//        this(splashScreenPath, statusText, new Color(190, 190, 190),
//                new Color(44, 70, 154), new Color(75, 141, 199), Color.WHITE);
        this(splashScreenPath, "", new Color(190, 190, 190),
                new Color(44, 70, 154), new Color(75, 141, 199), Color.WHITE);
    }

    /**
     * Creates a new
     * <code>SplashScreen</code>.
     *
     * @param splashScreenPath Path to an image file that a
     *                         <code>ClassLoader</code> will understand.
     * @param statusText       The status text to initially display.
     * @param statusBackground The color to use as the status bar's background.
     * @param foreground1      The color to use for the first part of the gradient
     *                         fill of the status bar.
     * @param foreground2      The color to use for the second part of the gradient
     *                         fill of the status bar.
     * @param textColor        The color to use for the status text.
     */
    public SplashScreen(String splashScreenPath, String statusText,
                        Color statusBackground, Color foreground1,
                        Color foreground2, Color textColor) {

        JPanel contentPane = new JPanel(new BorderLayout());
        fm = getFontMetrics(defaultFont);

        image = new ImageIcon(SplashScreen.class.getResource(splashScreenPath));

        // Create a panel for the splash screen image.
        imageLabel = new JLabel(image);
        contentPane.add(imageLabel);

        // Create the "progress bar" at the bottom.
        progressBar = new ProgressBar(image.getIconWidth(),
                statusText, statusBackground,
                foreground1, foreground2, textColor);
        contentPane.add(progressBar, BorderLayout.SOUTH);

        // Combine everything and get ready to go.
        setContentPane(contentPane);
        ComponentOrientation orientation = ComponentOrientation.
                getOrientation(getLocale());
        applyComponentOrientation(orientation);
        this.pack();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.setLocationRelativeTo(null);        // Center on the screen.

    }

    /**
     * Updates the percent complete bar and the associated status text.
     *
     * @param statusText      The new status text to display.
     * @param percentComplete The new percentage to have filled in the percent
     *                        complete bar.
     */
    public void updateStatus(String statusText, int percentComplete) {
//        progressBar.update(statusText, percentComplete);
        progressBar.update("", percentComplete);
        // Force a repaint since we (should be) on the EDT.
        // Note that repainting the imageLabel only seems necessary
        // on OS X (and possibly Linux); on Windows it is not needed.
        progressBar.paintImmediately(0, 0,
                progressBar.getWidth(), progressBar.getHeight());
        imageLabel.paintImmediately(0, 0,
                imageLabel.getWidth(), imageLabel.getHeight());
    }

    public static Map setNativeRenderingHints(Graphics2D g2d) {

        Map old = g2d.getRenderingHints();

        // Try to use the rendering hint set that is "native".
        Map hints = (Map) Toolkit.getDefaultToolkit().
                getDesktopProperty("awt.font.desktophints");
        if (hints != null) {
            g2d.addRenderingHints(hints);
        }

        return old;

    }

    @Override
    public void paintComponents(Graphics g) {

//        super.paintComponents(g);

        String version = DVPropMain.DV_NAME + " " + DVPropMain.DV_VERSION;

        int drawOffsetX = (getWidth() / 2) - (fm.stringWidth(version) / 2);
        int drawOffsetY = image.getIconHeight() - fm.getDescent() - 2;

        paintString(g, version, drawOffsetX, drawOffsetY);


    }

    private void paintString(Graphics g, String version, int drawOffsetX,
                             int drawOffsetY) {
        g.setFont(labelFont);

        g.setColor(versionColor1);
        g.drawString(version, drawOffsetX, drawOffsetY);
        // Draw a highlight effect
        g.setColor(versionColor2);
        g.drawString(version, drawOffsetX + 1, drawOffsetY + 1);
    } //}}}

    /**
     * The "progress bar" part of the splash screen. This component is somewhat
     * configurable; you can: <ul> <li>Configure the background color
     * <li>Configure the progress "fill in" color, even make it a gradient
     * <li>Set the status text and its color </ul>
     */
    private class ProgressBar extends JPanel {

        private static final long serialVersionUID = 1L;
        private Dimension preferredSize;
        private Color textColor;
        private GradientPaint paint;
        private int percentComplete;
        private String text;
        private int textX, textY;

        ProgressBar(int width, String initialText, Color background,
                    Color foreground1, Color foreground2,
                    Color textColor) {
            setBackground(background);
            preferredSize = new Dimension(width, statusBarHeight);
            paint = new GradientPaint(0.0f, 0.0f, foreground1,
                    0.0f, statusBarHeight, foreground2);
            this.textColor = textColor;
            update(text, percentComplete);
        }

        public Dimension getPreferredSize() {
            return preferredSize;
        }

        public void paintComponent(Graphics g) {

            // Fill in background.
            super.paintComponent(g);

            // Paint the filled-in portion of the status bar.
            int width = getWidth();
            Graphics2D g2d = (Graphics2D) g;
            Paint oldPaint = g2d.getPaint();
            g2d.setPaint(paint);
            int filledWidth = width * percentComplete / 100;
            int x = getComponentOrientation().isLeftToRight()
                    ? 0 : getWidth() - filledWidth;
            g2d.fillRect(x, 0, filledWidth, getHeight());
            g2d.setPaint(oldPaint);

            // Paint the status text.
            if (text != null) {

                // Try to use the rendering hint set that is "native".
                Map old = setNativeRenderingHints(g2d);

                g2d.setColor(textColor);
                g2d.drawString(text, textX, textY);

                if (old != null) {
                    g2d.addRenderingHints(old);
                }

            }

        }

        void update(String text, int percentComplete) {
            this.text = text;
            if (text != null) {
                FontMetrics fm = getFontMetrics(getFont());
                if (fm != null) {
                    int stringLength = fm.charsWidth(
                            text.toCharArray(), 0, text.length());
                    textX = (getWidth() - stringLength) / 2;
                    textY = (statusBarHeight + fm.getAscent()) / 2;
                }
            }
            this.percentComplete = percentComplete;
            repaint();

        }
    }
}
