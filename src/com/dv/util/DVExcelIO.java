/*
 * DVExcelIO.java  2/6/13 1:04 PM
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

package com.dv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author xyma
 */
public class DVExcelIO {

    public static Vector<Vector> readExcelReturnArrayList(String fileName, String sheetName, int rowCount) {

        Vector<Vector> hm = new Vector<Vector>();

        File file = new File(fileName);
        FileInputStream in = null;


        try {

            in = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheet(sheetName);

            HSSFRow row = null;
            HSSFCell cell = null;

            for (int i = 0; i < rowCount; i = i + 3) {

                Vector cellList = new Vector();

                row = sheet.getRow((short) i);
                if (row != null) {
                    cell = row.getCell(0);
                    String cellString = cell.toString().replace(".0", " ").trim();
                    cellString = cellString.replace("\n", " ").trim();
                    cellList.add(0, cellString);
                }

                row = sheet.getRow((short) i + 1);
                if (row != null) {
                    cell = row.getCell(0);
                    String cellString = cell.toString().replace(".0", " ").trim();
                    cellString = cellString.replace("\n", " ").trim();
                    cellList.add(1, cellString);
                }

                row = sheet.getRow((short) i + 2);
                if (row != null) {
                    cell = row.getCell(0);
                    String cellString = cell.toString().replace(".0", " ").trim();
                    cellString = cellString.replace("\n", " ").trim();
                    cellList.add(2, cellString);
                }

                hm.addElement(cellList);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }

        return hm;

    }

    public static boolean exportIntoExcel(String fullExcelFileName, String sheetName, Vector cols, Vector rows) {
        boolean isExportFine = true;


        HSSFWorkbook hsswb = null;
        HSSFSheet hsssh = null;
        HSSFRow row = null;

        try {
            File excel = new File(fullExcelFileName);
            if (!excel.exists()) {
                hsswb = new HSSFWorkbook();
                hsssh = hsswb.createSheet(sheetName);
                hsssh.setDefaultRowHeight((short) 10);
                hsssh.setDefaultColumnWidth(20);

            } else {
                hsswb = new HSSFWorkbook(new FileInputStream(excel));
                hsssh = hsswb.createSheet(sheetName);
                hsssh.setDefaultRowHeight((short) 10);
                hsssh.setDefaultColumnWidth(20);
            }

            row = hsssh.createRow((short) 2);
            HSSFCellStyle style = hsswb.createCellStyle();
            style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);


            style.setBottomBorderColor(HSSFColor.BLACK.index);
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            style.setTopBorderColor(HSSFColor.BLACK.index);
            style.setRightBorderColor(HSSFColor.BLACK.index);


            for (int i = 0; i < cols.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(cols.get(i).toString());
                cell.setCellStyle(style);
            }

            HSSFCellStyle style1 = hsswb.createCellStyle();

            style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style1.setBorderTop(HSSFCellStyle.BORDER_THIN);


            style1.setBottomBorderColor(HSSFColor.BLACK.index);
            style1.setLeftBorderColor(HSSFColor.BLACK.index);
            style1.setTopBorderColor(HSSFColor.BLACK.index);
            style1.setRightBorderColor(HSSFColor.BLACK.index);

            for (int i = 3; i <= rows.size() + 2; i++) {
                row = hsssh.createRow((short) i);

                for (int j = 0; j < cols.size(); j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(((Vector) rows.elementAt(i - 3)).get(j).toString());
                    cell.setCellStyle(style1);
                }
            }

            FileOutputStream fOut = new FileOutputStream(excel);
            hsswb.write(fOut);
            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            DVLOG.setErrorLog(DVExcelIO.class.getName(), e);
            return false;
        } catch (IllegalArgumentException e) {
            DVLOG.setErrorLog(DVExcelIO.class.getName(), e);
            return false;
        } catch (Exception e) {
            DVLOG.setErrorLog(DVExcelIO.class.getName(), e);
            return false;
        }

        return isExportFine;
    }

