package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass =MemberService.class )
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> dateList) {
        List<Integer> memberCount = new ArrayList<>();
        if(dateList!=null && dateList.size()>0){
            for (String date : dateList) {
                String lastDay = DateUtils.getLastDayOfMonth(date);
                Integer count=memberDao.findMemberCountByMonth(lastDay);
                memberCount.add(count);
            }
        }
        return memberCount;
    }
}
