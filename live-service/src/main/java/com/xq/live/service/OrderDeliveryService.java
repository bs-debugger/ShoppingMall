package com.xq.live.service;


import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/9/21.
 */
public interface OrderDeliveryService {
    List<Map<String, Object>> getDetail(String expressCodes);
}
