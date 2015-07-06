/*
 * DVDbUtil.java  2/6/13 1:04 PM
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

import java.sql.*;
import java.util.Vector;
import java.lang.String;

public class DVDbUtil {

    DatabaseMetaData dbMetaData = null;

    public synchronized boolean hasRecord(ResultSet rs) {
        try {
            boolean moreRecords = rs.next();
            if (!moreRecords) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Vector getAllRows(ResultSet rs, ResultSetMetaData rsmd, int maxrecords) {
        Vector<Vector> rows = new Vector<Vector>();
        int recordCount = 0;
        try {
            int[] columnTypes = new int[rsmd.getColumnCount()];
            do {
                recordCount++;
                rows.addElement(getCurrentRow(rs, rsmd));
                if (recordCount == maxrecords) {
                    break;
                }
            } while (rs.next());
        } catch (SQLException sqle) {
            DVLOG.setErrorLog(DVDbUtil.class.getName(), sqle);
        }
        return rows;
    }

    public Vector getCurrentRow(ResultSet rs, ResultSetMetaData rsmd) {
        Vector<String> currentRow = new Vector<String>();
        int count = 0;
        try {
            count = rsmd.getColumnCount();
            for (int i = 1; i <= count; ++i) {
                try {
                    if (rs.getObject(i) == null) {
                        currentRow.addElement("{null}");
                    } else {
                        setColumnValue(currentRow,rsmd.getColumnType(i),rs,i);
                    }
                } catch (SQLException qqqq) {
                    currentRow.addElement("{error}");
                    DVLOG.setErrorLog(DVDbUtil.class.getName(), qqqq);
                }
            }
        } catch (SQLException eee) {
            DVLOG.setErrorLog(DVDbUtil.class.getName(), eee);
        }
        return currentRow;
    }

    public void setColumnValue(Vector<String> currentRow, int columnType, ResultSet resultSet, int index) {
        int dataType = columnType;
        try {
            switch (dataType) {
                case Types.CHAR:
                    currentRow.addElement(resultSet.getString(index));
                    break;
                case Types.VARCHAR:
                    currentRow.addElement(resultSet.getString(index));
                    break;
                case Types.DATE:
                    currentRow.addElement(resultSet.getDate(index).toString());
                    break;
                case Types.TIME:
                    currentRow.addElement(resultSet.getTime(index).toString());
                    break;
                case Types.TIMESTAMP:
                    currentRow.addElement(resultSet.getTimestamp(index).toString());
                    break;
                case Types.LONGVARCHAR:
                case Types.CLOB:
                    currentRow.addElement(resultSet.getString(index));
                    break;
                case Types.LONGVARBINARY:
                case Types.VARBINARY:
                case Types.BINARY:
                case Types.INTEGER:
                    currentRow.addElement(String.valueOf(resultSet.getInt(index)));
                    break;
                case Types.DECIMAL:
                    currentRow.addElement(resultSet.getBigDecimal(index).stripTrailingZeros().toPlainString());
                    break;
                case Types.DOUBLE:
                    currentRow.addElement(String.valueOf(resultSet.getDouble(index)));
                    break;
                case Types.FLOAT:
                    currentRow.addElement(String.valueOf(resultSet.getFloat(index)));
                    break;
                case Types.BLOB:
                    currentRow.addElement(resultSet.getBlob(index).toString());
                    break;
                default:
                    currentRow.addElement(resultSet.getObject(index).toString());
                    break;
            }
        } catch (Exception e) {
            try {
                currentRow.addElement(resultSet.getString(index));
            } catch (SQLException ee) {
                currentRow.addElement("{error}");
            }
        }
    }


    public Vector getColumn(ResultSetMetaData rsmd) {
        Vector<String> columnHeads = new Vector<String>();
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                if (rsmd.getColumnName(i) == null) {
                    columnHeads.addElement(" ");
                } else {
                    columnHeads.addElement(rsmd.getColumnName(i));
                }
            }
        } catch (SQLException sqle) {
            DVLOG.setErrorLog(DVDbUtil.class.getName(), sqle);
        }
        return columnHeads;
    }

    public Vector<String> getColumnType(ResultSetMetaData rsmd) {
        Vector<String> columnHeads = new Vector<String>();
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                Vector<String> colnameandsize = new Vector<String>();
                if (rsmd.getColumnTypeName(i) == null) {
//                    colnameandsize.addElement(" ");
                    columnHeads.addElement(null);
//                    colnameandsize.addElement(" ");
//                    colnameandsize.addElement(" ");

                } else {
//                    colnameandsize.addElement(rsmd.getColumnName(i));
//                    colnameandsize.addElement(rsmd.getColumnTypeName(i));
                    columnHeads.addElement(rsmd.getColumnTypeName(i));
//                    System.out.println(rsmd.getColumnTypeName(i));
////                    colnameandsize.addElement(rsmd.getCatalogName(i));
//                    colnameandsize.addElement(String.valueOf(rsmd.getColumnDisplaySize(i)));
//                    colnameandsize.addElement(String.valueOf(rsmd.getScale(i)));
//                    colnameandsize.addElement(String.valueOf(rsmd.getPrecision(i)));
                }
            }
        } catch (SQLException sqle) {
            DVLOG.setErrorLog(DVDbUtil.class.getName(), sqle);
        }
        return columnHeads;
    }

    public synchronized Vector getAllTableNames(Connection con, String SCHEMA) {
        Vector allTableNames = new Vector();
        Vector out = new Vector();
        ResultSet tabs = null;
        try {
            dbMetaData = con.getMetaData();

            String[] types = {"TABLE"};

            if (SCHEMA == null) {
                tabs = dbMetaData.getTables(null, null, null, types);
            } else {
                tabs = dbMetaData.getTables(null, SCHEMA, null, types);
            }
            while (tabs.next()) {

                allTableNames.add(tabs.getString(3));

            }
        } catch (SQLException sqle) {
            DVLOG.setErrorLog(DVDbUtil.class.getName(), sqle);
        }
//        getSchemaNames(dbMetaData);

        out = DVUtil.vectorSortIntoVector(allTableNames);
        return out;
    }

    public synchronized Vector getSchemaNames(DatabaseMetaData dbMetaData) {
        Vector out = new Vector();
        ResultSet tabs = null;
        try {
            tabs = dbMetaData.getSchemas();
            while (tabs.next()) {
                out.addElement(tabs.getString(1));
            }
        } catch (SQLException sqle) {
            DVLOG.setErrorLog(DVDbUtil.class.getName(), sqle);
        }
        return out;
    }

    public synchronized Vector getAllViews(Connection con, String SCHEMA) {
        Vector allTableNames = new Vector();
        Vector out = new Vector();
        ResultSet tabs = null;
        try {
            dbMetaData = con.getMetaData();
            String[] types = {"VIEW"};
            if (SCHEMA == null) {
                tabs = dbMetaData.getTables(null, null, null, types);
            } else {
                tabs = dbMetaData.getTables(null, SCHEMA, null, types);
            }

            while (tabs.next()) {

                allTableNames.add(tabs.getString(3));

            }
        } catch (SQLException sqle) {
            DVLOG.setErrorLog(DVDbUtil.class.getName(), sqle);
        }
        out = DVUtil.vectorSortIntoVector(allTableNames);
        return out;
    }

    public static Vector getAllTableAndView(Vector table, Vector view) {
        Vector out = new Vector();
        Vector all = new Vector();

        for (int i = 0; i < table.size(); i++) {
            all.addElement(table.get(i).toString());
        }
        for (int i = 0; i < view.size(); i++) {
            all.addElement(view.get(i).toString());
        }
        out = DVUtil.vectorSortIntoVector(all);
        return out;
    }




}
