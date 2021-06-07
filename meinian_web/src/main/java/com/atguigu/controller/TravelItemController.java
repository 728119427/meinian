package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/travelItem")
public class TravelItemController {

    @Reference
    private TravelItemService travelItemService;

    @PostMapping("/add")
    public Result add(@RequestBody TravelItem travelItem){

        try {
            travelItemService.add(travelItem);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_TRAVELITEM_SUCCESS);
    }

    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return travelItemService.findPage(queryPageBean);
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        try {
            travelItemService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        }catch (RuntimeException e) {
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    @GetMapping("/findById")
    public Result findById(Integer id){
        TravelItem travelItem=travelItemService.findById(id);
        return new Result(true,"拉取数据成功!",travelItem);
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody TravelItem travelItem){
        travelItemService.edit(travelItem);
        return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);

    }

    @GetMapping("/findAll")
    public Result findAll(){
        List<TravelItem> travelItemList=travelItemService.findAll();
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelItemList);
    }
}
