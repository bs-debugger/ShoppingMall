package com.xq.live.web.controllerForClientApp;

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
import com.xq.live.vo.out.SmsOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author lipeng
 * @date 2018-02-22 13:09
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/clientApp/sms")
public class SmsForClientAppController {

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
     * app用户注册发送验证码
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/sendForRegister", method = RequestMethod.POST)
    public BaseResp<SmsOut> sendForRegister(SmsSendInVo inVo) {

        if(inVo==null||inVo.getShopMobile()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }

        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        SmsOut smsOut = smsSendService.redisVerifyForApp(inVo);
        //判断缓存是否存在，并且在10分钟以内
        if(smsOut!=null){
           return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            params.add(randomCode);
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_VERIFY_SUCCESS, params, "", "", "");
            if (ret.result == 0) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
            }
            inVo.setRemark(ret.errMsg);
            inVo.setSmsContent(randomCode);
            Long id = smsSendService.create(inVo);
            SmsOut smsOutForNew = smsSendService.redisVerifyForApp(inVo);//再把数据缓存
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS, smsOutForNew);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<SmsOut>(ResultStatus.FAIL);
        }
    }

    /**
     * 商家端app用户登录发送验证码
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/sendForShopAppRegister", method = RequestMethod.POST)
    //@CrossOrigin
    public BaseResp<SmsOut> sendForShopAppRegister(SmsSendInVo inVo,HttpServletResponse response) {
        /*response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.addHeader("Access-Control-Max-Age", "1800");*/

        if(inVo==null||inVo.getShopMobile()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }
       /* *//**
         * 发送验证码的手机如果不在user表中，且用户的状态不为商家，则不让登录
         *//*
        User byMobile = userService.findByMobile(inVo.getShopMobile());
        if(byMobile==null||byMobile.getUserType()==1){
            return new BaseResp<SmsOut>(ResultStatus.error_para_cashier_user_type);
        }*/

        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        SmsOut smsOut = smsSendService.redisVerifyForShopApp(inVo);
        //判断缓存是否存在，并且在10分钟以内
        if(smsOut!=null){
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            params.add(randomCode);
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_VERIFY_SUCCESS, params, "", "", "");
            if (ret.result == 0) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
            }
            inVo.setRemark(ret.errMsg);
            inVo.setSmsContent(randomCode);
            Long id = smsSendService.create(inVo);
            SmsOut smsOutForNew = smsSendService.redisVerifyForShopApp(inVo);//再把数据缓存
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS, smsOutForNew);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<SmsOut>(ResultStatus.FAIL);
        }
    }

    /**
     *判断商家验证码是否正确,并且更新user表,如果user表中含有手机号,先比较手机号是否相同，如果不同则算新加入用户
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/isVerifyForShopApp",method = RequestMethod.GET)
    public BaseResp<Integer> isVerifyForShopApp(SmsSendInVo inVo){
        if(inVo==null||inVo.getShopMobile()==null||inVo.getSmsContent()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        Integer verify = smsSendService.isVerifyForShopApp(inVo);
        if(verify==-1||verify==null){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,verify);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS,verify);
    }

    /**
     *判断app验证码是否正确,并且更新user表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/isVerify",method = RequestMethod.GET)
    public BaseResp<Long> isVerify(SmsSendInVo inVo){
        if(inVo==null||inVo.getShopMobile()==null||inVo.getSmsContent()==null||inVo.getUserId()==null||inVo.getUserName()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        Long verify = smsSendService.isVerify(inVo);
        if(verify==-1||verify==null){
            return new BaseResp<Long>(ResultStatus.FAIL,verify);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS,verify);


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

}
