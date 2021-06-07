package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;

import java.util.List;

public interface TravelGroupService {
    void add(TravelGroup travelGroup, Integer[] travelItemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    TravelGroup findById(Integer id);

    List<Integer> findTravelItemByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup, Integer[] travelItemIds);

    List<TravelGroup> findAll();
}
