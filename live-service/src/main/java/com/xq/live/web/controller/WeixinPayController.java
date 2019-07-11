package com.xq.live.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.BaseResp;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.CostWeightConfig;
import com.xq.live.dao.ActGoodsSkuMapper;
import com.xq.live.dao.ActOrderMapper;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.PayUtils;
import com.xq.live.web.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付服务端controller
 * @author zhangpeng32
 * @date 2018-03-09 10:54
 * @copyright:hbxq
 */
@RestController
@RequestMapping("/wxpay")
public class WeixinPayController {
    private Logger logger = Logger.getLogger(WeixinPayController.class);

    @Autowired
    private SoService soService;

    @Autowired
    private ShopAllocationService shopAllocationService;

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private ActGoodsSkuMapper actGoodsSkuMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private ActSkuService actSkuService;

    @Autowired
    private ShopCashierService shopCashierService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private DeliveryCostService deliveryCostService;

    @Autowired
    private CostWeightConfig costWeightConfig;

    @Autowired
    private OrderAddressService orderAddressService;

    @Autowired
    private GoodsGoldLogService goodsGoldLogService;

    @Autowired
    private OrderCouponPostageService orderCouponPostageService;

    @Autowired
    private RedisCache redisCache;




    private WXPay wxpay;
    private PaymentConfig config;

