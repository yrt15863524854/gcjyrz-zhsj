package com.zhsj.business.excel.domain;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @className: ExcelWriteTest
 * @description: TODO
 * @author: yrt
 * date: 2022/5/3 22:07
 * version 1.0
 **/
public class ExcelWriteTest {
    @Test
    public void testWrite03() throws IOException {
        //1、创建工作瀑
        Workbook workbook = new HSSFWorkbook();
        //2、创建工作表
        Sheet sheet1 = workbook.createSheet("sheet1");
        //3、创建行
        Row row1 = sheet1.createRow(0);
        //4、创建列
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("sheet1第一个行第一列");//(1,1)
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue("sheet2第一行第二列");

        //sheet2
        Sheet sheet2 = workbook.createSheet("sheet2");
        Row row = sheet2.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("sheet2第一行第一列");

        FileOutputStream fileOutputStream = new FileOutputStream("D:/zhsj/uploadPath" + "1.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }
    @Test
    public void testWrite07() throws IOException {
        //1、创建工作瀑
        Workbook workbook = new XSSFWorkbook();
        //2、创建工作表
        Sheet sheet1 = workbook.createSheet("sheet1");
        //3、创建行
        Row row1 = sheet1.createRow(0);
        //4、创建列
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("sheet1第一个行第一列");//(1,1)
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue("sheet2第一行第二列");

        //sheet2
        Sheet sheet2 = workbook.createSheet("sheet2");
        Row row = sheet2.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("sheet2第一行第一列");

        FileOutputStream fileOutputStream = new FileOutputStream("D:/zhsj/uploadPath" + "2.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    @Test
    public void testWrite07S() throws IOException {
        //1、创建工作瀑,会生成临时文件速度快
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        //2、创建工作表
        Sheet sheet1 = workbook.createSheet("sheet1");
        //3、创建行
        Row row1 = sheet1.createRow(0);
        //4、创建列
        Cell cell1 = row1.createCell(0);
        cell1.setCellValue("sheet1第一个行第一列");//(1,1)
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue("sheet2第一行第二列");

        //sheet2
        Sheet sheet2 = workbook.createSheet("sheet2");
        Row row = sheet2.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("sheet2第一行第一列");

        FileOutputStream fileOutputStream = new FileOutputStream("D:/zhsj/uploadPath" + "2.xlsx");
        workbook.dispose();
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }
}
