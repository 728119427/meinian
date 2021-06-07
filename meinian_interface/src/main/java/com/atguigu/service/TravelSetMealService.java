package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface TravelSetMealService {
    void add(Setmeal setmeal, Integer[] travelgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map> findSetmealCount();

    Map<String, Object> getBusinessReportData();
}
