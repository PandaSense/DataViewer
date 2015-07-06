/*
 * DVUpgradeWorker.java  2/20/13 9:35 AM
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

package com.dv.ui.action;

import com.dv.prop.DVPropMain;
import com.dv.ui.component.DataViewerUpgradeDialog;
import com.dv.ui.config.DataViewerOptionView;
import com.dv.upgrade.DVUpgradeUtil;
import com.dv.util.DVLOG;
import com.dv.util.DataViewerUtilities;

import javax.swing.*;

/**
 * Date: 2/20/13
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class DVUpgradeWorker extends SwingWorker<Boolean, Void> {

    boolean isInitOk = true;
    String message = "Please set Auto-Updated server detail information in Option Panel correctly\n" +
            "Do you want to set Auto-Update server ?";

    protected Boolean doInBackground() {
        return checkHasUpdate();
    }

    public boolean checkHasUpdate() {
        isInitOk = DVUpgradeUtil.init();
        if (DVUpgradeUtil.init()) {
            isInitOk = DVUpgradeUtil.needToUpgrade();
            if (isInitOk) {
                return true;
            }
        } else return false;
        return true;
    }

    protected void done() {
        try {
            if (get()) {
                if (isInitOk) {
                    new DataViewerUpgradeDialog(DVPropMain.DV_FRAME.get("MAIN"),0);
                }
            } else {
                int wantToSet = DataViewerUtilities.displayConfirmCancelDialog(message);
                if (wantToSet == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new DataViewerOptionView(DVPropMain.DV_FRAME.get("MAIN"), true);
                        }
                    });
                }
            }
        } catch (Exception e) {
            DVLOG.setErrorLog(DVUpgradeWorker.class.getName(), e);
        }
    }
}
