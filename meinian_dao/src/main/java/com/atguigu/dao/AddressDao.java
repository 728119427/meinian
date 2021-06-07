package com.atguigu.dao;

import com.atguigu.pojo.Address;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressDao {
    List<Address> findAllMaps();

    List<Address> findPage(@Param("query") String queryString);

    void addAddress(Address address);

    void deleteById(@Param("id") Integer id);
}
