package com.xq.live.dao;
import com.xq.live.model.OperationLog;
import com.xq.live.vo.in.MerchantDetailsInVo;
import com.xq.live.vo.in.ShopZoneInVo;
import com.xq.live.vo.out.MerchantDetailsOut;
import com.xq.live.vo.out.ShopZoneOut;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MerchantDetailsMapper {
    /**
     *  商户明细列表
     * @param
     * @return  明细列表
     */
    List<MerchantDetailsOut> getList(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 商户明细详情
     * */
    List<MerchantDetailsOut> getDetails(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 商户明细非订单详情
     * */
    List<MerchantDetailsOut> getNoDetails(MerchantDetailsInVo merchantDetailsInVo);

    /**
     *
     * */
    /**
     * 商户明细非订单详情count
     * */
    int getNoDetailsCount(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 商户明细详情count
     * */
    int getListCount(MerchantDetailsInVo merchantDetailsInVo);

    /**
     *  商户明细列表count
     * @param
     * @return  明细列表数量
     */
    int getDetailsCount(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 商户明细头部信息
     * */
    List<Map<String,Object>> getTableList(MerchantDetailsInVo merchantDetailsInVo);

    /**
     * 获取专区信息
     * */
    List<ShopZoneOut> listAllShopZone();

    /**
     * 商户余额操作添加操作日志
     * */
    void insertOperationLog(OperationLog ol);
}