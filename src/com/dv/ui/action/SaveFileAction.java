/*
 * SaveFileAction.java  2/6/13 1:04 PM
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
import com.dv.ui.component.DVFileChooser;
import com.dv.ui.DVMainEditView;
import com.dv.util.DataViewerUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

/**
 * @author xyma
 */
public class SaveFileAction extends AbstractAction {

    DVMainEditView dvev;

    public SaveFileAction(DVMainEditView dvev) {

        this.dvev = dvev;
        putValue(AbstractAction.NAME, "Save ");
    }

    public void actionPerformed(ActionEvent e) {

        if (dvev.isNeedToSaveNewSQl()) {

            DVFileChooser fcd = new DVFileChooser(DataViewerUtilities.getLastOpenFilePath());
            int i = fcd.showSaveDialog(DVPropMain.DV_FRAME.get("MAIN"));

            if (i == JFileChooser.APPROVE_OPTION) {

                dvev.setFileFullpath(fcd.getSelectedFile().getPath());
                dvev.setFileName(fcd.getSelectedFile().getName());
                DVPropMain.DV_FILE_RECENT_PATH = fcd.getCurrentDirectory().toString();
                dvev.saveAsSQLFile(dvev.getFileFullpath());
            }
        } else {
            dvev.saveSQLFileDetail(dvev.getFileFullpath());
        }

    }
}
