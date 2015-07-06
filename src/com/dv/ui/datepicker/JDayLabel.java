/*
 * JDayLabel.java  7/23/13 12:45 PM
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

package com.dv.ui.datepicker;

import java.text.*;
import java.util.Date;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: JDatePicker</p>
 * <p>Description:JDayLable 带选择日期功能的JLabel </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author <a href="mailto:sunkingxie@hotmail.com"'>Sunking</a>
 * @version 1.0
 */


public class JDayLabel extends JLabel{
    private static ImageIcon todayIcon =
            OpenSwingUtil.getOpenSwingImage("today.gif", new ImageIcon());

    Date date = null;
    ImageIcon currentIcon = null;

    /**
     * 日期格式（TODAY/TIP用）
     */
    final SimpleDateFormat dateFormat
            = new SimpleDateFormat("yyyy/MM/dd");
    /**
     * 日格式
     */
    final SimpleDateFormat dayFormat = new SimpleDateFormat("d");

    public JDayLabel(Date date){
        this(date, true);
    }

    public JDayLabel(Date date, boolean isSmallLabel){
        setPreferredSize(new Dimension(40, 20));
        setToolTipText(dateFormat.format(date));
        this.date = date;
        if(isSmallLabel){
            setHorizontalAlignment(JLabel.CENTER);
            setText(dayFormat.format(date));
            Date d = new Date();
            if(dateFormat.format(date).equals(dateFormat.format(d))){
                currentIcon = todayIcon;
            }
        } else{
            setText("Today:" + dateFormat.format(new Date()));
            setIcon(todayIcon);
            setHorizontalAlignment(JLabel.LEFT);
        }
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public void paint(Graphics g){
        super.paint(g);
        if(currentIcon != null && isEnabled()){
            int x = (this.getWidth() - currentIcon.getIconWidth()) / 2;
            int y = (this.getHeight() - currentIcon.getIconHeight()) / 2;
            currentIcon.paintIcon(this, g, x, y);
        }
    }
}
