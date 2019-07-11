package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.OrderAddress;
import com.xq.live.vo.in.OrderAddressInVo;
import com.xq.live.vo.out.OrderAddressOut;

/**
 * Created by ss on 2018/9/4.
 * 用户收获地址管理
 */
public interface OrderAddressService {

    /**
     * 根据userid分页查询所有收获地址
     * @param userId
     * @return
     */
    public Pager<OrderAddressOut>  getAddressList(Long userId);

    /**
     * 根据id查询单个收获地址
     * @param Id
     * @return
     */
    public OrderAddressOut getAddress(Long Id);

    /**
     * 用户添加收货地址
     * @param inVo
     * @return
     */
    public Integer inUserAddress(OrderAddress inVo);

    /**
     * 用户删除收货地址(物理删除)
     * @param inVo
     * @return
     */
    public Integer outUserAddress(OrderAddress inVo);

    /**
     * 用户修改收货地址(物理删除)
     * @param inVo
     * @return
     */
    public Integer upUserAddress(OrderAddress inVo);


}
