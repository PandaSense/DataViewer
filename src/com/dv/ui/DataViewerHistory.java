/*
 * DataViewerHistory.java  2/6/13 1:04 PM
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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

import com.dv.prop.DVPropMain;
import com.dv.ui.component.DVNomalSQLPane;
import com.dv.ui.datepicker.JDatePicker;
import com.dv.util.FileIO;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * @author Nick
 */
public class DataViewerHistory extends JFrame implements ActionListener {

    private RTextScrollPane logAreaScrollPane1;
    private Gutter gutter;
    private static final String MSG = "com.dv.ui.DataViewerToolBar";
    private static final ResourceBundle mainViewResource = ResourceBundle.getBundle(MSG);
    private DVNomalSQLPane logViewPane = new DVNomalSQLPane(this);
    private String currentDvLogFilePath = DVPropMain.DV_LOG_FOLDER + "INFO/INFOR_DV";
    private SimpleDateFormat dateformatDetail = new SimpleDateFormat("yyyy-MM-dd");
    private String currentDate;
    private String logFileName;


    public DataViewerHistory(java.awt.Frame parent) {

        logAreaScrollPane1 = new RTextScrollPane(logViewPane, true);
        gutter = logAreaScrollPane1.getGutter();
        gutter.setBackground(new Color(Integer.parseInt("-329480")));
        gutter.setLineNumberFont(new Font(DVPropMain.DV_SQL_FONT_NAME, Font.PLAIN, Integer.parseInt(DVPropMain.DV_SQL_FONT_SIZE)));
        gutter.setForeground(new Color(Integer.parseInt("-12566464")));

        initComponents();
        setTextArea();
        buildInterface();

        setTitle(DVPropMain.DV_NAME + "-History Log View");


        loadHistoryLogFile(currentDvLogFilePath);
        Image icon = Toolkit.getDefaultToolkit().getImage(DataViewerHistory.class.getResource(mainViewResource.getString("history")));
        setIconImage(icon);
        Point oh = parent.getLocationOnScreen();
        setLocation((int) oh.getX() + parent.getWidth() / 2 - getWidth() / 2, (int) oh.getY() + parent.getHeight() / 2 - getHeight() / 2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void setTextArea() {
        historyTextArea.setLayout(new BorderLayout());
        historyTextArea.add(logAreaScrollPane1, BorderLayout.CENTER);
    }

    public void buildInterface() {
        viewButton.addActionListener(this);
        historyDatePicker.setEditable(false);
        historyDatePicker.setLocale(Locale.ENGLISH);
        try {
            historyDatePicker.setSelectedDate(new Date());
            currentDate = getDateString(historyDatePicker.getSelectedDate());
        } catch (Exception e) {

        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewButton) {
            if (getDateString(historyDatePicker.getSelectedDate()).equals(currentDate)) {
                if (sqlResultCheckBox.isSelected()) {
                    loadHistoryLogFile(buildErrorLogFilePath(getDateString(historyDatePicker.getSelectedDate())));
                } else {
                    loadHistoryLogFile(currentDvLogFilePath);
                }
            } else {
                if (sqlResultCheckBox.isSelected()) {
                    logFileName = buildErrorLogFilePath(getDateString(historyDatePicker.getSelectedDate()));
                } else {
                    logFileName = buildLogFilePath(getDateString(historyDatePicker.getSelectedDate()));
                }
                loadHistoryLogFile(logFileName);
            }
        }
    }

    public String getDateString(Date date) {
        String dateString = dateformatDetail.format(date);
        return dateString;
    }

    public void loadHistoryLogFile(String fileFullPath) {
        logViewPane.setText(FileIO.read(fileFullPath));
        setTitle(DVPropMain.DV_NAME + "-History Log View" + "-" + fileFullPath);
    }

    public String buildLogFilePath(String dateString) {

        return DVPropMain.DV_LOG_FOLDER + "INFO/INFOR_DV" + dateString.trim() + ".log";
    }

    public String buildErrorLogFilePath(String dateString) {

        return DVPropMain.DV_HISTORY_RESULT_FOLDER + "SQL_HISTORY_" + dateString.trim() + ".dvh";
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        label1 = new JLabel();
        historyDatePicker = new JDatePicker(JDatePicker.STYLE_CN_DATE1);
        viewButton = new JButton();
        sqlResultCheckBox = new JCheckBox();
        historyTextArea = new JPanel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder(new EtchedBorder(), "Select DataViewer Log :"));

            //---- label1 ----
            label1.setText("Select Date :");

            //---- historyDatePicker ----
            historyDatePicker.setBorder(null);

            //---- viewButton ----
            viewButton.setText("View Log");

            //---- sqlResultCheckBox ----
            sqlResultCheckBox.setText("SQL Result History");

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(historyDatePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addGap(26, 26, 26)
                                    .addComponent(viewButton, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                    .addGap(37, 37, 37)
                                    .addComponent(sqlResultCheckBox, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(275, Short.MAX_VALUE))
            );
            panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(historyDatePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(viewButton)
                                            .addComponent(sqlResultCheckBox)
                                            .addComponent(label1))
                                    .addContainerGap(22, Short.MAX_VALUE))
            );
        }

        //======== historyTextArea ========
        {

            GroupLayout historyTextAreaLayout = new GroupLayout(historyTextArea);
            historyTextArea.setLayout(historyTextAreaLayout);
            historyTextAreaLayout.setHorizontalGroup(
                    historyTextAreaLayout.createParallelGroup()
                            .addGap(0, 849, Short.MAX_VALUE)
            );
            historyTextAreaLayout.setVerticalGroup(
                    historyTextAreaLayout.createParallelGroup()
                            .addGap(0, 533, Short.MAX_VALUE)
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(historyTextArea, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(historyTextArea, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel label1;
    private JDatePicker historyDatePicker;
    private JButton viewButton;
    private JCheckBox sqlResultCheckBox;
    private JPanel historyTextArea;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
