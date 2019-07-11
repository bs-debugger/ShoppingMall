package com.xq.live.service.impl;

import com.xq.live.dao.ShopAllocationMapper;
import com.xq.live.model.ShopAllocation;
import com.xq.live.service.ShopAllocationService;
import com.xq.live.vo.in.ShopAllocationInVo;
import com.xq.live.vo.out.ShopAllocationOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ss on 2018/5/25.
 */
@Service
public class ShopAllocationServiceImpl implements ShopAllocationService{

    @Autowired
    private ShopAllocationMapper allocationMapper;

    @Override
    public ShopAllocationOut admin(ShopAllocationInVo shopAllocationInVo) {
        ShopAllocationOut allocationOut=allocationMapper.selectByPrimaryKey(shopAllocationInVo);
        if (allocationOut==null){
            return null;
        }
        return allocationOut;
    }

    @Override
    public List<ShopAllocationOut> listAll(ShopAllocationInVo shopAllocationInVo) {
        List<ShopAllocationOut> list=allocationMapper.selectByCan(shopAllocationInVo);
        if (list==null|list.size()<1){
            return null;
        }
        return list;
    }

    @Override
    public int updateForPayment(ShopAllocationInVo shopAllocationInVo) {
        int i=allocationMapper.updateByInfo(shopAllocationInVo);
        if (i>0){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public int insertInfo(ShopAllocationInVo allocation) {
        int i=allocationMapper.insertByID(allocation);
        if (i>0){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public int deleteForShopConfig(Long id) {
        int i=allocationMapper.deleteByPrimaryKey(id);
        if (i>0){
            return 1;
        }else {
            return 0;
        }
    }
}
