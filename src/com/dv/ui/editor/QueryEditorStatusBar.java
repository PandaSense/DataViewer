/*
 * QueryEditorStatusBar.java  2/6/13 1:04 PM
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

package com.dv.ui.editor;

import com.dv.swing.editor.DVIndeterminateProgressBar;
import com.dv.ui.eq.CharLimitedTextField;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * Query Editor status bar panel.
 */
public class QueryEditorStatusBar extends AbstractStatusBarPanel {

    /**
     * The buffer containing constantly changing values
     */
    private StringBuffer caretBuffer;

    /**
     * the progress bar
     */
    private DVIndeterminateProgressBar progressBar;
    /**
     * the status bar panel fixed height
     */
    private static final int HEIGHT = 21;
    CharLimitedTextField maxResultField;

    public QueryEditorStatusBar() {
        super(HEIGHT);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        caretBuffer = new StringBuffer();


        // setup the progress bar
        progressBar = new DVIndeterminateProgressBar(false);
        Color colo = new Color(201, 201, 176);
        progressBar.setScrollbarColour(colo);

        addLabel(0, 80, true); // activity label

        addComponent(progressBar, 1, 120, false); // progress bar
        addLabel(2, 90, false); // execution time
        addLabel(3, 35, false); // insert mode
        addLabel(4, 60, false); // caret position
        addLabel(5, 40, true); // commit mode
        addLabel(6, 40, true);
//        addLabel(7, 60, false);

        getLabel(6).setForeground(Color.BLUE);

        maxResultField = new CharLimitedTextField(5);

        maxResultField.setText("10");
        maxResultField.setForeground(Color.RED);
        maxResultField.setAlignmentX(1.0F);
        maxResultField.setMaximumSize(new java.awt.Dimension(6, HEIGHT));
        maxResultField.setName("maxResultField"); // NOI18N

        addComponent(maxResultField, 7, 25, true);

        // set some labels to center alignment
        getLabel(3).setHorizontalAlignment(JLabel.CENTER);
        getLabel(4).setHorizontalAlignment(JLabel.CENTER);
        getLabel(6).setHorizontalAlignment(JLabel.CENTER);
        setColAndRow(0, 0);
        getLabel(6).setToolTipText("Col and Row is " + getLabel(6).getText());
    }

    /**
     * Cleanup code to ensure the progress thread is dead.
     */
    public void cleanup() {
        progressBar.cleanup();
        progressBar = null;
    }

    /**
     * Starts the progress bar.
     */
    public void startProgressBar() {
        progressBar.start();
    }

    /**
     * Stops the progress bar.
     */
    public void stopProgressBar() {
        progressBar.stop();
    }

    /**
     * Sets the query execution time to that specified.
     */
    public void setExecutionTime(String text) {
        setLabelText(2, text);
    }

//    public void setServerUserId(String text) {
//        setLabelText(7, text);
//    }

    /**
     * Sets the editor commit status to the text specified.
     */
    public void setAutoCompleteStatus(String autoCommit) {

        setLabelText(5, " Auto-Complete : " + autoCommit);
    }

    /**
     * Sets the editor insert mode to that specified.
     */
    public void setInsertionMode(String text) {
        setLabelText(3, text);
    }

    /**
     * Sets the editor status to the text specified.
     */
    public void setStatus(String text) {
        setLabelText(0, text);
//        setStatusTooltip(text);
    }

    public void setStatusTooltip(String text) {

        StringBuffer s = new StringBuffer("");

        String[] textOne = text.split(":");

        String[] textOneOne = textOne[0].split("@");

        s.append("<html><p>" + "DataBase Server : " + "<font color=red face=\"DialogInput\" >" + textOneOne[0].trim() + "</font></p>");

        s.append("<p>" + "DataBase Host  : " + "<font color=red face=\"DialogInput\" >" + textOneOne[1].trim() + "</font></p>");

        textOneOne = textOne[1].split("@");

        s.append("<p>" + "DataBase User  : " + "<font color=red face=\"DialogInput\" >" + textOneOne[0].trim() + "</font></p>");

        if (textOneOne.length == 1) {
            s.append("<p>" + "DataBase Schema : " + "<font color=red face=\"DialogInput\" >" + "***" + "</font></p></html>");

        } else {
            s.append("<p>" + "DataBase Schema : " + "<font color=red face=\"DialogInput\" >" + textOneOne[1].trim() + "</font></p></html>");
        }

        getLabel(0).setToolTipText(s.toString());
    }

    /**
     * Sets the caret position to be formatted.
     *
     * @param l - the line number
     * @param c - the column number
     */
    public void setCaretPosition(int l, int c) {
        caretBuffer.append(" ").append(l).append(':').append(c);
        setLabelText(4, caretBuffer.toString());
        caretBuffer.setLength(0);
    }

    public void setColAndRow(int col, int row) {
        caretBuffer.append(col).append("  :  ").append(row);
        setLabelText(6, caretBuffer.toString());
        caretBuffer.setLength(0);

    }

    public String getMaxRecoder() {
        return maxResultField.getText().trim();
    }
}
