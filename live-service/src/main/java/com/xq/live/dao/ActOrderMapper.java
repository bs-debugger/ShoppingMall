package com.xq.live.dao;

import com.xq.live.model.ActOrder;
import com.xq.live.model.OrderInfo;
import com.xq.live.vo.out.ActOrderOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActOrder record);

    /*
    添加
    * */
    int insertOrderLog(ActOrder record);

    int batchInsertOrderLog(ActOrder actOrder);

    int insertSelective(ActOrder record);

    ActOrder selectByPrimaryKey(Long id);

    /*根据团id和用户id查询，避免重复添加*/
    List<ActOrder> selectByActGoodsSkuId(ActOrder record);
    /*查找拼团支付过的下级s*/
    List<ActOrder> selectSubordinate(ActOrder record);

    int updateByPrimaryKeySelective(ActOrder record);
    /*修改用户邀请人*/
    int updateUserParent(ActOrder record);
    /*根据传入的订单集合修改删除状态*/
    int updateOrderIsDeleted(Long orderId);
    /*增加人数*/
    int updateByPeoPleNum(ActOrder record);
    /*支付成功后修改用户状态*/
    int updateUserState(ActOrder record);

    int updateByPrimaryKey(ActOrder record);

    /*修改活动结束后用户参团状态*/
    int updateOverTimeByStatus(ActOrder record);

    /*二级分销查找一级用户*/
    ActOrderOut selectFirstDistributionByOrderId(Long orderId);

    /*二级分销查找二级用户*/
    ActOrder selectSecondDistributionByOrderId(Long orderId);

    /*根据订单范围查找 修改活动库存用*/
    List<ActOrder> selectByOrderIdAndAct(List<OrderInfo> list);

    /*查找一个团活动下的所有活动订单信息*/
    List<ActOrderOut> selectActOrderByActGoodsSkuId(Long actGoodsSkuId);
}
