package com.xq.live.service.impl;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ListObjConverter;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.MerchantDetailsMapper;
import com.xq.live.dao.OrderCouponMapper;
import com.xq.live.dao.OrderInfoMapper;
import com.xq.live.dao.OrderListMapper;
import com.xq.live.model.OperationLog;
import com.xq.live.model.OrderCoupon;
import com.xq.live.model.User;
import com.xq.live.service.OrderCouponService;
import com.xq.live.service.OrderListService;
import com.xq.live.vo.in.OrderCouponInVo;
import com.xq.live.vo.in.OrderListInVo;
import com.xq.live.vo.out.OrderCouponOut;
import com.xq.live.vo.out.OrderListOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderListServiceImpl implements OrderListService {

    @Autowired
    private OrderListMapper orderListMapper;

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MerchantDetailsMapper merchantDetailsMapper;

    @Autowired
    private OrderCouponService orderCouponService;
    /**
     * 订单列表
     * */
    @Override
    public Pager<OrderListOut> getList(OrderListInVo orderListInVo) {
        //创建分页对象
        Pager<OrderListOut> result = new Pager<>();
        //查询列表
        List<OrderListOut> list = orderListMapper.getList(orderListInVo);
        //转为输出实例
        List<OrderListOut> accountOuts = ListObjConverter.convert(list, OrderListOut.class);
        result.setPage(orderListInVo.getPage());
        result.setList(accountOuts);
        return result;
    }
    /**
     * 订单列表条数
     * */
    @Override
    public BaseResp getListTotal(OrderListInVo orderListInVo) {
        //数据封装
        BaseResp bs = new BaseResp();
        try {
            bs.setMessage("查询订单条数成功");
            bs.setCode(0);
            bs.setData(orderListMapper.getListTotal(orderListInVo));
        } catch (Exception e) {
            e.printStackTrace();
            bs.setMessage("查询订单条数失败");
        }
        return bs;
    }

    /**
     * 订单状态修改
     * */
    @Override
    @Transactional
    public BaseResp updateOrderList(OrderCouponInVo inVo) {
        //数据封装
        BaseResp bs = new BaseResp();
        //获取用户信息
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
       //判断订单是否存在和使用
        OrderCouponOut orderCouponOut = orderListMapper.selectByPrimaryKey(inVo.getOrderId());
        if(orderCouponOut==null){
            bs.setMessage("订单不存在或已使用");
            bs.setCode(1);
            return bs;
        }
        //退款申请中和已退款的票卷不能使用
        if(orderCouponOut.getStatus()!=null&&(orderCouponOut.getStatus()== OrderCoupon.STATUS_REFUND
                ||orderCouponOut.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION)){
            bs.setMessage("退款中的卷不能核销");
            bs.setCode(1);
            return bs;
        }
        inVo.setHxUserId(user.getId());//票券核销人是当前用户id
        inVo.setHxUserName(user.getUserName());
        inVo.setShopId(orderCouponOut.getShopId());
        inVo.setShopName(inVo.getShopName());
        inVo.setSalepointId(0L);
        if(inVo==null||inVo.getId()==null||inVo.getShopId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        int i = orderCouponService.hxCouponV2(inVo);
        //记录操作日志
        OperationLog ol = new OperationLog();
        ol.setType(2);
        ol.setRemake("操作人:"+UserContext.getUserName()+"审核成功order_coupon"+inVo.getId().toString());
        merchantDetailsMapper.insertOperationLog(ol);
        bs.setCode(0);
        bs.setMessage("审核通过");
        return bs;
    }

}
