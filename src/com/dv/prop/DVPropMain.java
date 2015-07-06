/*
 * DVPropMain.java  2/6/13 1:04 PM
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

package com.dv.prop;

import com.dv.dbinstance.DVServerInstance;
import com.dv.ui.component.FindAndReplaceFrame;

import java.util.*;

import com.dv.ui.frequent.DataViewerFrequentView;
import com.dv.ui.frequent.DataViewerNewFrequentView;
import com.dv.util.DVFileUtil;
import com.dv.util.DataViewerUtilities;
import com.dv.util.FileIO;

import java.io.File;
import javax.swing.JFrame;

import org.fife.ui.autocomplete.CompletionProvider;

public class DVPropMain {

    public static final String DV_ABS_PATH = getABSpath();
    public static final String DV_USER_HOME_PATH = System.getProperty("user.home") + "/.dataviewer/";
    public static String DV_USER_HOME_CONFIG_FOLDER = DV_USER_HOME_PATH + "config/";
    public static String DV_USER_HOME_UPGRADE_FOLDER = DV_USER_HOME_PATH + "upgrade/";
    public static String DV_LOG_FOLDER = DV_USER_HOME_PATH + "log/";
    public static String DV_HISTORY_RESULT_FOLDER = DV_USER_HOME_PATH + "history/";
    public static String DV_CONFIG_FOLDER = DV_USER_HOME_CONFIG_FOLDER;
    public static String DV_HISTORY_FOLDER = getABSpath() + "history/";
    public static String DV_OLD_LOG_FOLDER = getABSpath() + "log/";
    public static String DV_INSTANCE_FOLDER = getABSpath() + "resource/";
    public static String DV_SQL_FOLDER = getABSpath() + "sql/";
    public static String DV_EXPORT_FOLDER = getABSpath() + "export/";
    public static String DV_LIB_HOME = getABSpath() + "lib/";

    public static boolean isCreate = setDataViewerHomeFolder();
    public static String DV_ICON_PATH = "/com/dv/ui/resources/icons/";
    public static final String DV_NAME = DataViewerUtilities.getDVName();
    public static final String DV_VERSION = DataViewerUtilities.getDVVersion();
    public static Properties editorMain = DVFileUtil.getPropertiesFile(getConfigHomeFolder() + "DV_EDITOR.properties");
    private static final String MSG = "com.dv.prop.DataViewerDBDriver";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);
    private static final String MAINMSG = "com.dv.prop.DataViewer";
    private static final ResourceBundle mainUSERResource = ResourceBundle.getBundle(MAINMSG);


    public static boolean setDataViewerHomeFolder() {
        File sqlFile = new File(DVPropMain.DV_USER_HOME_PATH);

        if (!sqlFile.exists()) {
            sqlFile.mkdir();
        }

        sqlFile = new File(DV_USER_HOME_UPGRADE_FOLDER);
        if (!sqlFile.exists()) {
            sqlFile.mkdir();
        }

        sqlFile = new File(DVPropMain.DV_USER_HOME_UPGRADE_FOLDER + "path");

        if (!sqlFile.exists()) {
            try {
                sqlFile.createNewFile();
            } catch (Exception e) {
            }
        }

        FileIO.overwrite(DVPropMain.DV_USER_HOME_UPGRADE_FOLDER + "path", DVPropMain.DV_LIB_HOME);

        sqlFile = new File(DVPropMain.DV_HISTORY_RESULT_FOLDER);

        if (!sqlFile.exists()) {
            sqlFile.mkdir();
            File sourceFile = new File(DVPropMain.DV_HISTORY_FOLDER);
            if (sourceFile.exists()) {
                File[] full = sourceFile.listFiles();
                if (full.length != 0) {
                    for (int i = 0; i < full.length; i++) {
                        FileIO.copy(DVPropMain.DV_HISTORY_FOLDER + full[i].getName(), DVPropMain.DV_HISTORY_RESULT_FOLDER + full[i].getName());
                        full[i].delete();
                    }
                }
                sourceFile.delete();
            }
        }

        sqlFile = new File(DVPropMain.DV_OLD_LOG_FOLDER);
        if (sqlFile.exists()) {
            File sourceFile = new File(DVPropMain.DV_LOG_FOLDER);
            if (!sourceFile.exists()) {
                sourceFile.mkdir();
            }
            sourceFile = new File(DVPropMain.DV_LOG_FOLDER + "INFO/");

            if (!sourceFile.exists()) {
                sourceFile.mkdir();
            }
            sourceFile = new File(DVPropMain.DV_OLD_LOG_FOLDER + "INFO/");
            File[] full = sourceFile.listFiles();
            if (full.length != 0) {
                for (int i = 0; i < full.length; i++) {
                    if (!full[i].getName().equals("INFOR_DV")) {
                        FileIO.copy(DVPropMain.DV_OLD_LOG_FOLDER + "INFO/" + full[i].getName(), DVPropMain.DV_LOG_FOLDER + "INFO/" + full[i].getName());
                    }
                    full[i].delete();
                }
            }
            sourceFile.delete();

            sourceFile = new File(DVPropMain.DV_OLD_LOG_FOLDER + "ERROR/");

            if (sourceFile.exists()) {
                full = sourceFile.listFiles();
                if (full.length != 0) {
                    for (int i = 0; i < full.length; i++) {
                        full[i].delete();
                    }
                }
                sourceFile.delete();
            }
            sqlFile.delete();
        }

        sqlFile = new File(DVPropMain.DV_USER_HOME_CONFIG_FOLDER);
        if (!sqlFile.exists()) {
            sqlFile.mkdir();
            moveOldHomeFileIntoNewHome();
        }
        return true;
    }

    public static void moveOldHomeFileIntoNewHome() {

        String oldConfigFolder = DVPropMain.getABSpath() + "config/";
        File newHome = new File(DVPropMain.DV_USER_HOME_CONFIG_FOLDER);
        File tempHome = new File(oldConfigFolder);

        if (tempHome.exists()) {
            File temp = new File(oldConfigFolder + "dvdatabase.xml");
            if (temp.exists()) {
                FileIO.copy(oldConfigFolder + "dvdatabase.xml", DVPropMain.DV_USER_HOME_CONFIG_FOLDER + "dvdatabase.xml");
            }
            temp = new File(oldConfigFolder + "dvrecentserver.xml");
            if (temp.exists()) {
                FileIO.copy(oldConfigFolder + "dvrecentserver.xml", DVPropMain.DV_USER_HOME_CONFIG_FOLDER + "dvrecentserver.xml");
            }
            temp = new File(oldConfigFolder + "DV_EDITOR.properties");
            if (temp.exists()) {
                FileIO.copy(oldConfigFolder + "DV_EDITOR.properties", DVPropMain.DV_USER_HOME_CONFIG_FOLDER + "DV_EDITOR.properties");
            }
            temp = new File(oldConfigFolder + "DV_LAYOUT.properties");
            if (temp.exists()) {
                FileIO.copy(oldConfigFolder + "DV_LAYOUT.properties", DVPropMain.DV_USER_HOME_CONFIG_FOLDER + "DV_LAYOUT.properties");
            }

            temp = new File(oldConfigFolder + "DB2_SQL_ERROR_CODE_CN.sqlcodes");
            if (temp.exists()) {
                temp.delete();
            }
            temp = new File(oldConfigFolder + "DB2_SQL_ERROR_CODE_EN.sqlcodes");
            if (temp.exists()) {
                temp.delete();
            }

            File[] full = tempHome.listFiles();
            if (full.length != 0) {
                for (int i = 0; i < full.length; i++) {
                    full[i].delete();
                }
            }
            tempHome.delete();
        } else if (!tempHome.exists() && !newHome.exists()) {
            try {
                newHome.createNewFile();
                newHome = new File(DVPropMain.DV_USER_HOME_CONFIG_FOLDER + "DV_EDITOR.properties");
                newHome.createNewFile();
                newHome = new File(DVPropMain.DV_USER_HOME_CONFIG_FOLDER + "DV_LAYOUT.properties");
                newHome.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    public static String getDBDriver(String type) {
        if (type == null || type.equals("")) {
            type = DB_DEFAULT_TYPE;
        }
        return mainViewResource.getString(type + ".DRIVER");
    }


    public static String getDBDriverJar(String type) {
        if (type == null || type.equals("")) {
            type = DB_DEFAULT_TYPE;
        }
        return mainViewResource.getString(type + ".JAR");
    }


    public static String getConfigHomeFolder() {

        File homeConfig = new File(DV_USER_HOME_CONFIG_FOLDER);
        if (homeConfig.exists()) {
            return DV_USER_HOME_CONFIG_FOLDER;
        } else {
            return getABSpath() + "config/";
        }
    }

    public static String getDBURL(String type) {

        if (type == null || type.equals("")) {
            type = DB_DEFAULT_TYPE;
        }
        return mainViewResource.getString(type + ".URL");
    }

    public static void resetEditorConfig() {
        DV_FONT_SIZE = editorMain.get("DV_FONT_SIZE").toString();
        DV_SQL_FONT_SIZE = editorMain.get("DV_SQL_FONT_SIZE").toString();
        LOOK_AND_FEEL_NAME = editorMain.get("LOOK_AND_FEEL_NAME").toString();
        DV_FORMAT_WORD_WIDTH = editorMain.get("DV_FORMAT_WORD_WIDTH").toString();
        DV_FILE_RECENT_PATH = editorMain.get("DV_FILE_RECENT_PATH").toString();
        DV_EDITOR_MARGIN_SIZE = editorMain.get("DV_EDITOR_MARGIN_SIZE").toString();
        DV_USER_DB_TYPE = editorMain.get("DV_USER_DB_TYPE").toString();
        DV_AUTO_COMMIT = editorMain.get("DV_AUTO_COMMIT").toString();
        DV_DISPLAY_GRID = editorMain.get("DV_DISPLAY_GRID").toString();
        DV_SQLCODE_CHINESE = editorMain.get("DV_SQLCODE_CHINESE").toString();
        DV_SYSTEM_TRAY = editorMain.get("DV_SYSTEM_TRAY").toString();
        DV_AUTO_UPDATE = editorMain.get("DV_AUTO_UPDATE").toString();
        DV_ENCRYPTED_DATABASE_PASSWORD = editorMain.get("DV_ENCRYPTED_DATABASE_PASSWORD").toString();

        DV_SQL_EDITAREA_HAS_MARGIN = editorMain.get("DV_SQL_EDITAREA_HAS_MARGIN").toString();
        DV_LANDK_JAR_NAME = editorMain.get("DV_LANDK_JAR_NAME").toString();

        DV_CTC_WITH_DATE = getAutoUpdateServerPropDetail("DV_CTC_WITH_DATE");
        DV_AUTO_UPDATE_SERVER = getAutoUpdateServerPropDetail("DV_AUTO_UPDATE_SERVER");
        DV_AUTO_UPDATE_SERVER_USER = getAutoUpdateServerPropDetail("DV_AUTO_UPDATE_SERVER_USER");
        DV_AUTO_UPDATE_SERVER_PASSWORD = getAutoUpdateServerPropDetail("DV_AUTO_UPDATE_SERVER_PASSWORD");
    }

    public static String DB_DEFAULT_TYPE = mainUSERResource.getString("DB_TYPE");
    public static String DV_SQL_FONT_NAME = mainUSERResource.getString("DV_SQL_FONT_NAME");
    public static String DV_FONT_NAME = mainUSERResource.getString("DV_FONT_NAME");
    public static String DV_FONT_SIZE = getEditPropDetail("DV_FONT_SIZE");
    public static String DV_SQL_FONT_SIZE = getEditPropDetail("DV_SQL_FONT_SIZE");
    public static String LOOK_AND_FEEL_NAME = getEditPropDetail("LOOK_AND_FEEL_NAME");
    public static String DV_FORMAT_WORD_WIDTH = getEditPropDetail("DV_FORMAT_WORD_WIDTH");
    public static String DV_FILE_RECENT_PATH = getEditPropDetail("DV_FILE_RECENT_PATH");
    public static String DV_EDITOR_MARGIN_SIZE = getEditPropDetail("DV_EDITOR_MARGIN_SIZE");
    public static String DV_USER_DB_TYPE = getEditPropDetail("DV_USER_DB_TYPE");
    public static String DV_AUTO_COMMIT = getEditPropDetail("DV_AUTO_COMMIT");
    public static String DV_LOCATION = getEditPropDetail("DV_LOCATION");
    public static String DV_DISPLAY_GRID = getEditPropDetail("DV_DISPLAY_GRID");
    public static String DV_SYSTEM_TRAY = getEditPropDetail("DV_SYSTEM_TRAY");
    public static String DV_SQLCODE_CHINESE = getEditPropDetail("DV_SQLCODE_CHINESE");
    public static String DV_RECENT_DB_INSTANCE = getEditPropDetail("DV_RECENT_DB_INSTANCE");
    public static String DV_AUTO_UPDATE = getEditPropDetail("DV_AUTO_UPDATE");

//    public static String DV_MANUAL_USER_HOME_PATH = getEditPropDetail("DV_MANUAL_USER_HOME_PATH");

    public static String DV_SUPPORT_DB_NAME = getDBSupportType("DV_SUPPORT_DB_NAME");

    public static String[] DV_DB_TYPE_ARRAY = DV_SUPPORT_DB_NAME.split(",");


    public static String DV_SQL_EDITAREA_HAS_MARGIN = getEditPropDetail("DV_SQL_EDITAREA_HAS_MARGIN");
    public static String DV_SQL_RESULT_MAX_NUMBER = getEditPropDetail("DV_SQL_RESULT_MAX_NUMBER");

    public static String DV_LANDK_JAR_NAME = getEditPropDetail("DV_LANDK_JAR_NAME");

    public static String DV_AUTO_UPDATE_SERVER = getAutoUpdateServerPropDetail("DV_AUTO_UPDATE_SERVER");
    public static String DV_AUTO_UPDATE_SERVER_USER = getAutoUpdateServerPropDetail("DV_AUTO_UPDATE_SERVER_USER");
    public static String DV_AUTO_UPDATE_SERVER_PASSWORD = getAutoUpdateServerPropDetail("DV_AUTO_UPDATE_SERVER_PASSWORD");

    public static String DV_CTC_WITH_DATE = getAutoUpdateServerPropDetail("DV_CTC_WITH_DATE");

//    public static String DV_ENCRYPTED_DATABASE_PASSWORD = getNormalEditPropDetail("DV_ENCRYPTED_DATABASE_PASSWORD");

    public static String DV_ENCRYPTED_DATABASE_PASSWORD = "1";

    public static HashMap<String, Object> DB_SERVER_CONNECT = new HashMap<String, Object>();
    public static HashMap<String, Vector> DB_SERVER_TABLE_VIEW_NAME = new HashMap<String, Vector>();
    public static HashMap<String, String> DB_SERVER_SCHEMA = new HashMap<String, String>();
    public static HashMap<String, String> DB_SERVER_CON_DETAIL = new HashMap<String, String>();
    public static HashMap<String, String> DB_SERVER_CON_USER = new HashMap<String, String>();
    public static HashMap<String, String> DB_SERVER_CON_PW = new HashMap<String, String>();
    public static HashMap<String, JFrame> DV_FRAME = new HashMap<String, JFrame>();
    public static HashMap<String, DVServerInstance> DV_SERVER_INSTANCE = new HashMap<String, DVServerInstance>();
    public static HashMap<String, FindAndReplaceFrame> DV_FindAndReplaceFrame = new HashMap<String, FindAndReplaceFrame>();
    public static HashMap<String, CompletionProvider> DB_AUTOCOMPLETE_POOL = new HashMap<String, CompletionProvider>();
    //Recent Record List Config
//    public static HashMap<String, DVRecentServerRecord> DV_RECENT_INSTANCE_MAP = new HashMap<String, DVRecentServerRecord>();
    public static HashMap<String, String> DV_RECENT_INSTANCE_COUNT = new HashMap<String, String>();
    public static HashMap<String, String> DV_RECENT_INSTANCE_SQLFILEFULLPATH = new HashMap<String, String>();
    public static HashMap<String, String> DV_RECENT_INSTANCE_LASTTIME = new HashMap<String, String>();
    public static HashMap<String, String> DV_RECENT_INSTANCE_NAME = new HashMap<String, String>();
    public static HashMap<String, String> DV_RECENT_INSTANCE_SEQ = new HashMap<String, String>();
    public static HashMap<String, String> DV_RECENT_INSTANCE_LAST_EXECUTE_SQL_LINE = new HashMap<String, String>();
//    public static HashMap<String, DataViewerFrequentView> DV_DVFrequentView = new HashMap<String, DataViewerFrequentView>();

    public static HashMap<String, DataViewerNewFrequentView> DV_DVFrequentView = new HashMap<String, DataViewerNewFrequentView>();


    public static HashMap<String, Throwable> DATAVIEWER_EXCEPTION = new HashMap<String, Throwable>();
    //upgrade detail setting
    public static HashMap<String, String> DV_UPGRADE_FILES_DETAIL = new HashMap<String, String>();
    public static ArrayList<String> DV_UPGRADE_FILES = new ArrayList<String>();

    public static ResourceBundle DV_SQL_CODES = getSqlCodes();

    public static ResourceBundle getSqlCodes() {
        if (DVPropMain.DV_SQLCODE_CHINESE.equals("1")) {
            return ResourceBundle.getBundle("com.dv.prop.DB2_SQL_ERROR_CODE_EN");
        } else {
            return ResourceBundle.getBundle("com.dv.prop.DB2_SQL_ERROR_CODE_EN");
        }
    }

    public static String getEditPropDetail(String propkey) {
        String propvValue;
        try {
            propvValue = editorMain.get(propkey.toUpperCase()).toString();
        } catch (Exception e) {
            editorMain.put(propkey.toUpperCase(), mainUSERResource.getString(propkey.toUpperCase()));
            return mainUSERResource.getString(propkey.toUpperCase());
        }
        return propvValue;
    }

    public static String getDBSupportType(String propkey) {
        String propvValue;
        try {
            editorMain.put(propkey.toUpperCase(), mainUSERResource.getString(propkey.toUpperCase()));
        } catch (Exception e) {
            editorMain.put(propkey.toUpperCase(), mainUSERResource.getString(propkey.toUpperCase()));
            return mainUSERResource.getString(propkey.toUpperCase());
        }
        return mainUSERResource.getString(propkey.toUpperCase());

    }


    public static String getNormalEditPropDetail(String propkey) {
        String propvValue;
        try {
            propvValue = editorMain.get(propkey.toUpperCase()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return propvValue;
    }

    public static String getAutoUpdateServerPropDetail(String propkey) {
        String propvValue;
        try {
            propvValue = editorMain.get(propkey.toUpperCase()).toString();
        } catch (Exception e) {
            return "";
        }
        return propvValue;
    }

    public static void setDvEditPropDetail(String key, String value) {

        editorMain.put(key, value);
    }

    public static String getSQLCodeDes(int propkey) {
        String propvValue;
        try {
            propvValue = DV_SQL_CODES.getString(String.valueOf(propkey).toUpperCase());
        } catch (Exception e) {
            return "";
        }
        return propvValue;
    }

    public static String getABSpath() {
        String url = DVPropMain.class.getProtectionDomain().getCodeSource().getLocation().toString();

//        String url = DVPropMain.class.getResource("/").toString().replaceAll("%20", " ");
//        String url = DVPropMain.class.getResource("/").toString();

        int urlA = url.lastIndexOf("/");
//        Actrual env set        

        return url.substring(6, urlA - 3).replaceAll("%20", " ");

//        netBean Debug env set       
//        return url.substring(6);
    }

    public static String getDataBaseServerType(String key) {
        String[] temp = key.split("@");
        String type;
        try {
            type = DVPropMain.DV_SERVER_INSTANCE.get(temp[0]).getType();
        } catch (Exception e) {
            return null;
        }
        return type;
    }

}
