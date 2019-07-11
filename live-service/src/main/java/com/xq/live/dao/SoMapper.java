package com.xq.live.dao;

import com.xq.live.model.So;
import com.xq.live.vo.in.CouponConditionInVo;
import com.xq.live.vo.in.SoConditionInVO;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SoInVo record);

    So selectByPrimaryKey(Long id);

    int selectByUserIdTotal(Long userId);

    int updateByPrimaryKeySelective(So record);

    int updateByPrimaryKey(So record);

    int listTotal(SoInVo inVo);

    int listForShopTotal(SoInVo inVo);

    List<SoOut> list(SoInVo inVo);

    List<SoOut> listForShop(SoInVo inVo);

    List<SoOut> listNoPage(SoInVo inVo);

    List<SoOut> listForShopNoPage(SoInVo inVo);

    SoOut selectByPk(Long id);

    SoOut selectByPkForShop(Long id);

    SoForOrderOut selectByPkForOrder(Long id);

    int paid(SoInVo inVo);

    int hexiao(SoInVo inVo);

    int cancel(SoInVo inVo);

    /**
     * 判断用户是否已经领取过活动卷
     * @param inVo
     * @return
     */
    Integer hadBeenGiven(SoInVo inVo);

    /**
     * 根据订单ID获取userId
     * @param inVo
     * @return
     */
    Long getUserIDBySoId(SoInVo inVo);

    SoOut totalAmount(SoInVo inVo);

    /**
     * 根据shopid获取一段时间内的所有订单食典券
     * @param
     * @return
     */
    Integer skulistTotal(SoInVo inVo);

    /**
     * 根据shopid获取一段时间内的所有订单商家订单
     * @param
     * @return
     */
    Integer solistTotal(SoInVo inVo);

    Integer selectSkuAllocation(Long userId);

    /**
     * 退款申请
     * @param inVo
     * @return
     */
    int refundApplication(SoInVo inVo);

    /**
     * 退款之后修改 so_status
     * @param so
     * @return
     */
    Integer UpdateById(SoConditionInVO so);

    /**
     * 修改票卷的状态
     * @param couponConditionVo
     * @return
     */
    Integer UpdateCouponBySoId(CouponConditionInVo couponConditionVo);

}
