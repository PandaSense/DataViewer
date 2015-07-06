/*
 * DVComparator.java  2/6/13 1:04 PM
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dv.ui.frequent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @author xyma
 */
public class DVComparator implements Comparator {

    HashMap source;

    public DVComparator(HashMap source) {

        this.source = source;
    }

    public int compare(Object o1, Object o2) {

        if (Integer.valueOf(source.get(o1).toString()) < Integer.valueOf(source.get(o2).toString())) {
            return 1;
        } else if (Integer.valueOf(source.get(o1).toString()) == Integer.valueOf(source.get(o2).toString())) {

            return 0;
        } else {
            return -1;
        }
    }
}
