package com.xq.live.dao;

import com.xq.live.model.OrderCoupon;
import com.xq.live.vo.in.OrderCouponConditionInVO;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.out.OrderCouponOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCouponMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderCoupon record);

    int insertSelective(OrderCoupon record);

    OrderCoupon selectByPrimaryKey(Long id);

    OrderCouponOut selectDetailByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderCoupon record);

    int updateByCouponCodeSelective(OrderCoupon record);

    int updateByPrimaryKey(OrderCoupon record);

    int updateUseCoupon(OrderCoupon record);

    int listTotal(OrderCouponInVo inVo);

    List<OrderCouponOut> list(OrderCouponInVo inVo);

    List<OrderCouponOut> listForSend(OrderCouponInVo inVo);

    Integer listTotalForSend(OrderCouponInVo inVo);

    OrderCoupon getByCouponCode(String couponCode);

    OrderCouponOut getDetailByCouponCode(String couponCode);

    int listCouponTotal(OrderCouponInVo inVo);

    List<OrderCouponOut> listCoupon(OrderCouponInVo inVo);

    int updateByVersionCouponCodeSelective(OrderCoupon orderCoupon);

    /**
     * 根据orderCode获取相关的OrderCoupon票卷
     * @param orderCode
     * @return
     */
    List<OrderCoupon> listOrderCouponByOrdercode(String orderCode);

    /**
     * 根据orderCode获取相关的OrderCoupon票卷
     * @param inVo
     * @return
     */
    int updateModifyingTheDisplay(OrderCouponInVo inVo);

    int batchInsert(OrderCoupon coupon);

    /**
     * 查询列表数据用于导出
     * @param inVo
     * @return
     */
    List<OrderCouponOut> listCouponForExport(OrderCouponInVo inVo);

    /**
     * 查询总计用于导出
     * @param inVo
     * @return
     */
    OrderCouponOut selectTotalForExport(OrderCouponInVo inVo);

    /**
     * 查询列表数据用于导出 新
     * @param inVo
     * @return
     */
    List<OrderCouponOut> listCouponForExportV(OrderCouponInVo inVo);

    /**
     * 查询总计用于导出 新
     * @param inVo
     * @return
     */
    OrderCouponOut selectTotalForExportV(OrderCouponInVo inVo);




    /**
     * 通过orderId去查询已显示的票券信息
     * @param orderId
     * @return
     */
    List<OrderCoupon> listByOrderId(Long orderId);

    /**
     * 更新票卷的状态
     * @param orderCouponConditionVO
     * @return
     */
    Integer updateStatus(OrderCouponConditionInVO orderCouponConditionVO);

    int listTotalForVip(OrderCouponInVo inVo);

    List<OrderCouponOut> listForVip(OrderCouponInVo inVo);
}
