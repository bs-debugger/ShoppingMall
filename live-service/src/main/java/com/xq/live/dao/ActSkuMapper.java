package com.xq.live.dao;

import com.xq.live.model.ActSku;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.out.ActSkuOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActSkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActSkuInVo record);

    int insertSelective(ActSkuInVo record);

    ActSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActSkuInVo record);

    int updateByPrimaryKey(ActSkuInVo record);

    List<ActSkuOut> listForNewAct(ActSkuInVo inVo);

    int listTotal(ActSkuInVo inVo);

    ActSkuOut findByInVo(ActSkuInVo inVo);

    int countByActId(Long actId);

    int updateLuoXuan(List<ActSkuOut> record);

    List<ActSkuOut> listActSkuOut(ActSkuInVo record);

}
