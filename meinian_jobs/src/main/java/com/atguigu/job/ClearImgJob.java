package com.atguigu.job;

import com.atguigu.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import utils.QiniuUtils;

import java.util.Set;

@Component
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //开始清理图片
        //获取集合的差值
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_DB_RESOURCES, RedisConstant.SETMEAL_PIC_RESOURCES);
        for (String imgName : sdiff) {
            System.out.println("删除的图片名称是"+imgName);
            //从七牛中删除
            QiniuUtils.deleteFileFromQiniu(imgName);
            //从redis删除
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);




        }
    }
}
