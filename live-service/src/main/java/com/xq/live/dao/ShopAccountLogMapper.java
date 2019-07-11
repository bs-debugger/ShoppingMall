package com.xq.live.dao;


import com.xq.live.model.ShopAccountLog;
import org.apache.ibatis.annotations.Param;

public interface ShopAccountLogMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(ShopAccountLog record);

    int insertSelective(ShopAccountLog record);

    /**
     * 查询上一次同步时间
     * @return
     */
    ShopAccountLog serachHisDate();

    int updateByPrimaryKeySelective(ShopAccountLog record);

    int updateByPrimaryKey(ShopAccountLog record);

    /**
     * 同步商户微信收入数据
     * @param startTime
     * @param endTime
     * @return
     */
    int wxPayByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 同步用户微信收入数据
     * @param startTime
     * @param endTime
     * @return
     */
    int wxUserPayByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 同步商户微信退款数据
     * @param startTime
     * @param endTime
     * @return
     */
    int wxRefundByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 同步平台用户微信退款数据
     * @param startTime
     * @param endTime
     * @return
     */
    int wxUserRefundByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 同步余额收入统计
     * @param startTime
     * @param endTime
     * @return
     */
    int yuePayByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 同步余额收入统计
     * @param startTime
     * @param endTime
     * @return
     */
    int yueUserPayByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 同步商户余额退款统计
     * @param startTime
     * @param endTime
     * @return
     */
    int yueRefundByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 同步平台用户余额退款统计
     * @param startTime
     * @param endTime
     * @return
     */
    int yueUserRefundByTime(@Param("startTime")String startTime,@Param("endTime")String endTime);
}