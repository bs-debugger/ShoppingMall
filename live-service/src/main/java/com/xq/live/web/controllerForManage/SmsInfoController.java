package com.xq.live.web.controllerForManage;

import com.xq.live.common.BaseResp;
import com.xq.live.model.SmsInfo;
import com.xq.live.service.SmsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "短信内容模板-SmsInfoController")
@RequestMapping("/manage/smsInfo")
@RestController
public class SmsInfoController {

    @Resource
    SmsInfoService smsInfoService;

    @ApiOperation(value = "根据短信类型查找内容1 商户提现驳回；2 用户提现驳回")
    @GetMapping(value = "/findSmsTemplateByType/{smsType}")
    public BaseResp<List<SmsInfo>> findSmsTemplateByType(@PathVariable(value = "smsType") Integer smsType) {
        return smsInfoService.findSmsTemplateByType(smsType);
    }
}
