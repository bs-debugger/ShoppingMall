package com.xq.live.dao;

import com.xq.live.model.BargainLog;
import com.xq.live.vo.in.BargainLogInVo;

import java.util.List;

public interface BargainLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BargainLog record);

    int insertSelective(BargainLog record);

    BargainLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BargainLog record);

    int updateByPrimaryKey(BargainLog record);


    /**
     * 查询用户是否帮忙砍过菜
     * @param inVo
     * @return
     */
    BargainLog getByshiro(BargainLogInVo inVo);

    /**
     * 按小组查询列表
     * @param inVo
     * @return
     */
    List<BargainLog> selectByGroup(BargainLogInVo inVo);

    /**
     * 插入发起人的第一条记录
     * @param inVo
     * @return
     */
    Integer insertforparent(BargainLogInVo inVo);
}