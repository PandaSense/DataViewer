/*
 * HeapMemoryDialog.java  2/6/13 1:04 PM
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
package com.dv.ui.heap;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

/* ----------------------------------------------------------
 * CVS NOTE: Changes to the CVS repository prior to the 
 *           release of version 3.0.0beta1 has meant a 
 *           resetting of CVS revision numbers.
 * ----------------------------------------------------------
 */

/**
 * @author Takis Diakoumis
 * @version $Revision: 1460 $
 * @date $Date: 2009-01-25 11:06:46 +1100 (Sun, 25 Jan 2009) $
 */
public class HeapMemoryDialog extends JDialog
        implements ActionListener {

    private HeapMemoryPanel heapPanel;

    public HeapMemoryDialog(Frame owner) {
        super(owner, "Java Heap Memory", false);
        try {
            jbInit(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit(Frame parent) {
        heapPanel = new HeapMemoryPanel();

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);

        Container c = this.getContentPane();
        c.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 0, 1);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        c.add(heapPanel, gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.bottom = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        c.add(closeButton, gbc);

        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        Point oh = parent.getLocationOnScreen();
        setLocation((int) oh.getX() + parent.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + parent.getHeight() / 2 - getHeight() / 2);
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    public void dispose() {
        if (heapPanel != null) {
            heapPanel.stopTimer();
        }
        super.dispose();
    }

}