/*
 * DVQueryWorker.java  2/6/13 1:04 PM
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

import com.dv.prop.DVPropMain;
import com.dv.ui.DVMainEditView;
import com.dv.ui.editor.QueryEditorStatusBar;
import com.dv.util.DVDbUtil;

import java.util.ArrayList;

import com.dv.util.DVFileUtil;
import com.dv.util.DVLOG;
import com.dv.util.DataViewerUtilities;
import com.dv.util.DateFormatFactory;

import java.awt.Color;
import java.sql.*;
import java.util.Vector;
import javax.swing.SwingWorker;

/**
 * @author xyma
 */
public class DVQueryWorker extends SwingWorker<Boolean, Void> {

    boolean pass = true;
    String queryWorkerSingleSql = null;
    String queryWorkerType = null;
    String lastQueryInformation = null;
    Connection queryWorkerConnection = null;
    String exceptionMessage = null;
    Statement queryWorkerStatement;
    ResultSet queryWorkerResultset;
    String[] queryWorkerBatchSql;
    QueryEditorStatusBar queryEditorStatusBar;
    DVMainEditView queryWorkerMainEditView;
    DVDbUtil util = new DVDbUtil();
    boolean isc;
    public boolean needToRow = false;

    public String databaseType;
    int sqlCode = 9999;
    StringBuffer content = null;

    int maxRecordNumber = 0;

    public DVQueryWorker(Connection con, String sql, String type, String[] batchS, DVMainEditView queryWorkerMainEditView) {
        queryWorkerConnection = con;
        queryWorkerSingleSql = sql;
        queryWorkerBatchSql = batchS;
        queryWorkerType = type;
        this.queryEditorStatusBar = queryWorkerMainEditView.getQesb();
        this.queryWorkerMainEditView = queryWorkerMainEditView;
        databaseType=DVPropMain.getDataBaseServerType(queryWorkerMainEditView.getSubSVKey());
        maxRecordNumber = queryWorkerMainEditView.getMaxRecordFieldNumber();
    }

    protected Boolean doInBackground() {
        if (queryWorkerBatchSql != null) {
            return sqlBatchExecute(queryWorkerConnection, queryWorkerBatchSql, queryWorkerType);
        } else {
            return sqlExecuteAndExecuteLine(queryWorkerConnection, queryWorkerSingleSql, queryWorkerType);
        }
    }

