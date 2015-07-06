/*
 * Project_Date.java  2/6/13 1:04 PM
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class Project_Date {
    /**
     * wait for special time
     *
     * @param second
     */
    public static void sleep_time(long second) {
        Date begin = new Date();
        while (true) {
            Date end = new Date();
            if ((end.getTime() - begin.getTime()) > second * 1000) {
                break;
            }
        }
    }

    /**
     * converts date with input format to DDMMYYYY to YYYY-MM-DD
     *
     * @param date - date in DDMMYYYY format
     * @return - date in YYYY-MM-DD format
     */
    public static String DDMMYYYY_to_YYYY_MM_DD(String date) {
        return date.substring(4, 8) + "-" + date.substring(2, 4) + "-" + date.substring(0, 2);
    }

    /**
     * converts date with input format of either YYYY/MM/DD or YYYY-MM-DD to DDMMYYYY
     *
     * @param date
     * @return converted date
     */
    public static String date_to_DDMMYYYY(String date) {
        return date.substring(8, 10) + date.substring(5, 7) + date.substring(0, 4);
    }

    /**
     * returns the number of days in string for the given month
     *
     * @param year  the year to use
     * @param month "01" - "12" representing the month
     * @return the number of days in String
     */
    public static String days_per_month(String year, String month) {
        // If we want to handle leap year, replace 2006 by a variable that gives the year we want to work with
        GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);

        // Get the number of days in that month
        return Integer.toString(cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
    }

    /**
     * returns the date and time format for log entry
     *
     * @param format date and time format string
     * @return formatted date and time
     * @see SimpleDateFormat
     */
    public static String get_date_time(String format) {
        return get_date_time(format, "local");
    }

    /**
     * returns the date and time format for log entry
     *
     * @param format date and time format string
     * @param zone   timezone or "local" if using default timezone
     * @return formatted date and time
     * @see SimpleDateFormat
     */
    public static String get_date_time(String format, String zone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (zone.compareTo("local") == 0)
            sdf.setTimeZone(TimeZone.getDefault());
        else
            sdf.setTimeZone(TimeZone.getTimeZone(zone));
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * Takes a date in YYYY-MM-DD for and update the year to the specified year.
     * This is typically used to change the default start and end dates to the current year.
     *
     * @param date - eg "2006-01-01"
     * @param year - eg "2007", must be 4 chars in length
     * @return - date with year changed, eg "2007-01-01"
     */
    public static String update_year(String date, String year) {
        return year + date.substring(4, 10);
    }

}
