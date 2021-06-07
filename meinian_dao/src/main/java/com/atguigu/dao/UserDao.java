package com.atguigu.dao;

import com.atguigu.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    User findByUsername(@Param("username") String username);
}
