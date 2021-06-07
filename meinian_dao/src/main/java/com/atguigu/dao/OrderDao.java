package com.atguigu.dao;

import com.atguigu.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDao {
    List<Order> findByCriteria(Order criteria);

    void add(Order order);

    Map findById(Integer id);

    Integer findNewOrderToday(String today);

    Integer findVisitNumberToday(String today);

    Integer findNewVisitAfter(String firstDay);

    Integer findNewOrderBetween(@Param("begin") String begin, @Param("end") String end);
}
