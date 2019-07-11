package com.xq.live.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.live.express.Express;
import com.xq.live.common.Constants;
import com.xq.live.common.RedisCache;
import com.xq.live.service.OrderDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2018/9/21.
 */
@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryService {

    @Autowired
    private RedisCache redisCache;

    /**
     * 快递单号查询物流
     *
     * @param expressCodes 快递单号（多个单号用","隔开）
     * @return
     */
    @Override
    public List<Map<String, Object>> getDetail(String expressCodes) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<String> expressCodeList = Arrays.asList(expressCodes.split(","));
        for (String expressCode : expressCodeList) {
            Map<String, Object> re = getExpress(expressCode);
            if(re!=null)
            result.add(re);
        }
        return result;
    }

    /**
     * 单号查询快递公司
     *
     * @param expressCode 快递单号
     * @return
     */
    private String getExpName(String expressCode) {
        String result = "";
        String querys = "?nu=" + expressCode;
        Express express = new Express();
        String rs = express.sendGet(Constants.EXP_NAME_URL + querys, Constants.EXP_SECRETID, Constants.EXP_SECRETKEY);
        JSONObject jsonObject = JSONObject.parseObject(rs);
        JSONObject date = jsonObject.getJSONObject("showapi_res_body");
        JSONArray value = date.getJSONArray("data");
        JSONObject data = value.getJSONObject(0);
        result = data.getString("simpleName");
        return result;
    }

    private Map<String, Object> getExpress(String expressCode){
        Map<String, Object> result = new HashMap<String, Object>();

        String skey="expressCode_"+expressCode;
        //获取缓存
        Map<String,Object> stringBargainMap=redisCache.get(skey, Map.class);
        if(stringBargainMap!=null){
            result=stringBargainMap;
        }else{
            Map<String, Object> re = new HashMap<String, Object>();
            String expName = getExpName(expressCode);//单号查询快递公司
            Express express = new Express();
            String querys = "?com=" + expName + "&nu=" + expressCode;
            String rs = express.sendGet(Constants.EXP_POINT_URL + querys, Constants.EXP_SECRETID, Constants.EXP_SECRETKEY);
            JSONObject jsonObject = JSONObject.parseObject(rs);
            Integer  showapi_res_code=jsonObject.getInteger("showapi_res_code");
            if(showapi_res_code==0){
                JSONObject date = jsonObject.getJSONObject("showapi_res_body");
                Object value = date.get("data");
                Integer status=date.getInteger("status");
                String state="待查询";
                switch (Integer.valueOf(status)) {
                    case -1:
                        state= "待查询";
                        break;
                    case 0:
                        state= "查询异常";
                        break;
                    case 1:
                        state= "暂无记录";
                        break;
                    case 2:
                        state= "在途中";
                        break;
                    case 3:
                        state= "派送中";
                        break;
                    case 4:
                        state= "已签收";
                        break;
                    case 5:
                        state= "用户拒签";
                        break;
                    case 6:
                        state= "疑难件";
                        break;
                    case 7:
                        state= "无效单";
                        break;
                    case 8:
                        state= "超时单";
                        break;
                    case 9:
                        state= "签收失败";
                        break;
                    case 10:
                        state= "退回";
                        break;
                    default:
                        state= "待查询";
                }
                re.put("expressCode", expressCode);
                re.put("list", value);
                re.put("status",date.getInteger("status"));
                re.put("tel",date.getString("tel"));
                re.put("expTextName",date.getString("expTextName"));
                re.put("state",state);
                result=re;
                if(status==4){
                    redisCache.set(skey,result,3l, TimeUnit.DAYS);//缓存已签收的快递信息,有效期为3天
                }else{
                    redisCache.set(skey,result,2l, TimeUnit.HOURS);//缓存快递信息,有效期为2小时
                }
            }
        }
        return  result;
    }

}