    public boolean sqlExecuteAndExecuteLine(Connection con, String sql, String typ) {
        queryEditorStatusBar.startProgressBar();
        pass = true;
        ResultSetMetaData rsmdn = null;
        queryWorkerStatement = null;
        queryWorkerResultset = null;
        content = new StringBuffer();
        queryWorkerMainEditView.clearBatchMap();
        Long start = System.currentTimeMillis();
        try {
            setExecuteV(false);
            isc = con.isClosed();
            queryWorkerStatement = con.createStatement();
            queryWorkerResultset = queryWorkerStatement.executeQuery(sql);

            rsmdn = queryWorkerResultset.getMetaData();

//            util.getColumnType(rsmdn);

            if (util.hasRecord(queryWorkerResultset)) {
                queryWorkerMainEditView.setRow(util.getAllRows(queryWorkerResultset, rsmdn, maxRecordNumber));
                queryWorkerMainEditView.setCol(util.getColumn(rsmdn));
                queryWorkerMainEditView.getResultTextPane().setForeground(Color.BLACK);
                lastQueryInformation = "\n" + "Query Result is " + String.valueOf(queryWorkerMainEditView.getRow().size()) + " record(s) selected. [Execute Time : " + DateFormatFactory.getCurrentDateTimeString() + " ]";
                content.append(DVFileUtil.formatSQLResultToShow(queryWorkerMainEditView.getCol(), queryWorkerMainEditView.getRow()));
                queryWorkerMainEditView.setLastQueryInformation(lastQueryInformation);
                content.append(lastQueryInformation);
                if (needToRow) {
                    queryWorkerMainEditView.getResultTextPane().setText(queryWorkerMainEditView.setVectorCol(queryWorkerMainEditView.getRow()));
                    needToRow = false;
                } else {
                    queryWorkerMainEditView.getResultTextPane().setText(content.toString());
                    queryWorkerMainEditView.setHistoryResultContent(sql, content.toString());
                }
                queryWorkerMainEditView.getResultTextPane().setCaretPosition(0);
                setExecuteV(true);
                needToRow = false;
                queryWorkerMainEditView.setInforMessage("Execute SQL successfully");
                queryEditorStatusBar.setColAndRow(queryWorkerMainEditView.getCol().size(), queryWorkerMainEditView.getRow().size());
                queryWorkerMainEditView.setCanBeFormatBatchSqlResult(false);
                content = null;
            } else {
                setExecuteV(true);
                queryWorkerMainEditView.setRow(new Vector());
                queryWorkerMainEditView.setCol(util.getColumn(rsmdn));
                needToRow = false;
                lastQueryInformation = "\n" + "Query Result is " + "0" + " record(s) selected. [Execute Time : " + DateFormatFactory.getCurrentDateTimeString() + " ]";
                queryWorkerMainEditView.setLastQueryInformation(lastQueryInformation);
                content.append(DVFileUtil.formatSQLResultToShow(queryWorkerMainEditView.getCol(), queryWorkerMainEditView.getRow()));
                content.append(lastQueryInformation);
                queryWorkerMainEditView.getResultTextPane().setBorder(null);
                queryWorkerMainEditView.getResultTextPane().setText(content.toString());
                queryWorkerMainEditView.getResultTextPane().setCaretPosition(0);
                queryWorkerMainEditView.setHistoryResultContent(sql, content.toString());
                queryWorkerMainEditView.setInforMessage("There is no any record");
                queryEditorStatusBar.setColAndRow(0, 0);
                queryWorkerMainEditView.setCanBeFormatBatchSqlResult(false);
                content = null;
            }
            if (DVPropMain.DV_DISPLAY_GRID.equals("1")) {
                queryWorkerMainEditView.setGridResultData();
            }
        } catch (SQLException sqle) {
            exceptionMessage = sqle.getMessage();
            sqlCode = sqle.getErrorCode();
            DVLOG.setErrorLog(DVQueryWorker.class.getName(), sqle);
            setExceptionProcess(false);
        } catch (Exception sqle) {
            exceptionMessage = sqle.getMessage();
            DVLOG.setErrorLog(DVQueryWorker.class.getName(), sqle);
            setExceptionProcess(false);
        } finally {
            closeResultandStatament();
            DataViewerUtilities.scheduleGC();
        }
        Long end = System.currentTimeMillis();
        Long time = end - start;
        queryEditorStatusBar.setExecutionTime(time.toString() + " ms");
        return pass;
    }

    public void setExceptionProcess(boolean isBatch) {
        if (!isBatch) {
            pass = false;
            setExecuteV(true);
            needToRow = false;
            queryWorkerMainEditView.setCanBeFormatBatchSqlResult(false);
            queryWorkerMainEditView.getResultTextPane().setText("");
        } else {
            pass = false;
            setExecuteV(true);
            queryWorkerMainEditView.setCanBeFormatBatchSqlResult(false);
            queryWorkerMainEditView.getResultTextPane().setText("");
        }
        content = null;
    }

