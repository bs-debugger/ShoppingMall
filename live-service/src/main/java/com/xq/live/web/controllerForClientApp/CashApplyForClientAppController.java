package com.xq.live.web.controllerForClientApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.*;
import com.xq.live.service.*;
import com.xq.live.vo.in.CashApplyInVo;
import com.xq.live.vo.in.OrderWriteOffInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.OrderWriteOffOut;
import com.xq.live.vo.out.SoWriteOffOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * com.xq.live.web.controller
 *  提现业务
 * @author zhangpeng32
 * Created on 2018/5/6 下午4:51
 * @Description:
 */
@RequestMapping(value = "clientApp/tx")
@RestController
public class CashApplyForClientAppController {

    @Autowired
    private CashApplyService cashApplyService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ShopCashierService shopCashierService;

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderWriteOffService orderWriteOffService;
    /**
     * 申请提现
     *
     * 注:userId和userName是当前用户的,要从网关中取
     * @param cashApply
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResp<?> create(@Valid CashApply cashApply, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        cashApply.setUserId(user.getId());
        cashApply.setUserName(user.getUserName());
        //查询账户信息
        UserAccount account = accountService.findAccountByUserId(cashApply.getUserId());
        if(account == null){
            return new BaseResp<Long>(ResultStatus.error_user_account_info);
        }

        //提取金额大于账户余额
        if(cashApply.getCashAmount().compareTo(account.getAccountAmount()) > 0){
            return new BaseResp<Long>(ResultStatus.error_cash_apply_amount);
        }

        cashApply.setAccountId(account.getId());
        cashApply.setAccountName(account.getAccountName());
        cashApply.setApplyType(CashApply.CASH_APPLY_TYPE_SHOP);
        Long res = cashApplyService.create(cashApply);
        return new BaseResp<Long>(ResultStatus.SUCCESS, res);
    }

    /**
     * 提现列表查询,核销员要看管理员的提现申请列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseResp<?> list(CashApplyInVo inVo){
        if(inVo == null || inVo.getUserId() == null){
            return new BaseResp<Pager<CashApply>>(ResultStatus.error_para_user_empty);
        }
        Pager<CashApply> res = cashApplyService.list(inVo);
        return new BaseResp<Pager<CashApply>>(ResultStatus.SUCCESS, res);
    }

    /**
     * 根据id查询单个提现详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<CashApply> getShopById(@PathVariable(value = "id") Long id) {
        CashApply result = cashApplyService.get(id);
        return new BaseResp<CashApply>(ResultStatus.SUCCESS, result);
    }

    /**
     * 通过shopId查询商家的缴费记录从而查出他可提现的发起时间和结束时间
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectCashTime",method = RequestMethod.GET)
    public BaseResp<CashApply> selectCashTime(Long shopId){
        //判断该商家是否存在管理员
        ShopCashier shopCashier = shopCashierService.adminByShopId(shopId);
        if(shopCashier==null||shopCashier.getCashierId()==null){
            return new BaseResp<CashApply>(ResultStatus.error_shop_admin);
        }

        List<CashApply> list = cashApplyService.selectByUserId(shopCashier.getCashierId());
        if(list==null||list.size()==0){
            return new BaseResp<CashApply>(ResultStatus.SUCCESS,null);
        }
        //目前先按照创建时间来排序,后期可以在此扩展改变(最先的时间放到前面)
        Collections.sort(list, new Comparator<CashApply>() {
            public int compare(CashApply o1, CashApply o2) {
                if (o1.getCreateTime().before(o2.getCreateTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        CashApply newCash = null;
        for (CashApply apply : list) {
            //获取最近的审核通过数据
            if((apply.getApplyStatus()==CashApply.CASH_APPLY_STATUS_TG)||(apply.getApplyStatus()==CashApply.CASH_APPLY_STATUS_WAIT)){
                newCash = apply;
                break;
            }
        }
        return new BaseResp<CashApply>(ResultStatus.SUCCESS,newCash);
    }

    /**
     * 通过shopId查询商家的提现
     * 注:这里面可以判断商家是否有权限发起提交申请
     *
     *
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectByShopId",method = RequestMethod.GET)
    public BaseResp<CashApply> selectByShopId(Long shopId)throws ParseException {
        //判断该商家是否存在管理员
        ShopCashier shopCashier = shopCashierService.adminByShopId(shopId);
        if(shopCashier==null||shopCashier.getCashierId()==null){
            return new BaseResp<CashApply>(ResultStatus.error_shop_admin);
        }

        //查询账户信息,判断账户是否被冻结
        UserAccount account = accountService.findAccountByUserId(shopCashier.getCashierId());
        if(account == null){
            return new BaseResp<CashApply>(ResultStatus.error_user_account_info);
        }
        if(account.getAccountStatus()==UserAccount.ACCOUNT_STATUS_FROZEN){
            return new BaseResp<CashApply>(ResultStatus.error_shop_have_freeze);
        }

        //获取当前日期的前一天
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = dateFormat1.parse(dateFormat1.format(new Date()));
        List<CashApply> list = cashApplyService.selectByUserId(shopCashier.getCashierId());
        //当查询为空的时候，证明尚未发起任何提现申请，则提现申请的开始时间默认为2018/01/01
        if(list==null||list.size()==0){
            Date myDate1 = dateFormat1.parse("2018-01-01 00:00:00");
            //判断商家是否缴清服务费
            if (isJiao(shopId,myDate1,newDate)){
                return new BaseResp<CashApply>(ResultStatus.error_shop_no_service);
            }
            return new BaseResp<CashApply>(ResultStatus.SUCCESS,null);
        }

        CashApply cashApply = list.get(list.size() - 1);//查询提现申请中最后一条记录
        if(cashApply.getApplyStatus()==CashApply.CASH_APPLY_STATUS_WAIT){
            return  new BaseResp<CashApply>(ResultStatus.error_shop_have_apply);
        }

        //目前先按照创建时间来排序,后期可以在此扩展改变(最先的时间放到前面)
        Collections.sort(list, new Comparator<CashApply>() {
            public int compare(CashApply o1, CashApply o2) {
                if (o1.getCreateTime().before(o2.getCreateTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        CashApply newCash = null;
        for (CashApply apply : list) {
            //获取最近的审核通过数据
            if(apply.getApplyStatus()==CashApply.CASH_APPLY_STATUS_TG){
                newCash = apply;
                break;
            }
        }

        //判断商家是否缴清服务费
        if (isJiao(shopId,newCash.getEndTime(),newDate)){
            return new BaseResp<CashApply>(ResultStatus.error_shop_no_service);
        }
        return new BaseResp<CashApply>(ResultStatus.SUCCESS,newCash);
    }


    //判断在某个时间段内商家是否缴清服务费
    public Boolean isJiao(Long shopId,Date beginTime,Date endTime){
        SoWriteOffInVo inVo = new SoWriteOffInVo();

        inVo.setShopId(shopId);
        inVo.setBegainTime(beginTime);
        inVo.setEndTime(endTime);
        inVo.setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
        SoWriteOffOut offOut=soWriteOffService.listAmount(inVo).get(0);
        if (offOut!=null&&offOut.getTotalService().compareTo(BigDecimal.ZERO)!=0){
            return true;//未缴清服务费
        }
        return false;
    }

    /**
     * 通过shopId查询商家的提现
     * 注:这里面可以判断商家是否有权限发起提交申请
     *
     *
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectByShopIdNew",method = RequestMethod.GET)
    public BaseResp<CashApply> selectByShopIdNew(Long shopId)throws ParseException {
        //判断该商家是否存在管理员
        ShopCashier shopCashier = shopCashierService.adminByShopId(shopId);
        if(shopCashier==null||shopCashier.getCashierId()==null){
            return new BaseResp<CashApply>(ResultStatus.error_shop_admin);
        }

        //查询账户信息,判断账户是否被冻结
        UserAccount account = accountService.findAccountByUserId(shopCashier.getCashierId());
        if(account == null){
            return new BaseResp<CashApply>(ResultStatus.error_user_account_info);
        }
        if(account.getAccountStatus()==UserAccount.ACCOUNT_STATUS_FROZEN){
            return new BaseResp<CashApply>(ResultStatus.error_shop_have_freeze);
        }

        //获取当前日期的前一天
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = dateFormat1.parse(dateFormat1.format(new Date()));
        List<CashApply> list = cashApplyService.selectByUserId(shopCashier.getCashierId());
        //当查询为空的时候，证明尚未发起任何提现申请，则提现申请的开始时间默认为2018/01/01
        if(list==null||list.size()==0){
            Date myDate1 = dateFormat1.parse("2018-01-01 00:00:00");
            //判断商家是否缴清服务费
            if (isJiaoNew(shopId,myDate1,newDate)){
                return new BaseResp<CashApply>(ResultStatus.error_shop_no_service);
            }
            return new BaseResp<CashApply>(ResultStatus.SUCCESS,null);
        }

        CashApply cashApply = list.get(list.size() - 1);//查询提现申请中最后一条记录
        if(cashApply.getApplyStatus()==CashApply.CASH_APPLY_STATUS_WAIT){
            return  new BaseResp<CashApply>(ResultStatus.error_shop_have_apply);
        }

        //目前先按照创建时间来排序,后期可以在此扩展改变(最先的时间放到前面)
        Collections.sort(list, new Comparator<CashApply>() {
            public int compare(CashApply o1, CashApply o2) {
                if (o1.getCreateTime().before(o2.getCreateTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        CashApply newCash = null;
        for (CashApply apply : list) {
            //获取最近的审核通过数据
            if(apply.getApplyStatus()==CashApply.CASH_APPLY_STATUS_TG){
                newCash = apply;
                break;
            }
        }

        //判断商家是否缴清服务费
        if (isJiaoNew(shopId,newCash.getEndTime(),newDate)){
            return new BaseResp<CashApply>(ResultStatus.error_shop_no_service);
        }
        return new BaseResp<CashApply>(ResultStatus.SUCCESS,newCash);
    }

    //判断在某个时间段内商家是否缴清服务费
    public Boolean isJiaoNew(Long shopId,Date beginTime,Date endTime){

        OrderWriteOffInVo inVo=new OrderWriteOffInVo();
        inVo.setShopId(shopId);
        inVo.setBeginTime(beginTime);
        inVo.setEndTime(endTime);
        inVo.setIsBill(SoWriteOff.SO_WRITE_OFF_NO_BILL);
        OrderWriteOffOut offOut=orderWriteOffService.listAmount(inVo).get(0);
        if (offOut!=null&&offOut.getTotalService().compareTo(BigDecimal.ZERO)!=0){
            return true;//未缴清服务费
        }
        return false;
    }
}
