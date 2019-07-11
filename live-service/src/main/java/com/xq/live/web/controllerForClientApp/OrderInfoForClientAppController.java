package com.xq.live.web.controllerForClientApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.OrderInfo;
import com.xq.live.model.User;
import com.xq.live.service.OrderInfoService;
import com.xq.live.vo.in.OrderInfoInVo;
import com.xq.live.vo.out.OrderInfoOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 商城系统订单相关接口
 * Created by lipeng on 2018/9/4.
 */
@RestController
@RequestMapping(value = "/clientApp/orderInfo")
public class OrderInfoForClientAppController {

    @Autowired
    private OrderInfoService orderInfoService;



    /**
     * 查询商城系统的订单详情(通过id)
     * @param id
     * @return
     */
    @RequestMapping(value = "/getDetail",method = RequestMethod.GET)
    public BaseResp<OrderInfoOut> getDetail(Long id){
         OrderInfoOut re = orderInfoService.getDetail(id);
        return new BaseResp<OrderInfoOut>(ResultStatus.SUCCESS,re);
    }

    /**
     * 查询商城系统的订单详情
     * @param orderCode(通过orderCode)
     * @return
     */
    @RequestMapping(value = "/getDetailByOrderCode/{orderCode}",method = RequestMethod.GET)
    public BaseResp<OrderInfoOut> getDetailByOrderCode(@PathVariable("orderCode") String  orderCode){
        OrderInfoOut re = orderInfoService.getDetailByOrderCode(orderCode);
        return new BaseResp<OrderInfoOut>(ResultStatus.SUCCESS,re);
    }

    /**
     * 分页查询记录列表  ----为了应付线上此接口直接先查父订单,为了该接口直接删除
     * 
     * 注:shopId为当前用户的所对应的shopId，从网关中获取
     * row=10&page=1,beginTime,endTime,shopId,(status)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<OrderInfoOut>> list(OrderInfoInVo inVo){
        /*User user = UserContext.getUserSession();
        if(user==null||user.getShopId()==null){
            return new BaseResp<Pager<OrderInfoOut>>(ResultStatus.error_param_empty);
        }
        inVo.setShopId(user.getShopId());*/
        inVo.setIsParent(OrderInfo.IS_PARENT_YES);//目前直接查父订单
        Pager<OrderInfoOut> result = orderInfoService.list(inVo);
        return new BaseResp<Pager<OrderInfoOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 查询免费兑换的订单
     *
     * 注意入参:page:1 rows:20 payType:0 categoryId
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listFree", method = RequestMethod.GET)
    public BaseResp<Pager<OrderInfoOut>> listFree(OrderInfoInVo inVo){
        if(inVo==null||inVo.getPayType()==null||inVo.getCategoryId()==null){
            return new BaseResp<Pager<OrderInfoOut>>(ResultStatus.error_param_empty);
        }
        inVo.setIsParent(OrderInfo.IS_PARENT_YES);//目前直接查父订单
        Pager<OrderInfoOut> result = orderInfoService.listFree(inVo);
        return new BaseResp<Pager<OrderInfoOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 查询商家营业数据
     * shopId,beginTime,endTime
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/queryShopTurnover",method = RequestMethod.GET)
    public BaseResp<Map<String,String>> queryShopTurnover(OrderInfoInVo inVo){
        Map<String,String> map = orderInfoService.queryShopTurnover(inVo);
        return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS,map);
    }

}