    public boolean sqlBatchExecute(Connection con, String[] sql, String typ) {
        StringBuffer sb = new StringBuffer();
        queryEditorStatusBar.startProgressBar();
        pass = true;
        ResultSetMetaData rsmdn = null;
        queryWorkerStatement = null;
        queryWorkerResultset = null;
        Long start = System.currentTimeMillis();
        queryWorkerMainEditView.clearBatchMap();
        ArrayList countline = queryWorkerMainEditView.getLineNumbers();
        try {
            setExecuteV(false);
            isc = con.isClosed();
            queryWorkerStatement = con.createStatement();
            Vector rows, cols;
            for (int i = 0; i < sql.length; i++) {
                content = new StringBuffer();
                queryWorkerMainEditView.getGutter().removeAllTrackingIcons();
                queryWorkerMainEditView.getGutter().addLineTrackingIcon(Integer.parseInt(countline.get(i).toString()), queryWorkerMainEditView.getTrackingIcon());
                queryWorkerMainEditView.setLastSqlLine(Integer.parseInt(countline.get(i).toString()));
                queryWorkerResultset = queryWorkerStatement.executeQuery(sql[i]);
                rsmdn = queryWorkerResultset.getMetaData();
                if (util.hasRecord(queryWorkerResultset)) {
                    rows = util.getAllRows(queryWorkerResultset, rsmdn, maxRecordNumber);
                    cols = util.getColumn(rsmdn);
                    lastQueryInformation = "\n" + "Query Result is " + String.valueOf(rows.size()) + " record(s) selected. [Execute Time : " + DateFormatFactory.getCurrentDateTimeString() + "]" + "\n\n";
                    content.append(DVFileUtil.formatSQLResultToShow(cols, rows));
                    content.append(lastQueryInformation);
                    sb.append(content.toString());
                    needToRow = false;
                    queryWorkerMainEditView.setBatchMap(String.valueOf(i), cols, rows);
                    queryWorkerMainEditView.setHistoryResultContent(sql[i], content.toString());
                    queryWorkerMainEditView.setBatchLastInformations(String.valueOf(i), lastQueryInformation);
                } else {
                    cols = util.getColumn(rsmdn);
                    rows = new Vector();
                    queryWorkerMainEditView.setColType(null);
                    needToRow = false;
                    lastQueryInformation = "\n" + "Query Result is " + "0" + " record(s) selected. [Execute Time : " + DateFormatFactory.getCurrentDateTimeString() + " ]" + "\n\n";
                    content.append(DVFileUtil.formatSQLResultToShow(cols, rows));
                    content.append(lastQueryInformation);
                    sb.append(content.toString());
                    queryWorkerMainEditView.setBatchMap(String.valueOf(i), cols, rows);
                    queryWorkerMainEditView.setHistoryResultContent(sql[i], content.toString());
                    queryWorkerMainEditView.setBatchLastInformations(String.valueOf(i), lastQueryInformation);
                }
            }
            queryWorkerMainEditView.getResultTextPane().setText(sb.toString());
            queryWorkerMainEditView.getResultTextPane().setCaretPosition(0);
            setExecuteV(true);
            queryWorkerMainEditView.setInforMessage("Execute Batch SQL successfully");
            queryEditorStatusBar.setColAndRow(0, 0);
            queryWorkerMainEditView.setCanBeFormatBatchSqlResult(true);
            content = null;
            if (DVPropMain.DV_DISPLAY_GRID.equals("1")) {
                queryWorkerMainEditView.setBatchGridResultData();
            }

        } catch (SQLException sqle) {
            exceptionMessage = sqle.getMessage();
            sqlCode = sqle.getErrorCode();
            DVLOG.setErrorLog(DVQueryWorker.class.getName(), sqle);
            setExceptionProcess(true);
        } catch (Exception ee) {
            exceptionMessage = ee.getMessage();
            DVLOG.setErrorLog(DVQueryWorker.class.getName(), ee);
            setExceptionProcess(true);
        } finally {
            closeResultandStatament();
            DataViewerUtilities.scheduleGC();
        }
        Long end = System.currentTimeMillis();
        Long time = end - start;
        queryEditorStatusBar.setExecutionTime(time.toString() + " ms");
        return pass;
    }

    public void closeResultandStatament() {
        if(!databaseType.equals("MYSQL")){
            try {
                queryWorkerStatement.cancel();
                queryWorkerResultset.close();
            } catch (Exception e) {
                DVLOG.setErrorLog(DVQueryWorker.class.getName(), e);
            }
        }
    }

    public void setExecuteV(boolean is) {
        queryWorkerMainEditView.getResultTextPane().setVisible(is);
        queryWorkerMainEditView.getExecute().setEnabled(is);
        queryWorkerMainEditView.getExecuteLine().setEnabled(is);
    }

    protected void done() {
        try {
            if (isCancelled()) {
                cancelExecuting();
                queryEditorStatusBar.stopProgressBar();
                return;
            }
            queryEditorStatusBar.stopProgressBar();
            if (!get()) {
                queryWorkerMainEditView.getResultTextPane().setText("");
                queryEditorStatusBar.setColAndRow(0, 0);
                queryWorkerMainEditView.setErrorMessage(exceptionMessage, sqlCode);
            }
        } catch (Exception e) {
            DVLOG.setErrorLog(DVQueryWorker.class.getName(), e);
        } finally {
            DataViewerUtilities.scheduleGC();
        }
    }

    public void cancelExecuting() {
        try {
            queryWorkerStatement.cancel();
            queryWorkerResultset.close();

            if (queryWorkerType.toUpperCase().equals("B")) {
                queryWorkerMainEditView.cleanBatchSql();
            } else {
                queryWorkerMainEditView.setRow(new Vector());
                queryWorkerMainEditView.setCol(new Vector());
            }
        } catch (Exception eee) {
            System.out.println(eee);
        } finally {
            DataViewerUtilities.scheduleGC();
        }
    }
}