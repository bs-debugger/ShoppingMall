package com.xq.live.dao;

import com.xq.live.model.VipGradeConfig;

public interface VipGradeConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(VipGradeConfig record);

    int insertSelective(VipGradeConfig record);

    VipGradeConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VipGradeConfig record);

    int updateByPrimaryKey(VipGradeConfig record);

    /**
     * 通过会员类型和等级查询会员等级配置
     * @param vipGradeConfig
     * @return
     */
    VipGradeConfig selectByTypeAndGrade(VipGradeConfig vipGradeConfig);
}
