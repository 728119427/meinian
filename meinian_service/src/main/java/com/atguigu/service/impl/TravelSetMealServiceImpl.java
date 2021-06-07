package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.TravelSetMealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.TravelSetMealService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;
import utils.DateUtils;

import java.util.*;


@Service(interfaceClass = TravelSetMealService.class)
@Transactional
public class TravelSetMealServiceImpl implements TravelSetMealService {

    @Autowired
    private TravelSetMealDao travelSetMealDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private JedisPool jedisPool;


    /**
     * 添加套餐
     * @param setmeal
     * @param travelgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] travelgroupIds) {
        //插入套餐
        travelSetMealDao.add(setmeal);
        //插入中间表信息
        if(travelgroupIds!=null && travelgroupIds.length>0){
            insert_travelGroup_setMeal(setmeal.getId(),travelgroupIds);
        }
        //插入图片名到redis，便于清理垃圾图片
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    /**
     * 分页
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        List<Setmeal> setmealList=travelSetMealDao.findPage(queryPageBean.getQueryString());
        PageInfo<Setmeal> setmealPageInfo = new PageInfo<>(setmealList);
        return new PageResult(setmealPageInfo.getTotal(),setmealPageInfo.getList());
    }

    @Override
    public List<Setmeal> findAll() {
        return travelSetMealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        return travelSetMealDao.findById(id);
    }

    @Override
    public List<Map> findSetmealCount() {
        return travelSetMealDao.findSetmelCount();
    }

    /**
     * 获取统计数据
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String,Object> map = new HashMap<>();
        try {
            //今天的日期
            String today = DateUtils.parseDate2String(new Date());
            //本周的周一日期
            String weekMonday= DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //本周的最后一天
            String weekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            //本月的第一天
            String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            //本月的最后一天
            String lastDayOfMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

            //今天新增会员数
            Integer todayNewMember=memberDao.findNewRegexToday(today);
            //总会员数
            Integer totalMember=memberDao.findTotalMembers();
            //本周新增会员数(>=本周的周一的日期)
            Integer thisWeekNewMember=memberDao.findNewRegexAfter(weekMonday);
            //本月新增会员数(>=本月的第一天的日期)
            Integer thisMonthNewMember=memberDao.findNewRegexAfter(firstDayOfMonth);
            //今日预约数
            Integer todayOrderNumber = orderDao.findNewOrderToday(today);
            // 今日已出游数
            Integer todayVisitsNumber = orderDao.findVisitNumberToday(today);
            // 本周出游数
            Integer thisWeekVisitsNumber = orderDao.findNewVisitAfter(weekMonday);
            //本月已出游数
            Integer thisMonthVisitsNumber = orderDao.findNewVisitAfter(firstDayOfMonth);
            //本周预约数(>=本周的周一的日期 <=本周的周日的日期)
            Integer thisWeekOrderNumber = orderDao.findNewOrderBetween(weekMonday,weekSunday);
            //本月预约数(>=每月的第一天的日期 <=每月的最后一天的日期)
            Integer thisMonthOrderNumber = orderDao.findNewOrderBetween(firstDayOfMonth,lastDayOfMonth);
            //热门套餐
            List<Map> hotSetmeal = travelSetMealDao.findHotSetMeal();

            //添加数据到map
            map.put("reportDate",today);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            map.put("hotSetmeal",hotSetmeal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 插入中间表
     * @param setMealIds
     * @param travelgroupIds
     */
    private void insert_travelGroup_setMeal(Integer setMealIds,Integer[] travelgroupIds) {
        for (Integer travelgroupId : travelgroupIds) {
            //循环插入
            travelSetMealDao.insert_travelGroup_setMeal(setMealIds,travelgroupId);
        }
    }
}
