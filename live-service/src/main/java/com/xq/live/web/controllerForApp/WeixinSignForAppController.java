package com.xq.live.web.controllerForApp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.BaseResp;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.vo.in.WeixinPhoneInvo;
import com.xq.live.vo.in.WeixinSendInvo;
import com.xq.live.vo.in.WeixinSignInVo;
import com.xq.live.vo.out.AccessTokenOut;
import com.xq.live.web.utils.AESDecodeUtils;
import com.xq.live.web.utils.PayUtils;
import com.xq.live.web.utils.SignUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lipeng on 2018/3/15.
 */

@RestController
@RequestMapping(value = "/app/wxsign")
public class WeixinSignForAppController {

    @Autowired
    private RedisCache redisCache;

    @RequestMapping(value = "/isSign",method = RequestMethod.GET)
    public BaseResp<String> isSign(@Valid WeixinSignInVo inVo, HttpServletRequest request,BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<String>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        String signature = inVo.getSignature();
        String timestamp = inVo.getTimestamp();
        String nonce = inVo.getNonce();
        String echostr = inVo.getEchostr();
        String ua = ((HttpServletRequest) request).getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
            return new BaseResp<String>(ResultStatus.SUCCESS,echostr);
        }
        boolean b = SignUtil.checkSignature(signature, timestamp, nonce);
        if(b==true){
            return new BaseResp<String>(ResultStatus.SUCCESS,"token验证正确");
        }
        return new BaseResp<String>(ResultStatus.FAIL,"token验证错误");

    }

    /**
     *获取微信用户的手机号
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/phoneAES",method = RequestMethod.POST)
    public BaseResp<String> phoneAES(@Valid WeixinPhoneInvo inVo,BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<String>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        byte[] encrypData = Base64.decodeBase64(inVo.getEncrypData());
        byte[] ivData = Base64.decodeBase64(inVo.getIvData());
        byte[] sessionKey = Base64.decodeBase64(inVo.getSessionKey());

        String decrypt = null;
        try {
             decrypt = AESDecodeUtils.decrypt(sessionKey, ivData, encrypData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new BaseResp<String>(ResultStatus.SUCCESS,decrypt);
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
     * 发送模板消息到微信
     * @param invo
     * @param result
     * @return
     */
    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public BaseResp<Map<String,String>>  send(@Valid WeixinSendInvo invo,BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Map<String,String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //拼接请求
        StringBuilder param = new StringBuilder();
        param.append("access_token=" + invo.getAccessToken() + "&touser=" + invo.getTouser() + "&template_id=" + invo.getTemplateId() + "&form_id=" + invo.getFormId() + "&data=" + invo.getData());
        if(invo.getPage()!=null){
            param.append("&page=" + invo.getPage());
        }
        if(invo.getColor()!=null){
            param.append("&color=" + invo.getColor());
        }
        if(invo.getEmphasisKeyword()!=null){
            param.append("&emphasis_keyword=" + invo.getEmphasisKeyword());
        }
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.SEND_URL, "POST", param.toString());
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        StringBuilder access = new StringBuilder();
        Map<String, String> res = new HashMap<String, String>();
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null&&errcode !=0) {
                //返回异常信息
                return new BaseResp<Map<String,String>>(errcode, jsonObject.getString("errmsg"), null);
            }
            res.put("errcode", jsonObject.getInteger("errcode").toString());
            res.put("errmsg", jsonObject.getString("errmsg"));
        }
        return new BaseResp<Map<String,String>>(ResultStatus.SUCCESS, res);
    }

    /**
     * 获取帐号下已存在的模板列表
     * @param invo
     * @return
     */
    @RequestMapping(value = "/getTemplateList",method = RequestMethod.POST)
    public BaseResp<Map<String,Object>> getTemplateList(WeixinSendInvo invo){
        if(invo==null||invo.getAccessToken()==null||invo.getOffset()==null||invo.getCount()==null){
            return new BaseResp<Map<String, Object>>(ResultStatus.error_param_empty);
        }

        /*String url = PaymentConfig.TEMPLATE_LIST_URL;
        String para = "access_token=" + invo.getAccessToken() + "&offset=" + invo.getOffset() + "&count=" + invo.getCount();

        String sr= HttpRequestUtil.sendPost(url, para, false);*/

        //拼接请求
        String param ="access_token=" + invo.getAccessToken() + "&offset=" + invo.getOffset() + "&count=" + invo.getCount();
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.TEMPLATE_LIST_URL, "POST", param);
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        Map<String, Object> res = new HashMap<String, Object>();
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null&&errcode !=0) {
                //返回异常信息
                return new BaseResp<Map<String,Object>>(errcode, jsonObject.getString("errmsg"), null);
            }
            JSONArray list = jsonObject.getJSONArray("list");
            res.put("errcode", jsonObject.getInteger("errcode").toString());
            res.put("errmsg", jsonObject.getString("errmsg"));
            res.put("list",list);
        }
        return new BaseResp<Map<String,Object>>(ResultStatus.SUCCESS, res);
    }
}
