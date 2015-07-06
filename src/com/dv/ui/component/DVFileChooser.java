/*
 * DVFileChooser.java  2/6/13 1:04 PM
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
package com.dv.ui.component;



import org.fife.ui.rtextfilechooser.RTextFileChooser;

/**
 * @author xyma
 */
public class DVFileChooser extends RTextFileChooser {

    public DVFileChooser(String startDirectory) {
        super(startDirectory);
        setFileChooser();
        setMultiSelectionEnabled(false);
    }

    public void setFileChooser() {
        FileSelector textFiles = new FileSelector(new String[]{"txt"}, "Text files");
        FileSelector sqlFiles = new FileSelector(new String[]{"sql"}, "SQL files");
        FileSelector xmlFiles = new FileSelector(new String[]{"xml"}, "Xml files");
        FileSelector rtfFiles = new FileSelector(new String[]{"rtf"}, "Rich Format Text files");
        FileSelector excelFiles = new FileSelector(new String[]{"xls"}, "MS excel files");

        setFileFilter(rtfFiles);
        setFileFilter(excelFiles);
        setFileFilter(xmlFiles);
        setFileFilter(textFiles);
        setFileFilter(sqlFiles);

    }
}
