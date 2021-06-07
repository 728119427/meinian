package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import utils.ValidateCodeUtils;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("send4Order")
    public Result send4Order(@RequestParam("telephone") String telephone){
        try {
            //生成验证码
            Integer integer = ValidateCodeUtils.generateValidateCode(4);
            System.out.println("生成的验证码是:"+integer);
            //保存到redis中
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,integer.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }


    @PostMapping("send4Login")
    public Result send4Login(@RequestParam("telephone") String telephone){
        try {
            //生成验证码
            Integer integer = ValidateCodeUtils.generateValidateCode(4);
            System.out.println("生成的验证码是:"+integer);
            //保存到redis中
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,integer.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }

}
