package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TravelSetMealDao {


    void add(Setmeal setmeal);

    void insert_travelGroup_setMeal(@Param("setmealId") Integer setMealIds, @Param("travelgroupId") Integer travelgroupIds);

    List<Setmeal> findPage(@Param("query") String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map> findSetmelCount();


    List<Map> findHotSetMeal();

}
