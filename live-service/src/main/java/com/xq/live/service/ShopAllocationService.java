package com.xq.live.service;

import com.xq.live.vo.in.ShopAllocationInVo;
import com.xq.live.vo.out.ShopAllocationOut;

import java.util.List;

/**
 * Created by ss on 2018/5/25.
 */
public interface ShopAllocationService {

    /**
     * 通过id,shopid查看单条数据
     * @param shopAllocationInVo
     * @return
     */
    ShopAllocationOut admin(ShopAllocationInVo shopAllocationInVo);


    /**
     * 通过id,shopid或者operatorid查看多条数据
     * @param shopAllocationInVo
     * @return
     */
    List<ShopAllocationOut> listAll(ShopAllocationInVo shopAllocationInVo);

    /**
     * 根据id或者shopid修改信息
     * @param shopAllocationInVo
     * @return
     */
    int updateForPayment(ShopAllocationInVo shopAllocationInVo);

    /**
     * 添加信息
     * @param shopAllocationInVo
     * @return
     */
    int insertInfo(ShopAllocationInVo shopAllocationInVo);

    /**
     * 删除信息
     * @param id
     * @return
     */
    int deleteForShopConfig(Long id);
}
