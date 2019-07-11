package com.xq.live.config;

import org.springframework.stereotype.Component;

/**
 * Created by lipeng on 2018/5/22.
 */
@Component
public class WechaPayConfig {
    /*@Bean
    public WxPayService wxMpService(WxPayConfig payConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    @Bean
    public WxPayConfig payConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(PaymentConfig.APPID);
        payConfig.setMchId(PaymentConfig.MCH_ID);
        payConfig.setMchKey(PaymentConfig.API_KEY);
        *//*payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
        payConfig.setKeyPath(this.properties.getKeyPath());*//*

        return payConfig;
    }*/
}
