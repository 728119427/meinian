package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;


    @PostMapping("/submit")
    public Result submitOrder(@RequestBody Map map) throws Exception {
        //校对验证码
        String telephone = (String) map.get("telephone");
        String validateCode= (String) map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if(codeInRedis==null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //添加预约类型
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        Result result=orderService.order(map);
        return result;
    }

    @PostMapping("/findById")
    public Result findById(@RequestParam("id")  Integer id){
        Map map=null;
        try {
            map=orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
