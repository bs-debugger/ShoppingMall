package com.xq.live.service.impl;

import com.xq.live.dao.ServiceAmountMapper;
import com.xq.live.model.ServiceAmount;
import com.xq.live.service.ServiceAmountService;
import com.xq.live.vo.in.ServiceAmountInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 缴纳服务费ServiceImpl
 * Created by lipeng on 2018/7/27.
 */
@Service
public class ServiceAmountServiceImpl implements ServiceAmountService {
    @Autowired
    private ServiceAmountMapper serviceAmountMapper;

    @Override
    public Long add(ServiceAmountInVo serviceAmountInVo) {
        int i = serviceAmountMapper.insert(serviceAmountInVo);
        if(i<1){
            return null;
        }
        return serviceAmountInVo.getId();
    }

    @Override
    public List<ServiceAmount> selectByShopId(Long shopId) {
        List<ServiceAmount> list = serviceAmountMapper.selectByShopId(shopId);
        return list;
    }

    @Override
    public List<ServiceAmount> allByShopId(ServiceAmountInVo record) {
        List<ServiceAmount> list = serviceAmountMapper.allByShopId(record);
        return list;
    }
}
