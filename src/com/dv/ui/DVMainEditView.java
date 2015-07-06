/*
 * DVMainEditView.java  2/6/13 1:04 PM
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
/*
 * DVMainEditView.java
 *
 * Created on Aug 8, 2010, 2:02:30 PM
 */
package com.dv.ui;

import com.dv.ui.autocomplete.DataViewerAutoCompletion;
import com.dv.ui.component.*;
import com.dv.export.DVExportView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import java.sql.*;
import javax.swing.JButton;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.Font;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import com.dv.ui.config.DataViewerOptionView;
import com.dv.ui.editor.QueryEditorStatusBar;
import com.dv.ui.gridresult.DVResultGridPanel;
import com.dv.ui.gridresult.DVResultTablePanel;
import com.dv.prop.DVPropMain;
import com.dv.util.*;
import com.dv.swing.editor.DVQueryEditorPane;
import com.dv.swing.editor.DVSQLResultPane;
import com.dv.ui.action.*;

import java.util.ArrayList;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * @author Java
 */
public class DVMainEditView extends JPanel implements ActionListener,
        KeyListener {

    /**
     * Creates new form DVMainEditView
     */
    private boolean needToSaveNewSQl = true;
    QueryEditorStatusBar qesb;
    private String singleSQL = new String();
    private String serverUserId = "";
    private String serverSchema = "";
    private String DB2ServerSID = null;


    public String getLastQueryInformation() {
        return lastQueryInformation;
    }

    public void setLastQueryInformation(String lastQueryInformation) {
        this.lastQueryInformation = lastQueryInformation;
    }

    private String lastQueryInformation = "";
    int noSQlCode = 9999;
    String errorMessage;

    public String getServerSchema() {
        return serverSchema;
    }

    Vector col = new Vector();
    Vector colType = new Vector();
    DVDbUtil util = new DVDbUtil();
    int count = 0, nCount = 0;
    DVSQLResultPane resultTextPane = new DVSQLResultPane();
    DVSQLResultPane resultHistoryTextPane;
    StringBuffer historyResult;
    public static String IS_BATCH_SQL_SYMBOL = ";\n";
    public static boolean IS_BATCH_RESULT = false;
    public String[] batchSQLSList;
    private JTabbedPane resultTabPane;
    private JMenuItem executeItem, executeLineItem, saveItem, saveAsItem,
            openItem, ctcItem, sctcItem, stopItem, rcItem, preferItem, tableInforItem, exportAndImportTableScript;
    private javax.swing.JButton executeLine;
    private javax.swing.JPanel dvMainPanel, editPanel, errorResultPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel toolPanel;
    RTextScrollPane sqlResultScrollPane, sqlHistoryResultScrollPane;
    private DVCleanSplitPane editAreaSplitPane, mainSplitPane;
    private javax.swing.JButton save;
    private javax.swing.JButton saveAs;
    private javax.swing.JButton openFile;
    private javax.swing.JButton stopExcute;
    private javax.swing.JButton reconnectButton;
    private javax.swing.JButton ctcButton, sctcButton;
//    private javax.swing.JButton layoutButton;
    JScrollPane resultScrollPane1;
    DVResultGridPanel resultGridPanel = new DVResultGridPanel();
    DVQueryWorker dvsw;
    DVUpdateWork dvupdate;
    // End of variables declaration
    private String key;
    private Connection mainConnection = null;

    public Connection getMainConnection() {
        return mainConnection;
    }

    ResultSetMetaData rsmdn = null;
    Statement stat = null;
    ResultSet rs = null;
    boolean isOk = false;
    boolean canBeFormatBatchSqlResult = false;
    JMenuItem exportFileMenu, exportResultIntoSQLMenu;
    private static final String MSG = "com.dv.ui.DataViewerReferMessage";
    private static final ResourceBundle mainViewResource = ResourceBundle
            .getBundle(MSG);
    ImageIcon trackingIcon = new ImageIcon(
            DVFrame.class.getResource(mainViewResource
                    .getString("bookmarkIcon")));
    public DataViewerAutoCompletion ac;
    public HashMap<String, Vector> batchResultCol = new HashMap<String, Vector>();
    public HashMap<String, Vector> batchResultRow = new HashMap<String, Vector>();
    public HashMap<String, String> batchSQLs = new HashMap<String, String>();

    public HashMap<String, String> batchLastInformations = new HashMap<String, String>();

    public HashMap<String, String> getBatchLastInformations() {
        return batchLastInformations;
    }

    public void setBatchLastInformations(String key, String value) {
        batchLastInformations.put(key, value);
    }

    private String title;
    private DVTabPaneView subMainTab;
    public String fileFullpath, fileName;
    private Gutter gutter;
    private LoggingOutputPane errorresultPane;
    RTextScrollPane sqlScrollPane;

    public void setHistoryResultContent(String sql, String contents) {

        historyResult = new StringBuffer();
        historyResult.append("\n"
                + mainViewResource.getString("History_Result_Text_1") + "\n");
        historyResult.append(mainViewResource
                .getString("History_Result_Text_3") + "\n");
        historyResult.append(sql + "\n");
        historyResult.append(mainViewResource
                .getString("History_Result_Text_2") + "\n");
        historyResult.append(contents);
        DVFileUtil.saveSqlHistoryFile(historyResult.toString());
        historyResult = null;
        cleanHistoryResultContent();
    }

    public void cleanHistoryResultContent() {
        DataViewerUtilities.scheduleGC();
    }

    public String getSingleSQL() {
        return singleSQL;
    }

    public void setSingleSQL(String sql) {
        singleSQL = sql;
    }

    public QueryEditorStatusBar getQesb() {
        return qesb;
    }

    public Gutter getGutter() {
        return gutter;
    }

    public HashMap<String, String> getBatchSQLs() {
        return batchSQLs;
    }

    public ImageIcon getTrackingIcon() {
        return trackingIcon;
    }

    private int needToChangeTabTitle = 0;

    public int getNeedToChangeTabTitle() {
        return needToChangeTabTitle;
    }

    public void setNeedToChangeTabTitle(int needToChangeTabTitle) {
        this.needToChangeTabTitle = needToChangeTabTitle;
    }

    public static ResourceBundle getMainViewResource() {
        return mainViewResource;
    }

    public DVTabPaneView getSubMainTab() {
        return subMainTab;
    }

    public String getServerName() {

        String[] nameSSS = key.split("@");
        return nameSSS[0].trim();
    }

    public DVMainEditView(String key, String title, DVTabPaneView subMainTab) {
        this.subMainTab = subMainTab;
        this.setLayout(new BorderLayout());
        this.key = key;
        this.title = title;
        this.serverUserId = DVPropMain.DB_SERVER_CON_USER.get(key);
        this.serverSchema = DVPropMain.DB_SERVER_SCHEMA.get(key);
        mainConnection = (Connection) DVPropMain.DB_SERVER_CONNECT.get(key);
        setDB2ServerSID();
        initComponents();
        buildTextAreaPopUpMenu();
        setAC();
    }


    public void setDB2ServerSID() {
        try {
            DB2ServerSID = DVPropMain.DV_SERVER_INSTANCE.get(getServerName()).getSid();
        } catch (Exception e) {

        }

    }

    public String getDB2ServerSID() {
        return DB2ServerSID;
    }

    public String getServerUserId() {
        return serverUserId;
    }

    public void setServerUserId(String serverUserId) {
        this.serverUserId = serverUserId;
    }

    public void setAC() {
        if (DVPropMain.DB_AUTOCOMPLETE_POOL.get(key) != null) {
            ac = new DataViewerAutoCompletion(DVPropMain.DB_AUTOCOMPLETE_POOL.get(key));
            ac.install(dvtextPane);
            ac.setTriggerKey(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                    InputEvent.CTRL_MASK));
            qesb.setAutoCompleteStatus("Open");

        } else {

            new DVAutoCompleteWorker(this).execute();

        }
    }

    public ArrayList getLineNumbers() {

        return dvtextPane.getSqlLineNumbers(IS_BATCH_SQL_SYMBOL);
    }

    public DVQueryEditorPane getDvtextPane() {
        return dvtextPane;
    }

    public DVSQLResultPane getResultTextPane() {
        return resultTextPane;
    }

    public Connection getSubSVConnection() {
        return (Connection) DVPropMain.DB_SERVER_CONNECT.get(key);
    }

    public String getSubSVSchema() {
        return DVPropMain.DB_SERVER_SCHEMA.get(key);
    }

    public QueryEditorStatusBar getSubSVQueryEditorStatusBar() {
        return qesb;
    }

    public String getSubSVKey() {
        return key;
    }

    public DataViewerAutoCompletion getSubSVAutoCompletion() {
        return ac;
    }

    public void setSubSVAutoCompletion(DataViewerAutoCompletion ccc) {

        ac = ccc;

    }

    public DVQueryEditorPane getSubSVDVQueryEditorPane() {
        return dvtextPane;
    }

    public void buildResultPane() {

        resultTextPane = new DVSQLResultPane();

        resultTextPane.setEditable(true);

        resultTextPane.setAutoscrolls(false);

        resultTextPane.setBorder(null);

        sqlResultScrollPane = new RTextScrollPane(resultTextPane, true);

        gutter = sqlResultScrollPane.getGutter();
        gutter.setBackground(new Color(Integer.parseInt(mainViewResource
                .getString("sqlResultGutterBackground"))));
        gutter.setLineNumberFont(new Font(DVPropMain.DV_SQL_FONT_NAME,
                Font.PLAIN, Integer.parseInt(DVPropMain.DV_SQL_FONT_SIZE)));
        gutter.setForeground(new Color(Integer.parseInt(mainViewResource
                .getString("sqlResultGutterForeground"))));
        gutter.setBookmarkingEnabled(true);
        gutter.setBookmarkIcon(trackingIcon);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        errorresultPane = new LoggingOutputPane();

        // errorresultPane.setContentType("text/html");

        errorresultPane.setFont(new Font(DVPropMain.DV_SQL_FONT_NAME,
                Font.PLAIN, Integer.parseInt(DVPropMain.DV_SQL_FONT_SIZE)));

        buildResultPane();

        buildExportPopupMenu();

        dvMainPanel = new javax.swing.JPanel();

        // main panel setting

        mainSplitPane = new DVCleanSplitPane();
        mainSplitPane.setOrientation(javax.swing.JSplitPane.HORIZONTAL_SPLIT);

        mainSplitPane.setDividerLocation(60);
        mainSplitPane.setDividerSize(4);

        editAreaSplitPane = new DVCleanSplitPane();

        qesb = new QueryEditorStatusBar();
        qesb.setInsertionMode("INS");

        qesb.setStatus(this.getServerName() + " [ " + this.getServerUserId() + "@"
                + this.getServerSchema() + " ]");
        qesb.setStatusTooltip(key + "  :  " + this.getServerUserId() + "@"
                + this.getServerSchema());
        qesb.setCaretPosition(1, 1);

        dvtextPane = new DVQueryEditorPane(qesb, this);

        jPanel2 = new javax.swing.JPanel();

        // toolbar
        dvSQLToolBar = new javax.swing.JToolBar();

        execute = new RolloverButton("", new ImageIcon(
                DVFrame.class.getResource(mainViewResource
                        .getString("executeIcon"))));
        executeLine = new RolloverButton();

        saveAs = new RolloverButton();
        save = new RolloverButton();
        openFile = new RolloverButton();
        openFile.setText("Open");

        ctcButton = new RolloverButton();
        ctcButton.addActionListener(this);

        reconnectButton = new RolloverButton("RC", new ImageIcon(
                DVFrame.class.getResource(mainViewResource
                        .getString("reconnectIcon"))));
        reconnectButton.addActionListener(this);
        reconnectButton.setToolTipText(mainViewResource
                .getString("reconnectButtonToolTip"));

        dvMainPanel.setName("jPanel1"); // NOI18N
        dvMainPanel.setLayout(new java.awt.BorderLayout());

        editAreaSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        editAreaSplitPane.setName("jSplitPane1"); // NOI18N

        dvtextPane.setName("dvtextPane"); // NOI18N

        editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());

        sqlScrollPane = new RTextScrollPane(dvtextPane, true);

        gutter = sqlScrollPane.getGutter();
        gutter.setBackground(new Color(Integer.parseInt(mainViewResource
                .getString("sqlEditGutterBackground"))));
        gutter.setLineNumberFont(new Font(DVPropMain.DV_SQL_FONT_NAME,
                Font.PLAIN, Integer.parseInt(DVPropMain.DV_SQL_FONT_SIZE)));

        gutter.setForeground(new Color(Integer.parseInt(mainViewResource
                .getString("sqlEditGutterForeground"))));
        gutter.setBookmarkingEnabled(true);
        gutter.setBookmarkIcon(new ImageIcon(DVFrame.class
                .getResource(mainViewResource.getString("bookmarkIcon"))));

        editPanel.add(sqlScrollPane, BorderLayout.CENTER);

        editPanel.add(qesb, BorderLayout.SOUTH);

        editAreaSplitPane.setLeftComponent(editPanel);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        // Build result tabpane

        resultTabPane = new ClosableTabbedPane(false);
        resultTabPane.setTabPlacement(SwingConstants.BOTTOM);
        resultTabPane.addTab(
                "SQL Result",
                new ImageIcon(DVMainEditView.class.getResource(mainViewResource
                        .getString("resultTabIcon"))), sqlResultScrollPane);

        resultTabPane.setSelectedIndex(0);

        errorResultPanel = new JPanel();
        errorResultPanel.setLayout(new BorderLayout());

        errorResultPanel.add(errorresultPane, BorderLayout.CENTER);

        resultTabPane.addTab(
                "Information",
                new ImageIcon(DVMainEditView.class.getResource(mainViewResource
                        .getString("statusIconError"))), errorResultPanel);

        /*
         * Set TableResultTab
         */

        setGridResultTab();


        jPanel2.add(resultTabPane, java.awt.BorderLayout.CENTER);
        jPanel2.setBorder(BorderFactory.createLineBorder(Color.black));

        editAreaSplitPane.setRightComponent(jPanel2);

        editAreaSplitPane.setDividerLocation(400);
        editAreaSplitPane.setDividerSize(4);

        dvMainPanel.add(editAreaSplitPane, java.awt.BorderLayout.CENTER);

        dvSQLToolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,
                1, 1, 1));
        dvSQLToolBar.setFloatable(false);
        dvSQLToolBar.setRollover(true);
        dvSQLToolBar.setCursor(new java.awt.Cursor(
                java.awt.Cursor.DEFAULT_CURSOR));
        dvSQLToolBar.setName("dvSQLToolBar"); // NOI18N

        execute.setText("Execute");
        execute.addActionListener(this);
        execute.setFocusable(false);
        execute.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        execute.setName("execute"); // NOI18N
        execute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        execute.registerKeyboardAction(this,
                KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        execute.setToolTipText(mainViewResource.getString("execute"));
        dvSQLToolBar.add(execute);

        executeLine.setFocusable(false);
        executeLine.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        executeLine.setText("EQLine");

        executeLine.addActionListener(this);
        executeLine.setName("executeLine"); // NOI18N
        executeLine.setIcon(new ImageIcon(DVFrame.class
                .getResource(mainViewResource.getString("executeLineIcon"))));
        executeLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        executeLine.registerKeyboardAction(this,
                KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        executeLine.setToolTipText(mainViewResource.getString("executeLine"));
        dvSQLToolBar.add(executeLine);

        openFile.addActionListener(new OpenFileAction(this));
        openFile.setIcon(new ImageIcon(DVFrame.class
                .getResource(mainViewResource.getString("openIcon"))));
        openFile.registerKeyboardAction(new OpenFileAction(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        openFile.setToolTipText(mainViewResource.getString("open"));
        dvSQLToolBar.add(openFile);

        saveAs.setText("SaveAs");
        saveAs.addActionListener(new SaveAsFileAction(this));
        saveAs.setIcon(new ImageIcon(DVFrame.class.getResource(mainViewResource
                .getString("saveAsIcon"))));
        saveAs.setFocusable(false);
        saveAs.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        saveAs.setName("saveAs"); // NOI18N
        saveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        dvSQLToolBar.add(saveAs);

        save.setText("Save");
        save.addActionListener(new SaveFileAction(this));
        save.setIcon(new ImageIcon(DVFrame.class.getResource(mainViewResource
                .getString("saveIcon"))));
        save.setFocusable(false);
        save.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        save.setName("save"); // NOI18N
        save.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        save.registerKeyboardAction(new SaveFileAction(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        save.setToolTipText(mainViewResource.getString("save"));
        dvSQLToolBar.add(save);

        ctcButton.setText("CTC");
        ctcButton.setIcon(new ImageIcon(DVFrame.class
                .getResource(mainViewResource.getString("ctcIcon"))));
        ctcButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ctcButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ctcButton.setEnabled(true);
        ctcButton.addActionListener(new CTCAction(this));
        ctcButton.setToolTipText(mainViewResource.getString("ctc"));
        ctcButton.registerKeyboardAction(new CTCAction(this),
                KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        dvSQLToolBar.add(ctcButton);

        sctcButton = new RolloverButton();
        sctcButton.setText("SCTC");
        sctcButton.setToolTipText("Super CTC");
        sctcButton.setIcon(new ImageIcon(DVFrame.class
                .getResource(mainViewResource.getString("sctcIcon"))));
        sctcButton.setEnabled(true);
        sctcButton.addActionListener(new SCTCAction(this));

        dvSQLToolBar.add(sctcButton);

        stopExcute = new RolloverButton();
        stopExcute.setText("Stop");
        stopExcute.setToolTipText(mainViewResource.getString("cancelButtonToolTip"));
        stopExcute.setIcon(new ImageIcon(DVFrame.class
                .getResource(mainViewResource.getString("stopIcon"))));
        stopExcute.setEnabled(true);
        stopExcute.addActionListener(this);

        dvSQLToolBar.add(stopExcute);

        dvSQLToolBar.add(reconnectButton);

//        layoutButton = new RolloverButton();
//        layoutButton.setText("Layout");
//        layoutButton.setIcon(new ImageIcon(DVFrame.class
//                .getResource(mainViewResource.getString("codeSearch"))));
//        layoutButton.setEnabled(true);
//        layoutButton.addActionListener(this);
//
//        dvSQLToolBar.add(layoutButton);

        dvSQLToolBar.setFloatable(false);
        dvSQLToolBar.setOpaque(true);
//        toolPanel = new javax.swing.JPanel();
//        toolPanel.setLayout(new BorderLayout());
//        toolPanel.add(dvSQLToolBar, BorderLayout.WEST);
//        dvMainPanel.add(toolPanel, java.awt.BorderLayout.NORTH);
        dvMainPanel.add(dvSQLToolBar, java.awt.BorderLayout.NORTH);
        add(dvMainPanel, java.awt.BorderLayout.CENTER);
    }

    public void setGridResultTab() {

        if (DVPropMain.DV_DISPLAY_GRID.equals("1")) {
            resultScrollPane1 = new JScrollPane();
            resultScrollPane1
                    .setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            resultScrollPane1
                    .setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            resultScrollPane1.setViewportView(resultGridPanel);

            resultTabPane.addTab(
                    "Grid Result",
                    new ImageIcon(DVMainEditView.class.getResource(mainViewResource
                            .getString("gridTabIcon"))), resultScrollPane1);
        }
    }

    public void setGridResultData() {
        resultGridPanel.removeAll();
        resultGridPanel.setResultTableLayout(1);
        resultGridPanel.addResultTable(new DVResultTablePanel(this.getRow(), this.getCol()));
    }

    public void setBatchGridResultData() {
        resultGridPanel.removeAll();
        resultGridPanel.setResultTableLayout(batchResultCol.size());
        for (int i = 0; i < batchResultCol.size(); i++) {
            resultGridPanel.addResultTable(new DVResultTablePanel(batchResultRow.get(String.valueOf(i)), batchResultCol.get(String.valueOf(i))));
        }
    }

    public void removeGridResultTab() {
        resultGridPanel.removeAll();
        resultGridPanel.repaint();
        resultGridPanel.validate();
        resultScrollPane1.removeAll();

        resultTabPane.removeTabAt(2);
        resultTabPane.repaint();
        resultTabPane.validate();
        DataViewerUtilities.scheduleGC();
    }

    public DVResultGridPanel getResultGridPanel() {
        return resultGridPanel;
    }

    public DVSQLResultPane getResultHistoryTextPane() {
        return resultHistoryTextPane;
    }

    public JTabbedPane getResultTabPane() {
        return resultTabPane;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == exportFileMenu) {
            DVExportView exportView = new DVExportView(
                    DVPropMain.DV_FRAME.get("MAIN"), this);
        }

        if (e.getSource() == preferItem) {
            DataViewerOptionView dvdbsr = new DataViewerOptionView(
                    DVPropMain.DV_FRAME.get("MAIN"), true);
        }
        if (e.getSource() == reconnectButton || e.getSource() == rcItem) {
            setReConnect();
        }
        if (e.getSource() == stopExcute || e.getSource() == stopItem) {
            try {
                dvsw.cancel(true);
            } catch (Exception rrr) {
                DVLOG.setErrorLog(DVMainEditView.class.getName(), rrr);
            }

            resultTextPane.setVisible(true);
            execute.setEnabled(true);
            executeLine.setEnabled(true);
        }

        if (e.getSource() == executeLine || e.getSource() == executeLineItem) {

            try {
                String sql = dvtextPane.getCurrentLineText().trim();

                if (sql == null || sql.equals("")) {
                    setErrorMessage(
                            mainViewResource.getString("noSQLToExecute"),
                            noSQlCode);
                } else {
                    if (sql.toUpperCase().equals("RECONNECT")) {
                        setReConnect();

                    } else if (isNotSelectSQL(sql.toUpperCase())) {

                        sqlUpdate("L", mainConnection);

                    } else {
                        sqlExecuteAndExecuteLine("L", mainConnection);
                    }
                }

            } catch (Exception ee) {

                DVLOG.setErrorLog(DVMainEditView.class.getName(), ee);
                setErrorMessage(mainViewResource.getString("noSQLToExecute"),
                        noSQlCode);

            }
        }

        if (e.getSource() == execute || e.getSource() == executeItem) {
            try {
                String sql = dvtextPane.getSelectedText().trim();

                if (sql == null || sql.equals("")) {
                    setErrorMessage(
                            mainViewResource.getString("noSQLToExecute"),
                            noSQlCode);
                } else {

                    if (isBatchSQL(sql)) {
                        if (isNotSelectSQL(sql.toUpperCase())) {
                            sqlUpdate("B", mainConnection);
                        } else {
                            sqlExecuteAndExecuteLine("B", mainConnection);
                        }
                    } else if (sql.toUpperCase().equals("RECONNECT")) {
                        setReConnect();
                    } else if (isNotSelectSQL(sql.toUpperCase())) {

                        sqlUpdate("E", mainConnection);

                    } else {
                        sqlExecuteAndExecuteLine("E", mainConnection);
                    }

                }
            } catch (Exception dd) {
                DVLOG.setErrorLog(DVMainEditView.class.getName(), dd);
                setErrorMessage(mainViewResource.getString("noSQLToExecute"),
                        noSQlCode);
            }
        }

    }

    public void buildExportPopupMenu() {

        exportFileMenu = new JMenuItem("Export SQL result into file");
        resultTextPane.getPopupMenu().addSeparator();
        resultTextPane.getPopupMenu().add(exportFileMenu);
        exportFileMenu.addActionListener(this);

        exportResultIntoSQLMenu = new JMenuItem("Export result as SQL");
        resultTextPane.getPopupMenu().addSeparator();
        resultTextPane.getPopupMenu().add(exportResultIntoSQLMenu);
        exportResultIntoSQLMenu.addActionListener(this);
    }

    public HashMap<String, Vector> getBatchResultCol() {
        return batchResultCol;
    }

    public void setBatchResultCol(HashMap<String, Vector> batchResultCol) {
        this.batchResultCol = batchResultCol;
    }

    public HashMap<String, Vector> getBatchResultRow() {
        return batchResultRow;
    }

    public void setBatchResultRow(HashMap<String, Vector> batchResultRow) {
        this.batchResultRow = batchResultRow;
    }

    public boolean isNeedToSaveNewSQl() {
        return needToSaveNewSQl;
    }

    public void setNeedToSaveNewSQl(boolean needToSaveNewSQl) {
        this.needToSaveNewSQl = needToSaveNewSQl;
    }

    public String getFileFullpath() {
        return fileFullpath;
    }

    public void setFileFullpath(String fileFullpath) {
        this.fileFullpath = fileFullpath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setReConnect() {
        try {
            Connection connn = null;
            String url = DVPropMain.DB_SERVER_CON_DETAIL.get(key);
            String user = DVPropMain.DB_SERVER_CON_USER.get(key);
            String pw = DVPropMain.DB_SERVER_CON_PW.get(key);

            String dbType = DVPropMain.getDataBaseServerType(key);

            connn = DvConnectFactory.getOtherConnection(dbType, url, user, pw);

            if (connn == null) {
                DataViewerUtilities.displayErrorMessageForConnection();
            } else {
                mainConnection = connn;
                DVPropMain.DB_SERVER_CONNECT.put(key, connn);
            }
        } catch (SQLException www) {
            DVLOG.setErrorLog(DVMainEditView.class.getName(), www);
            setErrorMessage(www.getMessage(), www.getErrorCode());

        } catch (Exception www) {
            DVLOG.setErrorLog(DVMainEditView.class.getName(), www);
            setErrorMessage(www.getMessage(), noSQlCode);
        }
    }

    public boolean isNotSelectSQL(String sql) {
        String contain = sql;
        boolean isSelect = false;

        isSelect=DVQueryType.isNotSelectSQL(contain);

        return isSelect;
    }

    private boolean hasMaxRecord = true;

    public void sqlUpdate(String type, Connection con) {

        String sql = null;
        String tempSQl = null;
        getGutter().removeAllTrackingIcons();
        try {
            if (type.equals("E")) {
                tempSQl = dvtextPane.getSelectedText().trim();
                getGutter().addLineTrackingIcon(
                        getDvtextPane().getStartLineofSelectedContent(),
                        getTrackingIcon());
                setLastSqlLine(getDvtextPane().getStartLineofSelectedContent());
                sql = parseSQL(tempSQl);
                DVLOG.setInfoLog(DVMainEditView.class.getName() + "  " + key
                        + "\n" + tempSQl);
                dvupdate = new DVUpdateWork(con, sql, type, null, this);

            } else if (type.equals("L")) {
                tempSQl = dvtextPane.getCurrentLineText().trim();
                getGutter()
                        .addLineTrackingIcon(
                                getDvtextPane().getCaretLineNumber(),
                                getTrackingIcon());
                setLastSqlLine(getDvtextPane().getStartLineofSelectedContent());
                sql = parseSQL(tempSQl);

                DVLOG.setInfoLog(DVMainEditView.class.getName() + "  " + key
                        + "\n" + tempSQl);

                dvupdate = new DVUpdateWork(con, sql, type, null, this);

            } else if (type.equals("B")) {
                tempSQl = dvtextPane.getSelectedText().trim();
                String[] batchSql = createBatchUpdateSQL(tempSQl
                        .split(IS_BATCH_SQL_SYMBOL));
                DVLOG.setInfoLog(DVMainEditView.class.getName() + "  " + key
                        + "\n" + tempSQl);
                dvupdate = new DVUpdateWork(con, null, type, batchSql, this);
            }
            dvupdate.execute();

        } catch (Exception e) {

            setErrorMessage(e.toString(), noSQlCode);
        }
    }

    public void sqlExecuteAndExecuteLine(String type, Connection con) {
        String sql = new String();
        String tempSql = "";

        IS_BATCH_RESULT = false;
        getGutter().removeAllTrackingIcons();

        try {
            if (qesb.getMaxRecoder() != null
                    && !qesb.getMaxRecoder().equals("")) {

                Integer.parseInt(qesb.getMaxRecoder());
                hasMaxRecord = true;

            } else {
                hasMaxRecord = false;
            }
            if (type.equals("E")) {
                tempSql = dvtextPane.getSelectedText().trim();
                getGutter().addLineTrackingIcon(
                        getDvtextPane().getStartLineofSelectedContent(),
                        getTrackingIcon());
                setLastSqlLine(getDvtextPane().getStartLineofSelectedContent());
                sql = createSQL(tempSql);
                this.setSingleSQL(tempSql);
                dvsw = new DVQueryWorker(con, parseSQL(sql), type, null, this);
                DVLOG.setInfoLog(DVMainEditView.class.getName() + "  " + key
                        + "\n" + tempSql);

            } else if (type.equals("L")) {
                tempSql = dvtextPane.getCurrentLineText().trim();
                getGutter()
                        .addLineTrackingIcon(
                                getDvtextPane().getCaretLineNumber(),
                                getTrackingIcon());
                setLastSqlLine(getDvtextPane().getStartLineofSelectedContent());
                sql = createSQL(tempSql);
                this.setSingleSQL(tempSql);
                dvsw = new DVQueryWorker(con, parseSQL(sql), type, null, this);
                DVLOG.setInfoLog(DVMainEditView.class.getName() + "  " + key
                        + "\n" + tempSql);
            } else if (type.equals("B")) {
                IS_BATCH_RESULT = true;
                tempSql = dvtextPane.getSelectedText().trim();
                batchSQLSList = createBatchSQL(tempSql
                        .split(IS_BATCH_SQL_SYMBOL));
                dvsw = new DVQueryWorker(con, null, type, batchSQLSList, this);
                DVLOG.setInfoLog(DVMainEditView.class.getName() + "  " + key
                        + "\n" + tempSql);
            }
            dvsw.execute();

        } catch (Exception eee) {

            setErrorMessage(eee.toString(), noSQlCode);

        }
    }

    int lastSqlLine = 1;

    public int getLastSqlLine() {
        return lastSqlLine;
    }

    public void setLastSqlLine(int lastSqlLine) {
        DVPropMain.DV_RECENT_INSTANCE_LAST_EXECUTE_SQL_LINE.put(getServerName(), String.valueOf(lastSqlLine));
        this.lastSqlLine = lastSqlLine;
    }

    public static boolean isIS_BATCH_RESULT() {
        return IS_BATCH_RESULT;
    }

    public void loadSQLFileDetail(String fullFile) {

        try {

            dvtextPane.load(FileLocation.create(fullFile),
                    System.getProperty("file.encoding"));
            subMainTab.setTitleAt(subMainTab.getSelectedIndex(), title + "@"
                    + fileName);
            needToSaveNewSQl = false;
            DVLOG.setInfoLog(DVMainEditView.class.getName() + "  " + "Load [ "
                    + fileName + " ] successfully");
            setNeedToChangeTabTitle(dvtextPane.getText().length());
        } catch (Exception ioe) {
            DVLOG.setErrorLog(DVMainEditView.class.getName(), ioe);
            setErrorMessage(ioe.getMessage(), noSQlCode);
        }
    }

    public void saveSQLFileDetail(String file) {
        try {
            String content = dvtextPane.getText().trim();
            if (content != null && !content.equals("") && content.length() >= 1) {

                dvtextPane.saveAs(FileLocation.create(file));
                subMainTab.setTitleAt(subMainTab.getSelectedIndex(), title
                        + "@" + fileName);

                setNeedToChangeTabTitle(dvtextPane.getText().length());

                DVPropMain.DV_FRAME.get("MAIN").setTitle(
                        DVPropMain.DV_NAME
                                + "-"
                                + DVPropMain.DV_VERSION
                                + "-"
                                + getSubMainTab().getTabTitleAt(
                                getSubMainTab().getSelectedIndex()));
            } else {
                setErrorMessage(mainViewResource.getString("noSQLToExecute"),
                        noSQlCode);
            }
        } catch (Exception e) {
            DVLOG.setErrorLog(DVMainEditView.class.getName(), e);
            setErrorMessage(e.getMessage(), noSQlCode);
        }
    }

    public void saveAsSQLFile(String fFile) {
        try {
            saveSQLFileDetail(fFile);
            needToSaveNewSQl = false;
        } catch (Exception sss) {
            DVLOG.setErrorLog(DVMainEditView.class.getName(), sss);
            setErrorMessage("Save [ " + fileName + " ] failed", noSQlCode);
        }
    }

    public void setInforMessage(String message) {
        resultTabPane.setSelectedIndex(0);
    }

    public void setErrorMessage(String message, int sqlCode) {
        resultTabPane.setSelectedIndex(1);
        errorresultPane.setText("");
        String codesMessage = DVPropMain.getSQLCodeDes(sqlCode);
        if (codesMessage != "") {

            errorresultPane.append(SqlMessages.ERROR_MESSAGE, "\n[Error]>"
                    + message + " <==> " + codesMessage + "\n");

        } else {

            errorresultPane.append(SqlMessages.ERROR_MESSAGE, "\n[Error]>"
                    + message);
        }
        errorresultPane.append("[Executed: "
                + DateFormatFactory.getCurrentDateTimeString() + " ]");
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void setBatchMap(String k, Vector cols, Vector rows) {
        batchResultCol.put(k, cols);
        batchResultRow.put(k, rows);
    }

    public void setBatchSQL(String k, String sql) {
        batchSQLs.put(k, sql);
    }

    public void clearBatchMap() {
        try {
            if (!batchResultCol.isEmpty()) {
                batchResultCol.clear();
            }
            if (!batchResultRow.isEmpty()) {
                batchResultRow.clear();
            }

        } catch (NullPointerException ee) {
        }
    }

    public void cleanBatchSql() {
        // batchSQLs = new HashMap<String, String>();

        try {
            batchSQLs.clear();
        } catch (NullPointerException ee) {
        }
    }

    private javax.swing.JToolBar dvSQLToolBar;
    private DVQueryEditorPane dvtextPane;
    private javax.swing.JButton execute;

    public JButton getExecute() {
        return execute;
    }

    public JButton getExecuteLine() {
        return executeLine;
    }

    public boolean isCanBeFormatBatchSqlResult() {
        return canBeFormatBatchSqlResult;
    }

    public void setCanBeFormatBatchSqlResult(boolean canBeFormatBatchSqlResult) {
        this.canBeFormatBatchSqlResult = canBeFormatBatchSqlResult;
    }

    Vector row = new Vector();

    public Vector getCol() {
        return col;
    }

    public void setCol(Vector col) {
        this.col = col;
    }

    public Vector getColType() {
        return colType;
    }

    public void setColType(Vector colType) {
        this.colType = colType;
    }

    public Vector getRow() {
        return row;
    }

    public void setRow(Vector row) {
        this.row = row;
    }

    public void buildTextAreaPopUpMenu() {

        executeItem = new JMenuItem(mainViewResource.getString("execute"));
        executeLineItem = new JMenuItem(
                mainViewResource.getString("executeLine"));
        saveItem = new JMenuItem(mainViewResource.getString("save"));
        saveAsItem = new JMenuItem(mainViewResource.getString("saveAs"));
        openItem = new JMenuItem(mainViewResource.getString("open"));
        ctcItem = new JMenuItem(mainViewResource.getString("ctc"));
        sctcItem = new JMenuItem("Super CTC ...");
        stopItem = new JMenuItem(mainViewResource.getString("stop"));
        rcItem = new JMenuItem(mainViewResource.getString("reconnect") + " "
                + key);

        preferItem = new JMenuItem(mainViewResource.getString("preferences"));

        executeItem.setAccelerator(KeyStroke.getKeyStroke('E',
                InputEvent.CTRL_MASK));
        executeLineItem.setAccelerator(KeyStroke.getKeyStroke('L',
                InputEvent.CTRL_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke('O',
                InputEvent.CTRL_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S',
                InputEvent.CTRL_MASK));
        ctcItem.setAccelerator(KeyStroke
                .getKeyStroke('T', InputEvent.CTRL_MASK));

        executeItem.addActionListener(this);
        executeLineItem.addActionListener(this);
        saveItem.addActionListener(new SaveFileAction(this));
        saveAsItem.addActionListener(new SaveAsFileAction(this));
        openItem.addActionListener(new OpenFileAction(this));
        ctcItem.addActionListener(new CTCAction(this));
        sctcItem.addActionListener(new SCTCAction(this));
        stopItem.addActionListener(this);
        rcItem.addActionListener(this);

        preferItem.addActionListener(this);

        dvtextPane.getPopupMenu().addSeparator();

        dvtextPane.getPopupMenu().add(executeItem);
        dvtextPane.getPopupMenu().add(executeLineItem);
        dvtextPane.getPopupMenu().add(openItem);
        dvtextPane.getPopupMenu().add(saveItem);
        dvtextPane.getPopupMenu().add(saveAsItem);
        dvtextPane.getPopupMenu().add(ctcItem);
        dvtextPane.getPopupMenu().add(sctcItem);
        dvtextPane.getPopupMenu().add(stopItem);
        dvtextPane.getPopupMenu().add(rcItem);

        if (DVPropMain.getDataBaseServerType(key).equals("DB2")||DVPropMain.getDataBaseServerType(key).equals("DB2GSMA")) {
            dvtextPane.getPopupMenu().addSeparator();
            tableInforItem = new JMenuItem("Check Table Refer ..");
            tableInforItem.addActionListener(new DVSqlBuildAction(0, this));
            dvtextPane.getPopupMenu().add(tableInforItem);
        }
        dvtextPane.getPopupMenu().addSeparator();
        dvtextPane.getPopupMenu().add(preferItem);
    }

    public String parseSQL(String sql) {
        String[] sqlA = sql.split("\n");
        StringBuffer sqlBuffer = new StringBuffer();
        for (int i = 0; i < sqlA.length; i++) {
            if (!sqlA[i].trim().startsWith("--")) {
                sqlBuffer.append(sqlA[i].trim() + "\n");
            }
        }
        return sqlBuffer.toString();
    }


    public String createSQL(String sql) {
        String isSql = new String();

        if (sql.trim().toLowerCase().startsWith(";")) {

            sql = sql.trim().substring(1);

        }
        if (sql.trim().toLowerCase().endsWith(";")) {

            sql = sql.trim().substring(0, sql.length() - 1);
        }
        if (sql.startsWith("@")) {
            needToRow = true;
            setSqlCname(sql);

            isSql = "SELECT '" + tDot + "' || NAME" + " FROM SYSIBM.SYSCOLUMNS"
                    + " WHERE TBNAME = '" + tName + "'" + " AND TBCREATOR = '"
                    + DVPropMain.DB_SERVER_SCHEMA.get(key) + "'"
                    + " ORDER BY COLNO";

        } else if (hasMaxRecord) {
            isSql = setMaxSQLSentence(sql);
        } else {
            isSql = sql;
        }
        return isSql;
    }

    public String setMaxSQLSentence(String sql) {
        return sql;
    }


    public int getMaxRecordFieldNumber() {
        int maxNumber = 0;
        try {
            maxNumber = Integer.parseInt(qesb.getMaxRecoder());
            if (maxNumber == 0) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
        return maxNumber;
    }


    public String[] createBatchUpdateSQL(String[] sql) {
        String[] isSql = sql;

        for (int i = 0; i < isSql.length; i++) {
            isSql[i] = parseSQL(isSql[i]).trim();
        }

        return isSql;
    }

    public String[] createBatchSQL(String[] sql) {
        String[] isSql = sql;
        cleanBatchSql();
        int num = 0;

        for (int i = 0; i < isSql.length; i++) {
            if (!isSql[i].equals("")) {
                setBatchSQL(String.valueOf(num), isSql[i]);
                num++;
                isSql[i] = parseSQL(createSQL(isSql[i])).trim();
            }
        }
        return isSql;
    }

    public String tName;
    public String tDot;
    public boolean needToRow = false;
    boolean isc;

    public void setSqlCname(String sql) {

        String ss = sql.substring(1);

        String[] pp = ss.split(" ");
        if (pp.length == 1) {
            tName = pp[0];
        } else {
            tName = pp[0];
            tDot = pp[1];
        }

    }

    public String setVectorCol(Vector input) {

        String nSql = "";

        for (int i = 0; i < input.size(); i++) {
            nSql = nSql + input.get(i).toString();
        }
        nSql = nSql.replace('[', ' ');
        nSql = nSql.replace(']', ' ');

        return nSql.trim().substring(1);

    }

    public boolean isBatchSQL(String sql) {

        if (sql.trim().toLowerCase().startsWith(";")) {

            sql = sql.trim().substring(1);

        }
        if (sql.trim().toLowerCase().endsWith(";")) {

            sql = sql.trim().substring(0, sql.length() - 1);
        }
        String[] batchSQL = sql.split(IS_BATCH_SQL_SYMBOL);

        return batchSQL.length > 1 ? true : false;
    }
}
