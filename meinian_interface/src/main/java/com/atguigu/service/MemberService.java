package com.atguigu.service;

import com.atguigu.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberService {

    Member findByTelephone(@Param("telephone") String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> dateList);
}
