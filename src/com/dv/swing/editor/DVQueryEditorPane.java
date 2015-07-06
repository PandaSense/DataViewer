/*
 * DVQueryEditorPane.java  2/6/13 1:04 PM
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
import com.dv.ui.DVMainEditView;
import com.dv.ui.component.FindAndReplaceFrame;
import com.dv.ui.editor.QueryEditorStatusBar;
import com.dv.util.DataViewerUtilities;
import com.dv.util.IconGroupLoader;
import com.dv.util.SQLFormatter;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;

import org.fife.ui.rtextarea.IconGroup;

/**
 * @author Cypress
 */
public class DVQueryEditorPane extends TextEditorPane implements ActionListener, CaretListener, KeyListener {

    FindAndReplaceFrame raf;
    QueryEditorStatusBar qesb;
    private int currentPosition = 0;
    private JMenuItem sqlFormatItem, ToUpcaseItem, finaAndReplace;
    DVMainEditView mainView;

    public DVQueryEditorPane(QueryEditorStatusBar qesb, DVMainEditView mainView) {

        super();
        this.qesb = qesb;
        this.mainView = mainView;
        this.addCaretListener(this);
        setFont(new Font(DVPropMain.DV_SQL_FONT_NAME, Font.PLAIN, Integer.parseInt(DVPropMain.DV_SQL_FONT_SIZE)));
        setCurrentLineHighlightColor(new Color(232, 242, 254));
        setCaretPosition(0);
        requestFocusInWindow();
        setMarkOccurrences(true);
        setAntiAliasingEnabled(true);

        setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);

        if(DVPropMain.DV_SQL_EDITAREA_HAS_MARGIN.equals("1")){
            setMarginLineEnabled(true);
            setMarginLinePosition(Integer.parseInt(DVPropMain.DV_EDITOR_MARGIN_SIZE));
        }

        setMatchedBracketBGColor(Color.PINK);
        setAnimateBracketMatching(true);
        setMatchedBracketBorderColor(Color.PINK);
        setMarginLineColor(Color.RED);
        setCodeFoldingEnabled(false);
        setDragEnabled(true);

