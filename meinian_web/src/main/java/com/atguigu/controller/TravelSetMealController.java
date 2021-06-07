package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.TravelSetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import utils.QiniuUtils;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class TravelSetMealController {

    @Reference
    private TravelSetMealService travelSetMealService;
    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        try {
            String originalFilename = imgFile.getOriginalFilename();
            String uploadSaveName=UUID.randomUUID().toString().replace("-","")+"_"+originalFilename;
            //添加到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),uploadSaveName);
            //添加到redis，清理垃圾图片
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,uploadSaveName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,uploadSaveName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);

    }

    @PostMapping("add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] travelgroupIds){
        try {
            travelSetMealService.add(setmeal,travelgroupIds);
        } catch (Exception e) {
            //添加失败
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        //添加成功
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页
     * @return
     */
    @PostMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=travelSetMealService.findPage(queryPageBean);
        return pageResult;
    }
}
