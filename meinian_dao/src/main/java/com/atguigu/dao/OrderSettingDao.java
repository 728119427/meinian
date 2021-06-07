package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderSettingDao {
    void add(OrderSetting orderSetting);

    Long findCountByOrderDate(@Param("orderDate") Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(@Param("dateBegin") String dateBegin, @Param("dateEnd") String dateEnd);

    OrderSetting findByOrderDate(@Param("orderDate") Date date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
