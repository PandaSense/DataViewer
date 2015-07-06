/*
 * DVAutoCompleteWorker.java  2/6/13 1:04 PM
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

package com.dv.util;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

import com.dv.prop.DVPropMain;
import com.dv.swing.editor.DVQueryEditorPane;
import com.dv.ui.DVMainEditView;
import com.dv.ui.autocomplete.DataViewerACListCellRender;
import com.dv.ui.autocomplete.DataViewerAutoCompletion;
import com.dv.ui.editor.QueryEditorStatusBar;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;

/**
 * @author xyma
 */
public class DVAutoCompleteWorker extends SwingWorker<Boolean, Void> {

    QueryEditorStatusBar qesb;
    DVDbUtil dvdb = new DVDbUtil();
    Vector tableAndViewer;
    DVMainEditView dvsv;
    String key;
    String schema;
    Connection con;
    DataViewerAutoCompletion ac;
    DVQueryEditorPane dvre;

    public DVAutoCompleteWorker(DVMainEditView dvsv) {
        this.qesb = dvsv.getSubSVQueryEditorStatusBar();
        this.key = dvsv.getSubSVKey();
        this.schema = dvsv.getSubSVSchema();
        this.dvsv = dvsv;
        String dbType = DVPropMain.getDataBaseServerType(key);
        try {
            this.con = DvConnectFactory.getOtherConnection(dbType, DVPropMain.DB_SERVER_CON_DETAIL.get(key), DVPropMain.DB_SERVER_CON_USER.get(key), DVPropMain.DB_SERVER_CON_PW.get(key));
        } catch (Exception e) {
            DVLOG.setErrorLog(DVAutoCompleteWorker.class.getName(), e);
            this.con = dvsv.getSubSVConnection();
            DVLOG.setInfoLog("Set connection as default subviewer. [ " + con.toString() + " ]");
        }

        this.ac = dvsv.getSubSVAutoCompletion();
        this.dvre = dvsv.getSubSVDVQueryEditorPane();
    }

    protected Boolean doInBackground() {

        return setAutoCompleteDetail();
    }

    public boolean setAutoCompleteDetail() {
        boolean isOk = true;
        qesb.setAutoCompleteStatus("Close");
        try {

            Vector tableNames = dvdb.getAllTableNames(con, schema);
            Vector viewsNames = dvdb.getAllViews(con, schema);

            tableAndViewer = DVDbUtil.getAllTableAndView(tableNames, viewsNames);

            DVPropMain.DB_SERVER_TABLE_VIEW_NAME.put(key, tableAndViewer);

            CompletionProvider cp = DVAutoCProvider.buildAutoCProvider(tableAndViewer, schema);

            ac = new DataViewerAutoCompletion(cp);

            DVPropMain.DB_AUTOCOMPLETE_POOL.put(key, cp);

            ac.install(dvre);
            ac.setTriggerKey(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));

            dvsv.setSubSVAutoCompletion(ac);

            con.close();

        } catch (Exception e) {
            DVLOG.setErrorLog(DVAutoCompleteWorker.class.getName(), e);
            isOk = false;

        } finally {
            DataViewerUtilities.scheduleGC();
        }
        return isOk;
    }

    protected void done() {
        try {
            if (get()) {
                qesb.setAutoCompleteStatus("Open");
            } else {
                qesb.setAutoCompleteStatus("Processing");
            }
        } catch (Exception e) {
            qesb.setAutoCompleteStatus("Failed");
        }

    }
}
