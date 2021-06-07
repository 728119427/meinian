package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelGroupService.class)
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    private TravelGroupDao travelGroupDao;


    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        //添加抱团游
        travelGroupDao.add(travelGroup);
        //添加中间表
        insert_travelGroup_travelItem(travelGroup.getId(),travelItemIds);
    }

    /**
     * 分页
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<TravelGroup> travelGroupList=travelGroupDao.findPage(queryPageBean.getQueryString());
        PageInfo<TravelGroup> pageInfo = new PageInfo<>(travelGroupList, 5);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 根据id查询group
     * @param id
     * @return
     */
    @Override
    public TravelGroup findById(Integer id) {
        return travelGroupDao.findById(id);
    }

    /**
     * 根据groupId查询itemIds
     * @param id
     * @return
     */
    @Override
    public List<Integer> findTravelItemByTravelGroupId(Integer id) {
        return travelGroupDao.findTravelItemByTravelGroupId(id);
    }

    /**
     * 编辑travelGroup
     * @param travelGroup
     * @param travelItemIds
     */
    @Override
    public void edit(TravelGroup travelGroup, Integer[] travelItemIds) {
        //更新group
        travelGroupDao.edit(travelGroup);
        //删除旅游团对应的中间表数据
        travelGroupDao.deleteItemIdsByGroupId(travelGroup.getId());
        //重新插入旅游团中间表数据
        insert_travelGroup_travelItem(travelGroup.getId(),travelItemIds);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }

    /**
     * 插入中间表
     * @param travelGroupId
     * @param travelItemIds
     */
    public void insert_travelGroup_travelItem(Integer travelGroupId,Integer[] travelItemIds){
        if(travelItemIds!=null && travelItemIds.length>0){
            for (Integer travelItemId : travelItemIds) {
                travelGroupDao.insert_travelGroup_travelItem(travelGroupId,travelItemId);
            }
        }

    }
}
