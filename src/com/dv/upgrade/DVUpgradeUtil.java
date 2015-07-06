/*
 * DVUpgradeUtil.java  2/6/13 1:04 PM
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


package com.dv.upgrade;

import com.dv.prop.DVPropMain;
import com.dv.util.DVLOG;
import com.dv.util.DataViewerDesEncrypter;
import com.dv.util.FileIO;
import java.io.FileOutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * @author Nick
 */
public class DVUpgradeUtil {

    private static String ftpPath = "/DataViewer/upgrade/";
    private static String upgradeVersionFileName = "version";
    private static String upgradeVersionInforFile = "release";
    private static String upgradeVersionJarFile = "DataViewer.jar";
    private static FTPClient ftpClient;

    public static String getUpgradeVersionFileName() {
        return upgradeVersionFileName;
    }

    public static String getUpgradeVersionInforFile() {
        return upgradeVersionInforFile;
    }

    public static String getUpgradeVersionJarFile() {
        return upgradeVersionJarFile;
    }

    public static String getFtpPath() {
        return ftpPath;
    }

    public static void setFtpPath(String ftpPath) {
        DVUpgradeUtil.ftpPath = ftpPath;
    }

    public static FTPClient getFtpClient() {
        return ftpClient;
    }

    public static boolean init() {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(DVPropMain.DV_AUTO_UPDATE_SERVER, 21);
            if (!ftpClient.login(DVPropMain.DV_AUTO_UPDATE_SERVER_USER, DataViewerDesEncrypter.decrypt(DVPropMain.DV_AUTO_UPDATE_SERVER_PASSWORD))) {
                return false;
            }
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.changeWorkingDirectory(getFtpPath());
        } catch (Exception e) {
            DVLOG.setErrorLog(DVUpgradeUtil.class.getName(), e);
            return false;
        }
        return true;
    }

    public static boolean needToUpgrade() {
        FileOutputStream fos = null;
        String distinationFile = null;
        try {
            ftpClient.enterLocalPassiveMode();
            FTPFile[] files = ftpClient.listFiles();
            if (files.length >= 3) {
                distinationFile = getFtpPath() + getUpgradeVersionFileName();
                fos = new FileOutputStream(DVPropMain.DV_USER_HOME_UPGRADE_FOLDER + getUpgradeVersionFileName());
                ftpClient.retrieveFile(distinationFile, fos);
                if (!FileIO.read(DVPropMain.DV_USER_HOME_UPGRADE_FOLDER + getUpgradeVersionFileName()).trim().equals(DVPropMain.DV_VERSION)) {
                    for (int i = 0; i < files.length; i++) {
                        distinationFile = getFtpPath() + files[i].getName();
                        fos = new FileOutputStream(DVPropMain.DV_USER_HOME_UPGRADE_FOLDER + files[i].getName());
                        ftpClient.retrieveFile(distinationFile, fos);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            DVLOG.setErrorLog(DVUpgradeUtil.class.getName(), e);
            return false;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
                fos.close();
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }
}
