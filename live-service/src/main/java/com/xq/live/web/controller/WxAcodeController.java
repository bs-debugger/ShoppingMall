package com.xq.live.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.WxXcxUtil;
import com.xq.live.vo.in.WeixinAcodeInVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

@RequestMapping("/wxacode")
@Api(value = "WxAcode", tags = "微信小程序码")
@RestController
public class WxAcodeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxXcxUtil wxXcxUtil;

    @PostMapping("/getUnlimited")
    @ApiOperation(value = "获取小程序码", produces = "application/octet-stream")
    public void getUnlimited(HttpServletResponse response, @RequestBody WeixinAcodeInVo weixinAcodeInVo) {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        String accessToken = wxXcxUtil.getAccessToken();
        byte[] acodeByte = getAcodeByte(accessToken, weixinAcodeInVo);
        try {
            OutputStream out = response.getOutputStream();
            out.write(acodeByte);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取小程序码图片的二进制内容
     * @param accessToken
     * @param weixinAcodeInVo
     * @return
     */
    private byte[] getAcodeByte(String accessToken, WeixinAcodeInVo weixinAcodeInVo) {
        // 请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 请求参数
        JSONObject jsonObject = new JSONObject();
        if (!StringUtils.isBlank(weixinAcodeInVo.getScene())) {
            jsonObject.put("scene", weixinAcodeInVo.getScene());
        }
        if (!StringUtils.isBlank(weixinAcodeInVo.getPage())) {
            jsonObject.put("page", weixinAcodeInVo.getPage());
        }
        if (weixinAcodeInVo.getWidth() != null && weixinAcodeInVo.getWidth() <= 1280 && weixinAcodeInVo.getWidth() >= 280) {
            jsonObject.put("width", weixinAcodeInVo.getWidth());
        }
        if (weixinAcodeInVo.getAutoColor() != null) {
            jsonObject.put("auto_color", weixinAcodeInVo.getAutoColor());
            if (!weixinAcodeInVo.getAutoColor() && !StringUtils.isBlank(weixinAcodeInVo.getLineColor())) {
                String lineColor = weixinAcodeInVo.getLineColor();
                String[] lcs = lineColor.split(",");
                if (lcs.length == 3) {
                    JSONObject lcsObj = new JSONObject();
                    lcsObj.put("r", lcs[0]);
                    lcsObj.put("g", lcs[1]);
                    lcsObj.put("b", lcs[2]);
                    jsonObject.put("line_color", lcsObj);
                }

            }
        }
        if (weixinAcodeInVo.getIsHyaline() != null) {
            jsonObject.put("is_hyaline", weixinAcodeInVo.getIsHyaline());
        }
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject, httpHeaders);
        URI uri = URI.create(String.format(WxXcxUtil.GET_WXACODE_UNLIMIT_URL, accessToken));
        ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, byte[].class);
        if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
            return response.getBody();
        }
        return null;
    }

}
