package com.atguigu.dao;

import com.atguigu.pojo.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberDao {
    Member findByTelephone(@Param("telephone") String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(@Param("date") String lastDay);


    Integer findNewRegexToday(String today);

    Integer findTotalMembers();

    Integer findNewRegexAfter(String weekMonday);
}
