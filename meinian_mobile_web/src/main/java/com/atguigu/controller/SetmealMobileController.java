package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.TravelSetMealService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    private TravelSetMealService travelSetMealService;

    @PostMapping("/getSetmeal")
    public Result findAll(){
        try {
            List<Setmeal> setmealList=travelSetMealService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }


    @PostMapping("/findById")
    public Result findById(@RequestParam("id") Integer id){
        try {
            Setmeal setmeal=travelSetMealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
