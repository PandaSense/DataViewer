/*
 * DVNomalSQLPane.java  2/6/13 1:04 PM
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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * @author Cypress
 */
public class DVNomalSQLPane extends TextEditorPane {

    FindAndReplaceFrame raf;
    JFrame parent;

    public DVNomalSQLPane(JFrame parent) {
        this.parent=parent;
        setFont(new Font(DVPropMain.DV_SQL_FONT_NAME, Font.PLAIN, Integer.parseInt(DVPropMain.DV_SQL_FONT_SIZE)));
        this.setHighlightCurrentLine(true);
        setCaretPosition(0);
        setMarkOccurrences(true);
        setAntiAliasingEnabled(true);
        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);

        setMatchedBracketBGColor(Color.PINK);
        setAnimateBracketMatching(false);
        setMatchedBracketBorderColor(Color.PINK);

        setCodeFoldingEnabled(false);

        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                myTextPaneKeyReleased(ke);
            }
        });
    }

    public void myTextPaneKeyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_F) {
            raf = new FindAndReplaceFrame(parent, this);
        }
    }
}
