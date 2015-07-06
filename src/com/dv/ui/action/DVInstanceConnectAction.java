/*
 * DVInstanceConnectAction.java  2/6/13 1:04 PM
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
import com.dv.ui.DVMainEditView;
import com.dv.ui.DVSearchFieldView;
import com.dv.ui.frequent.DVRecentRecordProcess;
import com.dv.util.DVLOG;
import com.dv.util.DataViewerUtilities;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import javax.swing.AbstractAction;

import com.dv.util.DvConnectFactory;

/**
 * @author xyma
 */
public class DVInstanceConnectAction extends AbstractAction {

    DVServerInstance instance;
    Connection conn = null;
    DVFrame parent;
    DVSearchFieldView search;
    String fname = null;
    int type = 0;
    DVMainEditView dvev;

    public DVInstanceConnectAction(DVFrame parentt, DVSearchFieldView searchs, int type) {

        this.parent = parentt;
        this.search = searchs;
        this.type = type;

    }

    public DVInstanceConnectAction(DVFrame parentt, String fname, int type) {

        this.parent = parentt;
        this.fname = fname;
        this.type = type;

    }

    public void actionPerformed(ActionEvent e) {

        if (fname != null) {

            instance = DVPropMain.DV_SERVER_INSTANCE.get(fname.trim());

        } else {
            instance = DVPropMain.DV_SERVER_INSTANCE.get(search.getText().trim());
        }

        if (instance != null) {
            try {
                conn = DvConnectFactory.getOtherConnection(instance.getType(), getURL(), instance.getUser(), instance.getPassword());

                if (conn != null) {

                    setServerMainProp(instance.getInstanceName() + "@" + instance.getHost());

                    dvev = new DVMainEditView(instance.getInstanceName() + "@" + instance.getHost(), instance.getInstanceName(), parent.getMainTabPanel());
                    parent.getMainTabPanel().addTab(instance.getInstanceName(), dvev);
                    parent.getMainTabPanel().setSelectedIndex(parent.getMainTabPanel().getTabCount() - 1);

                    if (type == 1) {

                        if (!getDV_RECENT_INSTANCE_SQLFILEFULLPATH(instance.getInstanceName().trim()).equals("")) {

                            String fileFullPath = DVPropMain.DV_RECENT_INSTANCE_SQLFILEFULLPATH.get(fname);
                            dvev.setFileFullpath(fileFullPath);
                            dvev.setFileName(fileFullPath.substring(fileFullPath.lastIndexOf("\\") + 1, fileFullPath.length()));
                            dvev.loadSQLFileDetail(dvev.getFileFullpath());
                        }
                    }

                    DVRecentRecordProcess.setRecentRecordInfo(instance.getInstanceName().trim());
                    DVPropMain.DV_DVFrequentView.get("MainFrequent").setRecentRecords();

                } else {
                    DataViewerUtilities.displayErrorMessageForConnection();
                }

            } catch (Exception ee) {
                DVLOG.setErrorLog(DVInstanceConnectAction.class.getName(), ee);
                DataViewerUtilities.displayErrorMessageForException(ee);
            } finally {
                DataViewerUtilities.scheduleGC();
            }
        }
    }

    private String getDV_RECENT_INSTANCE_SQLFILEFULLPATH(String fname) {

        String propvValue;
        try {
            propvValue = DVPropMain.DV_RECENT_INSTANCE_SQLFILEFULLPATH.get(fname);
        } catch (Exception e) {
            return "";
        }
        return propvValue;
    }

    public String getURL() {
//        String url = DVPropMain.getDBURL(DVPropMain.DV_USER_DB_TYPE);

        String url = DVPropMain.getDBURL(instance.getType());

        String PORT = "[port]";
        String SID = "[sid]";
        String HOST = "[host]";

        String regex = PORT.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        url = url.replaceAll(regex, instance.getPort().trim());
        regex = HOST.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        url = url.replaceAll(regex, instance.getHost().trim());
        regex = SID.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        url = url.replaceAll(regex, instance.getSid().trim());

        return url;
    }

    public void setServerMainProp(String key) {
        DVPropMain.DB_SERVER_CONNECT.put(key, conn);
        DVPropMain.DB_SERVER_SCHEMA.put(key, instance.getSchama());
        DVPropMain.DB_SERVER_CON_DETAIL.put(key, getURL());
        DVPropMain.DB_SERVER_CON_USER.put(key, instance.getUser());
        DVPropMain.DB_SERVER_CON_PW.put(key, instance.getPassword());
    }
}
