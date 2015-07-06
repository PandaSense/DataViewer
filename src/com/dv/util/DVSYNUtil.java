/*
 * DVSYNUtil.java  2/6/13 1:04 PM
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


import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


public class DVSYNUtil {

    public static ArrayList getSQLSYNKeyWords(String filePath) {
        ArrayList keyWords = new ArrayList();
        File syn = new File(filePath);
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(syn));

            String readByte;

            while ((readByte = input.readLine()) != null) {
                keyWords.add(readByte.trim());
            }

            input.close();

        } catch (FileNotFoundException ioe) {
            DVLOG.setErrorLog(DVSYNUtil.class.getName(), ioe);

        } catch (IOException ioe) {
            DVLOG.setErrorLog(DVSYNUtil.class.getName(), ioe);
        }

        return keyWords;
    }


    public static String[] getSQLSYNKeyWordsString(ArrayList keyWords) {
        String[] _keys = new String[keyWords.size()];
        for (int i = 0; i < keyWords.size(); i++) {
            _keys[i] = keyWords.get(i).toString().trim().toUpperCase();
        }
        return _keys;

    }

    public static String getSQLStandardProcedureWords(ArrayList keyWords) {

        String key = new String();

        for (int i = 0; i < keyWords.size(); i++) {
            key = key + keyWords.get(i).toString().toLowerCase().trim() + "|";
        }

        for (int i = 0; i < keyWords.size(); i++) {
            if (i < keyWords.size() - 1) {
                key = key + keyWords.get(i).toString().toUpperCase().trim() + "|";
            } else {
                key = key + keyWords.get(i).toString().toUpperCase().trim();
            }
        }

        return key;
    }

    public static String getAllTablesAndViewsNamesWords(Vector keyWords) {
        String words = new String();


        for (int i = keyWords.size(); i > 0; i--) {

            words = words + keyWords.get(i - 1).toString().toUpperCase().trim() + "|";

        }

        for (int i = keyWords.size(); i > 0; i--) {
            if (i > 1) {
                words = words + keyWords.get(i - 1).toString().toLowerCase().trim() + "|";
            } else {
                words = words + keyWords.get(i - 1).toString().toLowerCase().trim();
            }
        }

        return words;

    }


}
