package com.xq.live.dao;

import com.xq.live.model.ActInfo;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.out.ActInfoOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActInfoMapper{

    int deleteByPrimaryKey(Long id);

    int insert(ActInfo record);

    int insertSelective(ActInfo record);

    ActInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActInfo record);

    int updateByPrimaryKey(ActInfo record);

    int listTotal(ActInfoInVo inVo);

    List<ActInfoOut> list(ActInfoInVo inVo);

    ActInfoOut findActInfoById(Long id);

    int listTotalForShop(ActInfoInVo inVo);

    List<ActInfoOut> listForShop(ActInfoInVo inVo);

    /*根据活动类型查看活动*/
    List<ActInfo> actTypeList(ActInfoInVo inVo);
}