    public static boolean exportBatchResultIntoExcel(String fullExcelFileName, String sheetName, HashMap<String, Vector> colsMap, HashMap<String, Vector> rowsMap) {
        HSSFWorkbook hsswb = null;
        HSSFSheet hsssh = null;
        HSSFRow row = null;
        Vector cols = new Vector();
        Vector rows = new Vector();

        try {
            File excel = new File(fullExcelFileName);
            if (!excel.exists()) {
                hsswb = new HSSFWorkbook();
                hsssh = hsswb.createSheet(sheetName);
                hsssh.setDefaultRowHeight((short) 10);
                hsssh.setDefaultColumnWidth(20);

            } else {
                hsswb = new HSSFWorkbook(new FileInputStream(excel));
                hsssh = hsswb.createSheet(sheetName);
                hsssh.setDefaultRowHeight((short) 10);
                hsssh.setDefaultColumnWidth(20);
            }


            int rowCount = 1;

            for (int k = 0; k < colsMap.size(); k++) {

                cols = colsMap.get(String.valueOf(k));
                rows = rowsMap.get(String.valueOf(k));

                rowCount = rowCount + 1;

                row = hsssh.createRow((short) (rowCount));
                HSSFCellStyle style = hsswb.createCellStyle();
                style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style.setBorderTop(HSSFCellStyle.BORDER_THIN);


                style.setBottomBorderColor(HSSFColor.BLACK.index);
                style.setLeftBorderColor(HSSFColor.BLACK.index);
                style.setTopBorderColor(HSSFColor.BLACK.index);
                style.setRightBorderColor(HSSFColor.BLACK.index);


                for (int i = 0; i < cols.size(); i++) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(cols.get(i).toString());
                    cell.setCellStyle(style);
                }

                HSSFCellStyle style1 = hsswb.createCellStyle();

                style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
                style1.setBorderTop(HSSFCellStyle.BORDER_THIN);


                style1.setBottomBorderColor(HSSFColor.BLACK.index);
                style1.setLeftBorderColor(HSSFColor.BLACK.index);
                style1.setTopBorderColor(HSSFColor.BLACK.index);
                style1.setRightBorderColor(HSSFColor.BLACK.index);

                int loop = rowCount;

                for (int i = 1 + loop; i <= rows.size() + loop; i++) {
                    row = hsssh.createRow((short) i);
                    for (int j = 0; j < cols.size(); j++) {
                        HSSFCell cell = row.createCell(j);
                        cell.setCellValue(((Vector) rows.elementAt(i - (1 + loop))).get(j).toString());
                        cell.setCellStyle(style1);
                    }
                    rowCount++;
                }
            }
            FileOutputStream fOut = new FileOutputStream(excel);
            hsswb.write(fOut);
            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            DVLOG.setErrorLog(DVExcelIO.class.getName(), e);
            return false;
        } catch (IllegalArgumentException e) {
            DVLOG.setErrorLog(DVExcelIO.class.getName(), e);
            return false;
        } catch (Exception e) {
            DVLOG.setErrorLog(DVExcelIO.class.getName(), e);
            return false;
        }
        return true;
    }

    public static Vector setExcelBHTIMFormat(String fullExcelFileName, String sheetName, int rowNumbers) {

        File file = new File(fullExcelFileName);
        FileInputStream in = null;
        Vector cols = new Vector();

        try {

            in = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(in);
            HSSFSheet sheet = workbook.getSheet(sheetName);

            HSSFRow row = null;
            HSSFCell cell = null;

            for (int i = 2; i < rowNumbers; i++) {

                row = sheet.getRow(i);

                cell = row.getCell(4);//9 for cty

                String ppp = cell.toString().trim();

                cell = row.getCell(9);

                String fff = cell.toString().trim();

                if (!ppp.equals("")) {

                    String contents = "Verify from FMS side for " + ppp + "(" + fff + ")";

//                    cols.addElement(contents);


                    row.getCell(16).setCellValue(contents);

                } else {
                    return null;
                }

            }


            FileOutputStream fOut = new FileOutputStream(file);
            workbook.write(fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception eee) {

        }

        return cols;
    }

//    public static void main(String args[]) {
//
//        Vector<Vector> cols = new Vector<Vector>();
//
//        cols = DVExcelIO.readExcelReturnArrayList("C:/D/DataViewer/sql/DB2_SQL_ERROR_CODE.xls", "DB2_ERROR_EN", 1542);
//
//        Vector detail;
//        
//        StringBuffer contents=new StringBuffer();
//        
//        for(int i=0;i<cols.size();i++){
//        	
//        	contents.append(cols.get(i).get(0).toString());
//        	contents.append("|");
//        	contents.append(cols.get(i).get(1).toString());
//        	contents.append("|");
//        	contents.append(cols.get(i).get(2).toString());
//        	contents.append("\n");
//        }
//        
//        DVFileUtil.saveSqlFile("C:/D/DataViewer/sql/DB2_SQL_ERROR_CODE_EN.codes", contents.toString().trim());
//        
//    }
}

