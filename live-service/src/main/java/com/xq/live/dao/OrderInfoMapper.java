package com.xq.live.dao;

import com.xq.live.model.OrderInfo;
import com.xq.live.vo.in.OrderInfoConditionInVO;
import com.xq.live.vo.in.OrderInfoInVo;
import com.xq.live.vo.out.OrderInfoOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfoInVo record);

    int batchInsert(OrderInfoInVo record);

    int insertSelective(OrderInfoInVo record);

    OrderInfo selectByPrimaryKey(Long id);

    OrderInfo selectByCode(String orderCode);

    int updateByPrimaryKeySelective(OrderInfoInVo record);

    int updateByPrimaryKey(OrderInfoInVo record);

    OrderInfoOut getDetail(Long id);

    OrderInfoOut getDetailByOrderCode(String orderCode);

    int listTotal(OrderInfoInVo inVo);

    List<OrderInfoOut> list(OrderInfoInVo inVo);

    int listTotalFree(OrderInfoInVo inVo);

    List<OrderInfoOut> listFree(OrderInfoInVo inVo);

    /**
     * 查看待支付的订单
     * @return
     */
    List<OrderInfo> getNotPaylist(OrderInfoInVo inVo);

    /**
     * 查看单个人的待支付的活动订单
     * @return
     */
    List<OrderInfo> onePeopleNotPaylist(OrderInfoInVo inVo);

    /**
     * 将订单状态从待支付改为取消
     * @param inVo
     * @return
     */
    int updateByRollback(List<OrderInfo> inVo);


    int paid(OrderInfoInVo inVo);

    int batchPaid(OrderInfoInVo inVo);

    int createQrcodeUrl(OrderInfoInVo inVo);

    int refundApplication(OrderInfoInVo inVo);

    OrderInfoOut queryCashAmount(OrderInfoInVo inVo);

    OrderInfoOut queryShopTurnover(OrderInfoInVo inVo);

    OrderInfoOut queryShopOrderTurnover(OrderInfoInVo inVo);

    int selectByUserIdTotal(Long userId);

    /**
     * 退款之后修改 status
     * @param vo
     * @return
     */
    Integer UpdateByOrderCode(OrderInfoConditionInVO vo);

}
