/*
 * DVImportFromAquaAction.java  2/6/13 1:04 PM
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
package com.dv.ui.action;

import com.dv.dbinstance.DVServerInstance;
import com.dv.prop.DVPropMain;
import com.dv.ui.DVFrame;
import com.dv.util.DataViewerUtilities;
import com.dv.util.DvRegServerPools;
import com.dv.xml.DVDatabaseXmlSet;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.JDialog;

/**
 * @author xyma
 */
public class DVImportFromAquaAction extends AbstractAction {

    DVFrame dvframe;
    String aquaPath = System.getProperty("user.home") + "/.datastudio/connections/";
    File aquaFolder;
    JDialog messagePartent;

    public DVImportFromAquaAction(DVFrame dvframe, JDialog messagePartent) {
        this.dvframe = dvframe;
        this.messagePartent = messagePartent;
    }

    public void actionPerformed(ActionEvent e) {
        aquaFolder = new File(aquaPath);
        if (aquaFolder.exists()) {
            importAquaConfig(aquaFolder);
        } else {
            DataViewerUtilities.displayWarningMessage(messagePartent, "No any Aqua Data Studio database config was found.");
        }
    }

    public void importAquaConfig(File aquaFolder) {
        File[] aquaCons = aquaFolder.listFiles();
        if (aquaCons.length != 0) {
            Properties props;
            String[] fullDBDetail = new String[8];
            DVDatabaseXmlSet xml = new DVDatabaseXmlSet(DVPropMain.DV_CONFIG_FOLDER + "dvdatabase.xml");
            for (int i = 0; i < aquaCons.length; i++) {
                props = DvRegServerPools.getDvProperties(aquaCons[i].getAbsolutePath());
                if (props != null) {
                    fullDBDetail[0] = props.getProperty("connection.host");
                    fullDBDetail[1] = props.getProperty("connection.sid");
                    fullDBDetail[2] = props.getProperty("connection.username");
                    fullDBDetail[3] = props.getProperty("connection.username");
                    fullDBDetail[4] = props.getProperty(" ");
                    fullDBDetail[5] = aquaCons[i].getName() + ".aqua";
                    fullDBDetail[6] = props.getProperty("connection.port");
                    fullDBDetail[7] = "DB2";
                    if (hasAquaDb(fullDBDetail[5].trim())) {
                        xml.updateConnection(fullDBDetail[5].trim(), fullDBDetail, "DB2");
                    } else {
                        xml.addConnection(fullDBDetail, "DB2");
                    }
                    DVPropMain.DV_SERVER_INSTANCE.put(fullDBDetail[5].trim(), new DVServerInstance(fullDBDetail));
                }
            }
            DataViewerUtilities.displayWarningMessage(messagePartent, "Import Aqua Data Studio database config completely.");
        } else {
            DataViewerUtilities.displayWarningMessage(messagePartent, "No any Aqua Data Studio database config was found.");
        }
    }

    public boolean hasAquaDb(String key) {
        try {
            DVPropMain.DV_SERVER_INSTANCE.get(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
