package com.xq.live.web.controllerForManage;

import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.OrderInvoiceService;
import com.xq.live.vo.in.OrderInvoiceInVo;
import com.xq.live.vo.out.OrderInvoiceOut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/manage/orderInvoice")
@Api(value = "OrderInvoice", tags = "发票审核")
@RestController
public class OrderInvoiceForManageController {

    @Autowired
    private OrderInvoiceService orderInvoiceService;

    @ApiOperation("分页查询申请状态的发票信息")
    @RequestMapping(value = "/showList", method = RequestMethod.POST)
    public BaseResp<PageInfo<OrderInvoiceOut>> showList(@RequestBody OrderInvoiceInVo orderInvoiceInVo) {
        return new BaseResp<>(ResultStatus.SUCCESS, orderInvoiceService.showListByTemp(orderInvoiceInVo));
    }

    /**
     * 批量同意发票申请and驳回
     * @return id 退款申请PayRefundApplication的ID
     */
    @ApiOperation("批量同意发票申请")
    @RequestMapping(value = "/agreeApply", method = RequestMethod.POST)
    public BaseResp agreeApply(@RequestBody OrderInvoiceInVo orderInvoiceInVo) {
        return orderInvoiceService.agreeApply(orderInvoiceInVo);
    }

    /**
     * 驳回原因
     * */
    @ApiOperation("驳回原因")
    @RequestMapping(value = "/getReason", method = RequestMethod.POST)
    public BaseResp getReason(){
        return orderInvoiceService.getReason();

    }
}
