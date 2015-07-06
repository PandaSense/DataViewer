/*
 * DVFrame.java  2/18/13 3:58 PM
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

package com.dv.ui;

import com.dv.ui.action.DVUpgradeWorker;
import com.dv.ui.component.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.dv.about.DVAboutDialog;
import com.dv.prop.DVPropMain;

import com.dv.search.DefaultCompletionFilter;
import com.dv.ui.component.SplashScreen;
import com.dv.ui.config.DataViewerOptionView;
import com.dv.ui.frequent.DataViewerNewFrequentView;
import com.dv.util.DVFileUtil;
import com.dv.util.DataViewerUtilities;

import com.dv.ui.heap.HeapMemoryDialog;

import com.dv.util.DVLOG;
import com.dv.xml.DVDatabaseXmlSet;
import com.dv.xml.DVResentRecordXMLSet;

import java.io.File;
import java.sql.Connection;
import java.util.*;

import javax.swing.*;

/**
 * @author Java
 */
public class DVFrame extends JFrame implements ActionListener {

    static {
        Locale.setDefault(Locale.getDefault());
        DVLOG log = new DVLOG(false);
        DataViewerUtilities.launchPluginLookAndFeel(DVPropMain.DV_LANDK_JAR_NAME, DVPropMain.LOOK_AND_FEEL_NAME);
    }

    private static final String MSG = "com.dv.ui.DataViewerToolBar";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);
    DVSearchFieldView dvfv;
    Vector completionFilterV;
    DVSystemTray dataviewertray;
    DVDatabaseXmlSet serverXml;
    DVResentRecordXMLSet recentXml;

    DataViewerNewFrequentView newFview;

    DVSystemTray systemTray;

    public DVSystemTray getSystemTray() {
        return systemTray;
    }

    public void setSystemTray(DVSystemTray systemTray) {
        this.systemTray = systemTray;
    }

    public void setLog() {
        DVLOG log = new DVLOG();
    }

    boolean hasTray = false;

    public boolean getHasTray() {
        return hasTray;
    }

    public void setHasTray(boolean hasTray) {
        this.hasTray = hasTray;
    }

    public boolean supportTray() {

        if (SystemTray.isSupported()) {
            return true;
        }
        return false;
    }

    public Vector getCompletionFilterV() {
        return completionFilterV;
    }

    public void setCompletionFilterV(Vector completionFilterV) {
        this.completionFilterV = completionFilterV;
    }

    public static ResourceBundle getMainViewResource() {
        return mainViewResource;
    }

    /**
     * Creates new form DVFrame
     */
    public DVFrame() {

        SplashScreen label = new SplashScreen(mainViewResource.getString("loadIcon"), "Loading");
        label.setVisible(true);
        label.setAlwaysOnTop(true);
        label.updateStatus(mainViewResource.getString("loadingTipOne"), 20);

        changePropToXml();
        setRecentServerXml();
        delayProgressBar(500);
        DVPropMain.DV_FRAME.put("MAIN", this);

        label.updateStatus(mainViewResource.getString("loadingTipTwo"), 40);
        initComponents();
        delayProgressBar(700);

        label.updateStatus(mainViewResource.getString("loadingTipThree"), 70);

        setTitle(DVPropMain.DV_NAME + "-" + DVPropMain.DV_VERSION);

        Image icon = Toolkit.getDefaultToolkit().getImage(DVFrame.class.getResource(mainViewResource.getString("version")));

        setIconImage(icon);

        delayProgressBar(700);

        label.updateStatus(mainViewResource.getString("loadingTipFour"), 95);
        delayProgressBar(700);
        label.updateStatus(mainViewResource.getString("loadingTipFive"), 100);
        delayProgressBar(1500);
        label.dispose();
        buildSystemTray();
        setVisible(true);
        checkUpdate();
        DataViewerUtilities.scheduleGC();
    }

    public void checkUpdate() {
        if (DVPropMain.DV_AUTO_UPDATE.equals("1")) {
            new DVUpgradeWorker().execute();
        }
    }

    public void buildSystemTray() {
        if (this.supportTray()) {
            if (DVPropMain.DV_SYSTEM_TRAY.equals("1")) {
                systemTray = new DVSystemTray(this);
                this.setHasTray(true);
            }
        }
    }

    public void delayProgressBar(int mitime) {
        try {
            Thread.sleep(mitime);
        } catch (Exception eee) {
        }
    }

    public void changePropToXml() {
        String filePath = DVPropMain.DV_CONFIG_FOLDER + "dvdatabase.xml";
        File xmlFile = new File(filePath);
        if (!xmlFile.exists()) {
            serverXml = new DVDatabaseXmlSet();
            serverXml.setPath(filePath);
            serverXml.buildNewXml();
            serverXml.buildXmlFile();
        }
        serverXml = new DVDatabaseXmlSet(filePath);
        serverXml.setDBInstance();

    }

    public void setRecentServerXml() {
        String filePath = DVPropMain.DV_CONFIG_FOLDER + "dvrecentserver.xml";
        File xmlFile = new File(filePath);
        if (xmlFile.exists()) {
            recentXml = new DVResentRecordXMLSet(filePath);
            recentXml.setRecentRecords();
        } else {
            recentXml = new DVResentRecordXMLSet();
        }
    }

    private void initComponents() {

        completionFilterV = serverXml.getDVRegServerNameList();

        dvMainPanel = new JPanel();

        dvSQLToolBar = new JToolBar();

        dvSQLToolBar.setMargin(new Insets(4, 10, 4, 10));

        dvSQLToolBar.setBorder(new BottomLineBorder(2));

        config = new RolloverButton();

        config.setText(mainViewResource.getString("optionText"));
        config.setToolTipText(mainViewResource.getString("optionToolTip"));

        Server = new RolloverButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("Form"); // NOI18N

        dvMainPanel.setName("dvMainPanel"); // NOI18N
        dvMainPanel.setLayout(new java.awt.BorderLayout());

        mainTabPanel = new DVTabPaneView();
        mainTabPanel.setOpaque(false);
        mainTabPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);


        newFview = new DataViewerNewFrequentView((DVFrame) DVPropMain.DV_FRAME.get("MAIN"));


        mainTabPanel.addTab(mainViewResource.getString("DV_Frequent_Tab_TEXT"), newFview);

