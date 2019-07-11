package com.xq.live.dao;

import com.xq.live.model.GoldLog;
import com.xq.live.vo.in.GoldLogInVo;

import java.util.List;

public interface GoldLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoldLog record);

    int insertSelective(GoldLog record);

    GoldLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoldLog record);

    int updateByPrimaryKey(GoldLog record);

    /**
     * 修改用户金币到账状态
     * @param inVo
     * @return
     */
    Integer changeState(List<GoldLog> inVo);

    /**
     * 查询发起人和帮砍人
     * @param inVo
     * @return
     */
    List<GoldLog> teamforparent(GoldLogInVo inVo);

    /**
     * 查询用户是否帮忙砍菜领过金币
     * @param inVo
     * @return
     */
    GoldLog getByshiro(GoldLogInVo inVo);

    /**
     * 按小组查询列表
     * @param inVo
     * @return
     */
    List<GoldLog> selectByGroup(GoldLogInVo inVo);

    /**
     * 查询用户发起过的次数(用来当作小组编号)
     * @param inVo
     * @return
     */
    Integer goldTotal(GoldLogInVo inVo);

    /**
     * 插入发起人的第一条记录
     * @param inVo
     * @return
     */
    Integer insertforparent(GoldLogInVo inVo);




}