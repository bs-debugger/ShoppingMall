package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.config.FreeSkuConfig;
import com.xq.live.model.Sku;
import com.xq.live.model.So;
import com.xq.live.model.UserAccount;
import com.xq.live.model.User;
import com.xq.live.service.AccountService;
import com.xq.live.service.SkuService;
import com.xq.live.service.SoService;
import com.xq.live.service.SoWriteOffService;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 订单controller
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:24
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/so")
public class SoController{

    @Autowired
    private SoService soService;

    @Autowired
    private FreeSkuConfig freeSkuConfig;

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SkuService skuService;

    /**
     * 查一条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SoOut> get(@PathVariable("id") Long id, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        SoOut soOut = soService.get(id);
        return new BaseResp<SoOut>(ResultStatus.SUCCESS, soOut);
    }

    /**
     * 查询我的订单中的订单详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getForOrder/{id}", method = RequestMethod.GET)
    public BaseResp<SoForOrderOut> getForOrder(@PathVariable("id") Long id) {
        SoForOrderOut forOrder = soService.getForOrder(id);
        return new BaseResp<SoForOrderOut>(ResultStatus.SUCCESS, forOrder);
    }

    /**
     * 分页查询列表
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<SoOut>> list(SoInVo inVo) {
        if(inVo==null||inVo.getUserId()==null){
            return new BaseResp<Pager<SoOut>>(ResultStatus.error_param_empty);
        }
        Pager<SoOut> result = soService.list(inVo);
        return new BaseResp<Pager<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查我的订单
     *注:此地方不能直接加入线程里的userId，因为在商家端里面需要通过shopId去查询订单
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/myorder", method = RequestMethod.GET)
    public BaseResp<List<SoOut>> top(SoInVo inVo) {
        if(inVo==null||inVo.getUserId()==null){
            return new BaseResp<List<SoOut>>(ResultStatus.error_param_empty);
        }
        List<SoOut> result = soService.findSoList(inVo);
        return new BaseResp<List<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查我的商家订单
     *查询我的订单中的商家订单，基本参数与平台订单相同  userId ,page,rows,soStatus
     *
     * 注:此地方不能直接加入线程里的userId，因为在商家端里面需要通过shopId去查询订单
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/myorderForShop", method = RequestMethod.GET)
    public BaseResp<Pager<SoOut>> myorderForShop(SoInVo inVo) {
        inVo.setSoType(So.SO_TYPE_SJ);
        Pager<SoOut> result = soService.findSoListForShop(inVo);
        return new BaseResp<Pager<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 生成订单
     *(生成平台订单，食典券订单，砍价券订单)
     * 平台普通订单: userId:52121 userName:13339981235 skuId:1256 skuNum:1 shopId:296 payType:2
     * 平台食典券订单: userId:52121 userName:13339981235 skuId:1256 skuNum:1 shopId:296 payType:2 dishSkuId dishSkuName
     * 平台砍价券订单: userId:52121 userName:13339981235 skuId:1256 skuNum:1 shopId:296 payType:2 dishSkuId dishSkuName
     * 平台抢购券订单: userId:52121 userName:13339981235 skuId:1256 skuNum:1 shopId:296 payType:2 dishSkuId dishSkuName
     * 平台兑换券订单: userId:52121 userName:13339981235 skuId:1256 skuNum:1 shopId:296 payType:2 dishSkuId dishSkuName
     * 注意:在购买砍价券订单之前，要根据支付金额判断是否有合适的砍菜券，如果没有则需要生成砍价券
     *     在购买抢购券订单之前，要根据支付金额判断是否有合适的砍菜券，如果没有则需要生成抢购券
     *     在购买兑换券订单之前，要根据支付金额判断是否有合适的砍菜券，如果没有则需要生成兑换券
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResp<Long> create(@Valid SoInVo inVo, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //当购买的是7.7元的活动券的时候,要判断之前是否领过该券，并且券是否已被使用
        if(inVo.getSkuId().equals(actSkuConfig.getSkuIdOther())){
            Integer integer = soWriteOffService.canGetAgio(inVo);
            if(integer>=1){
                return new BaseResp<Long>(ResultStatus.error_act_sku_not_use);
            }
        }
        if(inVo.getDishSkuId()!=null) {
            Sku sku = skuService.get(inVo.getDishSkuId());
            if (sku==null){
                return new BaseResp<Long>(ResultStatus.error_param_empty);
            }
            if(sku.getSkuType()==Sku.SKU_TYPE_DHC&&sku.getType()==Sku.TYPE_DZX) {
                //到店消费螃蟹订单一天只能下一单
                Integer i = soService.selectSkuAllocation(inVo.getUserId());
                if (i > 0) {
                    return new BaseResp<Long>(ResultStatus.error_no_use);
                }
            }
        }
        inVo.setUserIp(IpUtils.getIpAddr(request));
        Long id = soService.create(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 生成商家订单
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/createForShop", method = RequestMethod.POST)
    public BaseResp<Long> createForShop(SoInVo inVo,HttpServletRequest request) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //如果生成的商家单中用了券，则必须要传skuId和skuNum
        if(inVo==null||inVo.getUserId()==null||inVo.getUserName()==null||inVo.getPayType()==null||inVo.getSoAmount()==null||inVo.getShopId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserIp(IpUtils.getIpAddr(request));
        Long id = soService.createForShop(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 新用户首单免费
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/freeOrder", method = RequestMethod.POST)
    public BaseResp<Long> freeOrder(@Valid SoInVo inVo, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //判断是否是新用户
        Integer soOutNum = soService.selectByUserIdTotal(inVo.getUserId());
        if(soOutNum > 0){
            return new BaseResp<Long>(ResultStatus.error_user_not_new);
        }
        inVo.setSkuId(freeSkuConfig.getSkuId());
        inVo.setSkuNum(freeSkuConfig.getSkuNum());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setSoType(So.SO_TYPE_PT);
        inVo.setPayType(So.SO_PAY_TYPE_XQ);
        Long id = soService.freeOrder(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 领取活动券
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/freeOrderForAct", method = RequestMethod.POST)
    public BaseResp<Long> freeOrderForAct(@Valid SoInVo inVo, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setSoType(So.SO_TYPE_PT);
        inVo.setPayType(So.SO_PAY_TYPE_XQ);
        Long id = soService.freeOrderForAct(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     *  领取折扣券
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/freeOrderForAgio", method = RequestMethod.POST)
    public BaseResp<Long> freeOrderForAgio(@Valid SoInVo inVo, BindingResult result,HttpServletRequest request){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        Integer integer = soWriteOffService.canGetAgio(inVo);
        if(integer>=1){
            return new BaseResp<Long>(ResultStatus.error_agio_fail);
        }
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setSoType(So.SO_TYPE_PT);
        inVo.setPayType(So.SO_PAY_TYPE_XQ);
        Long id = soService.freeOrder(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 订单支付
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/paid", method = RequestMethod.POST)
    public BaseResp<Integer> paid(SoInVo inVo) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //入参校验
        if (inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName())) {
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //订单不存在
        SoOut soOut = soService.get(inVo.getId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        //订单不是待支付状态，不能修改支付状态
        if(soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID){
            return new BaseResp<Integer>(ResultStatus.error_so_not_wait_pay);
        }
        inVo.setSkuId(soOut.getSkuId());
        int ret = soService.paid(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * 商家订单支付
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/paidForShop", method = RequestMethod.POST)
    public BaseResp<Integer> paidForShop(SoInVo inVo,HttpServletRequest request) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //入参校验
        if (inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName())) {
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //订单不存在
        So soOut = soService.selectForShop(inVo.getId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        //订单不是待支付状态，不能修改支付状态
        if(soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID){
            return new BaseResp<Integer>(ResultStatus.error_so_not_wait_pay);
        }
        inVo.setUserIp(IpUtils.getIpAddr(request));
        int ret = soService.paidForShop(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * 订单取消
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public BaseResp<Integer> cancel(SoInVo inVo) {
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        inVo.setUserName(user.getUserName());
        //入参校验
        if (inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName())) {
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //订单不存在
        SoOut soOut = soService.get(inVo.getId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        //只有待支付的订单才能取消
        if(soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID){
            return new BaseResp<Integer>(ResultStatus.error_so_cancel_status_error);
        }

        int ret = soService.cancel(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

}
