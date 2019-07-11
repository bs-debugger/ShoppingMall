package com.xq.live.web.controllerForClientApp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.BaseResp;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.PaymentForAppConfig;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Shop;
import com.xq.live.model.So;
import com.xq.live.model.User;
import com.xq.live.service.ServiceAmountService;
import com.xq.live.service.ShopService;
import com.xq.live.service.SoService;
import com.xq.live.service.SoWriteOffService;
import com.xq.live.vo.in.WeixinInVo;
import com.xq.live.vo.out.SoOut;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付服务端controller
 * @author lipeng
 * @date 2018-03-09 10:54
 * @copyright:hbxq
 */
@RestController
@RequestMapping("/clientApp/wxpay")
public class WeixinPayForClientAppController {
    private Logger logger = Logger.getLogger(WeixinPayForClientAppController.class);

    @Autowired
    private SoService soService;

    @Autowired
    private ShopService ShopService;

    @Autowired
    private ServiceAmountService serviceAmountService;

    @Autowired
    private SoWriteOffService soWriteOffService;

    private WXPay wxpay;
    private PaymentConfig config;

    private WXPay wxPayForApp;
    private PaymentForAppConfig configForApp;

    public WeixinPayForClientAppController() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
        configForApp = PaymentForAppConfig.getInstance();
        wxPayForApp = new WXPay(configForApp, WXPayConstants.SignType.MD5);
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

    /**
     * 小程序上的支付(支付到享七平台)
     * @param inVo
     * @param request
     * @param bindingResult
     * @return
     */
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

    /**
     * 商家端app上面的支付(支付到享七平台)
     *
     * 支付服务费一定不需要传:soId,openId
     * 注:现在的userId是当前用户，所以需要从网关中取
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/payForShopApp", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> payForShopApp(WeixinInVo inVo, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        if(inVo.getShopId()==null||inVo.getServicePrice()==null||inVo.getBeginTime()==null||inVo.getEndTime()==null||inVo.getUserId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }


        //判断商家是否存在
        Shop shopById = ShopService.getShopById(inVo.getShopId());
        if(shopById==null||shopById.getIsDeleted()==1){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);
        //String spbill_create_ip = "113.57.71.175";

        int price100 = inVo.getServicePrice().multiply(new BigDecimal(100)).intValue();

        Long date = new Date().getTime();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", configForApp.getAppID());//用商家端的APPID
        data.put("mch_id", configForApp.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", shopById.getShopName()+"-缴纳服务费");    //商品描述
        data.put("out_trade_no", date.toString());//商户订单号------用的是当前系统时间
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentForAppConfig.WX_NOTIFY_SHOP_APP_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentForAppConfig.TRADE_TYPE_APP);//支付方式
        data.put("attach", JSON.toJSONString(inVo));
        //data.put("openid", inVo.getOpenId());//app微信支付不需要openId

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appid", configForApp.getAppID());
        try {
            Map<String, String> rMap = wxPayForApp.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("noncestr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
                response.put("prepayid", rMap.get("prepay_id"));//app微信支付需要prepayid
                response.put("package", "Sign=WXPay");
//                response.put("signType", "MD5");//app微信支付不需要signType
                response.put("partnerid", configForApp.getMchID());
                response.put("timestamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentForAppConfig.API_KEY);
                response.put("sign", sign);//小程序里面是paySign,app支付里面是sign
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
     * 商家端app上面的支付(支付到享七平台)新
     *
     * 支付服务费一定不需要传:soId,openId
     * 注:现在的userId是当前用户，所以需要从网关中取
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/payForShopAppNew", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> payForShopAppNew(WeixinInVo inVo, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        if(inVo.getShopId()==null||inVo.getServicePrice()==null||inVo.getBeginTime()==null||inVo.getEndTime()==null||inVo.getUserId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }


        //判断商家是否存在
        Shop shopById = ShopService.getShopById(inVo.getShopId());
        if(shopById==null||shopById.getIsDeleted()==1){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);
        //String spbill_create_ip = "113.57.71.175";

        Long date = new Date().getTime();

        int price100 = inVo.getServicePrice().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", configForApp.getAppID());//用商家端的APPID
        data.put("mch_id", configForApp.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", shopById.getShopName()+"-缴纳服务费");    //商品描述
        data.put("out_trade_no", date.toString());//商户订单号------用的是当前系统时间
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentForAppConfig.WX_NOTIFY_SHOP_APP_URL_NEW);//支付成功后的回调地址
        data.put("trade_type", PaymentForAppConfig.TRADE_TYPE_APP);//支付方式
        data.put("attach", JSON.toJSONString(inVo));
        //data.put("openid", inVo.getOpenId());//app微信支付不需要openId

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appid", configForApp.getAppID());
        try {
            Map<String, String> rMap = wxPayForApp.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String result_code = rMap.get("result_code");//
            String return_code = rMap.get("return_code");//返回状态码
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("noncestr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
                response.put("prepayid", rMap.get("prepay_id"));//app微信支付需要prepayid
                response.put("partnerid", configForApp.getMchID());
                response.put("package", "Sign=WXPay");
//                response.put("signType", "MD5");//app微信支付不需要signType
                response.put("timestamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentForAppConfig.API_KEY);
                response.put("sign", sign);//小程序里面是paySign,app支付里面是sign
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
}
