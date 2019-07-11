package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.OrderAddressMapper;
import com.xq.live.model.OrderAddress;
import com.xq.live.service.OrderAddressService;
import com.xq.live.vo.in.OrderAddressInVo;
import com.xq.live.vo.out.OrderAddressOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ss on 2018/9/4.
 */
@Service
public class OrderAddressServiceImpl implements OrderAddressService{

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    /**
     * 根据userid分页查询所有收获地址
     * @param userId
     * @return
     */
    @Override
    public Pager<OrderAddressOut> getAddressList(Long userId) {
        Pager<OrderAddressOut> result = new Pager<OrderAddressOut>();
        OrderAddressInVo inVo = new OrderAddressInVo();
        inVo.setUserId(userId);
        Integer listTotal = orderAddressMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<OrderAddressOut> list = orderAddressMapper.getAddressList(inVo);
            for (int i=0;i<list.size();i++){
                if (list.get(i).getIsDefault()==1){
                    OrderAddressOut team=list.get(i);
                    OrderAddressOut change=list.get(0);
                    list.set(0,team);
                    list.set(i,change);
                }
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    /**
     * 根据id查询单个收获地址
     * @param Id
     * @return
     */
    @Override
    public OrderAddressOut getAddress(Long Id) {
        OrderAddressOut orderAddressOut=orderAddressMapper.getAddress(Id);
        return orderAddressOut;
    }

    /**
     * 用户添加收货地址
     * @param inVo
     * @return
     */
    @Transactional
    @Override
    public Integer inUserAddress(OrderAddress inVo) {
        if (inVo.getIsDefault()==1){//判断是否需要更改之前的默认地址
            OrderAddressInVo addressInVo = new OrderAddressInVo();
            addressInVo.setUserId(inVo.getUserId());
            Integer listTotal = orderAddressMapper.listTotal(addressInVo);
            if (listTotal!=null&&listTotal>0){
                List<OrderAddressOut> list = orderAddressMapper.getAddressList(addressInVo);
                for (OrderAddressOut addressOut:list){//判断是否有默认地址
                    if (addressOut.getIsDefault()==1){
                        OrderAddress orderAddress=new OrderAddress();
                        orderAddress.setId(addressOut.getId());
                        orderAddress.setIsDefault(OrderAddress.IS_NOT_DEFAULT);
                        Integer oldAddress=orderAddressMapper.updateByPrimaryKeySelective(orderAddress);
                        if (oldAddress<1){
                            throw new RuntimeException("更改之前地址默认值失败");
                        }
                    }
                }
            }
        }
        Integer i=orderAddressMapper.insert(inVo);
        return i;
    }

    /**
     * 用户删除收货地址(物理删除)
     * @param inVo
     * @return
     */
    @Transactional
    @Override
    public Integer outUserAddress(OrderAddress inVo) {
        inVo.setIsDeleted(OrderAddress.IS_DELETED);
        Integer oldAddress=orderAddressMapper.updateByPrimaryKeySelective(inVo);
        if (oldAddress<1){
            throw new RuntimeException("删除修改失败");
        }
        return oldAddress;
    }

    /**
     * 用户修改收货地址
     * @param inVo
     * @return
     */
    @Transactional
    @Override
    public Integer upUserAddress(OrderAddress inVo) {

        if (inVo.getIsDefault()!=null&&inVo.getIsDefault()==1){//判断是否需要更改之前的默认地址
            OrderAddressOut orderAddressOut=orderAddressMapper.getAddress(inVo.getId());//获取用户id
            OrderAddressInVo addressInVo = new OrderAddressInVo();
            addressInVo.setUserId(orderAddressOut.getUserId());
            Integer listTotal = orderAddressMapper.listTotal(addressInVo);
            if (listTotal>0){
                List<OrderAddressOut> list = orderAddressMapper.getAddressList(addressInVo);
                for (OrderAddressOut addressOut:list){//判断是否有默认地址
                    if (addressOut.getIsDefault()==1){
                        OrderAddress orderAddress=new OrderAddress();
                        orderAddress.setId(addressOut.getId());
                        orderAddress.setIsDefault(OrderAddress.IS_NOT_DEFAULT);
                        Integer oldAddress=orderAddressMapper.updateByPrimaryKeySelective(orderAddress);
                        if (oldAddress<1){
                            throw new RuntimeException("更改之前地址默认值失败");
                        }
                    }
                }
            }
        }
        Integer i=orderAddressMapper.updateByPrimaryKeySelective(inVo);
        if (i<1){
            throw new RuntimeException("修改失败");
        }
        return i;
    }

}
