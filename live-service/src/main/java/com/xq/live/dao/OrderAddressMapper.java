package com.xq.live.dao;

import com.xq.live.model.OrderAddress;
import com.xq.live.vo.in.OrderAddressInVo;
import com.xq.live.vo.out.OrderAddressOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAddressMapper {
    /**
     * 用户删除收货地址(物理删除)
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 用户添加收货地址
     * @param record
     * @return
     */
    int insert(OrderAddress record);

    int insertSelective(OrderAddress record);

    OrderAddress selectByPrimaryKey(Long id);

    /**
     * 用户修改收货地址
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(OrderAddress record);

    int updateByPrimaryKey(OrderAddress record);

    /**
     * 查询记录总数
     * @param inVo
     * @return
     */
    Integer listTotal(OrderAddressInVo inVo);

    /**
     * 根据userid分页查询所有收获地址
     * @param inVo
     * @return
     */
     List<OrderAddressOut> getAddressList(OrderAddressInVo inVo);

    /**
     * 根据id查询单个收获地址
     * @param Id
     * @return
     */
     OrderAddressOut getAddress(Long Id);

}
