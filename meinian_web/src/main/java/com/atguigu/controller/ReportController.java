package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import com.atguigu.service.TravelSetMealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    @Reference
    private TravelSetMealService travelSetMealService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        Map<String,Object> map = new HashMap<>();
        //获取日历对象
        Calendar calendar = Calendar.getInstance();
        //根据当前时间获取前12个月的日历
        calendar.add(Calendar.MONTH,-12);
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            //每次向后推一个月
            calendar.add(Calendar.MONTH,1);
            dateList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        map.put("months",dateList);
        //按月查询累计的会员注册数量
        List<Integer> memberCount=memberService.findMemberCountByMonth(dateList);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);

    }


    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        List<Map> list=travelSetMealService.findSetmealCount();
        Map<String,Object> map = new HashMap<>();
        map.put("setmealCount",list);
        List<String> setmealNames= new ArrayList<>();
        for (Map setmealCount : list) {
                String name= (String) setmealCount.get("name");
                setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,map);

    }

    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String,Object> map= null;
        try {
            map = travelSetMealService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @GetMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        //获取报表数据
        Map<String, Object> result = travelSetMealService.getBusinessReportData();
        //取出返回结果数据，准备将报表数据写入到Excel文件中
        String reportDate = (String) result.get("reportDate");
        Integer todayNewMember = (Integer) result.get("todayNewMember");
        Integer totalMember = (Integer) result.get("totalMember");
        Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
        Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
        Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
        Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
        Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
        Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
        Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
        Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
        //获取模板excel文件的路径
        String realPath = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        try {
            //读取模板文件创建excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(realPath));
            XSSFSheet sheet = workbook.getSheetAt(0);

            //设置日期
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);

            //设置会员统计数据
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);
            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);

            //设置出游数据
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            //设置热门套餐数据
            int num= 12;
            for (Map map : hotSetmeal) {
                row = sheet.getRow(num++);
                row.getCell(4).setCellValue((String) map.get("name"));
                row.getCell(5).setCellValue((Long) map.get("setmeal_count"));
                row.getCell(6).setCellValue(((BigDecimal) map.get("proportion")).doubleValue());
            }
            //文件下载,设置下载的数据类型
            response.setContentType("application/vnd.ms-excel");
            //设置下载形式
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);

            //释放资源
            out.flush();
            out.close();
            workbook.close();

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
