package com.xq.live.web.controllerForWeb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.BaseResp;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.dao.OrderInfoMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.service.impl.PullUserServiceImpl;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.PayUtils;
import com.xq.live.web.weixinPushUtils.AesException;
import com.xq.live.web.weixinPushUtils.WXPublicUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ss on 2018/10/20.
 */
@RestController
@RequestMapping("/website/wxpayNotify")
public class WxpayNotifyForWebController {

    private Logger logger = Logger.getLogger(WxpayNotifyForWebController.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SoService soService;

    @Autowired
    private PullUserServiceImpl pullUserService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private ActSkuService actSkuService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderCouponService orderCouponService;

    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    private WXPay wxpay;
    private PaymentConfig config;

    public WxpayNotifyForWebController() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
    }

    @RequestMapping("/verify_wx_token")
    public String verifyWXToken(HttpServletRequest request) throws AesException {
        String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (WXPublicUtils.verifyUrl(msgSignature, msgTimestamp, msgNonce)) {
            return echostr;
        }
        return null;
    }


    /**
     * 获取access_token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
    public BaseResp<AccessTokenOut> getAccessToken() throws Exception{
        String key = "access_token" + PaymentConfig.APPID;
        AccessTokenOut accessTokenOut = redisCache.get(key, AccessTokenOut.class);
        if(accessTokenOut!=null){
            return new BaseResp<AccessTokenOut>(ResultStatus.SUCCESS,accessTokenOut);
        }
        //获取access_token
        String param =  "&appid=" + PaymentConfig.APPID + "&secret=" + PaymentConfig.APP_SECRET ;
        System.out.println(PaymentConfig.ACCESS_TOKEN_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.ACCESS_TOKEN_URL, "GET", param);
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        accessTokenOut = new AccessTokenOut();
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return new BaseResp<AccessTokenOut>(errcode, jsonObject.getString("errmsg"), null);
            }
            accessTokenOut.setAccessToken(jsonObject.getString("access_token"));
            redisCache.set(key, accessTokenOut, 2l, TimeUnit.HOURS);
        }
        return new BaseResp<AccessTokenOut>(ResultStatus.SUCCESS, accessTokenOut);
    }



    /**
     * 微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                //查询订单 根据订单号查询订单  SoOut -订单实体类
                Long soId = Long.valueOf(out_trade_no);
                SoOut soOut = soService.get(soId);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || soOut == null || new BigDecimal(total_fee).compareTo(soOut.getSoAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (So.SO_STATUS_WAIT_PAID == soOut.getSoStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        SoInVo inVo = new SoInVo();
                        inVo.setId(soOut.getId());
                        inVo.setUserId(soOut.getUserId());
                        inVo.setUserName(soOut.getUserName());
                        inVo.setSkuId(soOut.getSkuId());
                        inVo.setSkuNum(soOut.getSkuNum());
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        int ret = soService.paid(inVo);
                        pushSo(soOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 商家订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShop")
    public void wxNotifyForShop(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                //查询订单 根据订单号查询订单  SoOut -订单实体类
                Long soId = Long.valueOf(out_trade_no);
                Long couponId =null;
                Long shopId = null;
                if(attach!=null) {
                    String[] nums = attach.split(",");//通过","分割，读取出couponId和shopId
                    if(nums.length>=2) {
                        if(StringUtils.isNotBlank(nums[0])){
                            couponId = Long.valueOf(nums[0]);
                        }
                        shopId = Long.valueOf(nums[1]);
                    }
                }

                SoOut soOut = soService.selectByPkForShop(soId);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || soOut == null || new BigDecimal(total_fee).compareTo(soOut.getSoAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (So.SO_STATUS_WAIT_PAID == soOut.getSoStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        //商家订单支付成功之后，钱进入平台，然后在商家的钱包中充入钱，等商家提现申请通过之后，转账给商家
                        SoInVo inVo = new SoInVo();
                        inVo.setId(soOut.getId());
                        inVo.setUserId(soOut.getUserId());
                        inVo.setUserName(soOut.getUserName());
                        inVo.setSkuId(soOut.getSkuId());
                        inVo.setSkuNum(soOut.getSkuNum());
                        inVo.setSoAmount(soOut.getSoAmount());
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        inVo.setCouponId(couponId);
                        inVo.setShopId(shopId);

                        /** 为了完成同一个事务,把券给核销掉  begin*/
                        if(couponId!=null) {
                            CouponOut cp = couponService.selectById(couponId);
                            Shop shopById = shopService.getShopById(shopId);
                            Sku sku = skuService.get(soOut.getSkuId());
                            SoWriteOff soWriteOff = new SoWriteOff();
                            soWriteOff.setSoId(cp.getSoId());//商家订单更改券的状态，是该券对应的soId
                            soWriteOff.setShopId(shopId);
                            soWriteOff.setShopName(shopById.getShopName());
                            soWriteOff.setShopAmount(soOut.getSoAmount().add(sku.getInPrice()));
                            soWriteOff.setCouponId(couponId);
                            soWriteOff.setCouponCode(cp.getCouponCode());
                            soWriteOff.setSkuId(soOut.getSkuId());
                            soWriteOff.setCouponAmount(cp.getCouponAmount());
                            soWriteOff.setUserId(soOut.getUserId());
                            soWriteOff.setUserName(soOut.getUserName());
                            /*soWriteOff.setCashierId(soOut.getUserId());//用户买单之后,直接自己核销了
                            soWriteOff.setCashierName(soOut.getUserName());//用户买单之后,直接自己核销了*/
                            soWriteOff.setPaidAmount(soOut.getSoAmount());
                            soWriteOff.setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
                            Long id = soWriteOffService.add(soWriteOff);
                        }
                        /** end */

