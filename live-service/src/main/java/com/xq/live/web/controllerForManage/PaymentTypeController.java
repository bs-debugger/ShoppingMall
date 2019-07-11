
package com.xq.live.web.controllerForManage;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.PaymentTypeService;
import com.xq.live.vo.in.PaymentTypeInVo;
import com.xq.live.vo.out.PaymentTypeOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "支付类型-PaymentTypeController")
@RestController
@RequestMapping("manage/paymentType")
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;


    /**
     * 支付类型列表
     * */
    @ApiOperation(value = "获取支付类型列表")
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public BaseResp<PageInfo<PaymentTypeOut>> getList(@RequestBody PaymentTypeInVo paymentTypeInVo){
        return new BaseResp<>(ResultStatus.SUCCESS, paymentTypeService.getList(paymentTypeInVo));
    }

}

