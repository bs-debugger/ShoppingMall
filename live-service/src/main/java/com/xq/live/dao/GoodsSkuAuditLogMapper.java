package com.xq.live.dao;

import com.xq.live.model.GoodsSkuAuditLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsSkuAuditLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSkuAuditLog record);

    int insertSelective(GoodsSkuAuditLog record);

    GoodsSkuAuditLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSkuAuditLog record);

    int updateByPrimaryKey(GoodsSkuAuditLog record);

    int  batchInsert(@Param("list") List<GoodsSkuAuditLog> list);
}