package com.atguigu.dao;

import com.atguigu.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao {
    List<Permission> findPermissionByRoleId(Integer id);
}
