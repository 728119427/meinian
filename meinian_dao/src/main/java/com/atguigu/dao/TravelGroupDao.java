package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelGroupDao {

    public void add(TravelGroup travelGroup) ;

    public void insert_travelGroup_travelItem(@Param("groupId") Integer travelGroupId, @Param("itemId") Integer travelItemId);

    List<TravelGroup> findPage(@Param("query") String queryString);

    TravelGroup findById(Integer id);

    List<Integer> findTravelItemByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup);

    void deleteItemIdsByGroupId(Integer id);

    List<TravelGroup> findAll();

    List<TravelGroup> findTravelGroupListBySetmealId(Integer id);

}
