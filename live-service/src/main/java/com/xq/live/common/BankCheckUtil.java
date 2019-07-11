package com.xq.live.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xq.live.model.Bank;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 校验银行四要素方法类
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/1515:17
 */
public class BankCheckUtil {

    private static final Logger log = LoggerFactory.getLogger(BankCheckUtil.class);

    private static final String HOST = "https://b4bankcard.market.alicloudapi.com";

    private static final String PATH = "/bank4Check";

    private static final String APPCODE = "2ca3b77c31ce451d8cb6944984920c6e";

    private static final String METHOD = "GET";

    /**
     * 校验银行卡四要素
     * @param accountNo  银行卡号
     * @param idCard 身份证号
     * @param mobile 手机号
     * @param name  持卡人姓名
     */
    public static Map<String,Object> checkBankCode(String accountNo, String idCard, String mobile, String name){
        Map<String,Object> data = new HashMap<>();
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + APPCODE);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("accountNo", accountNo);
        querys.put("idCard", idCard);
        querys.put("mobile", mobile);
        querys.put("name", name);
        log.info("校验银行开始执行，请求参数为："+querys.toString());
        try {
            HttpResponse response = HttpUtils.doGet(HOST, PATH, METHOD, headers, querys);
            JSONObject result = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            log.info("校验银行卡返回结果，结果："+result.toString());
            if("01".equals(result.getString("status"))){
                Bank bank = new Bank();
                bank.setAccountNo(result.getString("accountNo"));
                bank.setAddrCode(result.getString("addrCode"));
                bank.setArea(result.getString("area"));
                bank.setBirthday(result.getString("birthday"));
                bank.setBank(result.getString("bank"));
                bank.setCardName(result.getString("cardName"));
                bank.setCardType(result.getString("cardType"));
                bank.setCity(result.getString("city"));
                bank.setIdCard(result.getString("idCard"));
                bank.setMobile(result.getString("mobile"));
                bank.setName(result.getString("name"));
                bank.setPrefecture(result.getString("prefecture"));
                bank.setProvince(result.getString("province"));
                bank.setSex(result.getString("sex"));
                data.put("code","SUCC");
                data.put("data", bank);
            }else{
                data.put("code","FAILD");
            }
        } catch (Exception e) {
            log.error("校验银行出错："+e.toString());
            data.put("code","FAILD");
        }

        return data;
    }

    public static void main(String[] args){
        checkBankCode("6222023202025798488","420116199008066916","15717158590","徐镇");
    }
}
