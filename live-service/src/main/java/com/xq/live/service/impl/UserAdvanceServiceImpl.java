package com.xq.live.service.impl;

import com.xq.live.common.*;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.UserAdvanceService;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.in.UserAdvanceInVo;
import com.xq.live.vo.out.UserAdvanceOut;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserAdvanceServiceImpl implements UserAdvanceService {

    private Logger logger = Logger.getLogger(GoldLogServiceImpl.class);

    @Resource
    UserAdvanceMapper userAdvanceMapper;

    @Resource
    CashApplyMapper cashApplyMapper;

    @Resource
    UserAccountMapper userAccountMapper;

    @Resource
    AccountLogMapper accountLogMapper;

    @Resource
    RejectInfoMapper rejectInfoMapper;

    @Resource
    SmsSendMapper smsSendMapper;

    @Override
    public Pager<UserAdvanceOut> searchUserAdvanceList(UserAdvanceInVo inVo) {
        //创建分页对象
        Pager<UserAdvanceOut> result =  new Pager<UserAdvanceOut>();
        //获取数据实例
        List<UserAdvance> advanceList = userAdvanceMapper.searchUserAdvanceList(inVo);
        if(advanceList.isEmpty()){
            return result;
        }
        //转为输出实例
        List<UserAdvanceOut> accountOuts= ListObjConverter.convert(advanceList,UserAdvanceOut.class);
        result.setTotal(userAdvanceMapper.searchUserAdvanceTotal(inVo));
        result.setPage(inVo.getPage());
        result.setList(accountOuts);;
        return result;
    }

    @Override
    public BaseResp batchApplyPass(List<Integer> cashIds) {
        if(cashIds==null){
            return new BaseResp(ResultStatus.error_param_empty_id);
        }
        int  total=userAdvanceMapper.batchMotifyApplyStatus(cashIds,2);
        if(total>0){
            logger.info(total+"条数据已审批通过！");
        }
        return new BaseResp(ResultStatus.SUCCESS);
    }

    @Override
    @Transactional
    public BaseResp batchApplyReject(UserAdvanceInVo inVo1) {
        if(inVo1.getItems()==null){
            return new BaseResp(ResultStatus.error_param_empty_id);
        }
        for (Integer cashId :inVo1.getItems()) {
            //查找提现详情
            CashApply cashApply=cashApplyMapper.selectByPrimaryKey(Long.valueOf(cashId));
            //找到该用户的账号
            UserAccount account = userAccountMapper.findAccountByUserId(cashApply.getUserId());
            //用户余额
            BigDecimal preAmount=account.getUserAmount();
            //驳回返还用户余额:累加(提现余额+用户账户余额=总余额)
            BigDecimal afterAmount=account.getUserAmount().add(cashApply.getCashAmount());

            //更新用户账上余额
            UserAccountInVo inVo=new UserAccountInVo();
            inVo.setId(account.getId());
            inVo.setUserAmount(afterAmount);
            if(userAccountMapper.updateByPrimaryKeySelective(inVo)>0){
                //记录账户日志
                AccountLog accountLog=new AccountLog();
                accountLog.setUserId(account.getUserId());
                accountLog.setUserName(account.getUserName());
                accountLog.setAccountId(account.getId());
                accountLog.setAccountName(account.getAccountName());
                accountLog.setPreAmount(preAmount);
                accountLog.setAfterAmount(afterAmount);
                accountLog.setOperateAmount(cashApply.getCashAmount());
                accountLog.setOperateType(2);//操作类型:收入
                accountLog.setRemark("用户提现");
                accountLog.setCreateTime(new Date());
                accountLog.setType(3);//日志类型:用户余额
                accountLog.setCashApplyId(cashApply.getId());//绑定提现编号
                accountLog.setUpdateTime(new Date());
                accountLogMapper.insertSelective(accountLog);
            }else{
                logger.warn("用户名:"+account.getUserName()+",余额更新失败");
                continue;
            }
            //记录驳回明细
            RejectInfo rejectInfo=new RejectInfo();
            rejectInfo.setCashId(cashApply.getId());
            rejectInfo.setContent(inVo1.getContent());
            rejectInfo.setType(2);//用户提现驳回
            rejectInfo.setImageUrl(inVo1.getImageUrl());
            rejectInfo.setIsSend(inVo1.getIsSend());
            rejectInfo.setUpdateTime(new Date());
            rejectInfo.setCreateTime(new Date());
            rejectInfo.setIsDeleted(0);
            rejectInfoMapper.insertSelective(rejectInfo);
            //发送短信
            if(inVo1.getIsSend()==1){
                //发送短信
                String message = MessageFormat.format(Constants.WINNER_LOOK_ACCOUNT_DEPOSIT_REJECT, inVo1.getContent());
                boolean isSend=SmsUtils.send(cashApply.getUserName(), message);
                //记录短信信息
                SmsSendInVo smsSendInVo = new SmsSendInVo();
                smsSendInVo.setSmsType(3);//提现驳回
                smsSendInVo.setSmsContent(message);
                smsSendInVo.setShopMobile(null);
                smsSendInVo.setShopId(null);
                smsSendInVo.setShopName(null);
                smsSendInVo.setUserName(cashApply.getUserName());
                smsSendInVo.setSendStatus(isSend==true?1:0);
                smsSendInVo.setRemark("用户提现驳回");
                smsSendInVo.setCreateTime(new Date());
                smsSendMapper.create(smsSendInVo);
            }
        }
        int  total=userAdvanceMapper.batchMotifyApplyStatus(inVo1.getItems(),3);
        if(total>0){
            logger.info(total+"条数据已审批驳回！");
        }
        return new BaseResp(ResultStatus.SUCCESS);
    }
}
