/*
 * DVTabPaneView.java  2/6/13 1:04 PM
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
package com.dv.ui;

import com.dv.ui.component.ClosableTabbedPane;
import com.dv.ui.component.DVFileChooser;
import com.dv.prop.DVPropMain;
import com.dv.swing.editor.DVQueryEditorPane;
import com.dv.util.DVLOG;
import com.dv.util.DataViewerUtilities;

import java.io.IOException;
import java.sql.Connection;
import javax.swing.JFileChooser;

import org.fife.ui.rsyntaxtextarea.FileLocation;

/**
 * @author xyma
 */
public class DVTabPaneView extends ClosableTabbedPane {

    DVMainEditView currentMainView;
    DVMainEditView[] allMainView;

    public DVMainEditView[] getNeedAllMainView() {

        return allMainView;
    }

    DVQueryEditorPane currentEditPane;

    public DVMainEditView getCurrentMainView() {
        return (DVMainEditView) getComponentAt(getSelectedIndex());
    }

    public void removeTabAt(int index) {
        if (index != 0) {
            currentEditPane = ((DVMainEditView) getComponentAt(index)).getDvtextPane();
            closeConnection(((DVMainEditView) getComponentAt(index)).getMainConnection());
            if (currentEditPane.isDirty()) {
                if (!((DVMainEditView) getComponentAt(index)).isNeedToSaveNewSQl()) {
                    saveCurrentSQLFile(currentEditPane, index);
                } else {
                    saveAsSQLFile(currentEditPane, index);
                }
            } else {
                super.removeTabAt(index);
            }
        } else {
            DataViewerUtilities.displayWarningMessage("Please don't close Frequent View .");
        }
    }

    public void removeAllTabExceptFrequent() {
        if (getTabCount() > 1) {
            for (int i = getTabCount() - 1; i > 0; i--) {
                if (getTabTitleAt(i).indexOf("*") >= 0
                        && ((DVMainEditView) getComponentAt(i)).getDvtextPane().isDirty()) {
                    this.removeTabAt(i);
                }
            }
        }
    }

    public boolean isDirty() {

        if (getTabCount() > 1) {

            for (int i = 1; i < getTabCount(); i++) {

                if (getTabTitleAt(i).indexOf("*") >= 0
                        && ((DVMainEditView) getComponentAt(i)).getDvtextPane().isDirty()) {
                    ((DVMainEditView) getComponentAt(i)).getDvtextPane().requestFocus();
                    return true;
                }
            }
        }
        return false;
    }

    public void saveCurrentSQLFile(DVQueryEditorPane currentEditPane, int index) {

        try {
            currentEditPane.save();

        } catch (IOException ee) {

            DVLOG.setErrorLog(DVTabPaneView.class.getName(), ee);
        }
        super.removeTabAt(index);
    }

    public void saveAsSQLFile(DVQueryEditorPane currentEditPane, int index) {

        DVFileChooser fcd = new DVFileChooser(
                DataViewerUtilities.getLastOpenFilePath());
        int i = fcd.showSaveDialog(DVPropMain.DV_FRAME.get("MAIN"));
        if (i == JFileChooser.APPROVE_OPTION) {
            try {
                currentEditPane.saveAs(FileLocation.create(fcd.getSelectedFile().getPath()));
            } catch (IOException ee) {
                DVLOG.setErrorLog(DVTabPaneView.class.getName(), ee);
            }
            super.removeTabAt(index);
        } else if (i == JFileChooser.CANCEL_OPTION) {
            super.removeTabAt(index);
        }
    }

    public void removeAllConnects() {
        if (getTabCount() > 1) {
            for (int i = 1; i < getTabCount(); i++) {
                DVLOG.setInfoLog("Remove connection is [ "
                        + ((DVMainEditView) getComponentAt(i)).getMainConnection().toString() + " ]");
                closeConnection(((DVMainEditView) getComponentAt(i)).getMainConnection());
            }
        }
    }

    public void changeAllGridSQLResult() {
        if (getTabCount() > 1) {
            for (int i = 1; i < getTabCount(); i++) {
                if (DVPropMain.DV_DISPLAY_GRID.equals("0")) {
                    if (((DVMainEditView) getComponentAt(i)).getResultTabPane().getTabCount() == 3) {
                        ((DVMainEditView) getComponentAt(i)).removeGridResultTab();
                    }
                } else {
                    if (((DVMainEditView) getComponentAt(i)).getResultTabPane().getTabCount() != 3) {
                        ((DVMainEditView) getComponentAt(i)).setGridResultTab();
                        ;
                    }
                }
            }
        }
    }

    public void closeConnection(Connection conn) {
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (Exception ee) {
            DVLOG.setErrorLog(DVTabPaneView.class.getName(), ee);
            return ;
        }
    }
}
