/*
 * DVAutoSqlBuildAction.java  4/24/13 3:33 PM
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
import com.dv.sqlbuild.DataViewerTableInforDialog;
import com.dv.ui.DVMainEditView;
import com.dv.ui.gridresult.DVResultTablePanel;
import com.dv.util.DVDbUtil;
import com.dv.util.DataViewerUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;


public class DVSqlBuildAction extends AbstractAction {

    Connection con = null;
    Statement stan;
    ResultSet rsn;
    ResultSetMetaData rsmdn = null;

    Vector cols, heads;
    String tableName, inforName, creatorName;
    int sqlType;

    DVMainEditView dvev;
    DVDbUtil util = new DVDbUtil();

    private int IS_SINGLE_TABLE_INFORMATION = 0;

    String DB2ServerHost=null,DB2ServerSID=null;

    DataViewerTableInforDialog inforDialog;
    DVResultTablePanel tablePanel;

    public DVSqlBuildAction(int sqlType, DVMainEditView dvev) {
        this.sqlType = sqlType;
        this.dvev = dvev;
        DB2ServerSID=dvev.getDB2ServerSID();
    }

    public void actionPerformed(ActionEvent e) {
        this.inforName = dvev.getDvtextPane().getSelectedText();
        con = dvev.getSubSVConnection();
        if (sqlType == 0) {
            if (inforName!=null) {
                setTableAndSchema();
                String feedback = setRowsAndCols();
                if (feedback.equals("1")) {
                    DataViewerUtilities.displayWarningMessage("There is no any table information,please check if the table name is right.");
                } else if (feedback.equals("0")) {
                    buildInforDialog();
                } else {
                    DataViewerUtilities.displayWarningMessage("There is some exception like below : \n" + feedback);
                }
            } else {
                DataViewerUtilities.displayWarningMessage("Please select table name ,or select schema.table name like XXXXX.XXXXXX.");
            }
        }
    }

    public void buildInforDialog() {

        inforDialog=new DataViewerTableInforDialog(DVPropMain.DV_FRAME.get("MAIN"),creatorName,tableName,cols,heads);
    }
    public void setTableAndSchema() {

        if ((inforName.split("\\.")).length > 1) {
            tableName = inforName.split("\\.")[1];
            creatorName = inforName.split("\\.")[0];
        } else {
            tableName = inforName;
            creatorName = dvev.getSubSVSchema();
        }
    }

    public String setRowsAndCols() {
        String sql = buildSql(tableName, creatorName);

        try {
            stan = con.createStatement();
            rsn = stan.executeQuery(sql);
            rsmdn = rsn.getMetaData();

            if (util.hasRecord(rsn)) {
                cols = util.getAllRows(rsn, rsmdn,-1);
                heads = util.getColumn(rsmdn);
            } else {
                return "1";
            }
        } catch (Exception e) {

            return e.getMessage();

        } finally {
            DataViewerUtilities.scheduleGC();
        }
        return "0";
    }


    public String buildSql(String tableName, String creatorName) {

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT COLNO,NAME,KEYSEQ,COLTYPE,LENGTH,SCALE,NULLS,REMARKS \n");
//        if(DB2ServerSID!=null&&DB2ServerSID.toUpperCase().equals("RDBND50A")){
//            sqlBuffer.append("FROM DB2ADM.SYSCOLUMNS WHERE TBNAME IN \n");
//        }else{
            sqlBuffer.append("FROM SYSIBM.SYSCOLUMNS WHERE TBNAME IN \n");
//        }
        sqlBuffer.append("('" + tableName + "') \n");
        sqlBuffer.append("AND TBCREATOR IN ('" + creatorName + "') ORDER BY COLNO");
//        System.out.println(sqlBuffer.toString());
        return sqlBuffer.toString();
    }

}
