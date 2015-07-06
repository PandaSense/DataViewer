/*
 * CTCAction.java  2/6/13 1:04 PM
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

package com.dv.ui.action;

import com.dv.ui.DVFrame;
import com.dv.ui.DVMainEditView;
import com.dv.util.DVFileUtil;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * @author xyma
 */
public class CTCAction extends AbstractAction {

    DVMainEditView dvev;

    public CTCAction(DVMainEditView dvev) {

        this.dvev = dvev;
        putValue(AbstractAction.NAME, "CTC ");
    }

    public void actionPerformed(ActionEvent e) {

        if (!dvev.getResultTextPane().getText().trim().equals("")) {
            if (!dvev.isCanBeFormatBatchSqlResult()) {
                String content = DVFileUtil.formatSQLResultToWord(dvev.getCol(), dvev.getRow(),dvev.getLastQueryInformation());
                StringSelection ss = new StringSelection(content);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
                dvev.clearBatchMap();

            } else {

                String content = DVFileUtil.formatBatchSQLResultToWord(dvev.getBatchResultCol(), dvev.getBatchResultRow(),dvev.getBatchLastInformations());
                StringSelection ss = new StringSelection(content);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            }
        } else {
            dvev.setErrorMessage(DVMainEditView.getMainViewResource().getString("noSQLToExecute"), 9999);
        }
    }
}
