/*
 * DvConnectFactory.java  2/6/13 1:04 PM
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.dv.prop.DVPropMain;
import com.ibm.db2.jcc.DB2BaseDataSource;

public class DvConnectFactory {

    private static String DRIVER = DVPropMain.getDBDriver(DVPropMain.DV_USER_DB_TYPE);

    public static Connection getNewConnection(String key, String URL, String USER, String PASSWORD) throws SQLException {
        Connection conn = null;
        String driver = DVPropMain.getDBDriver(DVPropMain.getDataBaseServerType(key));
        try {
            Class.forName(driver);
            if (USER == null || USER.equals("") || PASSWORD == null || PASSWORD.equals("")) {
                conn = DriverManager.getConnection(URL);
            } else {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException cnfe) {
            DVLOG.setErrorLog(DvConnectFactory.class.getName(), cnfe);
        }
        return conn;
    }

    public static Connection getOtherConnection(String dbType, String url, String user, String password) throws SQLException {
        Connection conn = null;
        try {
            if (!dbType.equals("DB2") && !dbType.equals("DB2S")) {
                java.sql.Driver myDriver = DataViewerUtilities.launchPluginDataBaseDrivers(dbType);
                java.util.Properties prop = new java.util.Properties();
                prop.put("user", user);
                prop.put("password", password);
                conn = myDriver.connect(url, prop);
            } else {
                Class.forName(DVPropMain.getDBDriver(dbType));
                if (dbType.equals("DB2")) {
                    conn = DriverManager.getConnection(url, user, password);
                } else if (dbType.equals("DB2S")) {
                    Properties properties = new Properties();
//                    properties.setProperty("securityMechanism",Integer.toString(DB2BaseDataSource.ENCRYPTED_USER_AND_PASSWORD_SECURITY));
                    properties.setProperty("securityMechanism",Integer.toString(DB2BaseDataSource.ENCRYPTED_USER_PASSWORD_AND_DATA_SECURITY));
                    properties.setProperty("user", user);
                    properties.setProperty("password", password);
                    conn = DriverManager.getConnection(url, properties);
                }
            }
        } catch (Exception cnfe) {
            DVLOG.setErrorLog(DvConnectFactory.class.getName(), cnfe);
        }
        return conn;
    }
}
