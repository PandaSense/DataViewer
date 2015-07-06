/*
 * DvRegServerPools.java  2/6/13 1:04 PM
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

import com.dv.dbinstance.DVServerInstance;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import com.dv.prop.DVPropMain;

public class DvRegServerPools {

    public static Vector getDVRegServerNameList(String filePath) {
        Properties props = new Properties();
        Vector dvPoolsNameList_ = new Vector();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            dvPoolsNameList_.addElement("Create new DV");
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();

                dvPoolsNameList_.addElement(key);
            }

            in.close();
        } catch (IOException ioe) {
            DVLOG.setErrorLog(DvRegServerPools.class.getName(), ioe);
        }

        return dvPoolsNameList_;
    }

    public static Properties getDvProperties(String filePath) {

        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            in.close();
        } catch (IOException ioe) {
            DVLOG.setErrorLog(DvRegServerPools.class.getName(), ioe);
            return null;
        }
        return props;
    }

    //    public static void setDvRegServerValue(String key, String value) {
//
//        Properties prop = new Properties();
//        try {
//            InputStream fis = new FileInputStream(DVPropMain.DV_CONFIG_FOLDER + DVPropMain.DV_SERVER_REG_FILE);
//            prop.load(fis);
//            OutputStream fos = new FileOutputStream(DVPropMain.DV_CONFIG_FOLDER + DVPropMain.DV_SERVER_REG_FILE);
//
//
//            prop.setProperty(key, value);
//            prop.store(fos, "Set [" + key + "] value at [" + DateFormatFactory.getCurrentDateString() + "]");
//            fos.flush();
//            fos.close();
//            fis.close();
//        } catch (IOException ioe) {
//            DVLOG.setErrorLog(DvRegServerPools.class.getName(), ioe);
//        }
//
//    }
    public static void setDVRegSerevrValue(Map keyAndValue, String filePath) {

        Properties prop = new Properties();
        try {


            InputStream fis = new FileInputStream(filePath);
            prop.load(fis);
            OutputStream fos = new FileOutputStream(filePath);

            for (int i = 0; i < keyAndValue.size(); i++) {
                prop.putAll(keyAndValue);
            }

            prop.store(fos, "Set DocuVault DataBase Key values at [" + DateFormatFactory.getCurrentDateString() + "]");
            fos.flush();
            fos.close();
            fis.close();
        } catch (IOException ioe) {
            DVLOG.setErrorLog(DvRegServerPools.class.getName(), ioe);
        }

    }

    public static HashMap getDVRegServerHashmap(String filePath) {
        Properties props = new Properties();
        HashMap pools = new HashMap();

        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = (String) props.getProperty(key);
                pools.put(key, value);
            }

            in.close();
        } catch (IOException ioe) {
            DVLOG.setErrorLog(DvRegServerPools.class.getName(), ioe);
        }

        return pools;
    }

    public static void setDBInstance(String filePath) {

        HashMap instance = getDVRegServerHashmap(filePath);
        Vector key = getDVRegServerNameList(filePath);
        for (int i = 0; i < key.size(); i++) {

            if (!key.get(i).toString().equals("Create new DV")) {
                String[] ins = instance.get(key.get(i).toString()).toString().split(",");
                DVServerInstance svi = new DVServerInstance(ins);
                DVPropMain.DV_SERVER_INSTANCE.put(key.elementAt(i).toString(), svi);
            }
        }

    }
}
