package com.xq.live.service;

import com.xq.live.model.SmsSend;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.out.SmsOut;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-22 14:20
 * @copyright:hbxq
 **/
public interface SmsSendService {
    /**
     * 新增
     * @param inVo
     * @return
     */
    Long create(SmsSendInVo inVo);


    /**
     * 用户注册验证码缓存
     * @param inVo
     * @return
     */
    SmsOut redisVerify(SmsSendInVo inVo);

    /**
     * 绑定和修改银行卡验证码缓存
     * @param inVo
     * @return
     */
    SmsOut redisPayBinding(SmsSendInVo inVo);

    /**
     * 忘记安全密码验证码缓存
     * @param inVo
     * @return
     */
    public SmsOut redisSecurityCode(SmsSendInVo inVo);


    /**
     * app验证码缓存
     * @param inVo
     * @return
     */
    SmsOut redisVerifyForApp(SmsSendInVo inVo);

    /**
     * 商家端app验证码缓存
     * @param inVo
     * @return
     */
    SmsOut redisVerifyForShopApp(SmsSendInVo inVo);

    /**
     * 注册验证码是否通过
     * @param inVo
     * @return
     */
    Long isVerify(SmsSendInVo inVo);

    /**
     * 当微信有手机号的时候直接修改用户信息,不发送验证码判断
     * @param inVo
     * @return
     */
    Long isVerifyForUpdateUser(SmsSendInVo inVo);

    /**
     * 商家端app注册验证码是否通过
     * @param inVo
     * @return
     */
    Integer isVerifyForShopApp(SmsSendInVo inVo);

}
