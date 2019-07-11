package com.xq.live.web.utils;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.RedisCache;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import com.xq.live.vo.out.AccessTokenOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Created by ss on 2018/10/1.
 * 微信小程序推送
 *
 */
@Component
public class WeiXinPush {

    public final static String KEYWORD = "keyword";//单个内容

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;


    public static final String templateId_TYPE = "Wzxu3aDcDcdIuYP0N63hot2QKxopSy2FYln1X50pTa4";


    public static void main(String[] args) throws Exception {
        WeiXinPush weiXinPush = new WeiXinPush();
        weiXinPush.push(templateId_TYPE, "www.baidu.com", null, null,null);
    }


    /**
     * 推送消息
     *
     * @param templateId 模板消息id
     * @param page       跳转页面
     * @param keyWords   模板内容
     * @param formId   推送id
     * @param user   推送用户
     */
    public void push(String templateId, String page,String keyWords, String formId,User user) throws Exception {
       /*填充数据*/
        WeiXinTeamplateMsg weixinTemplate =WeiXinTeamplateMsg.New();
        weixinTemplate.setPage(page);
        weixinTemplate.setTemplate_id(templateId);
        weixinTemplate.setTouser(user.getOpenId());//"ogDNV4-C-sUyFLMpLeLZlXKmAQt4"
        weixinTemplate.setForm_id(formId);
        // 模板内容
        if (StringUtils.isNotBlank(keyWords)) {
            String[] keyWordArr = keyWords.split(",");
            //Map<String, Object> keyWordMap = new HashMap<>();
            for (int i = 0; i < keyWordArr.length; i++) {
                weixinTemplate.add(WeiXinPush.KEYWORD + (i + 1), keyWordArr[i]);
                //keyWordMap.put(MsgTemplateMode.KEYWORD + (i + 1), keyWordArr[i]);
            }
        }

        weixinTemplate.add("keyword1", "第一个");
        weixinTemplate.add("keyword2","第二个");
        weixinTemplate.add("keyword3","第三个");
        weixinTemplate.add("keyword4", "第四个");
        weixinTemplate.add("keyword5", "第五个");

        JSONObject jsonObject= WeiXinPush.sendWXTemplateMsg(weixinTemplate);
        System.out.println(jsonObject.toJSONString());
    }

 /**
    * 推送消息
    *
    * @param templateId 模板消息id
    * @param page       跳转页面
    * @param keyWords   模板内容
    * @param formId   推送id
    * @param userId   推送用户
    */

    /**
     * 获取存储access_token
     */
    public AccessTokenOut getToken(){
        String key = "access_token" + PaymentConfig.APPID;
        String pram="?appid="+PaymentConfig.APPID+"&secret="+PaymentConfig.APP_SECRET+"&grant_type=client_credential";
        String httpRets = PayUtils.httpRequest("https://api.weixin.qq.com/cgi-bin/token", "GET", pram);
        JSONObject jsons = JSONObject.parseObject(httpRets);
        System.out.println(key);
        String accessToken=jsons.getString("access_token");
        AccessTokenOut accessTokenOut = jsons.getObject("accessTokenOut",AccessTokenOut.class);
        /*AccessTokenOut accessTokenOut = new AccessTokenOut();*/
        accessTokenOut.setAccessToken("14_XrOhMwInlWqGGe4U3Lg9vJ7tXMCNwfTo_khJC36xbFffUq_7v3IX7997igfPX_2JmtuOD65LJVreqvzODJs_DpuhesgiFGl0g4ny-Yx7-Dn09l-DAs7lOXGGQbzfRhu3zAHiQy4TivfvTujqTLRbAEALDJ");

        System.out.println(accessTokenOut.getAccessToken());
        /*System.out.println(redisCache.get(key, AccessTokenOut.class));
        AccessTokenOut accessTokenOut = redisCache.get(key, AccessTokenOut.class);*/
        if(accessTokenOut!=null){
            return accessTokenOut;
        }
        //获取access_token
        String param =  "&appid=" + PaymentConfig.APPID + "&secret=" + PaymentConfig.APP_SECRET +"&grant_type=client_credential";
        System.out.println(PaymentConfig.ACCESS_TOKEN_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.ACCESS_TOKEN_URL, "POST", param);
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        accessTokenOut = new AccessTokenOut();
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return accessTokenOut;
            }
            accessTokenOut.setAccessToken(jsonObject.getString("access_token"));
            //redisCache.set(key, accessTokenOut, 2l, TimeUnit.HOURS);
        }
        return accessTokenOut;
    }

    public static JSONObject sendWXTemplateMsg(WeiXinTeamplateMsg templateData){
        String json = templateData.build();
        WeiXinPush xinPush=new WeiXinPush();
        // 获取access token
        String accessToken = xinPush.getToken().getAccessToken();
        //获取openId
        String param = "?access_token="+accessToken;
        System.out.println(PaymentConfig.GET_OPEN_ID_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" + param, "POST", json);
        //String post = HttpKit.post("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+getAccess_Token(), json);
        return JSONObject.parseObject(httpRet);
    }

}