package com.xq.live.service;

import com.xq.live.model.User;
import com.xq.live.vo.out.AccessTokenOut;

/**
 * Created by ss on 2018/10/15.
 */
public interface WeiXinPushService {

    /**
     * 获取存储access_token
     * @param
     * @return
     */
    public AccessTokenOut getToken();

    /**
     * 推送消息
     *
     * @param templateId 模板消息id
     * @param page       跳转页面
     * @param keyWords   模板内容
     * @param formId   推送id
     * @param user   推送用户
     */
    public Integer push(String templateId, String page,String keyWords, String formId,User user) throws Exception;

    /**
     * 推送消息
     *
     * @param templateId 模板消息id
     * @param page       跳转页面
     * @param keyWords   模板内容
     * @param userId   推送用户
     */
    public Integer pushByUserId(String templateId, String page,String keyWords, Long userId) ;
}
