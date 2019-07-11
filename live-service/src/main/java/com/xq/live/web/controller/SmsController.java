package com.xq.live.web.controller;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Constants;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.SmsSend;
import com.xq.live.model.User;
import com.xq.live.service.SmsSendService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.SmsOut;
import com.xq.live.web.utils.HttpRequestUtil;
import com.xq.live.web.utils.UserContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-22 13:09
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public BaseResp<Long> send(@Valid SmsSendInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        try {
            SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            params.add(inVo.getShopName());
            params.add(inVo.getUserName());
            inVo.setCreateTime(new Date());
            params.add(inVo.getCreateTime().toLocaleString());
            params.add(inVo.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_PAID_SUCCESS, params, "", "", "");
            if (ret.result == 0) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
            }
            inVo.setRemark(ret.errMsg);
            inVo.setSmsContent(this.getPaidSmsMsg(inVo, params));
            Long id = smsSendService.create(inVo);
            return new BaseResp<Long>(ResultStatus.SUCCESS, id);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Long>(ResultStatus.FAIL);
        }
    }

    /**
     * 用户注册发送验证码
     *
     * 注:现在的userId,userName是当前用户，所以需要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/sendForRegister", method = RequestMethod.POST)
    public BaseResp<SmsOut> sendForRegister(SmsSendInVo inVo) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        if(inVo==null||inVo.getShopMobile()==null||inVo.getUserId()==null||inVo.getUserName()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }

        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        SmsOut smsOut = smsSendService.redisVerify(inVo);
        //判断缓存是否存在，并且在10分钟以内
        if(smsOut!=null){
           return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            /*SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            params.add(randomCode);
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_VERIFY_SUCCESS, params, "", "", "");*/
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            String msg = MessageFormat.format(Constants.WINNER_LOOK_REGISTER, randomCode);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("userCode", Constants.WINNER_LOOK_USER_NAME);
            paramMap.put("userPass", Constants.WINNER_LOOK_PASSWORD);
            paramMap.put("DesNo", inVo.getShopMobile());
            paramMap.put("Msg", msg);
            String s = HttpRequestUtil.httpsPost(Constants.WINNER_LOOK_HTTPS_SEND_MESSAGE_URL, paramMap);
            Document dom= DocumentHelper.parseText(s);
            Element root=dom.getRootElement();
            String ret=root.getText();
            if (ret.length() > 10) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
                inVo.setRemark("OK");
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
                inVo.setRemark(ret);
            }
            inVo.setSmsContent(randomCode);
            Long id = smsSendService.create(inVo);
            SmsOut smsOutForNew = smsSendService.redisVerify(inVo);//再把数据缓存
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS, smsOutForNew);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<SmsOut>(ResultStatus.FAIL);
        }
    }

    /**
     *判断验证码是否正确,并且更新user表,返回一个通行user对象
     *
     * 注:原来返回的是userId，现在返回的是一个user对象
     * 注:现在的userId,userName是当前用户，所以需要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/isVerify",method = RequestMethod.GET)
    public BaseResp<User> isVerify(SmsSendInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        if(inVo==null||inVo.getShopMobile()==null||inVo.getSmsContent()==null||inVo.getUserId()==null||inVo.getUserName()==null){
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        Long verify = smsSendService.isVerify(inVo);
        User re = null;
        if(verify==null||verify==-1){
            return new BaseResp<User>(ResultStatus.FAIL,re);
        }
        re = userService.getUserById(verify);
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getUserId());
        accountInVo.setUserName(inVo.getUserName());
        accountInVo.setAccountName(inVo.getShopMobile());
        //判断更新account_name是否成功
        int i= userService.updateByUserID(accountInVo);
        if (i==0){
            return new BaseResp<User>(ResultStatus.error_act_update);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS,re);


    }

    /**
     *当微信有手机号的时候直接修改用户信息,不发送验证码判断
     *
     * 注:原来返回的是userId，现在返回的是一个user对象
     * 注:现在的userId,userName是当前用户，所以需要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/isVerifyForUpdateUser",method = RequestMethod.GET)
    public BaseResp<User> isVerifyForUpdateUser(SmsSendInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        if(inVo==null||inVo.getShopMobile()==null||inVo.getUserId()==null||inVo.getUserName()==null){
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
        Long verify = smsSendService.isVerifyForUpdateUser(inVo);
        User re = null;
        if(verify==null||verify==-1){
            return new BaseResp<User>(ResultStatus.FAIL,re);
        }
        re = userService.getUserById(verify);
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getUserId());
        accountInVo.setUserName(inVo.getUserName());
        accountInVo.setAccountName(inVo.getShopMobile());
        //判断更新account_name是否成功
        int i= userService.updateByUserID(accountInVo);
        if (i==0){
            return new BaseResp<User>(ResultStatus.error_act_update);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS,re);


    }


    /**
     * 组装支付成功短信通知内容
     * @param inVo
     * @param params
     * @return
     */
    private String getPaidSmsMsg(SmsSendInVo inVo, List<String> params) {
        /*尊敬的 {1}，顾客{2}在{3}成功支付￥{4}元，您可以通过享七平台进行提现操作，谢谢。*/
        StringBuffer msgSb = new StringBuffer("");
        msgSb.append("尊敬的 ");
        msgSb.append(params.get(0)).append(",");
        msgSb.append("顾客").append(params.get(1));
        msgSb.append(params.get(2)).append("成功支付");
        msgSb.append("￥").append(params.get(3)).append("元，您可以通过享七平台进行提现操作，谢谢。");
        return msgSb.toString();
    }


    /**
     * 用户修改账户用户提现绑定银行卡信息发送验证码
     *
     * 注:现在的userId,userName是当前用户，所以需要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/sendForSecurityCode", method = RequestMethod.POST)
    public BaseResp<SmsOut> sendForSecurityCode(SmsSendInVo inVo) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        if(inVo==null||inVo.getShopMobile()==null||inVo.getUserId()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }

        inVo.setSmsType(SmsSend.SMS_TYPE_FORGET);
        SmsOut smsOut = smsSendService.redisSecurityCode(inVo);
        //判断缓存是否存在，并且在5分钟以内
        if(smsOut!=null){
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            String randomCode = RandomStringUtil.getRandomCode(6,0);
            inVo.setCreateTime(new Date());
            String msg = MessageFormat.format(Constants.WINNER_LOOK_ACCOUNT_NAME, randomCode);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("userCode", Constants.WINNER_LOOK_USER_NAME);
            paramMap.put("userPass", Constants.WINNER_LOOK_PASSWORD);
            paramMap.put("DesNo", inVo.getShopMobile());
            paramMap.put("Msg", msg);
            String s = HttpRequestUtil.httpsPost(Constants.WINNER_LOOK_HTTPS_SEND_MESSAGE_URL, paramMap);
            Document dom= DocumentHelper.parseText(s);
            Element root=dom.getRootElement();
            String ret=root.getText();
            if (ret.length() > 10) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
                inVo.setRemark("OK");
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
                inVo.setRemark(ret);
            }
            inVo.setSmsContent(randomCode);
            Long id = smsSendService.create(inVo);
            SmsOut smsOutForNew = smsSendService.redisPayBinding(inVo);//再把数据缓存
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS, smsOutForNew);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<SmsOut>(ResultStatus.FAIL);
        }
    }

}
