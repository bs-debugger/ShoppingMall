package com.xq.live.dao;

import com.xq.live.model.GoodsGoldLog;
import com.xq.live.vo.in.GoodsGoldLogInVo;

import java.util.List;

public interface GoodsGoldLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsGoldLog record);

    int insertSelective(GoodsGoldLog record);

    GoodsGoldLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsGoldLog record);

    int updateByPrimaryKey(GoodsGoldLog record);

    /**
     * 修改用户金币到账状态
     * @param inVo
     * @return
     */
    Integer changeState(List<GoodsGoldLog> inVo);

    /**
     * 查询发起人和帮砍人
     * @param inVo
     * @return
     */
    List<GoodsGoldLog> teamForParent(GoodsGoldLogInVo inVo);

    /**
     * 根据邀请人和sku查询
     * @param inVo
     * @return
     */
    GoodsGoldLog  selectByParentId(GoodsGoldLogInVo inVo);

    /**
     * 按小组查询列表
     * @param inVo
     * @return
     */
    List<GoodsGoldLog> selectByGroup(GoodsGoldLogInVo inVo);

    /**
     * 查询用户发起过的次数(用来当作小组编号)
     * @param inVo
     * @return
     */
    Integer goldTotal(GoodsGoldLogInVo inVo);

    /**
     * 插入发起人的第一条记录
     * @param inVo
     * @return
     */
    Integer insertForParent(GoodsGoldLogInVo inVo);


    /**
     * 修改删除状态
     * @param record
     * @return
     */
    Integer updateIsdelete(GoodsGoldLog record);
}