package com.xq.live.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.RedisCache;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.model.User;
import com.xq.live.service.MessageService;
import com.xq.live.service.UserService;
import com.xq.live.service.WeiXinPushService;
import com.xq.live.vo.in.FormIdInVo;
import com.xq.live.vo.out.AccessTokenOut;
import com.xq.live.web.utils.PayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ss on 2018/10/15.
 * 微信消息推送
 */
@Service
public class WeiXinPushServiceImpl implements WeiXinPushService {
    @Autowired
    RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    private Logger logger = Logger.getLogger(WeiXinPushServiceImpl.class);

    /**
     * 获取存储access_token
     */
    @Override
    public AccessTokenOut getToken() {
        String httpRet = PayUtils.httpRequest("https://xiang7.net/wxpayNotify/getAccessToken","GET",null);
        JSONObject jsons = JSONObject.parseObject(httpRet);
        System.out.println(jsons.getString("access_token"));
        AccessTokenOut accessTokenOut = jsons.getObject("data",AccessTokenOut.class);
        if (accessTokenOut.getAccessToken()!=null){
            return accessTokenOut;
        }else {
            return null;
        }
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
    @Override
    public Integer push(String templateId, String page,String keyWords, String formId,User user)  {
       /*填充数据*/
        WeiXinTeamplateMsg weixinTemplate =WeiXinTeamplateMsg.New();
        weixinTemplate.setPage(page);
        weixinTemplate.setTemplate_id(templateId);
        weixinTemplate.setTouser(user.getOpenId());//
        weixinTemplate.setForm_id(formId);
        // 模板内容
        if (StringUtils.isNotBlank(keyWords)) {
            String[] keyWordArr = keyWords.split(",");
            for (int i = 0; i < keyWordArr.length; i++) {
                weixinTemplate.add(WeiXinTeamplateMsg.KEYWORD + (i + 1), keyWordArr[i]);
            }
        }
        /*weixinTemplate.add("keyword1", "第一个");
        weixinTemplate.add("keyword2","第二个");
        weixinTemplate.add("keyword3","第三个");
        weixinTemplate.add("keyword4", "第四个");
        weixinTemplate.add("keyword5", "第五个");*/

        JSONObject jsonObject= this.sendWXTemplateMsg(weixinTemplate);
        System.out.println(jsonObject.toJSONString());
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != 0) {
                //返回异常信息
                AccessTokenOut accessTokenOut = new AccessTokenOut();
                accessTokenOut.setErrcode(errcode);
                logger.info("微信推送错误：" + "错误码" + errcode);
                return errcode;
            }
            return 1;
        }else {
            return null;
        }
    }


    public static JSONObject sendWXTemplateMsg(WeiXinTeamplateMsg templateData){
        String json = templateData.build();
        // 获取access token
        WeiXinPushServiceImpl weiXinPushService = new WeiXinPushServiceImpl();
        String accessToken = weiXinPushService.getToken().getAccessToken();
        //获取openId
        String param = "?access_token="+accessToken;
        System.out.println(PaymentConfig.GET_OPEN_ID_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send" + param, "POST", json);
        return JSONObject.parseObject(httpRet);
    }

    @Override
    public Integer pushByUserId(String templateId, String page, String keyWords, Long userId)  {
        User user =userService.getUserById(userId);
        if(user==null||user.getId()==null){
            return null;
        }

        List<FormIdInVo> formIdInVos=messageService.getFormIdList(user.getId());//获取缓存的formId记录
        if(formIdInVos!=null&&formIdInVos.size()>0){
            FormIdInVo formIdInVo=formIdInVos.get(0);
            String formId=formIdInVo.getFormId();
            if (formId == null) {
                return null;
            }
            this.push(templateId,page,keyWords,formId,user);//推送消息
            redisCache.hdel("formId_"+user.getOpenId(),formId);//formId使用后删除该条记录
            return 1;
        }
        return null;
    }
}