    public WeixinPayController() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
    }

    /**
     * @Description: 获取openId
     * @param: code
     * @Author: zhangpeng32
     * @Date: 2018/3/11 17:39
     * @Version: 1.0.0
     */
    @RequestMapping(value = "/getOpenId", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> getOpenId(String code, HttpServletRequest request) throws Exception{
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_weixin_user_code_empty);
        }
        //获取openId
        String param = "?grant_type=" + PaymentConfig.GRANT_TYPE + "&appid=" + PaymentConfig.APPID + "&secret=" + PaymentConfig.APP_SECRET + "&js_code=" + code;
        System.out.println(PaymentConfig.GET_OPEN_ID_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.GET_OPEN_ID_URL, "GET", param);
        Map<String, String> result = new HashMap<String, String>();
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return new BaseResp<Map<String, String>>(errcode, jsonObject.getString("errmsg"), null);
            }
            result.put("openId", jsonObject.getString("openid"));
            result.put("sessionKey", jsonObject.getString("session_key"));
            result.put("unionId",jsonObject.getString("unionid"));
        }
        return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 微信支付调用后台
     *
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> pay(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        try {
            //生成的随机字符串
            String nonce_str = WXPayUtil.generateNonceStr();
            ;
            //获取客户端的ip地址
            String spbill_create_ip = IpUtils.getIpAddr(request);

            int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", PaymentConfig.APPID);
            packageParams.put("mch_id", PaymentConfig.MCH_ID);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", soOut.getSkuName());    //商品描述
            //            packageParams.put("detail", "testdetail");    //商品描述
            packageParams.put("out_trade_no", soOut.getId().toString());//商户订单号
            packageParams.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", PaymentConfig.WX_NOTIFY_URL);//支付成功后的回调地址
            packageParams.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
            packageParams.put("openid", inVo.getOpenId());

            String reqStrXMl = WXPayUtil.mapToXml(packageParams);
            System.out.println("reqStrXMl : " + reqStrXMl);
            String sign = WXPayUtil.generateSignature(packageParams, PaymentConfig.API_KEY);
            System.out.println("sign : " + sign);
            String xml = WXPayUtil.generateSignedXml(packageParams, PaymentConfig.API_KEY);

            System.out.println("xml : " + xml);
            //调用统一下单接口，并接受返回的结果
            String result = PayUtils.httpRequest(PaymentConfig.UNIFIED_ORDER_URL, "POST", xml);


            System.out.println("调试模式_统一下单接口 返回XML数据：" + result);

            // 将解析结果存储在HashMap中
            Map map = WXPayUtil.xmlToMap(result);

            String return_code = (String) map.get("return_code");//返回状态码

            Map<String, String> response = new HashMap<String, String>();//返回给小程序端需要的参数
            response.put("appid", PaymentConfig.APPID);
            if (return_code == "SUCCESS" || return_code.equals(return_code)) {
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                response.put("prepayid", prepay_id);
                response.put("noncestr", nonce_str);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                response.put("package", "Sign=WXPay");
                //                //拼接签名需要的参数
                //                String stringSignTemp = "appId=" + PaymentConfig.APPID + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                //                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                //                String paySign = PayUtils.sign(stringSignTemp, PaymentConfig.API_KEY, "utf-8").toUpperCase();
                String paySign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", paySign);
            }

            return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResp<Map<String, String>>(ResultStatus.FAIL);
    }

    @RequestMapping(value = "/doUnifiedOrder", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrder(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", soOut.getSkuName());    //商品描述
        data.put("out_trade_no", soOut.getId().toString());//商户订单号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    @RequestMapping(value = "/doUnifiedOrderForShop", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrderForShop(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        /*SoOut soOut = soService.get(inVo.getSoId());*/
        SoOut soOut = soService.selectByPkForShop(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //如果商家订单中买了券，则必须要传入couponId这个参数
        if(soOut.getSkuId()!=null){
        //商家订单中判断couponId是否为空
        if(inVo.getCouponId()==null){
           return new BaseResp<Map<String, String>>(ResultStatus.error_para_coupon_id_empty);
           }
        }

        //商家订单中判断shopId是否为空
        if(inVo.getShopId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_para_shop_id_empty);
        }

        ShopAllocationInVo shopAllocationInVo = new ShopAllocationInVo();
        shopAllocationInVo.setShopId(inVo.getShopId());
        ShopAllocationOut admin = shopAllocationService.admin(shopAllocationInVo);

        //判断该商家是否配置付款方式
        if(admin==null){
          return new BaseResp<Map<String, String>>(ResultStatus.error_para_shop_allocation_empty);
        }

        //判断商家信息是否完全(商家是否配置管理员)
        ShopCashier shopCashier = shopCashierService.adminByShopId(inVo.getShopId());
        if(shopCashier==null){
          return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_DS){
            //平台代收所需参数
            data.put("appid", config.getAppID());
            data.put("mch_id", config.getMchID());
            data.put("nonce_str", nonce_str);
            if(soOut.getSkuName()!=null) {
                data.put("body", soOut.getSkuName());    //商品描述
            }else{
                data.put("body", "商家订单");
            }
            data.put("out_trade_no", soOut.getId().toString());//商户订单号
            data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            data.put("spbill_create_ip", spbill_create_ip);
            data.put("notify_url", PaymentConfig.WX_NOTIFY_SHOP_URL);//支付成功后的回调地址
            data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
            data.put("openid", inVo.getOpenId());
            if(inVo.getCouponId()!=null) {
                data.put("attach", inVo.getCouponId() + "," + inVo.getShopId());//自定义参数，返回给回调接口
            }else{
                data.put("attach", "," + inVo.getShopId());//自定义参数，返回给回调接口
            }
        }else if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_ZS){
            //可以参考此链接 https://developers.weixin.qq.com/blogdetail?action=get_post_info&lang=zh_CN&token=&docid=0f0110d772c9d219296b54d4665b7001
            //商家自收,此段代码的参数需要修改
            data.put("appid", config.getFW_APP_ID());//服务商appid
            data.put("mch_id", config.getFW_MCH_ID());//服务商mch_id
            data.put("nonce_str", nonce_str);
            if(soOut.getSkuName()!=null) {
                data.put("body", soOut.getSkuName());    //商品描述
            }else{
                data.put("body", "商家订单");
            }
            data.put("out_trade_no", soOut.getId().toString());//商户订单号
            data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            data.put("spbill_create_ip", spbill_create_ip);
            data.put("notify_url", PaymentConfig.WX_NOTIFY_SHOP_URL);//支付成功后的回调地址
            data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
            data.put("sub_openid", inVo.getOpenId());
            data.put("sub_appid", config.getAppID());
            data.put("sub_mch_id", inVo.getSubMchId());
            if(inVo.getCouponId()!=null) {
                data.put("attach", inVo.getCouponId() + "," + inVo.getShopId());//自定义参数，返回给回调接口
            }else{
                data.put("attach", "," + inVo.getShopId());//自定义参数，返回给回调接口
            }
        }



        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_DS){
            //平台代收二次签名所需参数
            response.put("appId", config.getAppID());
        }else if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_ZS){
            //可以参考此链接 https://developers.weixin.qq.com/blogdetail?action=get_post_info&lang=zh_CN&token=&docid=0f0110d772c9d219296b54d4665b7001
            //商家自收二次签名所需参数
            response.put("sub_appid", config.getAppID());//不知道是sub_appid还是sub_appId
        }
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    @RequestMapping(value = "/doUnifiedOrderForAct", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrderForAct(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //参数校验
        if(inVo.getActId()==null||inVo.getSkuId()==null||inVo.getShopId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //判断购买的券中所对应的推荐菜是否已报名，如果未报名则返回，涉及到钱的地方，各种限制死
        ActSkuInVo actSkuInVo = new ActSkuInVo();
        actSkuInVo.setActId(inVo.getActId());
        actSkuInVo.setSkuId(inVo.getSkuId());
        ActSkuOut byInVo = actSkuService.findByInVo(actSkuInVo);
        if(byInVo==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_actuser_code);
        }

        //判断商家信息是否完全(商家是否配置管理员)
        ShopCashier shopCashier = shopCashierService.adminByShopId(inVo.getShopId());
        if(shopCashier==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }

        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", soOut.getSkuName());    //商品描述
        data.put("out_trade_no", soOut.getId().toString());//商户订单号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_ACT_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());
        data.put("attach", inVo.getActId() + "," + inVo.getSkuId()+","+inVo.getShopId());//自定义参数，返回给回调接口

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 购买砍价券
     *
     * 注意:这里存在两种情况，一个是发起分享之后的购买，二是直接按照原价购买
     * 这两种情况要通过一个type来判断   购买类型  1.原价购买 2.砍价购买
     *
     * 注:soId,openId,skuId(购买的砍价菜),shopId,type,(groupId)
     * @param inVo
     * @param request
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/doUnifiedOrderForKj", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrderForKj(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //参数校验
        if(inVo.getSkuId()==null||inVo.getShopId()==null||inVo.getType()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //判断所买的砍价菜是否已经下架
        Sku sku = skuService.get(inVo.getSkuId());
        if(sku==null||sku.getStockNum()<=0||sku.getIsDeleted()==Sku.SKU_IS_DELETED){
            return new BaseResp<Map<String, String>>(ResultStatus.error_sku_is_deleted);
        }

        //砍价购买
        if(inVo.getType()==WeixinInVo.WEI_XIN_TYPE_KJ) {
            if(inVo.getGroupId()==null){
                return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
            }
            //判断缓存是否存在(如果缓存不存在，证明已经超过了24小时的购买时间，则不让购买)


        }


        //判断商家信息是否完全(商家是否配置管理员)
        ShopCashier shopCashier = shopCashierService.adminByShopId(inVo.getShopId());
        if(shopCashier==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }

        //传递给返回地址的参数(可以自定义加入一些参数，但是要注意转成json之后，总长度不能超过132)
        WeixinInVo attach = new WeixinInVo();
        attach.setSkuId(inVo.getSkuId());
        attach.setShopId(inVo.getShopId());
        attach.setType(inVo.getType());
        if(inVo.getGroupId()!=null){
            attach.setGroupId(inVo.getGroupId());
        }

        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", soOut.getSkuName());    //商品描述
        data.put("out_trade_no", soOut.getId().toString());//商户订单号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_KJ_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());
        data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 购买抢购券
     *
     *
     * 注:soId,openId,skuId(购买的抢购菜),shopId ,还有必传的抢购菜的缓存信息
     * @param inVo
     * @param request
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/doUnifiedOrderForQg", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrderForQg(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //参数校验
        if(inVo.getSkuId()==null||inVo.getShopId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //判断所买的抢购菜是否已经下架
        Sku sku = skuService.get(inVo.getSkuId());
        if(sku==null||sku.getStockNum()<=0||sku.getIsDeleted()==Sku.SKU_IS_DELETED){
            return new BaseResp<Map<String, String>>(ResultStatus.error_sku_is_deleted);
        }

        //判断商家信息是否完全(商家是否配置管理员)
        ShopCashier shopCashier = shopCashierService.adminByShopId(inVo.getShopId());
        if(shopCashier==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }

        //传递给返回地址的参数(可以自定义加入一些参数，但是要注意转成json之后，总长度不能超过132)
        WeixinInVo attach = new WeixinInVo();
        attach.setSkuId(inVo.getSkuId());
        attach.setShopId(inVo.getShopId());


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", soOut.getSkuName());    //商品描述
        data.put("out_trade_no", soOut.getId().toString());//商户订单号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_QG_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());
        data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 购买兑换券
     *
     *
     * 注:soId,openId,skuId(购买的兑换菜),shopId ,还有必传的兑换菜的缓存信息 type,(groupId)
     * 通过一个type来判断   购买类型  2.砍价购买
     * @param inVo
     * @param request
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/doUnifiedOrderForDh", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrderForDh(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //砍价购买
        if(inVo.getType()==WeixinInVo.WEI_XIN_TYPE_KJ) {
            if(inVo.getGroupId()==null){
                return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
            }
            //判断缓存是否存在(如果缓存不存在，证明已经超过了24小时的购买时间，则不让购买)
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //参数校验
        if(inVo.getSkuId()==null||inVo.getShopId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //判断所买的兑换菜是否已经下架
        Sku sku = skuService.get(inVo.getSkuId());
        if(sku==null||sku.getStockNum()<=0||sku.getIsDeleted()==Sku.SKU_IS_DELETED){
            return new BaseResp<Map<String, String>>(ResultStatus.error_sku_is_deleted);
        }

        //判断商家信息是否完全(商家是否配置管理员)
        ShopCashier shopCashier = shopCashierService.adminByShopId(inVo.getShopId());
        if(shopCashier==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }

        //传递给返回地址的参数(可以自定义加入一些参数，但是要注意转成json之后，总长度不能超过132)
        WeixinInVo attach = new WeixinInVo();
        attach.setSkuId(inVo.getSkuId());
        attach.setShopId(inVo.getShopId());
        /*--*/
        attach.setType(inVo.getType());
        if(inVo.getGroupId()!=null){
            attach.setGroupId(inVo.getGroupId());
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", soOut.getSkuName());    //商品描述
        data.put("out_trade_no", soOut.getId().toString());//商户订单号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_DH_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());
        data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }


    /**
     * 购买商城系统订单
     *
     *
     * 注:orderId,openId
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/doUnifiedOrderForShoppingMall", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrderForShoppingMall(WeixinInVo inVo, HttpServletRequest request) {
        if(inVo.getOrderId()==null||inVo.getOpenId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //根据id获取订单信息
        OrderInfoOut orderInfoOut=orderInfoService.getDetail(inVo.getOrderId());
        if (orderInfoOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (orderInfoOut.getStatus() != OrderInfo.STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //订单中部分商品已下架或已删除
        for (OrderItemOut orderItemOut:orderInfoOut.getOrderItemOuts()){
            if (orderItemOut.getGoodsIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED||
                    orderItemOut.getGoodsStatus()==GoodsSku.STATUS_XJ){
                return new BaseResp<Map<String, String>>(ResultStatus.error_goods_is_deleted);
            }
        }
        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", "享七商城");    //商品描述
        data.put("out_trade_no", orderInfoOut.getOrderCode());//商户订单标号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_SM_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 购买商城系统礼品卡订单------老接口以后要删除
     *
     * 礼品卡订单(属于虚拟订单,没有购物车的概念)
     * 注:orderId,openId
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/shoppingMallForCoupon", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> shoppingMallForCoupon(WeixinInVo inVo, HttpServletRequest request) {
        if(inVo.getOrderId()==null||inVo.getOpenId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //根据id获取订单信息
        OrderInfoOut orderInfoOut=orderInfoService.getDetail(inVo.getOrderId());
        if (orderInfoOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (orderInfoOut.getStatus() != OrderInfo.STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //订单中部分商品已下架或已删除
        for (OrderItemOut orderItemOut:orderInfoOut.getOrderItemOuts()){
            if (orderItemOut.getGoodsIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED||
                    orderItemOut.getGoodsStatus()==GoodsSku.STATUS_XJ){
                return new BaseResp<Map<String, String>>(ResultStatus.error_goods_is_deleted);
            }
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", "享七商城");    //商品描述
        data.put("out_trade_no", orderInfoOut.getOrderCode());//商户订单标号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_SMC_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 购买商城系统礼品卡订单(新)
     *
     * 礼品卡订单(属于虚拟订单,没有购物车的概念)
     * 注:orderId,openId,(type)
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/shoppingMallForCouponNew", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> shoppingMallForCouponNew(WeixinInVo inVo, HttpServletRequest request) {
        if(inVo.getOrderId()==null||inVo.getOpenId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //根据id获取订单信息
        OrderInfoOut orderInfoOut=orderInfoService.getDetail(inVo.getOrderId());
        if (orderInfoOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (orderInfoOut.getStatus() != OrderInfo.STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //订单中部分商品已下架或已删除
        for (OrderItemOut orderItemOut:orderInfoOut.getOrderItemOuts()){
            if (orderItemOut.getGoodsIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED||
                    orderItemOut.getGoodsStatus()==GoodsSku.STATUS_XJ){
                return new BaseResp<Map<String, String>>(ResultStatus.error_goods_is_deleted);
            }
        }

        //砍价购买
        GoodsBargainLogOut goodsBargainLogOut = new GoodsBargainLogOut();
        if(inVo.getType()==WeixinInVo.WEI_XIN_TYPE_SHOP_KJ) {
            //判断缓存是否存在(如果缓存不存在，证明已经超过了1小时的购买时间，则不让购买)
            GoodsGoldLogInVo goodsGoldLogInVo = new GoodsGoldLogInVo();
            ActOrderOut actOrderOut=actOrderMapper.selectFirstDistributionByOrderId(inVo.getOrderId());
            goodsGoldLogInVo.setActId(actOrderOut.getActGoodsSku().getActId());
            goodsGoldLogInVo.setParentId(orderInfoOut.getUserId());
            goodsGoldLogInVo.setGroupId(inVo.getGroupId());
            goodsGoldLogInVo.setRefId(orderInfoOut.getOrderItemOuts().get(0).getGoodsSkuId());
            goodsBargainLogOut = goodsGoldLogService.selectByRedis(goodsGoldLogInVo);
            if (goodsBargainLogOut==null){
                return new BaseResp<Map<String, String>>(ResultStatus.error_sku_too_many_pay);
            }
        }
        if (inVo.getType()==WeixinInVo.WEI_XIN_TYPE_SHOP_TG){
            ActOrderOut actOrder=actOrderMapper.selectFirstDistributionByOrderId(inVo.getOrderId());
            ActGoodsSku actGoodsSku=actGoodsSkuMapper.selectByPrimaryKey(actOrder.getActGoodsSkuId());
            if (actGoodsSku.getCurrentNum()>=actGoodsSku.getPeopleNum()){
                return new BaseResp<Map<String, String>>(ResultStatus.error_group_hasNumber_man);
            }
        }

        //传递给返回地址的参数(可以自定义加入一些参数，但是要注意转成json之后，总长度不能超过132)
        WeixinInVo attach = new WeixinInVo();
        attach.setType(inVo.getType());
        if(goodsBargainLogOut.getGroupId()!=null){
            attach.setGroupId(inVo.getGroupId());
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", "享七商城");    //商品描述
        data.put("out_trade_no", orderInfoOut.getOrderCode());//商户订单标号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_SMC_NEW_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());
        data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     *
     * 商城系统商家订单
     *
     * orderId,openId,(orderCouponId)
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/shoppingMallForShopOrder", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> shoppingMallForShopOrder(WeixinInVo inVo, HttpServletRequest request) {
        if(inVo.getOrderId()==null||inVo.getOpenId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //根据id获取订单信息
        OrderInfoOut orderInfoOut=orderInfoService.getDetail(inVo.getOrderId());
        if (orderInfoOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (orderInfoOut.getStatus() != OrderInfo.STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        if(orderInfoOut.getSourceType()!=OrderInfo.SOURCE_TYPE_SJ){
            return new BaseResp<Map<String, String>>(ResultStatus.error_order_source_type);
        }
        OrderItemOut orderItemOut = null;
        //如果商家订单中买了券，则必须要传入couponId这个参数
        if(orderInfoOut.getOrderItemOuts()!=null&&orderInfoOut.getOrderItemOuts().size()>0){
            orderItemOut = orderInfoOut.getOrderItemOuts().get(0);
            //商家订单中判断orderCouponId是否为空
            if(inVo.getOrderCouponId()==null){
                return new BaseResp<Map<String, String>>(ResultStatus.error_para_coupon_id_empty);
            }
        }

        ShopAllocationInVo shopAllocationInVo = new ShopAllocationInVo();
        shopAllocationInVo.setShopId(orderInfoOut.getShopId());
        ShopAllocationOut admin = shopAllocationService.admin(shopAllocationInVo);

        //判断该商家是否配置付款方式
        if(admin==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_para_shop_allocation_empty);
        }

        //判断商家信息是否完全(商家是否配置管理员)
        ShopCashier shopCashier = shopCashierService.adminByShopId(orderInfoOut.getShopId());
        if(shopCashier==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }

        //传递给返回地址的参数(可以自定义加入一些参数，但是要注意转成json之后，总长度不能超过132)
        WeixinInVo attach = new WeixinInVo();
        attach.setOrderCouponId(inVo.getOrderCouponId());
        attach.setShopId(orderInfoOut.getShopId());


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_DS){
            //平台代收所需参数
            data.put("appid", config.getAppID());
            data.put("mch_id", config.getMchID());
            data.put("nonce_str", nonce_str);
            if(orderItemOut!=null) {
                data.put("body", orderItemOut.getGoodsSkuName());    //商品描述
            }else{
                data.put("body", "商家订单");
            }
            data.put("out_trade_no", orderInfoOut.getOrderCode());//商户订单号
            data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            data.put("spbill_create_ip", spbill_create_ip);
            data.put("notify_url", PaymentConfig.WX_NOTIFY_SHOP_ORDER_URL);//支付成功后的回调地址
            data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
            data.put("openid", inVo.getOpenId());
            data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口
        }else if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_ZS){
            //可以参考此链接 https://developers.weixin.qq.com/blogdetail?action=get_post_info&lang=zh_CN&token=&docid=0f0110d772c9d219296b54d4665b7001
            //商家自收,此段代码的参数需要修改
            data.put("appid", config.getFW_APP_ID());//服务商appid
            data.put("mch_id", config.getFW_MCH_ID());//服务商mch_id
            data.put("nonce_str", nonce_str);
            if(orderItemOut!=null) {
                data.put("body", orderItemOut.getGoodsSkuName());    //商品描述
            }else{
                data.put("body", "商家订单");
            }
            data.put("out_trade_no", orderInfoOut.getOrderCode());//商户订单号
            data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            data.put("spbill_create_ip", spbill_create_ip);
            data.put("notify_url", PaymentConfig.WX_NOTIFY_SHOP_URL);//支付成功后的回调地址
            data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
            data.put("sub_openid", inVo.getOpenId());
            data.put("sub_appid", config.getAppID());
            data.put("sub_mch_id", inVo.getSubMchId());
            data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口
        }



        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_DS){
            //平台代收二次签名所需参数
            response.put("appId", config.getAppID());
        }else if(admin.getPaymentMethod()==ShopAllocation.SHOP_ALLOCATION_ZS){
            //可以参考此链接 https://developers.weixin.qq.com/blogdetail?action=get_post_info&lang=zh_CN&token=&docid=0f0110d772c9d219296b54d4665b7001
            //商家自收二次签名所需参数
            response.put("sub_appid", config.getAppID());//不知道是sub_appid还是sub_appId
        }
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 购买商城系统礼品卡订单(单独计算其运费接口)(老)------这个接口以后删除
     *
     * 礼品卡订单(目前暂不支持购物车模式)
     * 注:orderCouponId,orderAddressId,realWeight,templateId,openId
     *
     *
     * 注意:userId是当前用户的id，要从网关中获取
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/orderCouponForSendAmount", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> orderCouponForSendAmount(WeixinInVo inVo, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if(inVo.getOrderCouponId()==null||inVo.getOrderAddressId()==null
                ||inVo.getRealWeight()==null||inVo.getTemplateId()==null
                ||inVo.getUserId()==null||inVo.getOpenId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //根据orderCouponId获取票券信息
        OrderCouponOut orderCouponOut = orderCouponService.selectById(inVo.getOrderCouponId());
        if (orderCouponOut == null||orderCouponOut.getIsUsed()==OrderCoupon.ORDER_COUPON_IS_USED_YES) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_coupon_is_used);
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        //计算实际重量
        BigDecimal realWeight = this.subRealWeight(inVo.getRealWeight().doubleValue());

        //查询收货地址信息
        OrderAddressOut address = orderAddressService.getAddress(inVo.getOrderAddressId());
        if(address==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_fail_address);
        }
        OrderAddressInVo orderAddressInVo = new OrderAddressInVo();
        orderAddressInVo.setDictProvinceId(address.getDictProvinceId());
        orderAddressInVo.setDictCityId(address.getDictCityId());
        //计算单个订单项的运费
        BigDecimal sendAmount = deliveryCostService
                .calculateCost(orderAddressInVo, realWeight,inVo.getPiece(),inVo.getBulk(), inVo.getTemplateId(),inVo.getFormulaMode());

        //传递给返回地址的参数(可以自定义加入一些参数，但是要注意转成json之后，总长度不能超过132)
        WeixinInVo attach = new WeixinInVo();
        attach.setUserId(inVo.getUserId());

        //int price100 = sendAmount.multiply(new BigDecimal(100)).intValue();
        Long LongTime = System.currentTimeMillis() / 1000;
        int price100 = new BigDecimal(30).multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", "享七商城");    //商品描述
        data.put("out_trade_no", orderCouponOut.getCouponCode()+"_"+LongTime.toString());//商户订单标号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_CSA_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());
        data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }


    /**
     * 购买商城系统礼品卡订单(单独计算其运费接口)(新中新)
     * 礼品卡订单(目前暂不支持购物车模式)
     * 注意:userId是当前用户的id，要从网关中获取
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/orderCouponForSendAmountV1", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> orderCouponForSendAmountV1(OrderCouponPostageInVo inVo, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if(inVo==null||inVo.getId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }
        OrderCouponPostage postage=orderCouponPostageService.selectInfo(inVo);
        //根据orderCouponId获取票券信息
        OrderCouponOut orderCouponOut = orderCouponService.selectById(postage.getOrderCouponId());
        if (orderCouponOut == null||orderCouponOut.getIsUsed()==OrderCoupon.ORDER_COUPON_IS_USED_YES) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_coupon_is_used);
        }
        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        //传递给返回地址的参数(可以自定义加入一些参数，但是要注意转成json之后，总长度不能超过132)
        WeixinInVo attach = new WeixinInVo();
        attach.setUserId(inVo.getUserId());
        attach.setOrderCouponPostageId(inVo.getId());

        int price100 = postage.getSendAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", "享七商城");    //商品描述
        data.put("out_trade_no", postage.getOutTradeNo());//商户订单标号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_CSA_URLV1);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());
        data.put("attach", JSON.toJSONString(attach));//自定义参数，返回给回调接口

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }



    /**
     * 购买商城系统门店自提订单
     *
     * 门店自提订单( 目前暂不支持购物车模式)
     * 注:orderId,openId
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/shoppingMallForMDZT", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> shoppingMallForMDZT(WeixinInVo inVo, HttpServletRequest request) {
        if(inVo.getOrderId()==null||inVo.getOpenId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //根据id获取订单信息
        OrderInfoOut orderInfoOut=orderInfoService.getDetail(inVo.getOrderId());
        if (orderInfoOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (orderInfoOut.getStatus() != OrderInfo.STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //订单中部分商品已下架或已删除
        for (OrderItemOut orderItemOut:orderInfoOut.getOrderItemOuts()){
            if (orderItemOut.getGoodsIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED||
                    orderItemOut.getGoodsStatus()==GoodsSku.STATUS_XJ){
                return new BaseResp<Map<String, String>>(ResultStatus.error_goods_is_deleted);
            }
        }

        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", "享七商城");    //商品描述
        data.put("out_trade_no", orderInfoOut.getOrderCode());//商户订单标号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_MDZT_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 商城系统实物订单支付接口
     *
     *
     * 注:orderId(父订单的id),openId
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/shoppingMallRo", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> shoppingMallRo(WeixinInVo inVo, HttpServletRequest request) {
        if(inVo.getOrderId()==null||inVo.getOpenId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }

        //根据id获取订单信息
        OrderInfoOut orderInfoOut=orderInfoService.getDetail(inVo.getOrderId());
        if (orderInfoOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (orderInfoOut.getStatus() != OrderInfo.STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }
        //判断订单是否有子订单
        //拆单了的父订单里面的sendType为null(查子订单),不拆单的生成的父订单里面的sendType不为null(查父订单)
        if(orderInfoOut.getSendType()==null){
            //查询父订单里面所有子订单
            List<OrderInfoOut> orderInfoOuts = orderInfoService.getSonOrderList(orderInfoOut);
            for (OrderInfoOut infoOut : orderInfoOuts) {
                //订单中部分商品已下架或已删除
                for (OrderItemOut orderItemOut : infoOut.getOrderItemOuts()) {
                    if (orderItemOut.getGoodsIsDeleted() == GoodsSku.GOODS_SKU_IS_DELETED ||
                            orderItemOut.getGoodsStatus() == GoodsSku.STATUS_XJ) {
                        return new BaseResp<Map<String, String>>(ResultStatus.error_goods_is_deleted);
                    }
                }
            }
        }else {
            //订单中部分商品已下架或已删除
            for (OrderItemOut orderItemOut : orderInfoOut.getOrderItemOuts()) {
                if (orderItemOut.getGoodsIsDeleted() == GoodsSku.GOODS_SKU_IS_DELETED ||
                        orderItemOut.getGoodsStatus() == GoodsSku.STATUS_XJ) {
                    return new BaseResp<Map<String, String>>(ResultStatus.error_goods_is_deleted);
                }
            }
        }

        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = orderInfoOut.getRealAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", "享七商城");    //商品描述
        data.put("out_trade_no", orderInfoOut.getOrderCode());//商户订单编号(父订单编号)
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_RO_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 测试所用
     */
    @RequestMapping(value = "/kkk",method = RequestMethod.GET)
    public BaseResp<Integer> kkk(Long userId,String couponCode){
        OrderCoupon couponOut = orderCouponService.getByCouponCode(couponCode);
        PaidLog inVo = new PaidLog();
        inVo.setOrderCouponCode(couponOut.getCouponCode());
        inVo.setOperatorType(PaidLog.OPERATOR_TYPE_ZFYF);
        inVo.setPaidType(PaidLog.PAID_TYPE_WX);
        inVo.setUserId(userId);
        User userById = userService.getUserById(inVo.getUserId());
        if(userById!=null){
            inVo.setUserName(userById.getUserName());
        }
        int ret = orderCouponService.paidCouponSendAmount(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    public BigDecimal subRealWeight(Double weight){
        Double[] area = costWeightConfig.getArea();
        Double[] costWeight = costWeightConfig.getWeight();
        BigDecimal realWeight = BigDecimal.ZERO;
        if(weight==0){
            realWeight = BigDecimal.ZERO;
        }else if(weight>0&&weight<=area[0]){
            realWeight = new BigDecimal(2.5);
        }else if(weight>area[0]&&weight<=area[1]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[0]));
        }else if(weight>area[1]&&weight<=area[2]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[1]));
        }else if(weight>area[2]){
            realWeight = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(costWeight[2]));
        }
        return realWeight.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
