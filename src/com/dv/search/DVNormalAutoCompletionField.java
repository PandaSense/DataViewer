/*
 * DVNormalAutoCompletionField.java  2/6/13 1:04 PM
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

package com.dv.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 */
public class DVNormalAutoCompletionField extends JTextField implements DocumentListener, MouseListener, ListSelectionListener, ActionListener, KeyListener {

    private static int DEFAULT_PREFERRED_HEIGHT = 150;
    private ListPopup popup;

    public ListPopup getPopup() {
        return popup;
    }

    private int preferredHeight = DEFAULT_PREFERRED_HEIGHT;
    private CompletionFilter filter;

    public void setFilter(CompletionFilter f) {
        filter = f;
    }

    public DVNormalAutoCompletionField() {
        popup = new ListPopup();
        getDocument().addDocumentListener(this);
        addMouseListener(this);
        popup.addListSelectionListener(this);
        addActionListener(this);
        addKeyListener(this);
    }

    public void setPopupPreferredHeight(int h) {
        preferredHeight = h;
    }

    private boolean isListChange(ArrayList<String> array) {
        if (array.size() != popup.getItemCount()) {
            return true;
        }
        for (int i = 0; i < array.size(); i++) {
            if (!array.get(i).toString().toUpperCase().equals(popup.getItem(i).toString().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private void textChanged() {
        if (!popup.isVisible()) {
            showPopup();
            requestFocus();
        }
        if (filter != null) {
            ArrayList array = filter.filter(getText());
            changeList(array);
        }
    }

    public void showPopup() {
        popup.setPopupSize(getWidth(), preferredHeight);
        popup.show(this, 0, getHeight() - 1);
    }

    private void changeList(ArrayList array) {
        if (array.size() == 0) {
            if (popup.isVisible()) {
                popup.setVisible(false);
            }
        } else {
            if (!popup.isVisible()) {
                showPopup();
            }
        }
        if (isListChange(array) && array.size() != 0) {
            popup.setList(array);
        }
    }

    public void insertUpdate(DocumentEvent e) {
        textChanged();
    }

    public void removeUpdate(DocumentEvent e) {
        textChanged();
    }

    public void changedUpdate(DocumentEvent e) {
        textChanged();
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1 && !popup.isVisible())
            textChanged();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        String text = list.getSelectedValue().toString();
        setText(text);
        popup.setVisible(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (popup.isVisible()) {
            Object o = popup.getSelectedValue();
            if (o != null)
                setText(o.toString());
            popup.setVisible(false);
        }
        this.selectAll();
        this.requestFocus();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (popup.isVisible()) {
                if (!popup.isSelected())
                    popup.setSelectedIndex(0);
                else
                    popup.setSelectedIndex(popup.getSelectedIndex() + 1);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (popup.isVisible()) {
                if (!popup.isSelected())
                    popup.setLastOneSelected();
                else
                    popup.setSelectedIndex(popup.getSelectedIndex() - 1);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            if (popup.isVisible()) {
                if (!popup.isSelected())
                    popup.setSelectedIndex(0);
                else
                    popup.setSelectedIndex(popup.getSelectedIndex() + 5);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            if (popup.isVisible()) {
                if (!popup.isSelected())
                    popup.setLastOneSelected();
                else
                    popup.setSelectedIndex(popup.getSelectedIndex() - 5);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (popup.isVisible()) {

                if (popup.isSelected()) {

                    this.setText(popup.getSelectedValue().toString());
                }

            }


        }
    }

    public void keyReleased(KeyEvent e) {
    }

}
