package com.xq.live.dao;

import com.xq.live.model.SoWriteOff;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.SoWriteOffOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoWriteOffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SoWriteOff record);

    int insertSelective(SoWriteOff record);

    SoWriteOff selectByPrimaryKey(Long id);

    SoWriteOff selectByCouponId(Long couponId);

    int updateByPrimaryKeySelective(SoWriteOff record);

    int updateByPrimaryKey(SoWriteOff record);

    int updateByShopId(SoWriteOffInVo soWriteOffInVo);

    List<SoWriteOffOut> list(SoWriteOffInVo inVo);

    int listTotal(SoWriteOffInVo inVo);

    List<SoWriteOffOut> total(SoWriteOffInVo inVo);

    Integer selectActShopCount(Long shopId);

    Integer canGetAgio(SoInVo inVo);

    List<SoWriteOffOut> sotype(SoWriteOffInVo inVo);
}
