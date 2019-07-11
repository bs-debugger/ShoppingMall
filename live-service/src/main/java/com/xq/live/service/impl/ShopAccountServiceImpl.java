package com.xq.live.service.impl;

import com.xq.live.common.*;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ShopAccountService;
import com.xq.live.vo.in.IdInVo;
import com.xq.live.vo.in.ShopAccountInVo;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.out.ShopAccountOut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ShopAccountServiceImpl implements ShopAccountService {

    @Resource
    ShopAccountMapper shopAccountMapper;

    @Resource
    AccountLogMapper accountLogMapper;

    @Resource
    RejectInfoMapper rejectInfoMapper;

    @Resource
    CashApplyMapper cashApplyMapper;

    @Resource
    SmsSendMapper smsSendMapper;

    @Resource
    ShopAccountLogMapper shopAccountLogMapper;

    @Override
    public ShopAccount findShopAccountTotal(ShopAccountInVo inVo) {
        return shopAccountMapper.findShopAccountTotal(inVo);
    }

    @Override
    public Pager<ShopAccountOut> searchShopWithdrawList(ShopAccountInVo inVo) {
        //创建分页对象
        Pager<ShopAccountOut> result = new Pager<ShopAccountOut>();
        //获取数据实例
        List<ShopAccount> accountList = shopAccountMapper.searchShopWithdrawList(inVo);
        //转为输出实例
        List<ShopAccountOut> accountOuts = ListObjConverter.convert(accountList, ShopAccountOut.class);
        result.setTotal(shopAccountMapper.searchShopWithdrawTotal(inVo));
        result.setPage(inVo.getPage());
        result.setList(accountOuts);
        ;
        return result;
    }

    @Override
    @Transactional
    public BaseResp approvePass(IdInVo idInVo) {
        //主键必传项
        if (idInVo.getId() == null) {
            return new BaseResp(ResultStatus.error_param_empty_id);
        }
        ShopAccountInVo inVo = new ShopAccountInVo();
        inVo.setCashId(idInVo.getId());
        //修改已经审批状态
        inVo.setApplyStatus(2);
        int total = shopAccountMapper.updateApproveStatusById(inVo) == 1 ? 1 : 0;
        if (total == 0) {
            return new BaseResp(ResultStatus.error_allocation_update);
        }
        //返回结果集
        ShopAccount shopAccount = shopAccountMapper.selectByPrimaryKey(Long.valueOf(inVo.getCashId()));
        inVo.setStartTime(shopAccount.getBeginTime());
        inVo.setEndTime(shopAccount.getEndTime());

        //核销票券
        List<Integer> verificationTicketList = shopAccountMapper.verificationTicketList(inVo);
        if(!verificationTicketList.isEmpty()){
            shopAccountMapper.verificationTicketByTime(verificationTicketList);
        }

        //核销订单
        List<Integer> verificationOrderList = shopAccountMapper.verificationOrderList(inVo);
        if(!verificationOrderList.isEmpty()){
            shopAccountMapper.verificationOrderByTime(verificationOrderList);
        }

        return new BaseResp(ResultStatus.SUCCESS);
    }

    @Override
    @Transactional
    public BaseResp approveReject(ShopAccountInVo inVo) {
        if (inVo.getCashId() == null) {
            return new BaseResp(ResultStatus.error_param_empty_id);
        }
        ShopAccount shopAccount = shopAccountMapper.selectByPrimaryKey(Long.valueOf(inVo.getCashId()));
        //记录驳回明细
        RejectInfo rejectInfo = new RejectInfo();
        rejectInfo.setCashId(Long.parseLong(inVo.getCashId().toString()));
        rejectInfo.setContent(inVo.getContent());
        rejectInfo.setType(1);//商户提现驳回
        rejectInfo.setImageUrl(inVo.getImageUrl());
        rejectInfo.setIsSend(inVo.getIsSend());
        rejectInfo.setUpdateTime(new Date());
        rejectInfo.setCreateTime(new Date());
        rejectInfo.setIsDeleted(0);
        rejectInfoMapper.insertSelective(rejectInfo);
        //发送短信
        if (inVo.getIsSend() == 1) {
            //查询手机号
            CashApply cashApply = cashApplyMapper.selectByPrimaryKey(Long.parseLong(inVo.getCashId().toString()));
            //发送短信
            String message = MessageFormat.format(Constants.WINNER_LOOK_ACCOUNT_DEPOSIT_REJECT, inVo.getContent());
            boolean isSend=SmsUtils.send(cashApply.getUserName(), message);
            //记录短信信息
            SmsSendInVo smsSendInVo = new SmsSendInVo();
            smsSendInVo.setSmsType(3);//提现驳回
            smsSendInVo.setSmsContent(message);
            smsSendInVo.setShopMobile(cashApply.getUserName());
            smsSendInVo.setShopId(Long.valueOf(shopAccount.getShopId()));
            smsSendInVo.setShopName(shopAccount.getShopName());
            smsSendInVo.setUserName(cashApply.getUserName());
            smsSendInVo.setSendStatus(isSend==true?1:0);
            smsSendInVo.setRemark("商户提现驳回");
            smsSendInVo.setCreateTime(new Date());
            smsSendMapper.create(smsSendInVo);
        }
        inVo.setApplyStatus(3);
        int total = shopAccountMapper.updateApproveStatusById(inVo) == 1 ? 1 : 0;
        if (total > 0) {
            return new BaseResp(ResultStatus.SUCCESS);
        } else {
            return new BaseResp(ResultStatus.error_allocation_update);
        }
    }

    @Override
    public Pager<ShopAccountDetail> findShopAccountDetailById(ShopAccountInVo inVo) {
        ShopAccount shopAccount = shopAccountMapper.selectByPrimaryKey(Long.valueOf(inVo.getCashId()));
        inVo.setStartTime(shopAccount.getBeginTime());
        inVo.setEndTime(shopAccount.getEndTime());
        inVo.setShopId(shopAccount.getShopId());
        List<ShopAccountDetail> details = shopAccountMapper.findShopAccountDetailById(inVo);

        Pager<ShopAccountDetail> result = new Pager<ShopAccountDetail>();
        result.setTotal(shopAccountMapper.findShopAccountDetailByIdTotal(inVo));
        result.setPage(inVo.getPage());
        result.setList(details);
        ;
        return result;
    }

    @Override
    public Pager<AccountLog> findAccountLog(ShopAccountInVo inVo) {
        ShopAccount shopAccount = shopAccountMapper.selectByPrimaryKey(Long.valueOf(inVo.getCashId()));
        inVo.setStartTime(shopAccount.getBeginTime());
        inVo.setEndTime(shopAccount.getEndTime());
        inVo.setUserId(shopAccount.getUserId());
        List<AccountLog> details = accountLogMapper.selectSpecialLog(inVo);

        Pager<AccountLog> result = new Pager<AccountLog>();
        result.setTotal(accountLogMapper.selectSpecialLogTotal(inVo));
        result.setPage(inVo.getPage());
        result.setList(details);
        return result;
    }

    @Override
    public ShopAccount refreshShopAccountTotal(ShopAccountInVo inVo) {

       ShopAccountLog  shopAccountLog = shopAccountLogMapper.serachHisDate();
       if(shopAccountLog!=null ){
           SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           //获取上一次的刷新时间
           String startTime=sdf.format(shopAccountLog.getCreateTime());
           String endTime=sdf.format(new Date());
           //微信收款记录
           shopAccountLogMapper.wxPayByTime(startTime,endTime);
           //微信退款记录
           shopAccountLogMapper.wxRefundByTime(startTime,endTime);
           //余额支付记录
           shopAccountLogMapper.yuePayByTime(startTime,endTime);
           //余额退款明细
           shopAccountLogMapper.yueRefundByTime(startTime,endTime);
       }
        return findShopAccountTotal(inVo);
    }


}
