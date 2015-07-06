/*
 * ScrollingCreditsPanel.java  2/6/13 1:04 PM
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

package com.dv.about;

import com.dv.prop.DVPropMain;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * @author Cypress
 */
public class ScrollingCreditsPanel extends JPanel {

    private Timer timer;
    private Font nameFont;
    private Font titleFont;
    private String[] names;
    private String[] titles;
    private static final String MSG = "com.dv.about.DVVersion";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);

    protected ScrollingCreditsPanel() {
        setBorder(BorderFactory.createEtchedBorder());

        titleFont = new Font("dialog", Font.BOLD, 12);
        nameFont = new Font("dialog", Font.PLAIN, 12);
        loadNamesAndTitles();
        startTimer();
    }

    private void loadNamesAndTitles() {

        String namesAndTitles = mainViewResource.getString("about.panel.credits");
        namesAndTitles = DVPropMain.DV_NAME + " " + DVPropMain.DV_VERSION + namesAndTitles;
        String[] namesAndTitlesAsArray = namesAndTitles.split(",");
        titles = new String[namesAndTitlesAsArray.length];
        names = new String[namesAndTitlesAsArray.length];

        for (int i = 0; i < namesAndTitlesAsArray.length; i++) {

            String nameAndTitle = namesAndTitlesAsArray[i];
            int pipeIndex = nameAndTitle.indexOf('|');

            titles[i] = nameAndTitle.substring(0, pipeIndex);
            names[i] = nameAndTitle.substring(pipeIndex + 1);
        }

    }

    protected void startTimer() {
        final Runnable scroller = new Runnable() {

            public void run() {
                yOffset--;
                ScrollingCreditsPanel.this.repaint();
            }
        };

        TimerTask paintCredits = new TimerTask() {

            public void run() {
                EventQueue.invokeLater(scroller);
            }
        };
        timer = new Timer();
        timer.schedule(paintCredits, 500, 40);
    }

    protected void ensureTimerRunning() {
        if (timer == null) {
            startTimer();
        }
    }

    private int yOffset;
    int count = 0;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        Graphics2D g2d = (Graphics2D) g;

        renderingHintsForText(g2d);

        g2d.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, width / 2,
                0, Color.WHITE, true));

        g2d.fillRect(0, 0, width, height);

        int x = 0;
        int y = 0;
        int stringWidth = 0;

        g2d.setColor(Color.BLACK);

        for (int i = 0; i < names.length; i++) {
            g2d.setFont(nameFont);
            FontMetrics fm = g2d.getFontMetrics();
            stringWidth = fm.stringWidth(names[i]);
            x = (width - stringWidth) / 2;
            y += fm.getHeight() + 1;
            g2d.drawString(names[i], x, y + yOffset + height);

            g2d.setFont(titleFont);
            fm = g2d.getFontMetrics();
            stringWidth = fm.stringWidth(titles[i]);
            x = (width - stringWidth) / 2;
            y += fm.getHeight() + 1;
            g2d.drawString(titles[i], x, y + yOffset + height);

            if (i == names.length - 1) {
                if (Math.abs(yOffset) >= (y + height)) {
                    yOffset = 0;
                }
            }

            y += 15;
        }

    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    private void renderingHintsForText(Graphics2D g2d) {

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }
}
