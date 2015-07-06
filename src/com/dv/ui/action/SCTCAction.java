/*
 * SCTCAction.java  2/6/13 1:04 PM
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

import com.dv.prop.DVPropMain;
import com.dv.ui.DVMainEditView;
import com.dv.util.DVFileUtil;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * @author xyma
 */
public class SCTCAction extends AbstractAction {

    DVMainEditView dvev;
    private static final String MSG = "com.dv.export.DataviewerExportFile";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);

    public SCTCAction(DVMainEditView dvev) {
        this.dvev = dvev;
        putValue(AbstractAction.NAME, "SCTC ");
    }

    public void actionPerformed(ActionEvent e) {

        if (canBeExport()) {

            StringBuffer sb = new StringBuffer();

            if (dvev.IS_BATCH_RESULT) {

                for (int i = 0; i < dvev.getBatchSQLs().size(); i++) {
                    sb.append("SQL :" + "\n\n");
                    sb.append(dvev.getBatchSQLs().get(String.valueOf(i)) + "\n\n");
                    sb.append("SQL Results :" + "\n\n");
                    sb.append(DVFileUtil.formatSQLResultToWord(dvev.getBatchResultCol().get(String.valueOf(i)), dvev.getBatchResultRow().get(String.valueOf(i)),dvev.getBatchLastInformations().get(String.valueOf(i))) + "\n\n");
                }
            } else {
                sb.append("SQL :" + "\n\n");
                sb.append(dvev.getSingleSQL() + "\n\n");
                sb.append("SQL Results :" + "\n\n");
                sb.append(DVFileUtil.formatSQLResultToWord(dvev.getCol(), dvev.getRow(),dvev.getLastQueryInformation()));
            }
            StringSelection ss = new StringSelection(sb.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

        } else {
            showError(mainViewResource.getString("NoResultError"));
        }

    }

    public void showError(String message) {

        JOptionPane.showMessageDialog(DVPropMain.DV_FRAME.get("MAIN"), message, "Export", JOptionPane.ERROR_MESSAGE);
    }

    public boolean canBeExport() {

        if (dvev.getResultTextPane().getText().trim().equals("")) {

            return false;
        }
        return true;
    }
}
