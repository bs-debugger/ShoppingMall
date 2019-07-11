package com.xq.live.dao;

import com.xq.live.model.TopNotice;
import com.xq.live.vo.in.TopNoticeVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TopNotice record);

    int insertSelective(TopNotice record);

    TopNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TopNotice record);

    int updateByPrimaryKey(TopNotice record);

    /*查询公告*/
    List<TopNotice> selectBetweenTime(TopNoticeVo inVo);

}