package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/check")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        String telephone= (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //校对验证码
        if(codeInRedis==null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //校验成功，判断是不是会员
        Member member = memberService.findByTelephone(telephone);
        if(member==null){
            //不是会员，自动注册
            member=new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        //登录成功，写入cookie
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setMaxAge(60*60*24*30);
        response.addCookie(cookie);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);

    }

}
