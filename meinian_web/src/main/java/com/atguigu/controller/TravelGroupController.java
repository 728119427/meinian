package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {

    @Reference
    private TravelGroupService travelGroupService;


    @PostMapping("/add")
    public Result add(@RequestBody TravelGroup travelGroup,Integer[] travelItemIds){
        travelGroupService.add(travelGroup,travelItemIds);
        System.out.println("hello");
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);

    }

    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=travelGroupService.findPage(queryPageBean);
        return pageResult;
    }

    @GetMapping("/findById")
    public Result findById(Integer id){
        TravelGroup travelGroup=travelGroupService.findById(id);
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
    }

    @GetMapping("findTravelItemByTravelGroupId")
    public Result findTravelItemByTravelGroupId(Integer id){
        List<Integer> ids=travelGroupService.findTravelItemByTravelGroupId(id);
        return new Result(true,"查询跟团游得自由行成功！",ids);
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody TravelGroup travelGroup,@RequestParam("travelItemIds") Integer[] travelItemIds){
        travelGroupService.edit(travelGroup,travelItemIds);
        return new Result(true,MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
    }

    @GetMapping("findAll")
    public Result findAll(){
        List<TravelGroup> travelGroups=travelGroupService.findAll();
        return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroups);
    }


}
