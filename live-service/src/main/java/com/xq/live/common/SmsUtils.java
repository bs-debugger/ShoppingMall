package com.xq.live.common;

import com.xq.live.web.utils.HttpRequestUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信工具类
 * @author zhangmm
 * @date 2019-04-11 18.48
 */
public class SmsUtils {

    /**
     * 短信发送
     * @param mobile 手机号码
     * @param msg 消息
     * @return
     */
    public static boolean send(String mobile,String msg){
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("userCode", Constants.WINNER_LOOK_USER_NAME);
            paramMap.put("userPass", Constants.WINNER_LOOK_PASSWORD);
            paramMap.put("DesNo", mobile);
            paramMap.put("Msg", msg);
            String s = HttpRequestUtil.httpsPost(Constants.WINNER_LOOK_HTTPS_SEND_MESSAGE_URL, paramMap);
            Document dom= DocumentHelper.parseText(s);
            Element root=dom.getRootElement();
            String ret=root.getText();
            if(ret.length()>10){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static void main(String[] args){
        System.out.print(send("15927622660","银行卡错误"));
    }
}
