package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {
    /**
     * 添加item
     */
    void add(TravelItem travelItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();
}
