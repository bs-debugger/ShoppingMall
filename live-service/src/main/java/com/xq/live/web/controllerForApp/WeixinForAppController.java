package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * Created by lipeng on 2018/5/15.
 */
@Controller
@RequestMapping("/app/weixin")
public class WeixinForAppController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @Autowired
    private UserService userService;

    private Logger logger = Logger.getLogger(WeixinForAppController.class);


    /**
     * 有感跳转到获取openId和昵称的接口
     * @param returnUrl
     * @return
     */
    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        //1. 配置
        //2. 调用方法
        String url = "https://www.hbxq001.cn" + "/app/weixin/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO, URLEncoder.encode(returnUrl));

        return "redirect:" + redirectUrl;
    }

    /**
     * 无感跳转到获取openId接口
     * @param returnUrl
     * @return
     */
    @RequestMapping(value = "/qrAuthorize", method = RequestMethod.GET)
    public String qrAuthorize(@RequestParam("returnUrl") String returnUrl) {
        //1. 配置
        //2. 调用方法
        String url = "https://www.hbxq001.cn" + "/app/weixin/qrUserInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl));

        return "redirect:" + redirectUrl;
    }


    /**
     * 有感获取到用户的基本信息
     * @param code
     * @param returnUrl
     * @return
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public BaseResp<WxMpUser> userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            logger.error("【微信网页授权】{}", e);
            throw new RuntimeException("微信网页授权失败" + e.getMessage());
        }

        WxMpUser wxMpUser = new WxMpUser();

        try {
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            logger.error("【获取用户信息失败】{}", e);
            throw new RuntimeException("获取用户信息失败" + e.getMessage());
        }

        return new BaseResp<WxMpUser>(ResultStatus.SUCCESS,wxMpUser);

        /*String openId = wxMpUser.getOpenId();//获取用户信息正常，里面一定含有昵称

        User user = userService.findByOpenId(openId);
        //无手机号,发送验证码
        if(user.getMobile()==null){
            return new BaseResp<Long>(ResultStatus.error_mobile_is_null);
        }

        return new BaseResp<Long>(ResultStatus.SUCCESS,user.getId());*/
    }

    /**
     * 无感获取到用户的openId
     * @param code
     * @param returnUrl
     * @return
     */
    @RequestMapping(value = "/qrUserInfo", method = RequestMethod.GET)
    public BaseResp<WxMpOAuth2AccessToken> qrUserInfo(@RequestParam("code") String code,
                                       @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            logger.error("【微信网页授权】{}", e);
            throw new RuntimeException("微信网页授权失败" + e.getMessage());
        }
        return new BaseResp<WxMpOAuth2AccessToken>(ResultStatus.SUCCESS,wxMpOAuth2AccessToken);

        /*String openId = wxMpOAuth2AccessToken.getOpenId();

        User user = userService.findByOpenId(openId);
        if(user==null||user.getNickName()==null) {
            String url = "https://www.hbxq001.cn/app/weixin/authorize?returnUrl=" + returnUrl;
            return "redirect:" + url;
        }

        if(user.getMobile()==null){
            return "用户有昵称，但无手机号";
        }
        return user.getId().toString();*/
    }

}
