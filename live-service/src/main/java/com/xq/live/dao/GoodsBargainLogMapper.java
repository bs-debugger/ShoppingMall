package com.xq.live.dao;

import com.xq.live.model.GoodsBargainLog;
import com.xq.live.vo.in.GoodsBargainLogInVo;

import java.util.List;

public interface GoodsBargainLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsBargainLog record);

    int insertSelective(GoodsBargainLog record);

    GoodsBargainLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsBargainLog record);

    int updateByPrimaryKey(GoodsBargainLog record);



    /**
     * 查询用户是否帮忙砍过菜
     * @param inVo
     * @return
     */
    GoodsBargainLog getByShiro(GoodsBargainLogInVo inVo);

    /**
     * 按小组查询列表
     * @param inVo
     * @return
     */
    List<GoodsBargainLog> selectByGroup(GoodsBargainLogInVo inVo);

    /**
     * 按发起人查询团id和商品id
     * @param inVo
     * @return
     */
    List<GoodsBargainLog> selectByParentId(GoodsBargainLogInVo inVo);


    /**
     * 插入发起人的第一条记录
     * @param inVo
     * @return
     */
    Integer insertForParent(GoodsBargainLogInVo inVo);

    /**
     * 修改删除状态
     * @param record
     * @return
     */
    Integer updateIsdelete(GoodsBargainLog record);
}