//        mainTabPanel.setTabLeadingComponent(buildTopLeadingPopupButton());


        DVPropMain.DV_DVFrequentView.put("MainFrequent", newFview);


        setDisplays();
        dvMainPanel.add(mainTabPanel, java.awt.BorderLayout.CENTER);

        dvSQLToolBar.setFloatable(false);
        dvSQLToolBar.setRollover(true);
        dvSQLToolBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dvSQLToolBar.setName("DataViewerBar"); // NOI18N

        Server.setText(mainViewResource.getString("serverText"));
        Server.setToolTipText(mainViewResource.getString("serverToolTip"));
        Server.setIcon(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("serverIcon"))));
        Server.setFocusable(false);
        Server.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Server.addActionListener(this);

        dvSQLToolBar.add(Server);

        config.addActionListener(this);
        config.setFocusable(false);
        config.setIcon(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("configIcon"))));

        dvSQLToolBar.add(config);


        version = new javax.swing.JMenuItem();

        version.setIcon(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("help"))));
        version.setText(mainViewResource.getString("Version_Button_Text"));
        version.setFocusable(false);
        version.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        version.addActionListener(this);

        heapMemory = new javax.swing.JMenuItem();

        heapMemory.setText(mainViewResource.getString("Heap_Memory_Collect_Text"));
        heapMemory.setFocusable(false);
        heapMemory.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        heapMemory.addActionListener(this);

        history = new RolloverButton(mainViewResource.getString("History_Button_Text"));

        history.setIcon(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("history"))));
        history.setFocusable(false);
        history.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        history.addActionListener(this);

        setACV(completionFilterV);