                        int ret = soService.paidForShop(inVo);
                        pushSo(soOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 食典券订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForAct")
    public void wxNotifyForAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                //查询订单 根据订单号查询订单  SoOut -订单实体类
                Long soId = Long.valueOf(out_trade_no);
                Long actId =null;
                Long skuId = null;
                Long shopId = null;
                if(attach!=null) {
                    String[] nums = attach.split(",");//通过","分割，读取出couponId和shopId
                    if(nums.length>=3) {
                        actId = Long.valueOf(nums[0]);
                        skuId = Long.valueOf(nums[1]);
                        shopId = Long.valueOf(nums[2]);
                    }
                }

                SoOut soOut = soService.get(soId);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || soOut == null || new BigDecimal(total_fee).compareTo(soOut.getSoAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (So.SO_STATUS_WAIT_PAID == soOut.getSoStatus()) {//支付的状态判断
                        //给参与活动的推荐菜加5票
                        ActSkuInVo actSkuInVo = new ActSkuInVo();
                        actSkuInVo.setActId(actId);
                        actSkuInVo.setSkuId(skuId);
                        ActSkuOut byInVo = actSkuService.findByInVo(actSkuInVo);
                        actSkuInVo.setVoteNum(byInVo.getVoteNum() + 5);
                        actSkuInVo.setId(byInVo.getId());
                        Integer update = actSkuService.update(actSkuInVo);

                        //订单状态的修改。根据实际业务逻辑执行
                        SoInVo inVo = new SoInVo();
                        inVo.setId(soOut.getId());
                        inVo.setUserId(soOut.getUserId());
                        inVo.setUserName(soOut.getUserName());
                        inVo.setSkuId(soOut.getSkuId());
                        inVo.setSkuNum(soOut.getSkuNum());
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        inVo.setShopId(shopId);
                        inVo.setSoAmount(soOut.getSoAmount());
                        inVo.setDishSkuId(skuId);//将推荐菜的id放入到券的信息中
                        int ret = soService.paidForAct(inVo);
                        pushSo(soOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 砍价券订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForKjq")
    public void wxNotifyForKj(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                WeixinInVo attachInvo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来
                //查询订单 根据订单号查询订单  SoOut -订单实体类
                Long soId = Long.valueOf(out_trade_no);


                SoOut soOut = soService.get(soId);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || soOut == null || new BigDecimal(total_fee).compareTo(soOut.getSoAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (So.SO_STATUS_WAIT_PAID == soOut.getSoStatus()) {//支付的状态判断
                        //该砍价菜的库存数量减少1，并且在判断减少之后该砍价菜是否需要下架
                        Sku sku = skuService.get(attachInvo.getSkuId());
                        Integer stockNum = sku.getStockNum() - 1;
                        Integer sellNum = sku.getSellNum() + 1;
                        if(stockNum<=0){
                            sku.setIsDeleted(Sku.SKU_IS_DELETED);
                        }
                        sku.setSellNum(sellNum);
                        sku.setStockNum(stockNum);
                        skuService.update(sku);

                        //订单状态的修改。根据实际业务逻辑执行
                        SoInVo inVo = new SoInVo();
                        inVo.setId(soOut.getId());
                        inVo.setUserId(soOut.getUserId());
                        inVo.setUserName(soOut.getUserName());
                        inVo.setSkuId(soOut.getSkuId());
                        inVo.setSkuNum(soOut.getSkuNum());
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        inVo.setShopId(attachInvo.getShopId());
                        inVo.setSoAmount(soOut.getSoAmount());
                        inVo.setDishSkuId(attachInvo.getSkuId());
                        inVo.setType(attachInvo.getType());
                        inVo.setGroupId(attachInvo.getGroupId());
                        int ret = soService.paidForKc(inVo);//支付
                        pushSo(soOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 抢购券订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForQgq")
    public void wxNotifyForQg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                WeixinInVo attachInvo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来
                //查询订单 根据订单号查询订单  SoOut -订单实体类
                Long soId = Long.valueOf(out_trade_no);


                SoOut soOut = soService.get(soId);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || soOut == null || new BigDecimal(total_fee).compareTo(soOut.getSoAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (So.SO_STATUS_WAIT_PAID == soOut.getSoStatus()) {//支付的状态判断
                        //该抢购菜的库存数量减少1，并且在判断减少之后该抢购菜是否需要下架
                        Sku sku = skuService.get(attachInvo.getSkuId());
                        Integer stockNum = sku.getStockNum() - 1;
                        Integer sellNum = sku.getSellNum() + 1;
                        if(stockNum<=0){
                            sku.setIsDeleted(Sku.SKU_IS_DELETED);
                        }
                        sku.setSellNum(sellNum);
                        sku.setStockNum(stockNum);
                        skuService.update(sku);

                        //订单状态的修改。根据实际业务逻辑执行
                        SoInVo inVo = new SoInVo();
                        inVo.setId(soOut.getId());
                        inVo.setUserId(soOut.getUserId());
                        inVo.setUserName(soOut.getUserName());
                        inVo.setSkuId(soOut.getSkuId());
                        inVo.setSkuNum(soOut.getSkuNum());
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        inVo.setShopId(attachInvo.getShopId());
                        inVo.setSoAmount(soOut.getSoAmount());
                        inVo.setDishSkuId(attachInvo.getSkuId());


                        int ret = soService.paidForQg(inVo);//支付
                        pushSo(soOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 兑换券订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForDhq")
    public void wxNotifyForDh(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                WeixinInVo attachInvo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来
                //查询订单 根据订单号查询订单  SoOut -订单实体类
                Long soId = Long.valueOf(out_trade_no);


                SoOut soOut = soService.get(soId);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || soOut == null || new BigDecimal(total_fee).compareTo(soOut.getSoAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (So.SO_STATUS_WAIT_PAID == soOut.getSoStatus()) {//支付的状态判断
                        //该抢购菜的库存数量减少1，并且在判断减少之后该抢购菜是否需要下架
                        Sku sku = skuService.get(attachInvo.getSkuId());
                        Integer stockNum = sku.getStockNum() - 1;
                        Integer sellNum = sku.getSellNum() + 1;
                        sku.setSellNum(sellNum);
                        sku.setStockNum(stockNum);
                        skuService.update(sku);

                        //订单状态的修改。根据实际业务逻辑执行
                        SoInVo inVo = new SoInVo();
                        inVo.setId(soOut.getId());
                        inVo.setUserId(soOut.getUserId());
                        inVo.setUserName(soOut.getUserName());
                        inVo.setSkuId(soOut.getSkuId());
                        inVo.setSkuNum(soOut.getSkuNum());
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        inVo.setShopId(attachInvo.getShopId());
                        inVo.setSoAmount(soOut.getSoAmount());
                        inVo.setDishSkuId(attachInvo.getSkuId());
                        inVo.setType(attachInvo.getType());
                        inVo.setGroupId(attachInvo.getGroupId());


                        int ret = soService.paidForDh(inVo);//支付
                        pushSo(soOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 商城系统订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShoppingMall")
    public void wxNotifyForShoppingMall(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String orderCode = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号,可以把它作为支付日志里面的paid_no

                OrderInfo orderOut = orderInfoService.getByCode(orderCode);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || orderOut == null || new BigDecimal(total_fee).compareTo(orderOut.getRealAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (OrderInfo.STATUS_WAIT_PAID == orderOut.getStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        OrderInfoInVo inVo = new OrderInfoInVo();
                        BeanUtils.copyProperties(orderOut, inVo);
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        User userById = userService.getUserById(inVo.getUserId());
                        if(userById!=null){
                            inVo.setUserName(userById.getUserName());
                        }
                        int ret = orderInfoService.paid(inVo);
                        pushOrderInfo(orderOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 商城系统票券订单微信支付结果通知-----老接口以后删除
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShoppingMallCoupon")
    public void wxNotifyForShoppingMallCoupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String orderCode = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号,可以把它作为支付日志里面的paid_no
                OrderInfo orderOut = orderInfoService.getByCode(orderCode);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || orderOut == null || new BigDecimal(total_fee).compareTo(orderOut.getRealAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (OrderInfo.STATUS_WAIT_PAID == orderOut.getStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        OrderInfoInVo inVo = new OrderInfoInVo();
                        BeanUtils.copyProperties(orderOut, inVo);
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        User userById = userService.getUserById(inVo.getUserId());
                        if(userById!=null){
                            inVo.setUserName(userById.getUserName());
                        }
                        int ret = orderInfoService.paidCoupon(inVo);;
                        pushOrderInfo(orderOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 商城系统票券订单微信支付结果通知(新)
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShoppingMallCouponNew")
    public void wxNotifyForShoppingMallCouponNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String orderCode = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号,可以把它作为支付日志里面的paid_no
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                WeixinInVo attachInVo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来
                OrderInfo orderOut = orderInfoService.getByCode(orderCode);

                if (!PaymentConfig.MCH_ID.equals(mch_id) || orderOut == null || new BigDecimal(total_fee).compareTo(orderOut.getRealAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (OrderInfo.STATUS_WAIT_PAID == orderOut.getStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        OrderInfoInVo inVo = new OrderInfoInVo();
                        BeanUtils.copyProperties(orderOut, inVo);
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        User userById = userService.getUserById(inVo.getUserId());
                        if(userById!=null){
                            inVo.setUserName(userById.getUserName());
                        }
                        if (inVo.getFlagType()==WeixinInVo.WEI_XIN_TYPE_SHOP_TG){
                            PullUser pullUser = new PullUser();
                            pullUser.setType(PullUser.PULL_TYPE_PT);
                            pullUser.setParentId(inVo.getUserId());
                            Integer pull=pullUserService.updateActGroupNumUp(pullUser,inVo.getId());
                        }
                        int ret = orderInfoService.paidCouponNew(inVo, attachInVo);
                        if( ret>0){
                            pushOrderInfo(orderOut,total_fee);//支付成功之后给用户发送微信消息
                            updateUserAccount(orderOut);//二级分销给上级用户加钱
                        }
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 商城系统商家订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShopOrder")
    public void wxNotifyForShopOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String orderCode = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有couponId和shopId，可以通过此来完成对账
                WeixinInVo attachInVo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来
                OrderInfo orderOut = orderInfoService.getByCode(orderCode);

                if (!PaymentConfig.MCH_ID.equals(mch_id) || orderOut == null || new BigDecimal(total_fee).compareTo(orderOut.getRealAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (OrderInfo.STATUS_WAIT_PAID == orderOut.getStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        //商家订单支付成功之后，钱进入平台，然后在商家的钱包中充入钱，等商家提现申请通过之后，转账给商家
                        OrderInfoInVo inVo = new OrderInfoInVo();
                        BeanUtils.copyProperties(orderOut, inVo);
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        User userById = userService.getUserById(inVo.getUserId());
                        if(userById!=null){
                            inVo.setUserName(userById.getUserName());
                        }

                        /** 为了完成同一个事务,把券给核销掉  begin*/
                        if(attachInVo.getOrderCouponId()!=null) {
                            Shop shopById = shopService.getShopById(inVo.getShopId());

                            OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
                            orderCouponInVo.setId(attachInVo.getOrderCouponId());
                            orderCouponInVo.setShopId(inVo.getShopId());
                            if(shopById!=null){
                                orderCouponInVo.setShopName(shopById.getShopName());
                            }else {
                                orderCouponInVo.setShopName("享七自营");
                            }
                            int re = orderCouponService.hxCoupon(orderCouponInVo);
                        }
                        /** end */
                        int ret = orderInfoService.paidShopOrder(inVo, attachInVo);
                        pushOrderInfo(orderOut, total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 商城系统票券运费微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForCouponSendAmount")
    public void wxNotifyForCouponSendAmount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String couponCode = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号,可以把它作为支付日志里面的paid_no
                String attach = (String) notifyMap.get("attach");//商家订单中对应的attach，其中包含有userId
                WeixinInVo attachInvo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来

                String[] keyWordArr = couponCode.split("_");
                String newOrderCode=keyWordArr[0];
                OrderCoupon couponOut = orderCouponService.getByCouponCode(newOrderCode);//couponCode
                if (!PaymentConfig.MCH_ID.equals(mch_id) || couponOut == null) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (OrderCoupon.ORDER_COUPON_IS_USED_NO == couponOut.getIsUsed()) {//票券的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        PaidLog inVo = new PaidLog();
                        inVo.setOrderCouponCode(couponOut.getCouponCode());
                        inVo.setOperatorType(PaidLog.OPERATOR_TYPE_ZFYF);
                        inVo.setPaidType(PaidLog.PAID_TYPE_WX);
                        inVo.setUserId(attachInvo.getUserId());
                        inVo.setPaidNo(couponCode);
                        User userById = userService.getUserById(inVo.getUserId());
                        if(userById!=null){
                            inVo.setUserName(userById.getUserName());
                        }
                        int ret = orderCouponService.paidCouponSendAmount(inVo);
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 商城系统门店自提订单微信支付结果通知
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShoppingMallMDZT")
    public void wxNotifyForShoppingMallMDZT(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String orderCode = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号,可以把它作为支付日志里面的paid_no

                OrderInfo orderOut = orderInfoService.getByCode(orderCode);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || orderOut == null || new BigDecimal(total_fee).compareTo(orderOut.getRealAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (OrderInfo.STATUS_WAIT_PAID == orderOut.getStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        OrderInfoInVo inVo = new OrderInfoInVo();
                        BeanUtils.copyProperties(orderOut, inVo);
                        inVo.setUserIp(IpUtils.getIpAddr(request));
                        User userById = userService.getUserById(inVo.getUserId());
                        if(userById!=null){
                            inVo.setUserName(userById.getUserName());
                        }
                        int ret = orderInfoService.paidMDZT(inVo);;
                        pushOrderInfo(orderOut,total_fee);//支付成功之后给用户发送微信消息
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 订单支付成功之后微信消息推送
     * @param soOut
     */
    public  void pushSo(SoOut soOut,String totalFee) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        String shopName="享七自营";
        if(soOut.getShopId()!=null){
            ShopOut shopOut=shopMapper.findShopOutById(soOut.getShopId());
            if(shopOut!=null&&shopOut.getShopName()!=null){
                shopName=shopOut.getShopName();
            }
        }
        String keyWords=shopName+","+new BigDecimal(totalFee).divide(new BigDecimal(100))+","+str+","+soOut.getId()+",你购买的商品已支付成功，查看详情了解更多信息";//商户名称+支付金额+支付时间+交易单号+说明
        Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_PAY, "",keyWords,soOut.getUserId() );

        //完成之后发送消息到小程序的消息列表
        messageService.addMessage("付款成功", "您的 "+soOut.getSkuName()+" 购买成功。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, soOut.getUserId(), soOut.getUserId());
    }

    /**
     * 订单支付成功之后微信消息推送
     * @param orderOut
     */
    public void pushOrderInfo(OrderInfo orderOut,String totalFee)throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date=new java.util.Date();
        String str=sdf.format(date);
        OrderInfoOut detail = orderInfoMapper.getDetail(orderOut.getId());//查询订单详情
        List<OrderItemOut> orderItemOuts = detail.getOrderItemOuts();//查询订单项详情
        Long shopId= orderItemOuts.get(0).getShopId();
        ShopOut shopOut=shopMapper.findShopOutById(shopId);
        String shopName="享七自营";
        if(shopOut!=null&&shopOut.getShopName()!=null){
            shopName=shopOut.getShopName();
        }
        String keyWords=shopName+","+new BigDecimal(totalFee).divide(new BigDecimal(100))+","+str+","+orderOut.getOrderCode()+",你购买的商品已支付成功，查看详情了解更多信息";//商户名称+支付金额+支付时间+交易单号+说明 此处说明的逗号为中文逗号
        Integer res =weiXinPushService.pushByUserId(WeiXinTeamplateMsg.templateId_TYPE_PAY, "",keyWords,orderOut.getUserId() );

        //完成之后发送消息到小程序的消息列表
        messageService.addMessage("付款成功", "您的 "+orderItemOuts.get(0).getGoodsSkuName()+" 购买成功。", MessageText.MESSAGE_TEXT_TYPE_PRIVATE, orderOut.getUserId(), orderOut.getUserId());
    }

    /**
     * 更新用户账户信息，
     * 二级分销给上级和上上级增加奖励金
     */
    public void updateUserAccount(OrderInfo orderOut){
        actGoodsSkuService.distribution(orderOut);
    }

}
