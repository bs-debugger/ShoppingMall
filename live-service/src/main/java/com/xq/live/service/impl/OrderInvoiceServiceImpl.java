package com.xq.live.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Constants;
import com.xq.live.common.Pager;
import com.xq.live.common.SmsUtils;
import com.xq.live.dao.OrderAddressMapper;
import com.xq.live.dao.OrderInvoiceMapper;
import com.xq.live.dao.SmsSendMapper;
import com.xq.live.model.OrderInvoice;
import com.xq.live.service.OrderInvoiceService;
import com.xq.live.vo.in.OrderInvoiceInVo;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.out.OrderAddressOut;
import com.xq.live.vo.out.OrderInvoiceOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * 订单发票serviceImpl
 * Created by lipeng on 2018/12/22.
 */
@Service
public class OrderInvoiceServiceImpl implements OrderInvoiceService{
    @Autowired
    private OrderInvoiceMapper orderInvoiceMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private SmsSendMapper smsSendMapper;

    @Override
    public Pager<OrderInvoiceOut> list(OrderInvoiceInVo inVo) {
        Pager<OrderInvoiceOut> result = new Pager<OrderInvoiceOut>();
        int listTotal = orderInvoiceMapper.listTotal(inVo);
        if(listTotal > 0){
            List<OrderInvoiceOut> list = orderInvoiceMapper.list(inVo);
            for (OrderInvoiceOut orderInvoiceOut : list) {
                this.setOrderAddress(orderInvoiceOut);//设置发票开票接收地址信息
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    public Long add(OrderInvoice inVo) {
        int res = orderInvoiceMapper.insert(inVo);
        if(res < 1){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Boolean checkOrderInvoice(OrderInvoice inVo) {
        OrderInvoice orderInvoice = orderInvoiceMapper.selectByOrderCode(inVo);
        if(orderInvoice==null){
            return false;
        }
        Integer applyStatus = orderInvoice.getApplyStatus();
        if(applyStatus==OrderInvoice.APPLY_STATUS_FAIL){
            return false;
        }
        return null;
    }

    @Override
    public PageInfo<OrderInvoiceOut> showListByTemp(OrderInvoiceInVo orderInvoiceInVo) {
       PageHelper.startPage(orderInvoiceInVo.getPage(), orderInvoiceInVo.getRows());
       List<OrderInvoiceOut> orderInvoiceOutList = orderInvoiceMapper.selectListByTemp(orderInvoiceInVo);
        return new PageInfo<>(orderInvoiceOutList);
    }

    /**
     * 批量同意发票审核
     * */
    @Override
    @Transactional
    public BaseResp agreeApply(OrderInvoiceInVo orderInvoiceInVo) {
        //数据封装
        BaseResp bs = new BaseResp();
        List<OrderInvoice> orderInvoice=orderInvoiceMapper.selectById(orderInvoiceInVo);
        System.out.print(orderInvoiceInVo.getIds().split(",").length);
        try {
            if(orderInvoiceInVo.getIdsLength() != orderInvoice.size()){
                bs.setMessage("有申请不存在");
                bs.setCode(2);
                return  bs;
            }
            for (OrderInvoice invoice : orderInvoice) {

                if(orderInvoiceInVo.getApplyStatus()==2){
                invoice.setApplyStatus(orderInvoiceInVo.getApplyStatus());
                int i= orderInvoiceMapper.updateById(invoice);
                }else {
                    invoice.setApplyStatus(orderInvoiceInVo.getApplyStatus());
                    int i= orderInvoiceMapper.updateById(invoice);
                    //发送短信
                    if (orderInvoiceInVo.getIsSend() == 1) {
                        //发送短信
                        String msg = MessageFormat.format(Constants.ORDER_INVOICE,orderInvoiceInVo.getContent());
                        boolean isSend = SmsUtils.send(invoice.getMobile(), msg);
                        System.out.print(isSend);
                        //记录短信信息
                        SmsSendInVo smsSendInVo = new SmsSendInVo();
                        smsSendInVo.setSmsType(4);//发票审核驳回
                        smsSendInVo.setSmsContent(msg);
                        smsSendInVo.setShopMobile(null);
                        smsSendInVo.setShopId(null);
                        smsSendInVo.setShopName(null);
                        smsSendInVo.setUserName(invoice.getMobile());
                        smsSendInVo.setSendStatus(1);
                        smsSendInVo.setRemark("发票审核驳回");
                        smsSendInVo.setCreateTime(new Date());
                        smsSendMapper.create(smsSendInVo);
                    }
                }
            }
            bs.setCode(1);
            bs.setMessage("同意申请完成");
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }
    /**
     * 驳回原因
     * */
    @Override
    public BaseResp getReason() {
        //数据封装
        BaseResp bs = new BaseResp();
        try {
            bs.setData(orderInvoiceMapper.getReason());
            bs.setCode(0);
            bs.setMessage("查询驳回原因成功");
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }

    public void setOrderAddress(OrderInvoiceOut orderInvoiceOut){
        OrderAddressOut address = orderAddressMapper.getAddress(orderInvoiceOut.getOrderAddressId());
        orderInvoiceOut.setOrderAddressOut(address);
    }

}
