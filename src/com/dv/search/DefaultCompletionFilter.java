/*
 * DefaultCompletionFilter.java  2/6/13 1:04 PM
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
 * DefaultCompletionFilter.java
 *
 * Created on 2007-6-21, 23:18:58
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dv.search;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author William Chen
 */
public class DefaultCompletionFilter implements CompletionFilter {

    private Vector vector;

    public DefaultCompletionFilter() {
        vector = new Vector();
    }

    public DefaultCompletionFilter(Vector v) {
        vector = v;
    }

    public ArrayList filter(String text) {
        ArrayList list = new ArrayList();
        String txt = text.trim().toUpperCase();
        int length = txt.length();
        for (int i = 0; i < vector.size(); i++) {
            Object o = vector.get(i);
            String str = o.toString().toUpperCase();
            if (length == 0 || str.startsWith(txt))
                list.add(o);
        }
        return list;
    }
}
