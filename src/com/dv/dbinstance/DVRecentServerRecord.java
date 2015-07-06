/*
 * DVRecentServerRecord.java  2/6/13 1:04 PM
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

package com.dv.dbinstance;

/**
 * @author Cypress
 */
public class DVRecentServerRecord {

    String[] server = null;
    String serverName, index, sqlFileFullPath, count, lastUpdateTime;

    public DVRecentServerRecord(String[] server) {

        this.server = server;
        init();
    }

    public void init() {

        index = server[0];
        serverName = server[1];
        sqlFileFullPath = server[2];
        count = server[3];
        lastUpdateTime = server[4];

    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getLastCloseTime() {
        return lastUpdateTime;
    }

    public void setLastCloseTime(String lastCloseTime) {
        this.lastUpdateTime = lastCloseTime;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getSqlFileFullPath() {
        return sqlFileFullPath;
    }

    public void setSqlFileFullPath(String sqlFileFullPath) {
        this.sqlFileFullPath = sqlFileFullPath;
    }

    public String toString() {
        return getServerName() + " = " + getServerName() + "," + getIndex() + "," + getCount() + "," + getSqlFileFullPath() + "," + getLastCloseTime();
    }
}
