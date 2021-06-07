package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.POIUtils;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        //读取excel文件
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettings = new ArrayList<>();
            for (String[] strings : list) {
                //遍历每一行的数据创建预约对象，添加到集合
                System.out.println(Arrays.toString(strings));
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                orderSettings.add(orderSetting);
            }
            //调用service保存到数据库
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }


    }

    @GetMapping("getOrderSettingByMonth")
    public Result getOrderSettingByMonth(@RequestParam("date") String date){
        try {
            List<Map> list=orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }

    @PostMapping("/editNumberByDate")
    public Result  editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
