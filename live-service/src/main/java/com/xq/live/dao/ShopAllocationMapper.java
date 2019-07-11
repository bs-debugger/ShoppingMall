package com.xq.live.dao;

import com.xq.live.model.ShopAllocation;
import com.xq.live.vo.in.ShopAllocationInVo;
import com.xq.live.vo.out.ShopAllocationOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopAllocationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopAllocation record);

    int insertSelective(ShopAllocation record);
    //查询单条数据
    ShopAllocationOut selectByPrimaryKey(ShopAllocationInVo shopAllocationInVo);

    int updateByPrimaryKeySelective(ShopAllocation record);

    int updateByPrimaryKey(ShopAllocation record);

    //查询消息列表
    List<ShopAllocationOut> selectByCan(ShopAllocationInVo shopAllocationInVo);

    //修改信息
    int updateByInfo(ShopAllocationInVo shopAllocationInVo);

    //添加信息
    int insertByID(ShopAllocationInVo shopAllocationInVo);

}