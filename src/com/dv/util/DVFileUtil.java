/*
 * DVFileUtil.java  2/9/13 1:39 PM
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

import com.dv.prop.DVPropMain;
import com.dv.ui.DataViewer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

public class DVFileUtil {

    public static boolean isSave = false;
    public static StringBuffer blankBuffer, sepBuffer;
    private static String filePrefix = "SQL_HISTORY_";
    private static String fileSuffix = ".dvh";
    private static String currentDateFileName;
    private static String new_ln;

    public static boolean saveSqlFile(String fullPath, String contents) {
        boolean isSave = false;
        File sqlFile = new File(fullPath);

        String con = contents;

        if (con == null || con.equals("")) {
            isSave = false;

        } else {

            try {
                if (!sqlFile.exists()) {
                    sqlFile.createNewFile();
                    FileWriter outFile = new FileWriter(sqlFile);

                    BufferedWriter bufferOut = new BufferedWriter(outFile);

                    bufferOut.write(con);
                    bufferOut.flush();
                    bufferOut.close();

                    isSave = true;

                } else {
                    FileWriter outFile = new FileWriter(sqlFile, false);
                    BufferedWriter bufferOut = new BufferedWriter(outFile);
                    bufferOut.write(con);
                    bufferOut.flush();
                    bufferOut.close();

                    isSave = true;
                }

            } catch (IOException ioe) {
                isSave = false;
                DVLOG.setErrorLog(DVFileUtil.class.getName(), ioe);
            }

        }
        return isSave;
    }

    public static boolean saveSqlHistoryFile(String contents) {

        boolean isSave = false;

        currentDateFileName = DVPropMain.DV_HISTORY_RESULT_FOLDER + filePrefix + DateFormatFactory.getCurrentDateString() + fileSuffix;
        File sqlFile = new File(currentDateFileName);

        String con = contents;

        if (con == null || con.equals("")) {
            isSave = false;
        } else {
            try {
                if (!sqlFile.exists()) {

                    sqlFile.createNewFile();
                    FileWriter outFile = new FileWriter(sqlFile);
                    BufferedWriter bufferOut = new BufferedWriter(outFile);
                    bufferOut.write(con);
                    bufferOut.flush();
                    bufferOut.close();

                    isSave = true;
                } else {
                    FileWriter outFile = new FileWriter(sqlFile, true);
                    BufferedWriter bufferOut = new BufferedWriter(outFile);

                    bufferOut.write(con);
                    bufferOut.flush();
                    bufferOut.close();
                    isSave = true;
                }
            } catch (IOException ioe) {
                isSave = false;
                DVLOG.setErrorLog(DVFileUtil.class.getName(), ioe);
            }
        }
        return isSave;
    }

    public static String formatSQLResultToWord(Vector column, Vector row,String lastExecutionDate) {

        StringBuffer s = new StringBuffer("");

        new_ln = System.getProperty("line.separator");

        int length = Integer.parseInt(DVPropMain.DV_FORMAT_WORD_WIDTH);
        int size = 0;

        int numCol = column.size();

        int[] width = new int[numCol];
        String[] columnName = new String[numCol];
        String[] value;
        ArrayList<String[]> valueList = new ArrayList<String[]>();

        for (int i = 0; i < numCol; i++) {
            columnName[i] = column.get(i).toString();
        }
        for (int i = 0; i < row.size(); i++) {
            value = new String[numCol];
            for (int j = 0; j < numCol; j++) {
                String v = null;
                try {
                    v = ((Vector) row.get(i)).get(j).toString();
                } catch (Exception e) {
                    v = null;
                }
                if (v == null) {
                    v = " ";
                }
                value[j] = v;
            }
            valueList.add(value);
        }

        for (int i = 0; i < numCol; i++) {
            int columnWidth = columnName[i].length();
            int valueWidth = 0;
            for (int ii = 0; ii < valueList.size(); ii++) {
                valueWidth = Math.max(valueList.get(ii)[i].length(), valueWidth);
            }
            width[i] = Math.max(columnWidth, valueWidth);
        }

        int status = -1;
        int before = 0;

        while (true) {
            for (int i = before; i < numCol; i++) {
                status = i;
                String cName = columnName[i];
                cName = appendBlank(width[i], cName) + " ";
                s = s.append(cName);
                size = size + width[i] + 1;
                if (size > length) {
                    size = 0;
                    break;
                }
            }
            s = s.append(new_ln);
            for (int i = before; i < (status + 1); i++) {
                String separator = "";
                separator = appendSeparator(width[i], separator) + " ";
                s = s.append(separator);
            }
            s = s.append(new_ln);
            for (int ii = 0; ii < valueList.size(); ii++) {
                for (int i = before; i < (status + 1); i++) {
                    String v = valueList.get(ii)[i];
                    v = appendBlank(width[i], v) + " ";
                    s = s.append(v);
                }
                s = s.append(new_ln);
            }
            s = s.append(new_ln);
            before = status + 1;
            if (status == (numCol - 1)) {
                break;
            }
        }

        if(DVPropMain.DV_CTC_WITH_DATE.equals("1")){
            s = s.append(lastExecutionDate);
        }

        return s.toString();
    }

    public static String formatBatchSQLResultToWord(HashMap<String, Vector> cols, HashMap<String, Vector> rows,HashMap<String, String> batchLastInformations) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < cols.size(); i++) {

            s.append("\n" + formatSQLResultToWord(cols.get(String.valueOf(i)), rows.get(String.valueOf(i)),batchLastInformations.get(String.valueOf(i))) + "\n");
        }
        return s.toString();
    }

    public static String formatSQLResultToShow(Vector column, Vector row) {

        StringBuffer s = new StringBuffer("");
        new_ln = System.getProperty("line.separator");

        int numCol = column.size();

        int[] width = new int[numCol];
        String[] columnName = new String[numCol];
        String[] value;
        ArrayList<String[]> valueList = new ArrayList<String[]>();

        for (int i = 0; i < numCol; i++) {
            columnName[i] = column.get(i).toString();
        }

        for (int i = 0; i < row.size(); i++) {
            value = new String[numCol];
            for (int j = 0; j < numCol; j++) {
                String v = null;
                try {
                    v = ((Vector) row.get(i)).get(j).toString();
                } catch (Exception e) {
                    v = null;
                }
                if (v == null) {
                    v = " ";
                }
                value[j] = v;
            }
            valueList.add(value);
        }

        for (int i = 0; i < numCol; i++) {
            int columnWidth = columnName[i].length();
            int valueWidth = 0;
            for (int ii = 0; ii < valueList.size(); ii++) {
                valueWidth = Math.max(valueList.get(ii)[i].length(), valueWidth);
            }
            width[i] = Math.max(columnWidth, valueWidth);
        }

        int status = -1;
        int before = 0;
        while (true) {
            for (int i = before; i < numCol; i++) {
                status = i;
                String cName = columnName[i];
                cName = appendBlank(width[i], cName) + " ";

                s = s.append(cName);
            }
            s = s.append(new_ln);
            for (int i = before; i < (status + 1); i++) {
                String separator = "";

                separator = appendSeparator(width[i], separator) + " ";
                s = s.append(separator);
            }
            s = s.append(new_ln);
            for (int ii = 0; ii < valueList.size(); ii++) {
                for (int i = before; i < (status + 1); i++) {
                    String v = valueList.get(ii)[i];

                    v = appendBlank(width[i], v) + " ";
                    s = s.append(v);
                }
                s = s.append(new_ln);
            }
            s = s.append(new_ln);
            before = status + 1;
            if (status == (numCol - 1)) {
                break;
            }
        }
        return s.toString();
    }

    public static String appendBlank(int width, String s) {

        blankBuffer = new StringBuffer(s);
        if (s.length() < width) {
            for (int i = 0; i < (width - s.length()); i++) {
                blankBuffer.append(" ");
            }
        }
        return blankBuffer.toString();
    }

    public static String appendSeparator(int width, String s) {

        sepBuffer = new StringBuffer(s);

        for (int i = 0; i < width; i++) {
            sepBuffer.append("-");
        }
        return sepBuffer.toString();
    }

    public static Properties getPropertiesFile(String filePath) {
        Properties props = new Properties();

        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            props.load(in);
            in.close();
        } catch (IOException ioe) {
            createPropFile(filePath);
            return props;
        }
        return props;
    }

    public static void createPropFile(String filePath) {
        File propFile = new File(filePath);
        try {
            propFile.createNewFile();
        } catch (Exception e) {
        }
    }

    public static void savePropertiesFile(Properties props, String filePath) {
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
            props.store(out, DVPropMain.DV_NAME + " " + DVPropMain.DV_VERSION + " PropertiesFile setting");
            out.close();
        } catch (IOException ioe) {
            DVLOG.setErrorLog(DVFileUtil.class.getName(), ioe);
        }
    }

    public static String loadTextFile(String fileFullPath) {

        StringBuffer text = new StringBuffer();
        BufferedReader input;
        try {
            File f = new File(fileFullPath);
            input = new BufferedReader(new FileReader(f));

            while (input.readLine()!= null) {
                text.append(input.readLine() + "\n");
            }
            input.close();

        } catch (Exception ioe) {
            return ioe.getMessage();
        }
        return text.toString();

    }
}
