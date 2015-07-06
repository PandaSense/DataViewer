/*
 * DVSQLResultPane.java  2/6/13 1:04 PM
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
package com.dv.swing.editor;

import com.dv.prop.DVPropMain;
import com.dv.ui.component.FindAndReplaceFrame;
import com.dv.util.IconGroupLoader;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.IconGroup;

/**
 * @author Cypress
 */
public class DVSQLResultPane extends RSyntaxTextArea implements CaretListener {

    FindAndReplaceFrame raf;

    public DVSQLResultPane() {
        super();
        setFont(new Font(DVPropMain.DV_SQL_FONT_NAME, Font.PLAIN, Integer.parseInt(DVPropMain.DV_SQL_FONT_SIZE)));
        setHighlightCurrentLine(false);
        setCaretPosition(0);
        setMarkOccurrences(true);
        setAntiAliasingEnabled(true);
        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        addCaretListener(this);
//        setCodeFoldingEnabled(false);
        IconGroup newGroup = IconGroupLoader.getPopUpIconGroup();
        setIconGroup(newGroup);
        setTextMode(0);
        setCodeFoldingEnabled(false);
        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                myTextPaneKeyReleased(ke);
            }
        });

    }

    public void myTextPaneKeyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_F1) {
            raf = new FindAndReplaceFrame(DVPropMain.DV_FRAME.get("MAIN"), this);
        }

    }

    public void caretUpdate(CaretEvent e) {

        if (DVPropMain.DV_FindAndReplaceFrame.get("FINDANDREPLACE") != null) {

            DVPropMain.DV_FindAndReplaceFrame.get("FINDANDREPLACE").setTextComponet(this);

        }
    }
}
