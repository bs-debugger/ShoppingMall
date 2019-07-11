package com.xq.live.service.impl;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ListObjConverter;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.MerchantDetailsMapper;
import com.xq.live.model.AccountLog;
import com.xq.live.model.OperationLog;
import com.xq.live.model.ShopCashier;
import com.xq.live.service.AccountService;
import com.xq.live.service.MerchantDetailsService;
import com.xq.live.service.ShopCashierService;
import com.xq.live.vo.in.MerchantDetailsInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.MerchantDetailsOut;
import com.xq.live.vo.out.ShopZoneOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MerchantDetailsServiceImpl implements MerchantDetailsService {


    @Autowired
    private MerchantDetailsMapper merchantDetailsMapper;

    @Autowired
    private ShopCashierService shopCashierService;

    @Autowired
    private AccountService accountService;

    /**
     *  商户明细列表
     * @param
     * @return  明细列表
     */
    @Override
    public Pager<MerchantDetailsOut> getList(MerchantDetailsInVo merchantDetailsInVo) {
        //创建分页对象
        Pager<MerchantDetailsOut> result = new Pager<>();
        //查询列表
        List<MerchantDetailsOut> list = merchantDetailsMapper.getList(merchantDetailsInVo);
        //转为输出实例
        List<MerchantDetailsOut> accountOuts = ListObjConverter.convert(list, MerchantDetailsOut.class);
        result.setPage(merchantDetailsInVo.getPage());
        result.setTotal(merchantDetailsMapper.getListCount(merchantDetailsInVo));
        result.setList(accountOuts);
        return result;
    }

    /**
     *  商户明细头部信息
     * @param
     * @return
     */
    @Override
    public BaseResp getTableList(MerchantDetailsInVo merchantDetailsInVo) {
        //数据封装
        BaseResp bs = new BaseResp();
        bs.setData(merchantDetailsMapper.getTableList(merchantDetailsInVo));
        bs.setMessage("查询商户明细头部信息成功");
        bs.setCode(0);
        return bs;
    }
    /**
     * 商户明细详情
     * */
    @Override
    public Pager<MerchantDetailsOut> getDetails(MerchantDetailsInVo merchantDetailsInVo) {
        //创建分页对象
        Pager<MerchantDetailsOut> result = new Pager<>();
        //查询列表
        List<MerchantDetailsOut> list = merchantDetailsMapper.getDetails(merchantDetailsInVo);
        //转为输出实例
        List<MerchantDetailsOut> accountOuts = ListObjConverter.convert(list, MerchantDetailsOut.class);
        result.setPage(merchantDetailsInVo.getPage());
        result.setTotal(merchantDetailsMapper.getDetailsCount(merchantDetailsInVo));
        result.setList(accountOuts);
        return result;
    }

    @Override
    public Pager<MerchantDetailsOut> getNoDetails(MerchantDetailsInVo merchantDetailsInVo) {
        //创建分页对象
        Pager<MerchantDetailsOut> result = new Pager<>();
        //查询列表
        List<MerchantDetailsOut> list = merchantDetailsMapper.getNoDetails(merchantDetailsInVo);
        //转为输出实例
        List<MerchantDetailsOut> accountOuts = ListObjConverter.convert(list, MerchantDetailsOut.class);
        result.setPage(merchantDetailsInVo.getPage());
        result.setTotal(merchantDetailsMapper.getNoDetailsCount(merchantDetailsInVo));
        result.setList(accountOuts);
        return result;
    }

    @Override
    public List<ShopZoneOut> listAllShopZone() {
        return merchantDetailsMapper.listAllShopZone();
    }


    /**
     * 商户余额补贴
     * */
    @Override
    @Transactional
    public BaseResp updateBusinesses(UserAccountInVo userAccountInVo) {
        //数据封装
        BaseResp bs = new BaseResp();
        try {
            if(userAccountInVo==null||userAccountInVo.getRemake()==null||userAccountInVo.getType()==null){
                return new BaseResp<List<Map<String,String>>>(ResultStatus.error_param_empty);
            }
            ShopCashier shopCashier=shopCashierService.adminByShopId(userAccountInVo.getShopId());
            if(shopCashier!=null&&shopCashier.getCashierId()!=null){
                //1. 账户日志
                UserAccountInVo accountInVo = new UserAccountInVo();
                accountInVo.setUserId(shopCashier.getCashierId());
                accountInVo.setOccurAmount(userAccountInVo.getOccurAmount());
                accountInVo.setType(AccountLog.TYPE_SHOP);
                AccountLog accountLogInVo = new AccountLog();
                accountLogInVo.setRemark(userAccountInVo.getRemake());
                Integer re=0;
                if(userAccountInVo.getType()==0){
                    re= accountService.updateIncomeV2(accountInVo, accountLogInVo);
                }else if(userAccountInVo.getType()==1){
                    re= accountService.updatePayoutV2(accountInVo, accountLogInVo);
                }
                if(re>0){
                    bs.setMessage("增加成功"+userAccountInVo.getShopId().toString()+"增加了"+userAccountInVo.getOccurAmount());
                    bs.setCode(0);
                    //记录操作日志
                    OperationLog ol = new OperationLog();
                    ol.setType(1);
                    ol.setRemake("操作人:"+UserContext.getUserName()+"增加成功"+userAccountInVo.getShopId().toString()+"增加了"+userAccountInVo.getOccurAmount());
                    merchantDetailsMapper.insertOperationLog(ol);
                }else{
                    bs.setMessage("管理员在系统中未找到对应用户");
                    bs.setCode(1);
                }
            }else{
                bs.setMessage("未找到管理员");
                bs.setCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bs;
    }
}
