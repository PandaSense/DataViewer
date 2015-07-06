/*
 * DVSystemTray.java  2/6/13 1:04 PM
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

import com.dv.prop.DVPropMain;
import com.dv.ui.DVFrame;
import com.dv.util.DVLOG;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import javax.swing.JFrame;

/**
 * @author xyma
 */
public class DVSystemTray implements ActionListener, MouseListener {
    private SystemTray tray;
    private TrayIcon trayicon;
    private static final String MSG = "com.dv.ui.DataViewerToolBar";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);
    private PopupMenu popupMenu;
    private MenuItem displayDV, exitDV;
    private DVFrame parent;

    public DVSystemTray(DVFrame parent) {

        tray = SystemTray.getSystemTray();
        this.parent = parent;
        Image icon = Toolkit.getDefaultToolkit().getImage(DVSystemTray.class.getResource(mainViewResource.getString("tray")));

        trayicon = new TrayIcon(icon);
        setToolTipInformation(null);
        buildSystemTrayPopUpMenu();

        trayicon.setPopupMenu(popupMenu);
        trayicon.addMouseListener(this);
        setTrayIcon();
    }

    public TrayIcon getTrayicon() {
        return trayicon;
    }

    public void setToolTipInformation(String tips){
        if(tips==null){
            trayicon.setToolTip(DVPropMain.DV_NAME + " - " + DVPropMain.DV_VERSION);
        }else{
            trayicon.setToolTip(DVPropMain.DV_NAME + " - " + DVPropMain.DV_VERSION+"\n"+tips);
        }
    }

    public void setTrayicon(TrayIcon trayicon) {
        this.trayicon = trayicon;
    }

    private void buildSystemTrayPopUpMenu() {
        popupMenu = new PopupMenu();
        displayDV = new MenuItem("Open DataViewer");
        exitDV = new MenuItem("Exit");
        popupMenu.add(displayDV);
        popupMenu.addSeparator();
        popupMenu.add(exitDV);
        displayDV.addActionListener(this);
        exitDV.addActionListener(this);
    }

    public void setTrayIcon() {
        try {
            tray.add(trayicon);
        } catch (AWTException e) {
            DVLOG.setErrorLog(DVSystemTray.class.getName(), e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitDV) {
            setDataViewerNormal();
            parent.quit();
        }
        if (e.getSource() == displayDV) {
            setDataViewerNormal();
        }
    }

    public void setDataViewerNormal() {
        if (parent.getExtendedState() != JFrame.NORMAL) {
            parent.setVisible(true);
            parent.setExtendedState(JFrame.NORMAL);
        }
    }

    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 2) {
            setDataViewerNormal();
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
