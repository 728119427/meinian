package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utils.DateUtils;

import javax.xml.stream.util.XMLEventAllocator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 下订单
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Result order(Map map) throws Exception {
        //先判断是否有预约设置，即有没有开团
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting=orderSettingDao.findByOrderDate(date);
        if(orderSetting==null){
            //没有开团
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }else{
            //确认该团是否还有空位
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            if(reservations>=number){
                //预约已满
                return new Result(false,MessageConstant.ORDER_FULL);
            }
        }
        //是否是会员，不是则自动注册并预约
        String telephone = (String) map.get("telephone");
        Member member=memberDao.findByTelephone(telephone);
        if(member!=null){
            //是会员
            //是否重复预约
            String setmealId = (String) map.get("setmealId");
            Integer memberId = member.getId();
            Order criteria = new Order(memberId, date, null, null, Integer.parseInt(setmealId));
            List<Order> orders=orderDao.findByCriteria(criteria);
            if(orders!=null && orders.size()>0){
                //说明已经预约过
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //不是会员,注册会员
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //添加订单，
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        order.setOrderType((String) map.get("orderType"));
        orderDao.add(order);
        //修改预约数
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);

    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) {
        Map map= orderDao.findById(id);
        if(map!=null){
            Date date= (Date) map.get("orderDate");
            try {
                map.put("orderDate",DateUtils.parseDate2String(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
