package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.CashApplyMapper;
import com.xq.live.model.CashApply;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.service.CashApplyService;
import com.xq.live.vo.in.CashApplyInVo;
import com.xq.live.vo.in.UserAccountInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * com.xq.live.service.impl
 *
 * @author zhangpeng32
 * Created on 2018/5/6 下午5:10
 * @Description:
 */
@Service
public class CashApplyServiceImpl implements CashApplyService {

    @Autowired
    private CashApplyMapper cashApplyMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public Long create(CashApply cashApply) {
        //新增数据到提现申请表
        int ret = cashApplyMapper.insert(cashApply);
        if(ret > 0){
            //修改账户信息
            this.updateAccount(cashApply);
            return cashApply.getId();
        }
        return null;
    }

    @Override
    public Pager<CashApply> list(CashApplyInVo inVo) {
        Pager<CashApply> result = new Pager<CashApply>();
        int listTotal = cashApplyMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<CashApply> list = cashApplyMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<CashApply> selectByUserId(Long userId) {
        List<CashApply> list = cashApplyMapper.selectByUserId(userId);
        return list;
    }

    @Override
    public CashApply get(Long id) {
        return cashApplyMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新账户信息——提现
     * @param cashApply
     * @return
     */
    private int updateAccount(CashApply cashApply){
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(cashApply.getUserId());
        accountInVo.setOccurAmount(cashApply.getCashAmount());
        return accountService.payout(accountInVo, "提现操作");
    }

    @Override
    public Long createNew(CashApply cashApply) {
        //新增数据到提现申请表
        int ret = cashApplyMapper.insert(cashApply);
        if(ret > 0){
            return cashApply.getId();
        }
        return null;
    }

    @Override
    public CashApplyInVo getServiceAmount(CashApplyInVo cashApplyInVo) {
        CashApplyInVo cashApply=new CashApplyInVo();
        if(cashApplyInVo.getUserId()==null||cashApplyInVo.getCashAmount()==null){
            return cashApply;
        }
        UserAccount account = accountService.findAccountByUserId(cashApplyInVo.getUserId());
        if(account==null){
            return cashApply;
        }

        cashApply.setServiceRatio(new BigDecimal(0.01).setScale(4,BigDecimal.ROUND_HALF_UP));
        BigDecimal cashAmount=cashApplyInVo.getCashAmount();
        BigDecimal servicAmount=cashAmount.multiply(new BigDecimal(0.01)).setScale(2,BigDecimal.ROUND_HALF_UP);//服务费为提现金额的1%
        //服务费加提现金额大于用户余额，重新计算可提现金额，服务费为提现金额的1%
        if(cashAmount.add(servicAmount).compareTo(account.getUserAmount()) > 0){
            cashAmount= account.getUserAmount().divide(new BigDecimal(1+0.01),2, BigDecimal.ROUND_HALF_UP);
            servicAmount=account.getUserAmount().subtract(cashAmount);
        }

        cashApply.setCashAmount(cashAmount);
        cashApply.setServiceAmount(servicAmount);
        return cashApply;
    }

    @Override
    public Integer paystart(CashApply cashApplyInVo) {
        Integer pay=cashApplyMapper.paystart(cashApplyInVo);
        return pay;
    }

    @Override
    public Integer updateOrderCouponList(CashApplyInVo vo) {
        Integer i=cashApplyMapper.updateOrderCouponByShopIds(vo);
        Integer j=cashApplyMapper.updateOrderInfoByShopIds(vo);
        return i+j;
    }

    @Override
    public List<CashApply> selectListByAccountId(Long accountId) {
        return cashApplyMapper.selectListByAccountId(accountId);
    }
}
