package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.OrderCouponPostage;
import com.xq.live.model.User;
import com.xq.live.service.OrderCouponPostageService;
import com.xq.live.vo.in.OrderCouponPostageInVo;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ss on 2019/1/25.
 * 运费订单相关接口
 */
@RestController
@RequestMapping(value = "/couponPostage")
public class OrderCouponPostageController {

    @Autowired
    private OrderCouponPostageService orderCouponPostageService;

    /**
     * 查询活动详细页，包括访问量、参与数、投票数信息
     * @param inVo
     * @param request
     * @return
     */
    @ApiOperation(value = "创建运费订单",notes = "创建平台配送邮费订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderCouponId", value = "票券id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "orderCouponCode", value = "票券code", required = true, dataType = "string"),
            @ApiImplicitParam(name = "shopId", value = "商家id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "couponAddressId", value = "票券地址id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "realWeight", value = "重量", required = false, dataType = "int"),
            @ApiImplicitParam(name = "bulk", value = "体积", required = false, dataType = "int"),
            @ApiImplicitParam(name = "piece", value = "件数", required = false, dataType = "int"),
            @ApiImplicitParam(name = "templateId", value = "运费模板id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "remark", value = "备注", required = false, dataType = "string"),
            @ApiImplicitParam(name = "sendTime", value = "期望配送时间", required = false, dataType = "date"),
            @ApiImplicitParam(name = "formulaMode", value = "计算方式 0或则null为重量 1 为件数 2为体积", required = true, dataType = "int")

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResp<OrderCouponPostage> create(@RequestBody OrderCouponPostageInVo inVo, BindingResult result,HttpServletRequest request) {
        if(inVo==null||inVo.getOrderCouponId()==null||inVo.getOrderCouponCode()==null||
                inVo.getFormulaMode()==null||inVo.getTemplateId()==null){
            return new BaseResp<OrderCouponPostage>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<OrderCouponPostage>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Long id=orderCouponPostageService.postageInfo(inVo);
        inVo.setId(id);
        OrderCouponPostage orderCouponPostage=orderCouponPostageService.selectInfo(inVo);
        return new BaseResp<OrderCouponPostage>(ResultStatus.SUCCESS, orderCouponPostage);
    }

}
