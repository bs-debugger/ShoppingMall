package com.xq.live.common;

/**
 * 常量定义
 *
 * @author zhangpeng32
 * @date 2018-02-13 21:04
 * @copyright:hbxq
 **/
public class Constants {
    /**
     * 腾讯云cos常量
     */
    public final static String SECRET_KEY = "AF9rNhggk04ziIPN9jARWP5atkQxIVcY";

    public final static String ACCESS_KEY = "AKIDqfO1ShC7PhttiQoNTVM1Zw7BSaWNoe2G";

    public final static String REGION_NAME = "ap-shanghai";

    public final static String BUCKET_NAME = "xq-1256079679";

    public final static String COS_IMAGE_BASE_PATH = "https://xq-1256079679.file.myqcloud.com";

    public final static String BUCKET_MP4_NAME = "xqmp4-1256079679";

    public final static String COS_MP4_BASE_PATH = "https://xqmp4-1256079679.file.myqcloud.com";

    /**
     * 腾讯短信服务常量
     */
    public final static int SMS_APP_ID = 1400070009;

    public final static String SMS_APP_KEY = "19f12a3dd8177de5ec27be21b26ff9ee";

    public final static String SMS_NATION_CODE = "86";

    public final static int TEMP_ID_PAID_SUCCESS = 88505;//支付通知模板

    public final static int TEMP_ID_VERIFY_SUCCESS = 100937;//注册验证码通知模板

    /**
     * 全局域名常量
     */
    public final static String DOMAIN_XQ_URL = "https://www.hbxq001.cn";

    /**
     * 用户默然头像url
     */
    public final static String DEFAULT_ICON_URL = "https://xq-1256079679.file.myqcloud.com/test_图层 24_0.8.jpg";

    /**
     * 快递查询常量
     */
    public final static String EXP_SECRETID="AKID3tbzJxbx7vyXY3wK2l4235D9Nf6aYx2SBjW";

    public final static String EXP_SECRETKEY="h6rkQ9ee73J47sXvPcvmm4BJTGXRb3lk3qxzn9ve";

    public final static String EXP_NAME_URL="https://service-6t1c9ush-1255468759.ap-shanghai.apigateway.myqcloud.com/release/item-to-com";//单号查询快递公司url

    public final static String EXP_POINT_URL="https://service-6t1c9ush-1255468759.ap-shanghai.apigateway.myqcloud.com/release/point-list";//快递单号物流跟踪url

    public final static String USER_SESSION_KEY = "user_session_key";

    /**
     * 云信短信常量
     */
    public final static String WINNER_LOOK_HTTPS_SEND_MESSAGE_URL = "https://118.178.116.15:8443/winnerrxd/api/trigger/SendMsg";

    public final static String WINNER_LOOK_USER_NAME = "HBXQYJ";

    public final static String WINNER_LOOK_PASSWORD = "xiangqi2018.";

    public final static String WINNER_LOOK_REGISTER = "尊敬的用户：您的验证码为{0}，仅用于注册，如非本人操作，请忽略本短信【享七科技】";

    public final static String WINNER_LOOK_ACCOUNT_NAME = "验证码.{0}请在5分钟内完成银行卡绑定。请勿泄露给他人，如非本人操作请忽略。【享七科技】";

    public final static String WINNER_LOOK_ACCOUNT_DEPOSIT_REJECT = "尊敬的顾客:您好!感谢您使用享七购平台，您申请的提现因{0}，现已驳回。请重新提交申请。【享七科技】";

    public final static String REFUND_APPLICATION = "尊敬的顾客:您好!感谢您使用享七购平台，您购买的商品{0}，退款申请已被驳回，请重新提交申请或请致电：027-59728176，谢谢！【享七科技】";

    public final static String WINNER_FORGET_SECURITY_CODE = "尊敬的用户：您的验证码为{0}，仅用于修改安全密码，如非本人操作，请忽略本短信【享七科技】";

    public final static String WINNER_GOODS_SKU_REJECT = "尊敬的用户:您上传的商品[{0}]被驳回，请登录APP查看详情【享七科技】";

    public final static String ORDER_INVOICE = "尊敬的顾客:您好!感谢您使用享七购平台，您申请的发票审核{0}，现已被驳回，请重新提交申请或请致电：027-59728176，谢谢【享七科技】";

    public final static String WINNER_FORGET_PASSWORD = "尊敬的用户：您的验证码为{0}，仅用于修改密码，如非本人操作，请忽略本短信【享七科技】";

}
