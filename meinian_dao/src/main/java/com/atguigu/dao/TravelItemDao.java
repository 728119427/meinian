package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelItemDao {
    void add(TravelItem travelItem);

    List<TravelItem> findPage(@Param("query") String queryString);

    Long findCountByTravelItemId(Integer id);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

    List<TravelItem> findTravelItemListByGroupId(Integer id);
}
