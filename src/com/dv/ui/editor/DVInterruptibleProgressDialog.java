/*
 * DVInterruptibleProgressDialog.java  2/6/13 1:04 PM
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
package com.dv.ui.editor;

import com.dv.swing.editor.DVIndeterminateProgressBar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class DVInterruptibleProgressDialog extends JDialog
        implements Runnable,
        ActionListener {

    /**
     * The event parent to this object
     */
//    private InterruptibleProcess process;
    /**
     * The progress bar widget
     */
    private DVIndeterminateProgressBar progressBar;
    /**
     * The parent frame of this dialog
     */
    private Frame parentFrame;
    /**
     * The progress bar label text
     */
    private String labelText;

    public DVInterruptibleProgressDialog(Frame parentFrame,
                                         String title,
                                         String labelText) {

        super(parentFrame, title, true);
        this.parentFrame = parentFrame;
        this.labelText = labelText;

        try {

            init();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public DVInterruptibleProgressDialog(Dialog parentDialog,
                                         String title,
                                         String labelText,
                                         InterruptibleProcess process) {

        super(parentDialog, title, true);
        JDialog.setDefaultLookAndFeelDecorated(true);
//        this.process = process;
        this.labelText = labelText;

        try {

            init();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void run() {
        pack();

        Point oh = parentFrame.getLocationOnScreen();
        setLocation((int) oh.getX() + parentFrame.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + parentFrame.getHeight() / 2 - getHeight() / 2);

        progressBar.start();
        setVisible(true);
    }

    private void init() throws Exception {

        progressBar = new DVIndeterminateProgressBar();
        progressBar.setPreferredSize(new Dimension(260, 18));

        JPanel base = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        Insets ins = new Insets(10, 20, 10, 20);
        gbc.insets = ins;
        base.add(new JLabel(labelText), gbc);
        gbc.gridy = 1;
        gbc.insets.top = 0;
        base.add(progressBar, gbc);
        gbc.gridy = 2;
        gbc.weighty = 1.0;
//        base.add(cancelButton, gbc);

        base.setBorder(BorderFactory.createEtchedBorder());

        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());
        c.add(base, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.SOUTHEAST,
                GridBagConstraints.BOTH,
                new Insets(5, 5, 5, 5), 0, 0));

        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    }

    public void actionPerformed(ActionEvent e) {
//        try {
//
//            process.setCancelled(true);
//            process.interrupt();
//
//        } finally {
//
//            dispose();
//        }
    }

    public void dispose() {

        if (progressBar != null) {

            progressBar.stop();
            progressBar.cleanup();
        }

        super.dispose();
    }

    class CancelButton extends JButton {

        private int DEFAULT_WIDTH = 75;
        private int DEFAULT_HEIGHT = 30;

        public CancelButton() {
            super("Cancel");
            setMargin(Constants.EMPTY_INSETS);
        }

        public int getWidth() {
            int width = super.getWidth();
            if (width < DEFAULT_WIDTH) {
                return DEFAULT_WIDTH;
            }
            return width;
        }

        public int getHeight() {
            int height = super.getHeight();
            if (height < DEFAULT_HEIGHT) {
                return DEFAULT_HEIGHT;
            }
            return height;
        }

        public Dimension getPreferredSize() {
            return new Dimension(getWidth(), getHeight());
        }
    }
}
