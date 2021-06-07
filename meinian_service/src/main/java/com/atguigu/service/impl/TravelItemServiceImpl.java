package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TravelItemServiceImpl implements TravelItemService {
    @Autowired
    private TravelItemDao travelItemDao;

    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    /**
     * 分页查找
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<TravelItem> travelItemList = travelItemDao.findPage(queryPageBean.getQueryString());
        PageInfo<TravelItem> pageInfo = new PageInfo<>(travelItemList);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 删除自由行
     * @param id
     */
    @Override
    public void delete(Integer id) {
        long count=travelItemDao.findCountByTravelItemId(id);
        System.out.println(count);
        if(count>0){
            throw new RuntimeException("无法删除！");
        }
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem findById(Integer id) {
        return travelItemDao.findById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }
}
