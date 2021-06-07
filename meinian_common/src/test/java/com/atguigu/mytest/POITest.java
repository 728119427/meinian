package com.atguigu.mytest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {

    @Test
    public void test1() throws IOException {
        int type=0;
        XSSFWorkbook workbook = new XSSFWorkbook("f://ordersetting.xlsx");
        //获取sheet
        XSSFSheet sheet = workbook.getSheetAt(0);
        //遍历获得行对象
        for (Row cells : sheet) {
            //遍历获得列对象
            for (Cell cell : cells) {
                String stringCellValue = cell.getStringCellValue();
                System.out.println(stringCellValue);
            }
        }

    }

    @Test
    public void write() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("aaa");
        //创建第0行
        XSSFRow row = sheet.createRow(0);
        //创建单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        //创建第1行
        XSSFRow row1 = sheet.createRow(1);
        //创建单元格
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("10");

        //创建第2行
        XSSFRow row2 = sheet.createRow(2);
        //创建单元格
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小陈");
        row2.createCell(2).setCellValue("18");
        //通过流输出
        FileOutputStream fileOutputStream = new FileOutputStream("f://guigu.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        workbook.close();


    }


}
