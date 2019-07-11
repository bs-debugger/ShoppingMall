package com.xq.live.dao;

import com.xq.live.model.ServiceAmount;
import com.xq.live.vo.in.ServiceAmountInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceAmountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServiceAmountInVo record);

    int insertSelective(ServiceAmountInVo record);

    ServiceAmount selectByPrimaryKey(Long id);

    List<ServiceAmount> selectByShopId(Long shopId);

    int updateByPrimaryKeySelective(ServiceAmountInVo record);

    int updateByPrimaryKey(ServiceAmountInVo record);

    List<ServiceAmount> allByShopId(ServiceAmountInVo record);
}
