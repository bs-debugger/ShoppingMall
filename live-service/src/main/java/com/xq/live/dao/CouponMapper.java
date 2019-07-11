package com.xq.live.dao;

import com.xq.live.model.Coupon;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.out.CouponOut;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 券信息Dao
 */
@Repository
public interface CouponMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Coupon record);

    Coupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);

    List<CouponOut> list(CouponInVo inVo);

    int listTotal(CouponInVo inVo);

    /**
     * 使用抵用券
     * @param record
     * @return
     */
    int useCoupon(Coupon record);

    CouponOut getByCouponCode(String couponCode);

    public CouponOut selectById(Long id);

    /**
     * 单个使用人每天被核销的次数
     * @param userId
     * @return
     */
    Integer useLimit(Long userId);

    List<Coupon> listCouponBySoId(Long soId);
}
