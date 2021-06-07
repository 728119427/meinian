package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> orderSettings) {
        //遍历添加
        for (OrderSetting orderSetting : orderSettings) {
            //根据日期查询之前是否已经添加过
            Long count=orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if(count>0){
                //更新预约数
                orderSettingDao.editNumberByOrderDate(orderSetting);
            }else {
                //没有则直接添加
                orderSettingDao.add(orderSetting);
            }
        }
    }

    /**
     * 获取预约数据
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String dateBegin=date+"-01";
        String dateEnd=date+"-31";
        //查询数据库信息
        List<OrderSetting> orderSettings=orderSettingDao.getOrderSettingByMonth(dateBegin,dateEnd);
        //封装为map
        List<Map> maps = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettings) {
            Map<String,Object> map = new HashMap();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            maps.add(map);
        }

        return maps;

    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //根据日期查询之前是否已经添加过
        Long count=orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count>0){
            //更新预约数
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //没有则直接添加
            orderSettingDao.add(orderSetting);
        }
    }
}
