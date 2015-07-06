/*
 * DateFormatFactory.java  2/6/13 1:04 PM
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormatFactory {

    private static SimpleDateFormat dateformatDetail = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentDateString() {
        Date current = new Date();
        String dateString = dateformatDetail.format(current);
        return dateString;
    }
    
    public static String getCurrentDateStringFlora() {
        Date current = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(current);
        return dateString;
    }
    
    
    public static String getCurrentDateTimeString() {
        Date current = new Date();
        String dateString = dateformat.format(current);
        return dateString;
    }
    public static String getStaticString() {
        Date current = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(current);
        return dateString;
    }

    public static String getTimeString() {
        Date current = new Date();
        String dateString = new SimpleDateFormat("HH:mm:ss").format(current);
        return dateString;
    }

    public static void main(String args[]){
//        Date date = new Date(1399865496);
//        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
//        String formatted = format.format(date);
//        System.out.println(formatted);
//        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
//        formatted = format.format(date);
        System.out.println(DateFormatFactory.getCurrentDateStringFlora());


    }


}