        setTextMode(0);
        IconGroup newGroup = IconGroupLoader.getPopUpIconGroup();
        setIconGroup(newGroup);
        buildSQLEditAreaPopupMenu();
        registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);


        getDocument().addDocumentListener(this);
        addKeyListener(this);
    }

    public void buildSQLEditAreaPopupMenu() {

        sqlFormatItem = new JMenuItem(DVMainEditView.getMainViewResource().getString("sqlFormatItem"), new ImageIcon(DVQueryEditorPane.class.getResource("/com/dv/ui/resources/icon/SQL_Area_FormatSql16.png")));
        ToUpcaseItem = new JMenuItem(DVMainEditView.getMainViewResource().getString("ToUpcaseItem"), new ImageIcon(DVQueryEditorPane.class.getResource("/com/dv/ui/resources/icon/SQL_Area_SqlFormat16.png")));
        sqlFormatItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.ALT_MASK));
        ToUpcaseItem.setAccelerator(KeyStroke.getKeyStroke('U', InputEvent.ALT_MASK));

        finaAndReplace = new JMenuItem(DVMainEditView.getMainViewResource().getString("findAndReplace"),new ImageIcon(DVQueryEditorPane.class.getResource("/com/dv/ui/resources/icon/SQL_Area_Find16.png")));

        finaAndReplace.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_MASK));

        sqlFormatItem.addActionListener(this);
        ToUpcaseItem.addActionListener(this);
        finaAndReplace.addActionListener(this);

        getPopupMenu().addSeparator();
        getPopupMenu().add(sqlFormatItem);
        getPopupMenu().add(ToUpcaseItem);
        getPopupMenu().add(finaAndReplace);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this || e.getSource() == finaAndReplace) {
            raf = new FindAndReplaceFrame(DVPropMain.DV_FRAME.get("MAIN"), this);
            DVPropMain.DV_FindAndReplaceFrame.put("FINDANDREPLACE", raf);
        }

        if (e.getSource() == sqlFormatItem) {
            String text = getSelectedText();
            if (text != null) {
                replaceSelection(new SQLFormatter(text).format());
            }
        }

        if (e.getSource() == ToUpcaseItem) {

            String text = getSelectedText();

            if (text != null) {
                replaceSelection(text.toUpperCase());
            }
        }
    }

    public void caretUpdate(CaretEvent e) {

        setStatusBarLineAndRow();

        setTabTitle();

        setDVFrameTitle();

        if (DVPropMain.DV_FindAndReplaceFrame.get("FINDANDREPLACE") != null) {

            DVPropMain.DV_FindAndReplaceFrame.get("FINDANDREPLACE").setTextComponet(this);
        }
    }

    public void setDVFrameTitle() {

        DVPropMain.DV_FRAME.get("MAIN").setTitle(DVPropMain.DV_NAME + "-" + DVPropMain.DV_VERSION + "-" + mainView.getSubMainTab().getTabTitleAt(mainView.getSubMainTab().getSelectedIndex()));
    }

    public void selectRow(int line) {

        try {
            setCaretPosition(getLineStartOffset(line));
        } catch (Exception ee) {
        }
    }

    public void setTabTitle() {
        if (isNeedChangeTabTitle()) {
            String oldTitle = mainView.getSubMainTab().getTabTitleAt(mainView.getSubMainTab().getSelectedIndex());
            if (oldTitle.indexOf("*") < 0) {
                mainView.getSubMainTab().setTitleAt(mainView.getSubMainTab().getSelectedIndex(), oldTitle + "*");
            }
        }
    }

    public boolean isNeedChangeTabTitle() {

        return mainView.getNeedToChangeTabTitle() == mainView.getDvtextPane().getText().length() ? false : true;
    }

    public void setStatusBarLineAndRow() {
        currentPosition = getCaretPosition();
        Element map = getDocument().getDefaultRootElement();
        int row = map.getElementIndex(currentPosition);
        Element lineElem = map.getElement(row);
        int col = currentPosition - lineElem.getStartOffset();
        qesb.setCaretPosition(row + 1, col + 1);
    }

    public String getCurrentLineText() {
        int row = 0;
        try {
            Element map = getDocument().getDefaultRootElement();
            row = map.getElementIndex(currentPosition);

            return getTextAtRow(row);
        } catch (BadLocationException ee) {
            return "";
        }
    }

    public int getStartLineofSelectedContent() {
        int row = 0;
        try {
            Element map = getDocument().getDefaultRootElement();
            row = map.getElementIndex(this.getSelectionStart());

        } catch (Exception ee) {
        }

        return row;
    }

    public int getEndLineofSelectedContent() {
        int row = 0;
        try {
            Element map = getDocument().getDefaultRootElement();
            row = map.getElementIndex(this.getSelectionEnd());

        } catch (Exception ee) {
        }
        return row;
    }

    public ArrayList getSqlLineNumbers(String IS_BATCH_SQL_SYMBOL) {

        ArrayList lineNumberCount = new ArrayList();
        ArrayList temCount = new ArrayList();
        int c = getStartLineofSelectedContent() + 1;
        int b = getEndLineofSelectedContent() + 1;
        int allLineCount = b - c;

        int row = 0;
        try {
            for (int i = 0; i < allLineCount; i++) {
                if (getTextAtRow(c + i - 1).indexOf(IS_BATCH_SQL_SYMBOL) >= 0) {
                    temCount.add(row, c + i);
                    row++;
                }
            }
            int numnerCounts = 0;
            for (int j = 0; j < temCount.size() + 2; j++) {

                if (j == 0) {
                    for (int k = c; k < b; k++) {
                        if (!getTextAtRow(k).trim().equals("")) {
                            if ((k - 2) < 0) {
                                lineNumberCount.add(0, 0);
                            } else {
                                lineNumberCount.add(0, k - 1);
                            }
                            numnerCounts++;
                            break;
                        }
                    }
                } else if (j == temCount.size() + 2 - 1) {
                    for (int k = Integer.parseInt(temCount.get(temCount.size() - 1).toString()) + 1; k <= b; k++) {
                        if (!getTextAtRow(k).trim().equals("")) {
                            lineNumberCount.add(temCount.size(), k);
                            break;
                        }
                    }
                } else {
                    for (int k = Integer.parseInt(temCount.get(j - 1).toString()); k < b; k++) {
                        if (!getTextAtRow(k).trim().equals("")) {
                            lineNumberCount.add(numnerCounts, k);
                            numnerCounts++;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return lineNumberCount;
    }

    public String getTextAtRow(int rowNumber) throws BadLocationException {

        Element line = getDocument().getDefaultRootElement().getElement(rowNumber);

        int startOffset = line.getStartOffset();
        int endOffset = line.getEndOffset();

        return getText(startOffset, (endOffset - startOffset));
    }

    public String getWordEndingAt(int position) {

        String text = getText();
        if (DataViewerUtilities.isNull(text)) {
            return "";
        }
        char[] chars = text.toCharArray();
        int start = -1;
        int end = position - 1;

        for (int i = end - 1; i >= 0; i--) {
            if (!Character.isLetterOrDigit(chars[i])
                    && chars[i] != '_' && chars[i] != '.') {
                start = i;
                break;
            }
        }
        if (start < 0) {

            start = 0;
        }

        return text.substring(start, end).trim();
    }

    public int getAllLineNumber() {
        return this.getLineCount();
    }

    public int getAllStringNumber() {
        return this.getText().trim().length();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }
    /**
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_PERIOD) {
            String wordEndingAt = getWordEndingAt(getCaretPosition());
            if (!DataViewerUtilities.isNull(wordEndingAt)) {
                if (wordEndingAt.toUpperCase().equals(mainView.getSubSVSchema().toUpperCase())) {
                    mainView.getSubSVAutoCompletion().doCompletion();
                }
            }
        }
    }
}
