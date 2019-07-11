package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.OrderInvoice;
import com.xq.live.model.User;
import com.xq.live.service.OrderInvoiceService;
import com.xq.live.vo.in.OrderInvoiceInVo;
import com.xq.live.vo.out.OrderInvoiceOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 订单发票相关接口
 * Created by lipeng on 2018/12/22.
 */
@RestController
@RequestMapping(value = "/website/orderInvoice")
public class OrderInvoiceForWebController {
    @Autowired
    private OrderInvoiceService orderInvoiceService;

    /**
     * 查看发票列表
     * page,rows
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<OrderInvoiceOut>> list(OrderInvoiceInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<OrderInvoiceOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Pager<OrderInvoiceOut> list = orderInvoiceService.list(inVo);
        return new BaseResp<Pager<OrderInvoiceOut>>(ResultStatus.SUCCESS,list);
    }

    /**
     * 发票申请
     * 入参:orderCode,sourceType,initialType,invoiceAmount,invoiceOpen
     * (email)--电子发票,(dutyParagraph)--企业,(orderAddressId)--纸质发票
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid OrderInvoice inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getMobile()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        Boolean re = orderInvoiceService.checkOrderInvoice(inVo);
        if(re==true){
            return new BaseResp<Long>(ResultStatus.error_order_invoice_apply);
        }
        inVo.setUserId(user.getId());
        inVo.setMobile(user.getMobile());
        Long id = orderInvoiceService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS,id);
    }
}
