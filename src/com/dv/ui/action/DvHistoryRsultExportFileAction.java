/*
 * DvHistoryRsultExportFileAction.java  2/6/13 1:04 PM
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
package com.dv.ui.action;

import com.dv.ui.DVMainEditView;
import com.dv.util.DVFileUtil;
import com.dv.util.DataViewerUtilities;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * @author xyma
 */
public class DvHistoryRsultExportFileAction extends AbstractAction {

    DVMainEditView dvev;
    boolean isOk = true;

    public DvHistoryRsultExportFileAction(DVMainEditView dvev) {
        this.dvev = dvev;

    }

    public void actionPerformed(ActionEvent e) {

        if (!dvev.getResultHistoryTextPane().getText().trim().equals("")) {

            isOk = DVFileUtil.saveSqlHistoryFile(dvev.getResultHistoryTextPane().getText());

            if (isOk) {
                dvev.cleanHistoryResultContent();
                dvev.getResultHistoryTextPane().setText("");
            } else {
                DataViewerUtilities.displayErrorMessage("Save SQL History Result Failed,please check history error log file.");
            }
        } else {

            DataViewerUtilities.displayWarningMessage("There is no any result to save.");
        }
    }
}
