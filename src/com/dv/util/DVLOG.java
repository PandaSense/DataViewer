/*
 * DVLOG.java  2/6/13 1:04 PM
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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.dv.prop.*;

import java.util.ResourceBundle;

public class DVLOG {

    static Logger infoLog = null;
    static Logger errLog = null;

    static boolean isPrintToControl = false;

    public DVLOG() {
        init();
    }

    public DVLOG(boolean controlPrint) {
        init();
        isPrintToControl = controlPrint;
    }

    public void init() {
        System.setProperty("dvloghome", DVPropMain.DV_LOG_FOLDER);
        PropertyConfigurator.configure(DVLOG.class.getResource("/com/dv/util/dataviewerlog.properties"));
        errLog = Logger.getLogger("ERROR");
        infoLog = Logger.getLogger("INFO");
    }

    public static void setErrorLog(String className, Exception msg) {
        errLog.error(className, msg);
        DVPropMain.DATAVIEWER_EXCEPTION.put("ERROR",msg);
        if (isPrintToControl) {
            System.out.println(className);
            msg.printStackTrace();
        }
    }

    public static void setErrorLog(String message) {
        errLog.error(message);
        if (isPrintToControl) {
            System.out.println(message);
        }
    }

    public static void setInfoLog(String msg) {
        infoLog.info(msg);
        if (isPrintToControl) {
            System.out.println(msg);
        }
    }
}
