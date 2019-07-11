package com.xq.live.web.controller;

import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.FreeSkuConfig;
import com.xq.live.dao.GoodsSkuMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.BatchOrderInVo;
import com.xq.live.vo.in.GoodsSkuInVo;
import com.xq.live.vo.in.OrderInfoInVo;
import com.xq.live.vo.in.WeixinInVo;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.OrderInfoOut;
import com.xq.live.vo.out.OrderWriteOffResultOut;
import com.xq.live.web.utils.GetUserInfoUtil;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.text.ParseException;
import java.util.*;


/**
 * 商城系统订单相关接口
 * Created by lipeng on 2018/9/4.
 */
@Api(tags = "商城系统订单相关接口-OrderInfoController")
@RestController
@RequestMapping(value = "/orderInfo")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SalePointService salePointService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderCartService orderCartService;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private FreeSkuConfig freeSkuConfig;

    @Autowired
    private  OrderWriteOffService orderWriteOffService;

    private WXPay wxpay;
    private PaymentConfig config;
    public OrderInfoController() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
    }

    /**
     * 查询商城系统的订单详情(通过id)
     * @param id
     * @return
     */
    @ApiOperation(value = "查询商城系统的订单详情(通过id)")
    @RequestMapping(value = "/getDetail",method = RequestMethod.GET)
    public BaseResp<OrderInfoOut> getDetail(Long id){
        OrderInfoOut re = orderInfoService.getDetail(id);
        return new BaseResp<OrderInfoOut>(ResultStatus.SUCCESS,re);
    }

    /**
     * 查询商城系统的订单详情(通过id),
     * 再次支付之前调用，
     * 待支付状态订单会在微信中主动查询支付状态
     * 避免因微信支付回调不及时导致订单支付状态未修改
     * @param id
     * @return
     */
    @ApiOperation(value = "查询商城系统的订单详情(通过id)")
    @RequestMapping(value = "/getDetailNew",method = RequestMethod.GET)
    public BaseResp<OrderInfoOut> getDetailNew(Long id){
        OrderInfoOut re = orderInfoService.getDetailNew(id);
        return new BaseResp<OrderInfoOut>(ResultStatus.SUCCESS,re);
    }

    /**
     * 查询商城系统的订单详情
     * @param orderCode(通过orderCode)
     * @return
     */
    @ApiOperation(value = "查询商城系统的订单详情")
    @RequestMapping(value = "/getDetailByOrderCode/{orderCode}",method = RequestMethod.GET)
    public BaseResp<OrderInfoOut> getDetailByOrderCode(@PathVariable("orderCode") String  orderCode){
        OrderInfoOut re = orderInfoService.getDetailByOrderCode(orderCode);
        return new BaseResp<OrderInfoOut>(ResultStatus.SUCCESS,re);
    }

    /**
     * 查询商城系统的订单详情(通过id),
     * 再次支付之前调用，
     * 待支付状态订单会在微信中主动查询支付状态
     * 避免因微信支付回调不及时导致订单支付状态未修改
     * @param code
     * @return
     */
    @ApiOperation(value = "查询商城系统的订单详情(通过id)")
    @RequestMapping(value = "/getDetew",method = RequestMethod.GET)
    public BaseResp<OrderInfoOut> getDetew(String code){

        String nonce_str = WXPayUtil.generateNonceStr();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid","wxf91e2a026658e78e");
        data.put("mch_id","1499658152");
        data.put("nonce_str",nonce_str);
        data.put("out_trade_no",code);
        try{
            Map<String, String> rMap=new HashMap<String, String>();

            String sign = WXPayUtil.generateSignature(data, "4921641fc679dd76a7141ba750d88204");
            data.put("sign",sign);
            rMap = wxpay.orderQuery(data);
            String return_code = rMap.get("return_code");//返回状态码

            if ("SUCCESS".equals(return_code)){
                String result_code = rMap.get("result_code");//业务结果
                if( "SUCCESS".equals(result_code)){
                    String trade_state=rMap.get("trade_state");//交易状态
                    if("SUCCESS".equals(trade_state)){
                        String orderCode = (String) rMap.get("out_trade_no"); //商户订单号
                        String attach = (String) rMap.get("attach");//商家订单中对应的attach，其                          中包含有couponId和shopId，可以通过此来完成对账
                        String total_fee = (String) rMap.get("total_fee");
                        WeixinInVo attachInVo = JSON.parseObject(attach, WeixinInVo.class);//将附
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResp<OrderInfoOut>(ResultStatus.SUCCESS);
    }

    /**
     * 分页查询记录列表  ----为了应付线上此接口直接先查父订单,为了该接口直接删除
     *
     * 注:userId为当前用户，从网关中获取
     * row=10&page=1
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页查询记录列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<OrderInfoOut>> list(OrderInfoInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<OrderInfoOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setIsParent(OrderInfo.IS_PARENT_YES);//目前直接查父订单
        Pager<OrderInfoOut> result = orderInfoService.list(inVo);
        return new BaseResp<Pager<OrderInfoOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 查询免费兑换的订单
     *
     * 注意入参:page:1 rows:20 payType:0 (categoryId) (actId) (userId)
     * @param inVo
     * @return
     */
    @ApiOperation(value = "查询免费兑换的订单")
    @RequestMapping(value = "/listFree", method = RequestMethod.GET)
    public BaseResp<Pager<OrderInfoOut>> listFree(OrderInfoInVo inVo){
        if(inVo==null||inVo.getPayType()==null){
            return new BaseResp<Pager<OrderInfoOut>>(ResultStatus.error_param_empty);
        }
        inVo.setIsParent(OrderInfo.IS_PARENT_YES);//目前直接查父订单
        Pager<OrderInfoOut> result = orderInfoService.listFree(inVo);
        return new BaseResp<Pager<OrderInfoOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 分页订单页,其实正常来说应该有listRo和listVo两个查询接口,但是由于票券和实物目前放到了一起,所以直接用了一个接口
     * ,可以后期优化
     *
     * 注:userId为当前用户，从网关中获取
     * 注意:1.如果某个订单只有父订单,那么他就直接有对应商品
     *     2.如果某个订单有父订单和子订单,那么他的父订单里面没有商品,子订单里面有商品
     * @param inVo
     * @return
     */
    @ApiOperation(value = "分页订单页")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public BaseResp<Pager<OrderInfoOut>> listAll(OrderInfoInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<OrderInfoOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if(inVo== null||inVo.getUserId()==null){
            return new BaseResp<Pager<OrderInfoOut>>(ResultStatus.error_param_empty);
        }
        inVo.setIsParent(OrderInfo.IS_PARENT_YES);//目前直接查父订单,然后再查询父订单里面对应的子订单
        Pager<OrderInfoOut> result = orderInfoService.listAll(inVo);
        return new BaseResp<Pager<OrderInfoOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 生成订单(旧接口,以后删除掉)
     *
     * 注意:这个地方的sendType要根据用户选择的配送方式来确定,1平台邮购,2门店自提
     * 1平台邮购里面:要分成orderType  1实物订单  2虚拟订单
     * 2门店自提里面:要分成orderType  1实物订单  2虚拟订单
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 平台普通订单(实物订单): userId:52121 userName:13339981235 shopId:296 payType:2 orderAddressId:1 sendTime sendType:1
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark},...]---对应的支付为doUnifiedOrderForShoppingMall
     *
     * 平台票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:2 sendType:1
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:2 sendType:1
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台门店自提订单(实物订单): userId:52121 userName:13339981235 shopId:0 payType:2 salepointId sendType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark},...]---对应的支付为wxpay/shoppingMallForMDZT
     *
     * 平台门店自提票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 salepointId sendType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台门店自提票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 salepointId sendType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark},...]---对应的支付为wxpay/shoppingMallForCoupon
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "生成订单(旧接口,以后删除掉)")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResp<Long> create(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //为了应对上线期间的改动,先这么处理
        if(inVo.getSendType()==null){
            inVo.setSendType(OrderInfo.SEND_TYPE_PTYG);
        }
        inVo.setUserIp(IpUtils.getIpAddr(request));
        Long id = orderInfoService.create(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }


    /**
     * 生成订单(新接口)
     *
     * 注意:这个地方的sendType要根据用户选择的配送方式来确定,1平台邮购,2门店自提
     * 1平台邮购里面:要分成orderType  1实物订单  2虚拟订单
     * 2门店自提里面:要分成orderType  1实物订单  2虚拟订单
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 平台普通订单(实物订单): userId:52121 userName:13339981235 shopId:296 payType:2
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为doUnifiedOrderForShoppingMall
     *
     * 平台票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:2 singleType:1
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:2 singleType:2
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台门店自提订单(实物订单): userId:52121 userName:13339981235 shopId:0 payType:2
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForMDZT
     *
     * 平台门店自提票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 singleType:2
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台门店自提票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 singleType:1
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "生成订单(新接口)")
    @RequestMapping(value = "/createNew", method = RequestMethod.POST)
    public BaseResp<Long> createNew(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setSourceType(OrderInfo.SOURCE_TYPE_PT);
        Long id = orderInfoService.createNew(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 生成订单(新接口)
     *
     * 注意:这个地方的sendType要根据用户选择的配送方式来确定,1平台邮购,2门店自提
     * 1平台邮购里面:要分成orderType  1实物订单  2虚拟订单
     * 2门店自提里面:要分成orderType  1实物订单  2虚拟订单
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 平台普通订单(实物订单): userId:52121 userName:13339981235 shopId:296 payType:2
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为doUnifiedOrderForShoppingMall
     *
     * 平台票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:2 singleType:1
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:2 singleType:2
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台门店自提订单(实物订单): userId:52121 userName:13339981235 shopId:0 payType:2
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForMDZT
     *
     * 平台门店自提票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 singleType:2
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     *
     * 平台门店自提票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 singleType:1
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType为4,再传actId,totalKey,valueKey)
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32},...]---对应的支付为wxpay/shoppingMallForCoupon
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "生成订单(新接口)")
    @RequestMapping(value = "/createv1", method = RequestMethod.POST)
    public BaseResp<OrderInfoOut> createv1(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<OrderInfoOut>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<OrderInfoOut>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setSourceType(OrderInfo.SOURCE_TYPE_PT);
        Long id = orderInfoService.createNew(inVo);
        OrderInfoOut re = orderInfoService.getDetail(id);
        return new BaseResp<OrderInfoOut>(ResultStatus.SUCCESS, re);
    }

    /**
     * 生成商家订单(新接口)
     *
     *
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 商家订单: userId:52121 userName:13339981235 shopId:296 payType:2 singleType:2  skuAmount  realAmount
     * flagType:1
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32},...]---对应的支付为
     *
     *
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "生成商家订单(新接口)")
    @RequestMapping(value = "/createForShop", method = RequestMethod.POST)
    public BaseResp<Long> createForShop(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setSourceType(OrderInfo.SOURCE_TYPE_SJ);
        Long id = orderInfoService.createForShop(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 生成订单(免费赠送接口)
     *
     * 无支付接口
     *
     * 注意:这个地方的sendType要根据用户选择的配送方式来确定,1平台邮购,2门店自提
     * 1平台邮购里面:要分成orderType  1实物订单  2虚拟订单
     * 2门店自提里面:要分成orderType  1实物订单  2虚拟订单
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 平台普通订单(实物订单): userId:52121 userName:13339981235 shopId:296 payType:0
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32 remark},...]
     *
     * 平台票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:0 singleType:1
     * flagType:6
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32 remark},...]
     *
     * 平台票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:296 payType:0 singleType:2
     * flagType:6
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32 remark},...]
     *
     * 平台门店自提订单(实物订单): userId:52121 userName:13339981235 shopId:0 payType:0
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32 remark},...]
     *
     * 平台门店自提票券订单--整单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:0 singleType:2
     * flagType:6
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32 remark},...]
     *
     * 平台门店自提票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:0 singleType:1
     * flagType:6
     * [{goodsSkuId:1256 goodsNum:1 orderItemShopId:32 remark},...]
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "生成订单(免费赠送接口)")
    @RequestMapping(value = "/createNewForFree", method = RequestMethod.POST)
    public BaseResp<Long> createNewForFree(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request) throws ParseException {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        if(inVo.getFlagType()==OrderInfo.FLAG_TYPE_PT){
            List<OrderItem> orderItemList = new ArrayList<OrderItem>();
            OrderItem item = new OrderItem();
            item.setGoodsSkuId(freeSkuConfig.getGoodsSkuId());
            //item.setGoodsSpuId(freeSkuConfig.getGoodsSpuId());
            item.setGoodsNum(1);
            item.setOrderItemShopId(0L);
            item.setRemark("免费赠送");
            orderItemList.add(item);
            inVo.setOrderItemList(orderItemList);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setSourceType(OrderInfo.SOURCE_TYPE_PT);
        Long id = orderInfoService.createNewForFree(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 批量创建免费订单
     *
     * 平台门店自提票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 singleType:1
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType不为1,再传actId)(砍价4,41) (秒杀5,44)
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32},...]
     * batchOrderInVo:[batchSize,beginTime,endTime,quitOrderNum,successOrderNum,xqNum,kjNum,msNum]
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "批量创建免费订单")
    @RequestMapping(value = "/batchCreate", method = RequestMethod.POST)
    public BaseResp<Long> batchCreate(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        for(Integer i=0;i<inVo.getBatchOrderInVo().getBatchSize();i++){
            OrderInfoInVo infoInVo=new OrderInfoInVo();
            BeanUtils.copyProperties(inVo,infoInVo);
            try {
                Integer startNum=infoInVo.getBatchOrderInVo().getUserStart1();
                Integer endNum=infoInVo.getBatchOrderInVo().getUserEnd1();
                if(infoInVo.getBatchOrderInVo().getUserEnd2()!=null&&infoInVo.getBatchOrderInVo().getUserStart2()!=null&&infoInVo.getBatchOrderInVo().getUserSum2()!=null&&infoInVo.getBatchOrderInVo().getUserSum1()!=null){
                    Integer radam= getRandomInt(infoInVo.getBatchOrderInVo().getUserSum1()+infoInVo.getBatchOrderInVo().getUserSum2());
                    if (radam>infoInVo.getBatchOrderInVo().getUserSum1()){
                        startNum=infoInVo.getBatchOrderInVo().getUserStart2();
                        endNum=infoInVo.getBatchOrderInVo().getUserEnd2();
                    }
                }

                User user = getRandomUser(startNum,endNum);
                Shop shop = getRandomShop(infoInVo.getBatchOrderInVo().getShopNum());
                GoodsSku goodsSku = new GoodsSku();
                Integer randomNum = getRandomInt(10);
                OrderItem orderItem = new OrderItem();
                if(infoInVo.getBatchOrderInVo().getType()==1){//1,享七五元 502
                    goodsSku = goodsSkuService.selectOne(502l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if(infoInVo.getBatchOrderInVo().getType()==2){//2，享七十元 501
                    goodsSku = goodsSkuService.selectOne(501l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if(infoInVo.getBatchOrderInVo().getType()==3){//3，享七十五元 516
                    goodsSku = goodsSkuService.selectOne(516l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if(infoInVo.getBatchOrderInVo().getType()==4){//4，享七二十元 503
                    goodsSku = goodsSkuService.selectOne(503l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if(infoInVo.getBatchOrderInVo().getType()==5){//5，秒杀
                    goodsSku = getRandomSkuByShop(shop, infoInVo.getBatchOrderInVo().getShopNum());
                    infoInVo.setFlagType(5);
                    infoInVo.setActId(44l);
                    orderItem.setOrderItemShopId(shop.getId());
                    infoInVo.setShopId(shop.getId());
                }else if(infoInVo.getBatchOrderInVo().getType()==6){//6 砍价拼菜
                    goodsSku = getRandomSkuByShop(shop, infoInVo.getBatchOrderInVo().getShopNum());
                    infoInVo.setFlagType(4);
                    infoInVo.setActId(41l);
                    orderItem.setOrderItemShopId(shop.getId());
                    infoInVo.setShopId(shop.getId());
                }else if(infoInVo.getBatchOrderInVo().getType()==7){//7大闸蟹 2542
                    goodsSku = goodsSkuService.selectOne(2542l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if(infoInVo.getBatchOrderInVo().getType()==8){//8 食碘卷7961
                    goodsSku = goodsSkuService.selectOne(7961l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }

                Integer randomNums = getRandomInt(10);
                if(infoInVo.getBatchOrderInVo().getQuitOrderNum()>0&&infoInVo.getBatchOrderInVo().getQuitOrderNum()>=randomNums){
                    infoInVo.getBatchOrderInVo().setOrderStatus(BatchOrderInVo.ORDER_STATUS_QUIT);
                }else {
                    infoInVo.getBatchOrderInVo().setOrderStatus(BatchOrderInVo. ORDER_STATUS_SUCCESS);
                }

                orderItem.setGoodsSkuId(goodsSku.getId());
                orderItem.setGoodsSpuId(goodsSku.getSpuId());
                orderItem.setGoodsNum(1);
                infoInVo.getBatchOrderInVo().setHxShopId(shop.getId());
                infoInVo.getBatchOrderInVo().setHxShopName(shop.getShopName());
                List<OrderItem> orderItemList = new ArrayList<>();
                orderItemList.add(orderItem);
                infoInVo.setOrderItemList(orderItemList);
                infoInVo.setSingleType(1);
                infoInVo.setPayType(2);
                infoInVo.setCreateType(1);
                Date createTime = GetUserInfoUtil.randomDate(infoInVo.getBatchOrderInVo().getBeginTime(), infoInVo.getBatchOrderInVo().getEndTime());
                createTime.setHours(getRandomHours());
                infoInVo.setCreateTime(createTime);
                infoInVo.setUserId(user.getId());
                infoInVo.setUserName(user.getUserName());
                infoInVo.setUserIp(GetUserInfoUtil.getRandomIp());
                infoInVo.setSourceType(OrderInfo.SOURCE_TYPE_PT);

                Long id = orderInfoService.batchCreateForXq(infoInVo);

            }catch (Exception e){
            }
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS, null);
    }

    /**
     * 生成实物订单(普通订单和门店自提订单)
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 注意:这个地方的sendType,orderAddressId sendTime salepointId 都是放到那个list入参里面,然后通过list里面的这些数据
     * 来创建不同的子订单
     *
     * 之后把sendType和salepointId在放到order_item里面
     * 把orderAddressId和sendTime放到父订单上面，等创建子订单的时候，把这些信息放到子订单里面(支付成功之后进行订单拆分)
     *
     * 目前采用的是支付成功之前就进行订单的拆分
     *
     * 平台普通订单: userId:52121 userName:13339981235 shopId:296 payType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark
     *   sendType:1 orderAddressId sendTime},...]---对应支付 wxpay/shoppingMallRo
     *
     * 平台门店自提订单: userId:52121 userName:13339981235 shopId:0 payType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark
     *   sendType:2 salepointId},...]---对应支付 wxpay/shoppingMallRo
     *
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "生成实物订单(普通订单和门店自提订单)")
    @RequestMapping(value = "/createRo", method = RequestMethod.POST)
    public BaseResp<Long> createRo(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setOrderType(OrderInfo.ORDER_TYPE_RO);//实物订单
        Long id = orderInfoService.createRo(inVo);
        //删除购物车信息(此地方不用做回滚操作)
        List<OrderCart> list = new ArrayList<OrderCart>();
        List<OrderItem> listItems = inVo.getOrderItemList();
        for (OrderItem listItem : listItems) {
            OrderCart orderCart = new OrderCart();
            orderCart.setUserId(inVo.getUserId());
            orderCart.setGoodsSkuId(listItem.getGoodsSkuId());
            list.add(orderCart);
        }
        if(list.size()!=0) {
            Integer re = orderCartService.deleteOrderCart(list);//清除购买订单里面对应的购物车项
        }

        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 生成虚拟订单(票券订单和门店自提票券订单)
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 注意:这个地方orderType直接传1
     *
     * 平台票券订单: userId:52121 userName:13339981235 shopId:296 payType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark
     *   sendType:1},...]
     *
     *
     * 平台门店自提票券订单: userId:52121 userName:13339981235 shopId:0 payType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark
     *   sendType:2 salepointId},...]
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "生成虚拟订单(票券订单和门店自提票券订单)")
    @RequestMapping(value = "/createVo", method = RequestMethod.POST)
    public BaseResp<Long> createVo(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //inVo.setSendType(OrderInfo.SEND_TYPE_PTYG);
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setOrderType(OrderInfo.ORDER_TYPE_VO);//虚拟订单
        Long id = orderInfoService.createVo(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }


    /**
     * 查询购物车结算订单的所有费用(也可以用这个接口查询虚拟订单所有费用)
     *
     * 注:userId和userName是当前用户的,要从网关中获取
     * 注意:这个地方的sendType,orderAddressId sendTime salepointId 都是放到那个list入参里面,然后通过list里面的这些数据
     * 来创建不同的子订单
     *
     * 之后把sendType和salepointId在放到order_item里面
     * 把orderAddressId和sendTime放到父订单上面，等创建子订单的时候，把这些信息放到子订单里面(支付成功之后进行订单拆分)
     *
     * 目前采用的是支付成功之前就进行订单的拆分
     *
     * 平台普通订单: userId:52121 userName:13339981235 shopId:296 payType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark
     *   sendType:1 orderAddressId sendTime},...]
     *
     * 平台门店自提订单: userId:52121 userName:13339981235 shopId:0 payType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark
     *   sendType:2 salepointId},...]
     *
     * 平台票券订单: userId:52121 userName:13339981235 shopId:296 payType:2
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32 remark
     *   sendType:1},...]
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "查询购物车结算订单的所有费用")
    @RequestMapping(value = "/getAllOrderList", method = RequestMethod.POST)
    public BaseResp<List<OrderInfoInVo>> getAllOrderList(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<List<OrderInfoInVo>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<OrderInfoInVo>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        List<OrderInfoInVo> list = orderInfoService.getAllOrderList(inVo);
        return new BaseResp<List<OrderInfoInVo>>(ResultStatus.SUCCESS, list);
    }



    /**
     * 确认收货按钮
     *
     * 只有待收货状态，且有快递单号的订单能确认收货
     * @param id
     * @return
     */
    @RequestMapping(value = "/confirmCeceipt",method = RequestMethod.POST)
    public BaseResp<Integer> confirmCeceipt(Long id){
        Integer re = orderInfoService.confirmCeceipt(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    /**
     * 核销使用订单的二维码
     *
     * 注意入参:orderCode,hxUserId
     *
     * 注意hxUserId是当前用户的userId，要从网关中获取
     * @param inVo
     * @return
     */
    @ApiOperation(value = "核销使用订单的二维码")
    @RequestMapping(value = "/useOrderInfo",method = RequestMethod.POST)
    public BaseResp<Integer> useOrderInfo(OrderInfoInVo inVo,HttpServletRequest request){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setHxUserId(user.getId());
        if(inVo==null||inVo.getOrderCode()==null||inVo.getHxUserId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        //1、参数校验--验证订单是否存在,是否被核销
        OrderInfoOut orderInfoOut = orderInfoService.getDetailByOrderCode(inVo.getOrderCode());
        if(orderInfoOut == null || orderInfoOut.getStatus() != OrderInfo.STATUS_WAIT_SH){
            return new BaseResp<Integer>(ResultStatus.error_order_is_used);
        }

        //2.判断订单是否已过期
        if(orderInfoOut.getExpiryDate().before(new Date())){
            return new BaseResp<Integer>(ResultStatus.error_order_over_date);
        }

        //3.验证扫码人id
        List<SalePointUser> salePointUserByUserId = salePointService.getSalePointUserByUserId(inVo.getHxUserId());
        if(salePointUserByUserId == null||salePointUserByUserId.size()==0){
            return new BaseResp<Integer>(ResultStatus.error_para_cashier_id);
        }
        inVo.setId(orderInfoOut.getId());
        inVo.setUserId(orderInfoOut.getUserId());
        inVo.setUserIp(IpUtils.getIpAddr(request));

        User userById = userService.getUserById(orderInfoOut.getUserId());
        if(userById!=null){
            inVo.setUserName(userById.getUserName());
        }

        Integer re =  orderInfoService.useOrderInfo(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

    public Shop getRandomShop(Integer shopNum){
        Integer [] ids=new Integer[]{500,501,502,503,504,505,506,507,508,509,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,560,561,562,563,564,565,566,567,568,569,570,571,572,573,574,575,576,577,578,579,580,581,582,583,584,585,586,587,588,589,590,591,592,593,594,595,596,597,598,599,600,601,602,603,604,605,606,607,608,609,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,630,631,632,633,634,635,636,637,638,639,640,641,642,643,644,645,646,647,648,649,650,651,652,653,654,655,656,657,658,659,660,661,662,663,664,665,666,667,668,669,670,671,672,673,674,675,676,677,678,679,680,681,682,683,684,685,686,687,688,689,690,691,692,693,694,695,696,697,698,699,700,701,702,703,704,705,706,707,708,709,710,711,712,713,714,715,716,717,718,719,720,721,722,723,724,725,726,727,728,729,730,731,732,733,734,735,736,737,738,739,740,741,742,743,744,745,746,747,748,749,750,751,752,753,754,755,756,757,758,759,760,761,762,763,764,765,766,767,768,769,770,771,772,773,774,775,776,777,778,779,780,781,782,783,784,785,786,787,788,789,790,791,792,793,794,795,796,797,798,799,800,801,802,803,804,805,806,807,808,809,810,811,812,813,814,815,816,817,818,819,820,821,822,823,824,825,826,827,828,829,830,831,832,833,834,835,836,837,838,839,840,841,842,843,844,845,846,847,848,849,850,851,852,853,854,855,856,857,858,859,860,861,862,863,864,865,866,867,868,869,870,871,872,873,874,875,876,878,879,880,881,882,883,884,885,886,887,888,889,890,891,892,893,894,895,896,897,898,899,900,901,902,903,904,905,906,907,908,909,910,911,912,913,914,915,916,917,918,919,920,921,922,923,924,925,926,927,928,929,930,931,932,933,934,935,936,937,938,939,940,941,942,943,944,945,946,947,948,949,950,951,952,953,954,955,956,957,958,959,960,961,962,963,964,965,966,967,968,969,970,971,972,973,974,975,976,977,978,979,980,981,982,983,984,985,986,987,988,989,990,991,992,993,994,995,996,997,998,999,1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,1014,1015,1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1029,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1045,1046,1047,1048,1049,1050,1051,1052,1053,1054,1055,1056,1057,1058,1059,1060,1061,1062,1063,1064,1065,1066,1067,1068,1069,1070,1071,1072,1073,1074,1075,1076,1077,1078,1079,1080,1081,1082,1083,1084,1085,1086,1087,1088,1089,1090,1221,1222,1223,1224,1225,1226,1227,1228,1229,1230,1231,1233,1234,1235,1236,1237,1238,1239,1240,1241,1242,1243,1244,1245,1246,1247,1248,1249,1250,1251,1252,1253,1254,1255,1256,1257,1258,1259,1260,1261,1262,1263,1264,1265,1266,1267,1268,1269,1270,1271,1272,1273,1274,1275,1276,1277,1278,1279,1280,1281,1282,1283,1284,1285,1286,1287,1288,1289,1290,1291,1292,1293,1294,1295,1296,1297,1298,1299,1300,1301,1302,1303,1304,1305,1306,1307,1308,1309,1310,1311,1312,1313,1314,1315,1316,1317,1318,1319,1320,1321,1322,1323,1324,1325,1326,1327,1328,1329,1330,1331,1332,1333,1334,1335,1336,1337,1338,1339,1340,1341,1342,1343,1344,1345,1346,1347,1348,1349,1350,1351,1352,1353,1354,1355,1356,1357,1358,1359,1360,1361,1362,1363,1364,1365,1366,1367,1368,1369,1370,1371,1372,1373,1374,1375,1376,1377,1378,1379,1380,1381,1382,1383,1384,1385,1386,1387,1388,1389,1390,1391,1392,1393,1394,1395,1396,1397,1398,1399,1400,1401,1402,1403};
        if(shopNum>ids.length){
            shopNum=ids.length;
        }
        Random random=new Random();
        Integer i=random.nextInt(shopNum);
        Long shopId=Long.valueOf(ids[i]);
        Shop shop =shopMapper.selectByPrimaryKey(shopId);
        if(shop==null||shop.getId()==null){
            shop=getRandomShop(shopNum);
        }
        return shop;
    }

    public User getRandomUser(Integer start,Integer end){
        Random random=new Random();
        Integer i=random.nextInt(end-start+1)+start;
        User user = userMapper.selectByPrimaryKey(Long.valueOf(i));
        if(user==null||user.getId()==null){
            user=getRandomUser(start, end);
        }
        return user;
    }

    public GoodsSku getRandomSkuByShop(Shop shop,Integer shopNum){
        List<GoodsSku> goodsSkuList = goodsSkuService.ListByshopId(shop.getId());
        if(goodsSkuList==null||goodsSkuList.size()<1){
            shop=getRandomShop(shopNum);
            return getRandomSkuByShop(shop,shopNum);
        }
        Random random=new Random();
        Integer i=random.nextInt(goodsSkuList.size());
        return goodsSkuList.get(i);
    }

    public Integer getRandomInt(Integer max){
        Random random=new Random();
        Integer i=random.nextInt(max)+1;
        return i;
    }

    public Integer getRadomXQSkuId(){
        Integer [] ids =new Integer[]{828,828,828,828,828,828,828,828,828,828,828,828,502,502,502,502,502,502,502,502,501,501,501,501,501,501,501,501,501,501,501,501,516,516,516,516,516,516,516,516,516,516,516,516,516,516,503,503,503,503,503,503,503,503,503,503,503,503,503,503,503,517,517,517,517,517,517,517,517,517,517,517,517,517,517,517,517,517,517,517,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,504,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,518,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,505,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,753,753,753,753,753,753,753,753,753,753,753,753,753,753,753,753,753,753,753,753,753,754,754,754,754,754,754,754,754,754,754,754,755,755,755,755,755,755,755,755,756,756,756,757,757,757};
        Random random=new Random();
        Integer i=random.nextInt(ids.length);
        Integer result=ids[i];
        return result;
    }

    public Integer getRandomHours(){
        Integer result=12;
        Integer random=getRandomInt(4853)+1;
        if(random<88){
            result=0;
        }else if(random<103){
            result=1;
        }else if(random<115){
            result=2;
        }else if(random<124){
            result=3;
        }else if(random<130){
            result=4;
        }else if(random<145){
            result=5;
        }else if(random<160){
            result=6;
        }else if(random<175){
            result=7;
        }else if(random<205){
            result=8;
        }else if(random<293){
            result=9;
        }else if(random<473){
            result=10;
        }else if(random<771){
            result=11;
        }else if(random<1218){
            result=12;
        }else if(random<1963){
            result=13;
        }else if(random<2261){
            result=14;
        }else if(random<2469){
            result=15;
        }else if(random<2707){
            result=16;
        }else if(random<3065){
            result=17;
        }else if(random<3512){
            result=18;
        }else if(random<3959){
            result=19;
        }else if(random<4257){
            result=20;
        }else if(random<4615){
            result=21;
        }else if(random<4734){
            result=22;
        }else if(random<4853){
            result=23;
        }
        return result;
    }

    /**
     * 批量创建免费订单
     *
     * 平台门店自提票券订单--分单(虚拟订单): userId:52121 userName:13339981235 shopId:0 payType:2 singleType:1
     * flagType(标记类型根据活动来区分)
     * (如果flagType为1,忽略,如果flagType不为1,再传actId)(砍价4,41) (秒杀5,44)
     * [{goodsSkuId:1256 goodsSpuId:233 goodsNum:1 orderItemShopId:32},...]
     * batchOrderInVo:[batchSize,beginTime,endTime,quitOrderNum,successOrderNum,xqNum,kjNum,msNum]
     * @param inVo
     * @param result
     * @return
     */
    @ApiOperation(value = "批量创建免费订单")
    @RequestMapping(value = "/batchCreateReal", method = RequestMethod.POST)
    public BaseResp<Long> batchCreateReal(@RequestBody @Valid OrderInfoInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        for(Integer i=0;i<946;i++){
            OrderInfoInVo infoInVo=new OrderInfoInVo();
            BeanUtils.copyProperties(inVo,infoInVo);
            try {
                Integer startNum=infoInVo.getBatchOrderInVo().getUserStart1();
                Integer endNum=infoInVo.getBatchOrderInVo().getUserEnd1();
                if(infoInVo.getBatchOrderInVo().getUserEnd2()!=null&&infoInVo.getBatchOrderInVo().getUserStart2()!=null&&infoInVo.getBatchOrderInVo().getUserSum2()!=null&&infoInVo.getBatchOrderInVo().getUserSum1()!=null){
                    Integer radam= getRandomInt(infoInVo.getBatchOrderInVo().getUserSum1()+infoInVo.getBatchOrderInVo().getUserSum2());
                    if (radam>infoInVo.getBatchOrderInVo().getUserSum1()){
                        endNum=infoInVo.getBatchOrderInVo().getUserEnd2();
                        startNum=infoInVo.getBatchOrderInVo().getUserStart2();
                    }
                }

                User user = getRandomUser(startNum,endNum);
                Long shopId=Long.valueOf(getShopId(i));
                Shop shop =shopMapper.selectByPrimaryKey(shopId);

                GoodsSku goodsSku = new GoodsSku();
                Integer randomNum = getRandomInt(10);
                OrderItem orderItem = new OrderItem();

                Integer random = getRandomInt(10);
                GoodsSkuInVo goodsSkuInVo=new GoodsSkuInVo();
                goodsSkuInVo.setShopId(shopId);
                goodsSkuInVo.setActId(41l);
                List<GoodsSkuOut> list = goodsSkuMapper.selectActList(goodsSkuInVo);
                if(random<=6&&list!=null&&list.size()>0){
                    Integer random2=getRandomInt(list.size());
                    goodsSku.setId(list.get(random2-1).getId());
                    goodsSku.setSpuId(list.get(random2-1).getSpuId());
                    infoInVo.setFlagType(4);
                    infoInVo.setActId(41l);
                    orderItem.setOrderItemShopId(shop.getId());
                    infoInVo.setShopId(shop.getId());
                }else if(random==7){
                    goodsSku = goodsSkuService.selectOne(502l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if (random==8){
                    goodsSku = goodsSkuService.selectOne(501l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if (random==9){
                    goodsSku = goodsSkuService.selectOne(516l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else if (random==10){
                    goodsSku = goodsSkuService.selectOne(503l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }else{
                    goodsSku = goodsSkuService.selectOne(516l);
                    infoInVo.setFlagType(1);
                    orderItem.setOrderItemShopId(0l);
                    infoInVo.setShopId(0l);
                }

                Integer randomNums = getRandomInt(10);
                if(infoInVo.getBatchOrderInVo().getQuitOrderNum()>0&&infoInVo.getBatchOrderInVo().getQuitOrderNum()>=randomNums){
                    infoInVo.getBatchOrderInVo().setOrderStatus(BatchOrderInVo.ORDER_STATUS_QUIT);
                }else {
                    infoInVo.getBatchOrderInVo().setOrderStatus(BatchOrderInVo. ORDER_STATUS_SUCCESS);
                }

                orderItem.setGoodsSkuId(goodsSku.getId());
                orderItem.setGoodsSpuId(goodsSku.getSpuId());
                orderItem.setGoodsNum(1);
                infoInVo.getBatchOrderInVo().setHxShopId(shop.getId());
                infoInVo.getBatchOrderInVo().setHxShopName(shop.getShopName());
                List<OrderItem> orderItemList = new ArrayList<>();
                orderItemList.add(orderItem);
                infoInVo.setOrderItemList(orderItemList);
                infoInVo.setSingleType(1);
                infoInVo.setPayType(2);
                infoInVo.setCreateType(1);
                Date createTime = GetUserInfoUtil.randomDate(infoInVo.getBatchOrderInVo().getBeginTime(), infoInVo.getBatchOrderInVo().getEndTime());
                createTime.setHours(getRandomHours());
                infoInVo.setCreateTime(createTime);
                infoInVo.setUserId(user.getId());
                infoInVo.setUserName(user.getUserName());
                infoInVo.setUserIp(GetUserInfoUtil.getRandomIp());
                infoInVo.setSourceType(OrderInfo.SOURCE_TYPE_PT);

                Long id = orderInfoService.batchCreateForXq(infoInVo);

                System.out.print("执行到第："+i+"条");
            }catch (Exception e){
            }
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS, null);
    }

    public Integer getShopId(Integer index){
        Integer shopId=0;
        if (index < 1) {
            shopId = 318;
        } else if (index < 2) {
            shopId = 288;
        } else if (index < 3) {
            shopId = 277;
        } else if (index < 5) {
            shopId = 269;
        } else if (index < 7) {
            shopId = 327;
        } else if (index < 9) {
            shopId = 375;
        } else if (index < 11) {
            shopId = 306;
        } else if (index < 13) {
            shopId = 324;
        } else if (index < 15) {
            shopId = 333;
        } else if (index < 18) {
            shopId = 411;
        } else if (index < 21) {
            shopId = 289;
        } else if (index < 24) {
            shopId = 361;
        } else if (index < 27) {
            shopId = 313;
        } else if (index < 31) {
            shopId = 169;
        } else if (index < 35) {
            shopId = 329;
        } else if (index < 40) {
            shopId = 338;
        } else if (index < 45) {
            shopId = 398;
        } else if (index < 51) {
            shopId = 341;
        } else if (index < 57) {
            shopId = 397;
        } else if (index < 63) {
            shopId = 301;
        } else if (index < 69) {
            shopId = 270;
        } else if (index < 75) {
            shopId = 314;
        } else if (index < 82) {
            shopId = 424;
        } else if (index < 89) {
            shopId = 428;
        } else if (index < 96) {
            shopId = 302;
        } else if (index < 103) {
            shopId = 297;
        } else if (index < 110) {
            shopId = 325;
        } else if (index < 117) {
            shopId = 326;
        } else if (index < 125) {
            shopId = 310;
        } else if (index < 133) {
            shopId = 295;
        } else if (index < 141) {
            shopId = 403;
        } else if (index < 149) {
            shopId = 378;
        } else if (index < 157) {
            shopId = 335;
        } else if (index < 165) {
            shopId = 293;
        } else if (index < 173) {
            shopId = 376;
        } else if (index < 181) {
            shopId = 274;
        } else if (index < 189) {
            shopId = 337;
        } else if (index < 198) {
            shopId = 273;
        } else if (index < 207) {
            shopId = 315;
        } else if (index < 216) {
            shopId = 307;
        } else if (index < 225) {
            shopId = 402;
        } else if (index < 234) {
            shopId = 426;
        } else if (index < 243) {
            shopId = 384;
        } else if (index < 252) {
            shopId = 342;
        } else if (index < 261) {
            shopId = 399;
        } else if (index < 270) {
            shopId = 422;
        } else if (index < 280) {
            shopId = 380;
        } else if (index < 290) {
            shopId = 421;
        } else if (index < 302) {
            shopId = 347;
        } else if (index < 314) {
            shopId = 390;
        } else if (index < 326) {
            shopId = 414;
        } else if (index < 338) {
            shopId = 357;
        } else if (index < 350) {
            shopId = 360;
        } else if (index < 362) {
            shopId = 362;
        } else if (index < 374) {
            shopId = 356;
        } else if (index < 386) {
            shopId = 349;
        } else if (index < 398) {
            shopId = 373;
        } else if (index < 411) {
            shopId = 291;
        } else if (index < 424) {
            shopId = 359;
        } else if (index < 437) {
            shopId = 343;
        } else if (index < 450) {
            shopId = 268;
        } else if (index < 463) {
            shopId = 294;
        } else if (index < 476) {
            shopId = 292;
        } else if (index < 489) {
            shopId = 372;
        } else if (index < 503) {
            shopId = 276;
        } else if (index < 517) {
            shopId = 364;
        } else if (index < 531) {
            shopId = 287;
        } else if (index < 545) {
            shopId = 385;
        } else if (index < 559) {
            shopId = 331;
        } else if (index < 573) {
            shopId = 303;
        } else if (index < 588) {
            shopId = 409;
        } else if (index < 603) {
            shopId = 377;
        } else if (index < 618) {
            shopId = 351;
        } else if (index < 634) {
            shopId = 367;
        } else if (index < 650) {
            shopId = 336;
        } else if (index < 666) {
            shopId = 423;
        } else if (index < 682) {
            shopId = 374;
        } else if (index < 698) {
            shopId = 316;
        } else if (index < 714) {
            shopId = 311;
        } else if (index < 730) {
            shopId = 408;
        } else if (index < 747) {
            shopId = 285;
        } else if (index < 764) {
            shopId = 418;
        } else if (index < 782) {
            shopId = 395;
        } else if (index < 800) {
            shopId = 358;
        } else if (index < 822) {
            shopId = 348;
        } else if (index < 845) {
            shopId = 353;
        } else if (index < 868) {
            shopId = 410;
        } else if (index < 891) {
            shopId = 368;
        } else if (index < 918) {
            shopId = 352;
        } else if (index < 946) {
            shopId = 391;
        }

        return shopId;
    }

    /**
     * 判断是否是首次下单用户 0新用户 其他 老用户
     * @param userId
     * @return
     */
    @ApiOperation(value = "判断是否是首次下单用户")
    @RequestMapping(value = "/isNewUser",method = RequestMethod.GET)
    public BaseResp<Integer> isNewUser(Long userId){
        if(userId==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer newUser = orderInfoService.isNewUser(userId);
        if(newUser==0){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,0);
        }
        return new BaseResp<Integer>(ResultStatus.FAIL,newUser);
    }

    /**
     * 万达活动订单查询
     * startTime=2019-01-17&endTime=2019-02-22&shopZoneItemId=5
     * @param modelMap
     * @param inVo
     * @param request
     * @param response
     */
    @ApiOperation(value = "万达活动订单查询")
    @RequestMapping(value = "/selectWanDaWriteOff",method = RequestMethod.GET)
    public void selectWanDaWriteOff(ModelMap modelMap, OrderWriteOffResultOut inVo, HttpServletRequest request,
                                    HttpServletResponse response) {

        TemplateExportParams params = new TemplateExportParams(
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + "doc" + File.separator + "wandaOrder.xlsx");
        Map<String,Object> mapColl = orderWriteOffService.selectWanDaWriteOff(inVo);

        modelMap.put(TemplateExcelConstants.FILE_NAME,mapColl.get("shopZoneName"));
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, mapColl);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);

    }

}
