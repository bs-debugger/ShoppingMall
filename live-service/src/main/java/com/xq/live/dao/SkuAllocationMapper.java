package com.xq.live.dao;

import com.xq.live.model.SkuAllocation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuAllocationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkuAllocation record);

    int insertSelective(SkuAllocation record);

    SkuAllocation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SkuAllocation record);

    int updateByPrimaryKey(SkuAllocation record);

    List<SkuAllocation> list(SkuAllocation record);
}
