package com.atguigu.dao;

import com.atguigu.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleDao {
    List<Role> findRoleByUserId(Integer id);
}
