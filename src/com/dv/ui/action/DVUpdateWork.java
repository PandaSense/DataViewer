/*
 * DVUpdateWork.java  2/6/13 1:04 PM
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
import com.dv.ui.editor.QueryEditorStatusBar;
import com.dv.util.DVDbUtil;
import com.dv.util.DVLOG;
import com.dv.util.DataViewerUtilities;
import com.dv.util.DateFormatFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.SwingWorker;

/**
 * @author xyma
 */
public class DVUpdateWork extends SwingWorker<Boolean, Void> {

    boolean pass = true;
    String s = null;
    String t = null;
    Connection c = null;
    String m = null;
    Statement stan;
    ResultSet rsn;
    String[] bSQL;
    QueryEditorStatusBar qesb;
    DVMainEditView dvew;
    DVDbUtil util = new DVDbUtil();
    boolean isc;

    public DVUpdateWork(Connection con, String sql, String type, String[] batchS, DVMainEditView dvew) {

        c = con;
        s = sql;
        bSQL = batchS;
        t = type;
        this.qesb = dvew.getQesb();
        this.dvew = dvew;
    }

    protected Boolean doInBackground() {
        if (!t.equals("B")) {
            return sqlExecuteAndExecuteUpdate(c, s);
        } else {
            return batchSqlExecuteUpdate(c, bSQL);
        }
    }

    protected void done() {
        try {
            qesb.stopProgressBar();
            if (!get()) {
                dvew.getResultTextPane().setText("");
                qesb.setColAndRow(0, 0);
            }
        } catch (Exception err) {
            DVLOG.setErrorLog(DVUpdateWork.class.getName(), err);
        }
    }

    public boolean sqlExecuteAndExecuteUpdate(Connection con, String sql) {

        qesb.startProgressBar();
        pass = true;
        stan = null;
        Connection conn = null;
        Long start = System.currentTimeMillis();

        int resultNumber = 0;

        try {
            setExecuteV(false);
            conn = con;
            isc = conn.isClosed();
            stan = conn.createStatement();
            resultNumber = stan.executeUpdate(sql);

            if (isAutoCommit()) {

                conn.commit();
            }
            qesb.setColAndRow(0, 0);
            dvew.getResultTextPane().setText("\n " + resultNumber + " record(s) affected \n [Execute Time : " + DateFormatFactory.getCurrentDateTimeString() + "]");
            setExecuteV(true);
            dvew.setInforMessage("");

        } catch (SQLException sqle) {
            setExceptionProcess(false);
            dvew.setErrorMessage(sqle.getMessage(), sqle.getErrorCode());
            DVLOG.setErrorLog(DVUpdateWork.class.getName(), sqle);

        } catch (Exception eee) {

            setExceptionProcess(false);
            dvew.setErrorMessage(eee.getMessage(), 9999);
            DVLOG.setErrorLog(DVUpdateWork.class.getName(), eee);
        } finally {
            DataViewerUtilities.scheduleGC();
        }
        Long end = System.currentTimeMillis();
        Long time = end - start;
        qesb.setExecutionTime(time.toString() + " ms");
        return pass;
    }

    public boolean batchSqlExecuteUpdate(Connection con, String[] batchS) {
        qesb.startProgressBar();
        pass = true;
        stan = null;
        Connection conn = null;
        Long start = System.currentTimeMillis();

        ArrayList countline = dvew.getLineNumbers();

        StringBuffer inforResult=new StringBuffer();
        int resultNumber = 0;

        try {
            setExecuteV(false);
            conn = con;
            stan = conn.createStatement();

            for (int i = 0; i < batchS.length; i++) {
                dvew.getGutter().removeAllTrackingIcons();
                dvew.getGutter().addLineTrackingIcon(Integer.parseInt(countline.get(i).toString()), dvew.getTrackingIcon());
                dvew.setLastSqlLine(Integer.parseInt(countline.get(i).toString()));
                resultNumber = stan.executeUpdate(batchS[i]);
                inforResult.append("\n " + resultNumber + " record(s) affected \n [Execute Time : " + DateFormatFactory.getCurrentDateTimeString() + "]"+"\n");
            }

            dvew.getResultTextPane().setText(inforResult.toString());

            if (isAutoCommit()) {
                conn.commit();
            }
            qesb.setColAndRow(0, 0);
            setExecuteV(true);
            dvew.setInforMessage("");
        } catch (SQLException sqle) {
            setRollBack(conn);
            setExceptionProcess(false);
            dvew.setErrorMessage(sqle.getMessage(), sqle.getErrorCode());
            DVLOG.setErrorLog(DVUpdateWork.class.getName(), sqle);
        } catch (Exception eee) {
            setRollBack(conn);
            setExceptionProcess(false);
            dvew.setErrorMessage(eee.getMessage(), 9999);
            DVLOG.setErrorLog(DVUpdateWork.class.getName(), eee);
        } finally {
            DataViewerUtilities.scheduleGC();
        }
        Long end = System.currentTimeMillis();
        Long time = end - start;
        qesb.setExecutionTime(time.toString() + " ms");
        return pass;

    }

    public void setRollBack(Connection con) {

        try {
            con.rollback();
        } catch (SQLException sqle) {
            DVLOG.setErrorLog(DVUpdateWork.class.getName(), sqle);
        }
    }

    public void setExecuteV(boolean is) {
        dvew.getResultTextPane().setVisible(is);
        dvew.getExecute().setEnabled(is);
        dvew.getExecuteLine().setEnabled(is);
    }

    public void setExceptionProcess(boolean isBatch) {
        if (!isBatch) {
            pass = false;
            setExecuteV(true);

        } else {
            pass = false;
            setExecuteV(true);
        }
    }

    public boolean isAutoCommit() {
        return DVPropMain.DV_AUTO_COMMIT.equals("1") ? true : false;
    }
}