//        plugMenuButton = new DVPopupMenuButton(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("pluginIcon"))), "DataViewer plugin application");
//
//        plugMenuButton.setText("Plug App");
//
//        plugMenuButton.setFocusable(false);
//
//        dvSQLToolBar.add(plugMenuButton);

        dvSQLToolBar.add(history);

        releaseContext = new javax.swing.JMenuItem();
        releaseContext.setText(mainViewResource.getString("what_is_new"));
        releaseContext.setFocusable(false);
        releaseContext.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        releaseContext.addActionListener(this);

        menuButton = new DVPopupMenuButton(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("helpIcon"))), "Help and About");

        menuButton.addMenuItem(heapMemory);
        menuButton.addMenuItem(releaseContext);
        menuButton.addMenuItem(version);

        menuButton.setFocusable(false);

        dvSQLToolBar.add(menuButton);
        dvSQLToolBar.setOpaque(true);

        dvMainPanel.add(dvSQLToolBar, java.awt.BorderLayout.NORTH);

        getContentPane().add(dvMainPanel, java.awt.BorderLayout.CENTER);

        setLocationDetail();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                realAuit();
            }
        });
    }


    private DVPopupMenuButton topLeadPopupButton;

    private JMenuItem serverItem, configItem, historyItem, heapMemoryItem, releaseContextItem, versionItem;


    public DVPopupMenuButton buildTopLeadingPopupButton() {

        topLeadPopupButton = new DVPopupMenuButton(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("topLeadIcon"))), "Set your DataViewer");

        topLeadPopupButton.setText("DataViewer");
        topLeadPopupButton.setOpaque(false);
        topLeadPopupButton.setForeground(Color.BLUE);

        serverItem=new JMenuItem(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("serverIcon"))));
        serverItem.setText(mainViewResource.getString("serverText"));
        serverItem.setToolTipText(mainViewResource.getString("serverToolTip"));

        serverItem.addActionListener(this);

        configItem=new JMenuItem(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("configIcon"))));
        configItem.setText(mainViewResource.getString("optionText"));
        configItem.setToolTipText(mainViewResource.getString("optionToolTip"));

        configItem.addActionListener(this);


        historyItem=new JMenuItem(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("history"))));
        historyItem.setText(mainViewResource.getString("History_Button_Text"));

        historyItem.addActionListener(this);

        heapMemoryItem=new JMenuItem();
        heapMemoryItem.setText(mainViewResource.getString("Heap_Memory_Collect_Text"));

        heapMemoryItem.addActionListener(this);


        releaseContextItem=new JMenuItem();
        releaseContextItem.setText(mainViewResource.getString("what_is_new"));

        releaseContextItem.addActionListener(this);

        versionItem=new JMenuItem(new ImageIcon(DVFrame.class.getResource(mainViewResource.getString("help"))));
        versionItem.setText(mainViewResource.getString("Version_Button_Text"));

        versionItem.addActionListener(this);

        topLeadPopupButton.addMenuItem(serverItem);
        topLeadPopupButton.addMenuItem(configItem);
        topLeadPopupButton.addMenuItem(historyItem);
        topLeadPopupButton.addMenuItem(heapMemoryItem);
        topLeadPopupButton.addMenuItem(releaseContextItem);
        topLeadPopupButton.addMenuItem(versionItem);

        return topLeadPopupButton;


    }


    public void setDisplays() {
        if (DVPropMain.DV_RECENT_INSTANCE_NAME.size() > 0) {
            newFview.setRecentRecords();
        }
    }

    public void setACV(Vector completionFilterV) {
        completionFilterV.removeElement("Create new DV");

        newFview.getViewSearch().setFilter(new DefaultCompletionFilter(completionFilterV));
    }

    public DVTabPaneView getMainTabPanel() {
        return mainTabPanel;
    }

    public void setMainTabPanel(DVTabPaneView mainTabPanel) {
        this.mainTabPanel = mainTabPanel;
    }

    public void realAuit() {
        if (DVPropMain.DV_SYSTEM_TRAY.equals("1")) {
            this.setExtendedState(JFrame.ICONIFIED);
            this.setVisible(false);
        } else {
            quit();
        }
    }

    public void quit() {
        int isQuit = JOptionPane.showConfirmDialog(this, DVMainEditView.getMainViewResource().getString("dataviewer_close_message"), "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (isQuit == JOptionPane.OK_OPTION) {
            if (mainTabPanel.isDirty()) {
                int result = JOptionPane.showConfirmDialog(this, DVMainEditView.getMainViewResource().getString("dataviewer_quit_message"), "File Changed", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.NO_OPTION) {
                    toQuit();
                } else {
                    mainTabPanel.removeAllTabExceptFrequent();
                    toQuit();
                }
            } else {
                toQuit();
            }
        }
    }

    public void toQuit() {
        saveAndClose();
        System.exit(0);
    }

    public void saveAndClose() {

        if (DVPropMain.DV_RECENT_INSTANCE_COUNT.size() != 0) {
            saveRecentRecords();
        }
        DVPropMain.editorMain.put("DV_FILE_RECENT_PATH", DVPropMain.DV_FILE_RECENT_PATH);
        DVPropMain.editorMain.put("LOOK_AND_FEEL_NAME", DVPropMain.LOOK_AND_FEEL_NAME);
        saveLocationDetail();
        DVFileUtil.savePropertiesFile(DVPropMain.editorMain, DVPropMain.DV_CONFIG_FOLDER + "DV_EDITOR.properties");
        mainTabPanel.removeAllConnects();
        closeAllConnections();
    }

    public void saveRecentRecords() {
        if (DVPropMain.DV_RECENT_INSTANCE_NAME.size() > 0) {
            recentXml.buildNewRecentXML();
        }
    }

    public void setLocationDetail() {
        if (DVPropMain.DV_LOCATION.equals("")) {
            setDefaultLocation();
        } else {
            try {
                String[] loc = DVPropMain.DV_LOCATION.split(",");
                setSize(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]));
                setLocation(Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
            } catch (Exception ee) {
                setDefaultLocation();
            }
        }
    }

    public void setDefaultLocation() {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) scrSize.getHeight();
        int width = (int) scrSize.getWidth();
        setSize(950, 700);
        setLocation((width / 2 - getWidth() / 2), height / 2 - getHeight() / 2);
    }

    public void saveLocationDetail() {
        String PX = String.valueOf(this.getLocation().getX());
        String PY = String.valueOf(this.getLocation().getY());

        PX = PX.substring(0, PX.length() - 2);
        PY = PY.substring(0, PY.length() - 2);

        DVPropMain.DV_LOCATION = this.getWidth() + "," + this.getHeight() + "," + PX + "," + PY;
        DVPropMain.editorMain.put("DV_LOCATION", DVPropMain.DV_LOCATION);
    }

    public void closeAllConnections() {

        if (DVPropMain.DB_SERVER_CONNECT.size() > 0) {

            Set entries = DVPropMain.DB_SERVER_CONNECT.entrySet();

            Iterator iter = entries.iterator();

            while (iter.hasNext()) {

                Map.Entry entry = (Map.Entry) iter.next();

                try {

                    if (!((Connection) entry.getValue()).isClosed()) {

                        ((Connection) entry.getValue()).close();
                    }
                } catch (Exception ee) {
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == history||e.getSource() == historyItem) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DataViewerHistory(DVPropMain.DV_FRAME.get("MAIN"));
                }
            });
        }
        if (e.getSource() == releaseContext||e.getSource() == releaseContextItem) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DataViewerUpgradeDialog(DVPropMain.DV_FRAME.get("MAIN"), 1);
                }
            });
        }

        if (e.getSource() == Server||e.getSource() == serverItem) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DataViewerRegister((DVFrame) (DVPropMain.DV_FRAME.get("MAIN")), true);
                }
            });
        }

        if (e.getSource() == config||e.getSource() == configItem) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DataViewerOptionView(DVPropMain.DV_FRAME.get("MAIN"), true);
                }
            });
        }
        if (e.getSource() == version||e.getSource() == versionItem) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DVAboutDialog(DVPropMain.DV_FRAME.get("MAIN")).setVisible(true);
                }
            });
        }

        if (e.getSource() == heapMemory||e.getSource() == heapMemoryItem) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new HeapMemoryDialog(DVPropMain.DV_FRAME.get("MAIN")).setVisible(true);
                }
            });
        }
    }

    private javax.swing.JButton Server;
    private JPanel dvMainPanel;
    private javax.swing.JButton history;
    private javax.swing.JMenuItem version, heapMemory, releaseContext;

    private javax.swing.JButton config;
    private JToolBar dvSQLToolBar;
    private DVTabPaneView mainTabPanel;
    private DVPopupMenuButton menuButton, plugMenuButton;

    public DVPopupMenuButton getTableButton() {
        return menuButton;
    }

}
