package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AddressDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> findAllMaps() {
        return addressDao.findAllMaps();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<Address> addressList=addressDao.findPage(queryPageBean.getQueryString());
        PageInfo pageInfo = new PageInfo(addressList);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void addAddress(Address address) {
        addressDao.addAddress(address);
    }

    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }
}
