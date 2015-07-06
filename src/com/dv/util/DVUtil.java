/*
 * DVUtil.java  2/6/13 1:04 PM
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

import java.util.Vector;
import java.util.Arrays;

public class DVUtil {

    @SuppressWarnings("unchecked")
    public static String[] vectorSortIntoString(Vector source) {
        String[] target = new String[source.size()];

        for (int i = 0; i < source.size(); i++) {
            target[i] = source.get(i).toString();
        }
        Arrays.sort(target);

        return target;
    }

    @SuppressWarnings("unchecked")
    public static Vector vectorSortIntoVector(Vector source) {
        String[] target = DVUtil.vectorSortIntoString(source);
        Vector<String> out = new Vector<String>();

        for (int i = 0; i < target.length; i++) {
            out.add(target[i]);
        }

        return out;
    }

    public static String getAllTandVWords(Vector source) {
        String words = new String();
        for (int i = 0; i < source.size(); i++) {
            if (i < source.size() - 1) {
                words = words + source.get(i).toString().trim() + "|";

            } else {
                words = words + source.get(i).toString().trim();

            }

        }

        return words;
    }

    public String[] sortStringArray(String[] isort) {

//		Arrays.sort(isort,new ComparatorArrayList());

        return isort;
    }

    public class ComparatorArrayList {

        public int compare(Object o1, Object o2) {
            String a = (String) o1;
            String b = (String) o2;
            int c = a.length();
            int d = b.length();
            if (c < d) {
                return 1;
            } else if (c > d) {
                return -1;
            } else {
                return a.compareTo(b);
            }
        }
    }
}
