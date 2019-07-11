package com.xq.live.service.impl;

import com.vdurmont.emoji.EmojiParser;
import com.xq.live.common.Pager;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.poientity.OrderCouponEntity;
import com.xq.live.service.AccountService;
import com.xq.live.service.OrderCouponService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.CutOutTimeUtils;
import com.xq.live.web.utils.EmojiDealUtil;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商城系统订单票券ServiceImpl
 * Created by lipeng on 2018/9/14.
 */
@Service
public class OrderCouponServiceImpl implements OrderCouponService{
    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Autowired
    private OrderAddressMapper orderAddressMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private DeliveryTemplateMapper deliveryTemplateMapper;

    @Autowired
    private PaidLogMapper paidLogMapper;

    @Autowired
    private OrderCouponSendMapper orderCouponSendMapper;

    @Autowired
    private GoodsSpuMapper goodsSpuMapper;

    @Autowired
    private SalePointMapper salePointMapper;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private ActOrderMapper actOrderMapper;

    @Autowired
    private SalepointTopPicMapper salepointTopPicMapper;

    @Autowired
    private SalePointUserMapper salePointUserMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private ShopPromotionRulesMapper shopPromotionRulesMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderWriteOffMapper orderWriteOffMapper;

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private  ActGoodsSkuMapper actGoodsSkuMapper;


    @Override
    public OrderCouponOut selectById(Long id) {
        OrderCouponOut orderCouponOut = orderCouponMapper.selectDetailByPrimaryKey(id);
        if(orderCouponOut==null){
            return null;
        }
        GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderCouponOut.getGoodsSkuId());
        if(goodsSku==null){
            return null;
        }
        GoodsSpu goodsSpu = goodsSpuMapper.selectByPrimaryKey(goodsSku.getSpuId());
        orderCouponOut.setGoodsSpu(goodsSpu);
        orderCouponOut.setGoodsSku(goodsSku);
        this.setOrderAddressAndExpress(orderCouponOut);//设置地址信息和快递单号
        this.setSalePoint(orderCouponOut);//设置销售点
        return orderCouponOut;
    }

    @Override
    public OrderCouponOut getDetail(OrderCouponInVo inVo) {
        OrderCouponOut orderCouponOut = orderCouponMapper.selectDetailByPrimaryKey(inVo.getId());
        if(orderCouponOut==null){
            return null;
        }
        GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderCouponOut.getGoodsSkuId());
        if(goodsSku==null){
            return null;
        }
        ActOrderOut actOrderOut = actOrderMapper.selectFirstDistributionByOrderId(orderCouponOut.getOrderId());
        if (actOrderOut!=null){
            orderCouponOut.setActOrderOut(actOrderOut);
        }
        DeliveryTemplate deliveryTemplate=deliveryTemplateMapper.selectByPrimaryKey(goodsSku.getDeliveryTemplateId());
        orderCouponOut.setDeliveryTemplate(deliveryTemplate);
        orderCouponOut.setGoodsSku(goodsSku);
        GoodsSpu goodsSpu=goodsSpuMapper.selectByPrimaryKey(goodsSku.getSpuId());
        if(goodsSpu!=null){
            orderCouponOut.setGoodsSpu(goodsSpu);
        }
        this.setOrderAddressAndExpress(orderCouponOut);//设置地址信息和快递单号
        this.setSalePoint(orderCouponOut);//设置销售点
        this.selectShopAndSalePointByGoodsSkuId(orderCouponOut, inVo);//查询商品支持的商家信息和销售点
        this.setShowCode(orderCouponOut);//设置showCode
        this.setGoodsCateGory(orderCouponOut);//设置类目信息
        this.setOrderCouponSendList(orderCouponOut,inVo.getOrderCouponSendType(),inVo.getSendOrReceiveUserId());//设置票券的赠送和收取信息
        return orderCouponOut;
    }




    @Override
    public OrderCoupon getByCouponCode(String couponCode) {
        return orderCouponMapper.getByCouponCode(couponCode);
    }

    @Override
    public OrderCouponOut getDetailByCouponCode(String couponCode) {
        OrderCouponOut orderCouponOut = orderCouponMapper.getDetailByCouponCode(couponCode);
        if(orderCouponOut==null){
            return null;
        }
        GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderCouponOut.getGoodsSkuId());
        if(goodsSku==null){
            return null;
        }
        orderCouponOut.setGoodsSku(goodsSku);
        this.setOrderAddressAndExpress(orderCouponOut);//设置地址信息和快递单号
        this.setSalePoint(orderCouponOut);//设置销售点
        OrderCouponInVo inVo = new OrderCouponInVo();
        inVo.setGoodsSkuId(orderCouponOut.getGoodsSkuId());
        this.selectShopAndSalePointByGoodsSkuId(orderCouponOut, inVo);//查询商品支持的商家信息和销售点
        this.setShowCode(orderCouponOut);//设置showCode
        return orderCouponOut;
    }

    @Override
    public Pager<OrderCouponOut> list(OrderCouponInVo inVo) {
        Pager<OrderCouponOut> result = new Pager<OrderCouponOut>();
        int total = orderCouponMapper.listTotal(inVo);
        if(total > 0){
            List<OrderCouponOut> list = orderCouponMapper.list(inVo);
            for (OrderCouponOut orderCouponOut : list) {
                this.setSalePoint(orderCouponOut);//设置销售点
                this.setShowCode(orderCouponOut);//设置showCode
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(total);
        return result;
    }

    @Override
    public Integer updateCoupon(OrderCoupon orderCoupon) {
        int i = orderCouponMapper.updateByPrimaryKeySelective(orderCoupon);
        return i;
    }

    @Override
    public int paidCouponSendAmount(PaidLog inVo) {
        return paidLogMapper.insert(inVo);
    }

    @Override
    @Transactional
    public Integer sendCoupon(OrderCouponSend inVo) {
        int insert = orderCouponSendMapper.insert(inVo);

        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setCouponCode(inVo.getOrderCouponCode());
        orderCoupon.setOwnId(inVo.getReceiveUserId());
        orderCoupon.setUpdateTime(new Date());
        orderCouponMapper.updateByCouponCodeSelective(orderCoupon);
        return insert;
    }

    @Override
    @Transactional
    public Integer sendVersionCoupon(OrderCouponSend inVo) {
        int insert = orderCouponSendMapper.insert(inVo);

        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setCouponCode(inVo.getOrderCouponCode());
        orderCoupon.setOwnId(inVo.getReceiveUserId());
        orderCoupon.setUpdateTime(new Date());
        orderCoupon.setVersionNo(inVo.getVersionNo());
        orderCoupon.setGetTheStatus(OrderCoupon.ORDER_COUPON_GET_STATUS_NOT);
        int i = orderCouponMapper.updateByVersionCouponCodeSelective(orderCoupon);
        if(insert<1||i<1){
            throw new RuntimeException("领取失败");
        }
        return insert;
    }

    /*
    * 赠送票券
    * */
    @Override
    @Transactional
    public Integer complimentaryTickets(OrderCouponSend inVo) {
        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setCouponCode(inVo.getOrderCouponCode());
        orderCoupon.setUpdateTime(new Date());
        orderCoupon.setGetTheStatus(OrderCoupon.ORDER_COUPON_GET_STATUS_HAVE);
        int i = orderCouponMapper.updateByCouponCodeSelective(orderCoupon);
        if(i<1){
            throw new RuntimeException("赠送失败");
        }
        return i;
    }


    /*
   * 获取票券信息,判断是否退款
   * */
    @Override
    public OrderCoupon getOrderCouponInfo(OrderCouponSend inVo) {
        OrderCoupon orderCoupon = orderCouponMapper.getByCouponCode(inVo.getOrderCouponCode());
        return orderCoupon;
    }

    @Override
    public int addUserAccountToShop(OrderCouponInVo orderCouponInVo) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(orderCouponInVo.getShopId());
        if(shopCashier!=null && shopCashier.getCashierId()!=null) {
            //1. 账户日志
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());
            accountInVo.setOccurAmount(orderCouponInVo.getRealUnitPrice());
            accountService.income(accountInVo, "用户核销，票券单号：" + orderCouponInVo.getCouponCode());
        }
        return 1;
    }

    @Override
    public int addUserAccountToShopV1(OrderCouponInVo orderCouponInVo) {
        ShopCashier shopCashier = shopCashierMapper.adminByShopId(orderCouponInVo.getShopId());
        if(shopCashier!=null && shopCashier.getCashierId()!=null) {

            //1. 账户日志
            UserAccountInVo accountInVo = new UserAccountInVo();
            accountInVo.setUserId(shopCashier.getCashierId());
            accountInVo.setOccurAmount(orderCouponInVo.getRealShopUnitPrice().abs());
            accountInVo.setType(AccountLog.TYPE_SHOP);
            AccountLog accountLogInVo = new AccountLog();
            accountLogInVo.setRemark("用户核销，票券单号：" + orderCouponInVo.getShowCode());
            accountLogInVo.setOrderCouponCode(orderCouponInVo.getCouponCode());
            if(orderCouponInVo.getRealShopUnitPrice().compareTo(BigDecimal.ZERO)<0){
                accountService.updatePayoutV2(accountInVo, accountLogInVo);
            }else{
                accountService.updateIncomeV2(accountInVo, accountLogInVo);
            }
        }
        return 1;
    }

    @Override
    @Transactional
    public int hxCoupon(OrderCouponInVo inVo) {
        OrderCouponOut cp = this.selectById(inVo.getId());

        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setId(inVo.getId());
        orderCoupon.setIsUsed(OrderCoupon.ORDER_COUPON_IS_USED_YES);
        orderCoupon.setChangerId(cp.getOwnId());
        orderCoupon.setUsedTime(new Date());
        orderCoupon.setOwnId(cp.getOwnId());
        orderCoupon.setUpdateTime(new Date());
        orderCoupon.setType(OrderCoupon.TYPE_MDZTQ);//核销券都是门店自提
        orderCoupon.setCouponSalepointId(inVo.getSalepointId());
        int result = orderCouponMapper.updateByPrimaryKeySelective(orderCoupon);

        OrderWriteOff orderWriteOff = new OrderWriteOff();
        orderWriteOff.setOrderId(cp.getOrderId());
        orderWriteOff.setShopId(inVo.getShopId());
        orderWriteOff.setShopName(inVo.getShopName());
        orderWriteOff.setCouponAmount(cp.getCouponAmount());//票券所对应商品的理论售价
        orderWriteOff.setOrderCouponId(cp.getId());
        orderWriteOff.setOrderCouponCode(cp.getCouponCode());
        orderWriteOff.setGoodsSkuId(cp.getGoodsSkuId());
        orderWriteOff.setUserId(cp.getUserId());//票券购买人
        orderWriteOff.setUserName(cp.getUserName());
        orderWriteOff.setChangerId(inVo.getHxUserId());//票券核销人
        orderWriteOff.setChangerName(inVo.getHxUserName());
        orderWriteOff.setSalepointId(inVo.getSalepointId());
        orderWriteOff.setServiceAmount(cp.getServiceAmount());
        orderWriteOff.setShopServiceAmount(cp.getShopServiceAmount());
        orderWriteOff.setUserServiceAmount(cp.getUserServiceAmount());
        orderWriteOff.setRealUnitPrice(cp.getRealUnitPrice());
        orderWriteOff.setRealShopUnitPrice(cp.getRealShopUnitPrice());
        if(orderWriteOff.getShopId()!=null&&orderWriteOff.getShopId()>0&&orderWriteOff.getServiceAmount()!=null){
            orderWriteOff.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_NO_BILL);
        }else {
            orderWriteOff.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_IS_BILL);
        }

        int r = orderWriteOffMapper.insert(orderWriteOff);

        //给商家钱包加入余额
        if(!cp.getShopId().toString().equals(new Long(0).toString())){
            OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
            BeanUtils.copyProperties(cp, orderCouponInVo);
            int re = this.addUserAccountToShop(orderCouponInVo);
        }

        return result;
    }

    @Override
    @Transactional
    public int hxCouponV2(OrderCouponInVo inVo) {
        OrderCouponOut cp = this.selectById(inVo.getId());

        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setId(inVo.getId());
        orderCoupon.setIsUsed(OrderCoupon.ORDER_COUPON_IS_USED_YES);
        orderCoupon.setChangerId(cp.getOwnId());
        orderCoupon.setUsedTime(new Date());
        orderCoupon.setOwnId(cp.getOwnId());
        orderCoupon.setUpdateTime(new Date());
        orderCoupon.setType(OrderCoupon.TYPE_MDZTQ);//核销券都是门店自提
        orderCoupon.setCouponSalepointId(inVo.getSalepointId());
        int result = orderCouponMapper.updateByPrimaryKeySelective(orderCoupon);

        OrderWriteOff orderWriteOff = new OrderWriteOff();
        orderWriteOff.setOrderId(cp.getOrderId());
        orderWriteOff.setShopId(inVo.getShopId());
        orderWriteOff.setShopName(inVo.getShopName());
        orderWriteOff.setCouponAmount(cp.getCouponAmount());//票券所对应商品的理论售价
        orderWriteOff.setOrderCouponId(cp.getId());
        orderWriteOff.setOrderCouponCode(cp.getCouponCode());
        orderWriteOff.setGoodsSkuId(cp.getGoodsSkuId());
        orderWriteOff.setUserId(cp.getUserId());//票券购买人
        orderWriteOff.setUserName(cp.getUserName());
        orderWriteOff.setChangerId(inVo.getHxUserId());//票券核销人
        orderWriteOff.setChangerName(inVo.getHxUserName());
        orderWriteOff.setSalepointId(inVo.getSalepointId());
        orderWriteOff.setServiceAmount(cp.getServiceAmount());
        orderWriteOff.setShopServiceAmount(cp.getShopServiceAmount());
        orderWriteOff.setUserServiceAmount(cp.getUserServiceAmount());
        orderWriteOff.setRealUnitPrice(cp.getRealUnitPrice());
        orderWriteOff.setRealShopUnitPrice(cp.getRealShopUnitPrice());
        orderWriteOff.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_IS_BILL);

        int r = orderWriteOffMapper.insert(orderWriteOff);

        //给商家钱包加入余额
        if(!orderWriteOff.getShopId().toString().equals(new Long(0).toString())){
            OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
            BeanUtils.copyProperties(cp, orderCouponInVo);
            orderCouponInVo.setShopId(orderWriteOff.getShopId());
            int re = this.addUserAccountToShopV1(orderCouponInVo);
        }

        return result;
    }

    @Override
    @Transactional
    public int useCoupon(OrderCouponInVo inVo) {
        OrderCouponOut cp = this.selectById(inVo.getId());
        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setId(inVo.getId());
        orderCoupon.setCouponAddressId(inVo.getCouponAddressId());
        orderCoupon.setIsUsed(OrderCoupon.ORDER_COUPON_IS_USED_YES);
        orderCoupon.setChangerId(inVo.getChangerId());//票券使用人
        orderCoupon.setUsedTime(new Date());
        orderCoupon.setSendTime(inVo.getSendTime());
        orderCoupon.setRemark(inVo.getRemark());
        orderCoupon.setOwnId(inVo.getChangerId());//票券拥有人
        orderCoupon.setUpdateTime(new Date());
        orderCoupon.setType(OrderCoupon.TYPE_LPQ);//兑换券都是平台邮购
        orderCoupon.setExpressState(OrderCoupon.EXPRESSSTATE_WAIT);
        int result = orderCouponMapper.updateByPrimaryKeySelective(orderCoupon);

        OrderWriteOff orderWriteOff = new OrderWriteOff();
        orderWriteOff.setOrderId(cp.getOrderId());
        orderWriteOff.setShopId(inVo.getShopId());
        orderWriteOff.setShopName(inVo.getShopName());
        orderWriteOff.setCouponAmount(cp.getCouponAmount());//票券所对应商品的理论售价
        orderWriteOff.setOrderCouponId(cp.getId());
        orderWriteOff.setOrderCouponCode(cp.getCouponCode());
        orderWriteOff.setOrderAddressId(inVo.getCouponAddressId());
        orderWriteOff.setGoodsSkuId(cp.getGoodsSkuId());
        orderWriteOff.setUserId(cp.getUserId());//票券购买人
        orderWriteOff.setUserName(cp.getUserName());
        orderWriteOff.setSendTime(inVo.getSendTime());
        orderWriteOff.setRemark(inVo.getRemark());
        orderWriteOff.setSendAmount(inVo.getSendAmount());
        orderWriteOff.setServiceAmount(cp.getServiceAmount());
        orderWriteOff.setShopServiceAmount(cp.getShopServiceAmount());
        orderWriteOff.setUserServiceAmount(cp.getUserServiceAmount());
        orderWriteOff.setRealUnitPrice(cp.getRealUnitPrice());
        orderWriteOff.setRealShopUnitPrice(cp.getRealShopUnitPrice());
        if(orderWriteOff.getShopId()!=null&&orderWriteOff.getShopId()>0&&orderWriteOff.getServiceAmount()!=null){
            orderWriteOff.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_NO_BILL);
        }else {
            orderWriteOff.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_IS_BILL);
        }
        int r = orderWriteOffMapper.insert(orderWriteOff);

        //给商家钱包加入余额
        if(!orderWriteOff.getShopId().toString().equals(new Long(0).toString())){
            OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
            BeanUtils.copyProperties(cp, orderCouponInVo);
            orderCouponInVo.setShopId(orderWriteOff.getShopId());
            int re = this.addUserAccountToShopV1(orderCouponInVo);
        }

        return result;
    }

    @Override
    @Transactional
    public int useCouponForRedPacket(OrderCouponInVo inVo) {
        if(StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(inVo.getRemark()))){
            String hexadecimal = EmojiParser.parseToHtmlHexadecimal(inVo.getRemark());
            inVo.setRemark(hexadecimal);
        }
        OrderCouponOut cp = this.selectById(inVo.getId());
        OrderCoupon orderCoupon = new OrderCoupon();
        orderCoupon.setId(inVo.getId());
        orderCoupon.setCouponAddressId(inVo.getCouponAddressId());
        orderCoupon.setIsUsed(OrderCoupon.ORDER_COUPON_IS_USED_YES);
        orderCoupon.setChangerId(inVo.getChangerId());//票券使用人
        orderCoupon.setUsedTime(new Date());
        orderCoupon.setSendTime(inVo.getSendTime());
        orderCoupon.setRemark(inVo.getRemark());
        orderCoupon.setOwnId(inVo.getChangerId());//票券拥有人
        orderCoupon.setUpdateTime(new Date());
        orderCoupon.setType(OrderCoupon.TYPE_RED_PACKET);//红包券
        orderCoupon.setExpressState(OrderCoupon.EXPRESSSTATE_FINISH);
        int result = orderCouponMapper.updateByPrimaryKeySelective(orderCoupon);

        OrderWriteOff orderWriteOff = new OrderWriteOff();
        orderWriteOff.setOrderId(cp.getOrderId());
        orderWriteOff.setShopId(inVo.getShopId());
        orderWriteOff.setShopName(inVo.getShopName());
        orderWriteOff.setCouponAmount(cp.getCouponAmount());//票券所对应商品的理论售价
        orderWriteOff.setOrderCouponId(cp.getId());
        orderWriteOff.setOrderCouponCode(cp.getCouponCode());
        orderWriteOff.setOrderAddressId(inVo.getCouponAddressId());
        orderWriteOff.setGoodsSkuId(cp.getGoodsSkuId());
        orderWriteOff.setUserId(cp.getUserId());//票券购买人
        orderWriteOff.setUserName(cp.getUserName());
        /*orderWriteOff.setChangerId(inVo.getChangerId());//兑换使用,没有核销人
        orderWriteOff.setChangerName(inVo.getChangerName());*/
        orderWriteOff.setSendTime(inVo.getSendTime());
        orderWriteOff.setRemark(inVo.getRemark());
        orderWriteOff.setSendAmount(inVo.getSendAmount());
        orderWriteOff.setServiceAmount(cp.getServiceAmount());
        orderWriteOff.setShopServiceAmount(cp.getShopServiceAmount());
        orderWriteOff.setUserServiceAmount(cp.getUserServiceAmount());
        orderWriteOff.setRealUnitPrice(cp.getRealUnitPrice());
        orderWriteOff.setRealShopUnitPrice(cp.getRealShopUnitPrice());
        orderWriteOff.setIsBill(OrderWriteOff.ORDER_WRITE_OFF_IS_BILL);

        int r = orderWriteOffMapper.insert(orderWriteOff);

        //1. 账户日志
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getChangerId());
        accountInVo.setOccurAmount(cp.getCouponAmount());
        accountInVo.setType(AccountLog.TYPE_USER);
        AccountLog accountLogInVo = new AccountLog();
        accountLogInVo.setRemark("使用红包,票券编号:"+cp.getShowCode());
        accountLogInVo.setOrderCouponCode(cp.getCouponCode());
        accountService.updateIncomeV1(accountInVo, accountLogInVo);

        /*UserAccountInVo userAccountInVo = new UserAccountInVo();
        userAccountInVo.setUserId(inVo.getChangerId());
        userAccountInVo.setOccurAmount(cp.getCouponAmount());
        userAccountInVo.setType(AccountLog.TYPE_USER);
        StringBuilder remark = new StringBuilder();
        remark.append("使用红包,票券编号:"+cp.getCouponCode());
        accountService.updateIncome(userAccountInVo, remark.toString(), null, null);*/

        return result;
    }

    @Override
    public OrderCouponHxOut hxJurisdiction(Long id, Long userId, Long shopId) {
        OrderCouponHxOut orderCouponHxOut = new OrderCouponHxOut();
        //1.验证票券信息和商品信息是否正常
        OrderCouponOut orderCouponOut = orderCouponMapper.selectDetailByPrimaryKey(id);
        if(orderCouponOut==null){
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("查询票券信息异常");
            return orderCouponHxOut;
        }
        GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(orderCouponOut.getGoodsSkuId());
        if(goodsSku==null){
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("查询商品信息异常,请联系管理员");
            return orderCouponHxOut;
        }
        if(goodsSku.getSendType()==GoodsSku.SEND_TYPE_PTYG){
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("该商品只支持平台邮购,不可核销");
            return orderCouponHxOut;
        }
        Date nowDate = new Date();
        if(orderCouponOut.getIsUsed()==OrderCoupon.ORDER_COUPON_IS_USED_YES||
                orderCouponOut.getExpiryDate().before(nowDate)){
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("票券已被使用或者已过期");
            return orderCouponHxOut;
        }

        //退款申请中和已退款的票卷不能使用
        if(orderCouponOut.getStatus()!=null&&(orderCouponOut.getStatus()==OrderCoupon.STATUS_REFUND
                ||orderCouponOut.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION)){
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("退款中的卷不能使用");
            return orderCouponHxOut;
        }

        //判断订单是否存在
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderCouponOut.getOrderId());
        if(orderInfo==null){
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("订单不存在");
            return orderCouponHxOut;
        }
        //2.验证用户是否拥有核销权限
        OrderCouponHxOut re = this.selectHxJurisdiction(orderCouponOut,orderCouponHxOut,userId,shopId);
        return re;
    }

    /**
     * 判断是否拥有核销权限
     * @param orderCouponOut
     * @param userId
     * @param shopId
     * @return  re?null:有核销权限:无核销权限
     */
    public OrderCouponHxOut selectHxJurisdiction(OrderCouponOut orderCouponOut, OrderCouponHxOut orderCouponHxOut,Long userId, Long shopId) {
        SalePointInVo salePointInVo = new SalePointInVo();
        salePointInVo.setGoodsSkuId(orderCouponOut.getGoodsSkuId());
        List<SalePointOut> salePointOuts = salePointMapper.listSalepointByGoodsSkuId(salePointInVo);
        List<SalePointUser> list=salePointUserMapper.selectByUserId(userId);
        User user = userMapper.selectByPrimaryKey(userId);
        if(orderCouponOut.getShopId().equals(0L)){
             if(salePointOuts==null||salePointOuts.size()==0){//意味着是享七券
                 if(shopId==null){//自营店核销员,没有核销享七券的权限
                     orderCouponHxOut.setErrorCode("-1");
                     orderCouponHxOut.setErrorMessage("自营店核销员,没有核销享七券的权限");
                     return orderCouponHxOut;
                 }
                 orderCouponHxOut.setErrorCode("0");
                 orderCouponHxOut.setId(orderCouponOut.getId());
                 orderCouponHxOut.setHxUserId(userId);
                 orderCouponHxOut.setHxUserName(user.getUserName());
                 orderCouponHxOut.setShopId(shopId);
                 this.setShopName(orderCouponHxOut);//设置商家名字
                 return orderCouponHxOut;
             }
            if(shopId!=null){//如果商品是自营商品,能到核销操作的话,则必有核销点
                orderCouponHxOut.setErrorCode("-1");
                orderCouponHxOut.setErrorMessage("商家核销员无法核销自营商品");
                return orderCouponHxOut;
            }
            if(list==null||list.size()==0){
                orderCouponHxOut.setErrorCode("-1");
                orderCouponHxOut.setErrorMessage("用户没有核销该商品的权限");
                return orderCouponHxOut;
            }
            for (SalePointUser salePointUser : list) {
                for (SalePointOut salePointOut : salePointOuts) {
                    if(salePointOut.getId().equals(salePointUser.getSalepointId())){//为那种像螃蟹门店自提的商品
                        orderCouponHxOut.setErrorCode("0");
                        orderCouponHxOut.setId(orderCouponOut.getId());
                        orderCouponHxOut.setHxUserId(userId);
                        orderCouponHxOut.setHxUserName(user.getUserName());
                        orderCouponHxOut.setShopId(0L);
                        this.setShopName(orderCouponHxOut);//设置商家名字
                        orderCouponHxOut.setSalePointId(salePointOut.getId());
                        return orderCouponHxOut;
                    }
                }
            }
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("用户没有核销该商品的权限");
            return orderCouponHxOut;
        }else{
            if(shopId==null){
                orderCouponHxOut.setErrorCode("-1");
                orderCouponHxOut.setErrorMessage("自营用户没有核销商家商品的权限");
                return orderCouponHxOut;
            }
            if(!orderCouponOut.getShopId().equals(shopId)){
                orderCouponHxOut.setErrorCode("-1");
                orderCouponHxOut.setErrorMessage("用户没有核销该商品的权限");
                return orderCouponHxOut;
            }
            if(salePointOuts==null||salePointOuts.size()==0){//该商品为商家没有设置销售点的菜
                orderCouponHxOut.setErrorCode("0");
                orderCouponHxOut.setId(orderCouponOut.getId());
                orderCouponHxOut.setHxUserId(userId);
                orderCouponHxOut.setHxUserName(user.getUserName());
                orderCouponHxOut.setShopId(shopId);
                this.setShopName(orderCouponHxOut);//设置商家名字
                return orderCouponHxOut;
            }else{
                for (SalePointUser salePointUser : list) {
                    for (SalePointOut salePointOut : salePointOuts) {
                        if(salePointOut.getId().equals(salePointUser.getSalepointId())){
                            orderCouponHxOut.setErrorCode("0");
                            orderCouponHxOut.setId(orderCouponOut.getId());
                            orderCouponHxOut.setHxUserId(userId);
                            orderCouponHxOut.setHxUserName(user.getUserName());
                            orderCouponHxOut.setShopId(shopId);
                            this.setShopName(orderCouponHxOut);//设置商家名字
                            orderCouponHxOut.setSalePointId(salePointOut.getId());
                            return orderCouponHxOut;
                        }
                    }
                }
            }
            orderCouponHxOut.setErrorCode("-1");
            orderCouponHxOut.setErrorMessage("用户没有核销该自营商品的权限");
            return orderCouponHxOut;
        }
    }

    public void setShopName(OrderCouponHxOut orderCouponHxOut){
        if(new Long(0).equals(orderCouponHxOut.getShopId())){
            orderCouponHxOut.setShopName("享七自营");
        }else{
            Shop shopById = shopMapper.selectByPrimaryKey(orderCouponHxOut.getShopId());
            orderCouponHxOut.setShopName(shopById.getShopName());
        }
    }

    @Override
    public Pager<OrderCouponOut> listForSend(OrderCouponInVo inVo) {
        Pager<OrderCouponOut> result = new Pager<OrderCouponOut>();
        Integer total = orderCouponMapper.listTotalForSend(inVo);
        if(total!=null && total > 0){
            List<OrderCouponOut> list = orderCouponMapper.listForSend(inVo);
            for (OrderCouponOut orderCouponOut : list) {
                this.setSalePoint(orderCouponOut);//设置销售点
                this.setShowCode(orderCouponOut);//设置showCode
            }
            result.setList(list);
        }
        result.setRows(inVo.getRows());
        result.setPage(inVo.getPage());
        if (total==null){
            result.setTotal(0);
        }else {
            result.setTotal(total);
        }

        return result;
    }

    @Override
    public Pager<OrderCouponOut> listCoupon(OrderCouponInVo inVo) {
        Pager<OrderCouponOut> result = new Pager<OrderCouponOut>();
        int total = orderCouponMapper.listCouponTotal(inVo);
        if(total > 0){
            List<OrderCouponOut> list = orderCouponMapper.listCoupon(inVo);
            for (OrderCouponOut orderCouponOut : list) {
                this.setSalePoint(orderCouponOut);//设置销售点
                this.setShowCode(orderCouponOut);//设置showCode
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(total);
        return result;
    }

    @Override
    public Pager<OrderCouponOut> listCouponForShopUser(OrderCouponInVo inVo) {
        Pager<OrderCouponOut> result = new Pager<OrderCouponOut>();
        int total = orderCouponMapper.listTotalForSend(inVo);
        List<OrderCouponOut> list = new ArrayList<OrderCouponOut>();
        if(total > 0){
            list = orderCouponMapper.listForSend(inVo);
            List<ShopPromotionRulesOut> promotionRulesOuts = shopPromotionRulesMapper.selectByShopId(inVo.getShopId());
            List<Long> listLong = new ArrayList<Long>();
            for (ShopPromotionRulesOut promotionRulesOut : promotionRulesOuts) {
                //判断金额是否比满的金额大，大或者等于的话就加入进去
                int i = inVo.getFinalAmount().compareTo(promotionRulesOut.getManAmount());
                if(i == 0 || i ==1) {
                    listLong.add(promotionRulesOut.getGoodsSkuId());
                }
            }
            //删除不满足商家优惠力度的代金券
            if(promotionRulesOuts!=null&&promotionRulesOuts.size()>0){
                Iterator<OrderCouponOut> sListIterator = list.iterator();
                while (sListIterator.hasNext()) {
                    OrderCouponOut str = sListIterator.next();
                    if (!listLong.contains(str.getGoodsSkuId())) {
                        sListIterator.remove();
                    }
                }
            }
            //删除退款申请和已退款的代金券
            Iterator<OrderCouponOut> sListIterator = list.iterator();
            while (sListIterator.hasNext()) {
                OrderCouponOut str = sListIterator.next();
                if (str.getStatus()==OrderCoupon.STATUS_REFUND||str.getStatus()==OrderCoupon.STATUS_REFUND_APPLICATION) {
                    sListIterator.remove();
                }
            }
            for (OrderCouponOut orderCouponOut : list) {
                this.setSalePoint(orderCouponOut);//设置销售点
                this.setShowCode(orderCouponOut);//设置showCode
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(list.size());
        return result;
    }

    /**
     * 设置销售点
     * @param orderCouponOut
     */
    public void setSalePoint(OrderCouponOut orderCouponOut){
        if(orderCouponOut.getType()!=null&&orderCouponOut.getType()==OrderCoupon.TYPE_MDZTQ){
            if(orderCouponOut.getSalepointId()!=null) {//做兼容处理
                //根据销售点id去查询销售点信息
                SalePoint salePoint = salePointMapper.selectByPrimaryKey(orderCouponOut.getSalepointId());
                orderCouponOut.setSalePoint(salePoint);
            }else{
                //根据销售点id去查询销售点信息
                SalePoint salePoint = salePointMapper.selectByPrimaryKey(orderCouponOut.getCouponSalepointId());
                orderCouponOut.setSalePoint(salePoint);
            }
        }
    }

    /**
     * 设置地址信息和快递单号
     * @param orderCouponOut
     */
    private void setOrderAddressAndExpress(OrderCouponOut orderCouponOut) {
        OrderDelivery orderDelivery = orderDeliveryMapper.selectByOrderCouponCode(orderCouponOut.getCouponCode());//查询快递单号
        OrderAddressOut address = orderAddressMapper.getAddress(orderCouponOut.getCouponAddressId());//查询收货的信息
        if(orderDelivery!=null){
            orderCouponOut.setExpressCode(orderDelivery.getExpressCode());//设置快递单号
        }
        orderCouponOut.setOrderAddressOut(address);//设置收货的信息
    }

    /**
     * 通过GoodsSkuId查询支持的商家信息和销售点列表
     * @param orderCouponOut
     * @param inVo
     */
    private void selectShopAndSalePointByGoodsSkuId(OrderCouponOut orderCouponOut,OrderCouponInVo inVo) {
        SalePointInVo salePointInVo = new SalePointInVo();
        salePointInVo.setGoodsSkuId(orderCouponOut.getGoodsSkuId());
        salePointInVo.setLocationY(inVo.getLocationY());
        salePointInVo.setLocationX(inVo.getLocationX());
        List<SalePointOut> salePointOuts = salePointMapper.listSalepointByGoodsSkuId(salePointInVo);
        for (SalePointOut salePointOut : salePointOuts) {
            if (salePointOut != null) {
                List<SalepointTopPicOut> picOutList = salepointTopPicMapper.selectBySalepointId(salePointOut.getId());
                List<Pair<String, String>> picList = new ArrayList<>();
                if (picOutList != null && picOutList.size() > 0) {
                    for (SalepointTopPicOut picOut : picOutList) {
                        picList.add(new Pair<String, String>(picOut.getAttachment().getSmallPicUrl(), picOut.getAttachment().getPicUrl()));    //小图和大图url
                    }
                }
                salePointOut.setSalepointTopPics(picList);
            }
        }
        orderCouponOut.setSalePointOuts(salePointOuts);
        if(orderCouponOut.getShopId()==0){
            OrderWriteOffOut orderWriteOffOut = orderWriteOffMapper.selectDetailByOrderCouponId(orderCouponOut.getId());
            if(orderWriteOffOut==null) {
                ShopOut shopOut = new ShopOut();
                shopOut.setId(0L);
                shopOut.setShopName("享七自营");
                orderCouponOut.setShopOut(shopOut);
            }else{
                ShopInVo shopInVo = new ShopInVo();
                shopInVo.setId(orderWriteOffOut.getShopId());
                shopInVo.setLocationX(inVo.getLocationX());
                shopInVo.setLocationY(inVo.getLocationY());
                ShopOut shopOut = shopMapper.selectDetailById(shopInVo);
                if(shopOut!=null) {
                    shopOut.setId(orderWriteOffOut.getShopId());
                    shopOut.setShopName(orderWriteOffOut.getShopName());
                    orderCouponOut.setShopOut(shopOut);
                }else{
                    ShopOut shopOut1 = new ShopOut();
                    shopOut1.setId(0L);
                    shopOut1.setShopName("享七自营");
                    orderCouponOut.setShopOut(shopOut);
                }
            }
        }else {
            ShopInVo shopInVo = new ShopInVo();
            shopInVo.setId(orderCouponOut.getShopId());
            shopInVo.setLocationX(inVo.getLocationX());
            shopInVo.setLocationY(inVo.getLocationY());
            ShopOut shopOut = shopMapper.selectDetailById(shopInVo);
            orderCouponOut.setShopOut(shopOut);
        }
    }

    private void setShowCode(OrderCouponOut orderCouponOut) {
        if(StringUtils.isBlank(orderCouponOut.getShowCode())){
            SimpleDateFormat df = new SimpleDateFormat("MMdd");
            String date = df.format(orderCouponOut.getCreateTime());
            String s = CutOutTimeUtils.addZeroForNum(orderCouponOut.getId().toString(), 6);
            String showCode = date+s;
            orderCouponOut.setShowCode(showCode);
        }
    }

    @Override
    public Map<String, Object> orderCouponExport(OrderCouponInVo inVo) {
        List<OrderCouponOut> list=orderCouponMapper.listCouponForExport(inVo);
        OrderCouponOut orderCouponOut= orderCouponMapper.selectTotalForExport(inVo);
        Shop shop= shopMapper.selectByPrimaryKey(inVo.getShopId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> map = new HashMap<String, Object>();
        List<OrderCouponEntity> orderCouponEntityList = new ArrayList<OrderCouponEntity>();
        int i=0;
        for(OrderCouponOut orderCouponOut1:list){
            i++;
            OrderCouponEntity orderCouponEntity=new OrderCouponEntity();
            orderCouponEntity.setIndex(i+"");
            orderCouponEntity.setCouponCode(orderCouponOut1.getCouponCode());
            orderCouponEntity.setFlagTypeName(orderCouponOut1.getFlagTypeName());
            orderCouponEntity.setGoodsSkuName(orderCouponOut1.getGoodsSkuName());
            orderCouponEntity.setRealAmount(orderCouponOut1.getRealAmount());
            orderCouponEntity.setRealUnitPrice(orderCouponOut1.getRealUnitPrice());
            orderCouponEntity.setServiceAmount(orderCouponOut1.getServiceAmount());
            orderCouponEntity.setUsedTime(formatter.format(orderCouponOut1.getUsedTime()));
            orderCouponEntityList.add(orderCouponEntity);
        }

        map.put("list",orderCouponEntityList);
        map.put("shopName",shop.getShopName());
        map.put("beginTime",formatter.format(inVo.getBeginTime()));
        map.put("endTime",formatter.format(inVo.getEndTime()));
        map.put("total",list.size());
        map.put("totalAmount",orderCouponOut==null?0:orderCouponOut.getRealUnitPrice());
        map.put("totalService",orderCouponOut==null?0:orderCouponOut.getServiceAmount());
        map.put("totalRealAmount",orderCouponOut==null?0:orderCouponOut.getRealAmount());
        return map;
    }

    @Override
    public Map<String, Object> orderWriteCouponExport(OrderWriteOffInVo inVo) {
        Map<String,Object> map = new HashMap<String, Object>();
        OrderInfoInVo orderInfoInVo = new OrderInfoInVo();
        orderInfoInVo.setShopId(inVo.getShopId());
        orderInfoInVo.setBeginTime(inVo.getBeginTime());
        orderInfoInVo.setEndTime(inVo.getEndTime());
        //查询商家端商家订单营业额
        OrderInfoOut shopOrderTurnover = orderInfoMapper.queryShopOrderTurnover(orderInfoInVo);
        //查询商家端用户订单营业额和核销数目
        List<OrderWriteOffOut> orderWriteOffOuts = orderWriteOffMapper.selectTotalAmount(inVo);
        if(orderWriteOffOuts!=null&&orderWriteOffOuts.size()>0) {
            OrderWriteOffOut orderWriteOffOut = orderWriteOffOuts.get(0);
            map.put("orderWriteOffTotal", orderWriteOffOut.getTotalItem().toString());//核销票券的数目
            map.put("allRealShopUnitPrice",orderWriteOffOut.getTotalShopPrice().toString());//商家端用户订单总营业额
            map.put("allShopTurnover",orderWriteOffOut.getTotalShopPrice().
                    add(shopOrderTurnover.getAllShopOrderPrice()).toString());//商家总营业额
        }
        OrderCouponInVo orderCouponInVo = new OrderCouponInVo();
        orderCouponInVo.setShopId(inVo.getShopId());
        orderCouponInVo.setBeginTime(inVo.getBeginTime());
        orderCouponInVo.setEndTime(inVo.getEndTime());


        List<OrderCouponOut> list=orderCouponMapper.listCouponForExportV(orderCouponInVo);
        OrderCouponOut orderCouponOut= orderCouponMapper.selectTotalForExportV(orderCouponInVo);
        Shop shop= shopMapper.selectByPrimaryKey(inVo.getShopId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Map<String,Object> map = new HashMap<String, Object>();
        List<OrderCouponEntity> orderCouponEntityList = new ArrayList<OrderCouponEntity>();
        int i=0;
        for(OrderCouponOut orderCouponOut1:list){
            i++;
            OrderCouponEntity orderCouponEntity=new OrderCouponEntity();
            orderCouponEntity.setIndex(i+"");
            //设置showCode
                SimpleDateFormat df = new SimpleDateFormat("MMdd");
                String date = df.format(orderCouponOut1.getCreateTime());
                String s = CutOutTimeUtils.addZeroForNum(orderCouponOut1.getId().toString(), 6);
                String showCode = date+s;
                orderCouponOut1.setCouponCode(showCode);
            orderCouponEntity.setCouponCode(orderCouponOut1.getCouponCode());
            orderCouponEntity.setFlagTypeName(orderCouponOut1.getFlagTypeName());
            orderCouponEntity.setGoodsSkuName(orderCouponOut1.getGoodsSkuName());
            orderCouponEntity.setRealAmount(orderCouponOut1.getRealAmount());
            orderCouponEntity.setUserServiceAmount(orderCouponOut1.getUserServiceAmount());
            orderCouponEntity.setShopServiceAmount(orderCouponOut1.getShopServiceAmount());
            orderCouponEntity.setRealUnitPrice(orderCouponOut1.getRealUnitPrice());
            orderCouponEntity.setServiceAmount(orderCouponOut1.getServiceAmount());

            orderCouponEntity.setUsedTime(formatter.format(orderCouponOut1.getUsedTime()));
            orderCouponEntityList.add(orderCouponEntity);
        }

        map.put("list",orderCouponEntityList);
        map.put("shopName",shop.getShopName());
        map.put("beginTime",formatter.format(inVo.getBeginTime()));
        map.put("endTime",formatter.format(inVo.getEndTime()));
        map.put("total",list.size());
        map.put("totalAmount",orderCouponOut==null?0:orderCouponOut.getRealUnitPrice());
        map.put("totalService",orderCouponOut==null?0:orderCouponOut.getServiceAmount());
        map.put("totalRealAmount",orderCouponOut==null?0:orderCouponOut.getRealAmount());
        return map;
    }

    public void setGoodsCateGory(OrderCouponOut orderCouponOut) {
        if (orderCouponOut.getGoodsSpu() != null) {
            List<GoodsCategory> categoriesById = goodsCategoryMapper.findCategoriesById(orderCouponOut.getGoodsSpu().getCategoryId());
            orderCouponOut.setGoodsCategories(categoriesById);
            orderCouponOut.setIsXiangGou(0);
            for(GoodsCategory goodsCategory: categoriesById){
                if(goodsCategory.getStatus()==0||goodsCategory.getStatus()==3){//是否为享购商品，类目中status为1和3的为享购
                    orderCouponOut.setIsXiangGou(1);
                }
            }
        }
    }

    public void setOrderCouponSendList(OrderCouponOut orderCouponOut,Integer orderCouponSendType,Long sendOrReceiveUserId){
        OrderCouponSend orderCouponSend = new OrderCouponSend();
        if(orderCouponSendType==null||orderCouponSendType==OrderCoupon.ORDER_COUPON_SEND_SEND){
            orderCouponSend.setOrderCouponCode(orderCouponOut.getCouponCode());
            orderCouponSend.setSendUserId(sendOrReceiveUserId);
            List<OrderCouponSendOut> list = orderCouponSendMapper.list(orderCouponSend);
            orderCouponOut.setOrderCouponSendOuts(list);
        }else if(orderCouponSendType==OrderCoupon.ORDER_COUPON_SEND_RECEIVE){
            orderCouponSend.setOrderCouponCode(orderCouponOut.getCouponCode());
            orderCouponSend.setReceiveUserId(sendOrReceiveUserId);
            List<OrderCouponSendOut> list = orderCouponSendMapper.list(orderCouponSend);
            orderCouponOut.setOrderCouponSendOuts(list);
        }
    }

    @Override
    public int girlsDay(OrderCouponOut orderCouponOut)  {
        if(orderCouponOut==null||orderCouponOut.getUserId()==null){
            return 0;
        }
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date7Str="2019-03-07";
            String date8Str="2019-03-08";
            Date now=new Date();
            String nowStr=dateFormat.format(now);

            Integer wandaDay=17;//每月17号为万达邀吃日
            Calendar nowTime = Calendar.getInstance();
            Integer nowDay= nowTime.get(Calendar.DAY_OF_MONTH);

            if(nowDay.equals(wandaDay)){
                OrderWriteOffInVo orderWriteOffInVo=new OrderWriteOffInVo();
                orderWriteOffInVo.setUserId(orderCouponOut.getUserId());
                orderWriteOffInVo.setCreateTime(now);
                List<OrderWriteOff> orderWriteOffList=orderWriteOffMapper.selectDailyByUserId(orderWriteOffInVo);
                if(orderWriteOffList!=null&&orderWriteOffList.size()==1&&orderWriteOffList.get(0).getOrderCouponCode().equals(orderCouponOut.getCouponCode())){

                    //1. 账户日志
                    UserAccountInVo accountInVo = new UserAccountInVo();
                    accountInVo.setUserId(orderCouponOut.getUserId());
                    accountInVo.setOccurAmount(new BigDecimal(5));
                    accountInVo.setType(AccountLog.TYPE_USER);
                    AccountLog accountLogInVo = new AccountLog();
                    accountLogInVo.setRemark("邀吃日奖励金" );
                    accountLogInVo.setOrderCouponCode(orderCouponOut.getCouponCode());
                    accountService.updateIncomeV1(accountInVo, accountLogInVo);

                    return 1;
                }
            }

            if(date7Str.equals(nowStr)){
                Date date7=dateFormat.parse(date7Str);
                OrderWriteOffInVo orderWriteOffInVo=new OrderWriteOffInVo();
                orderWriteOffInVo.setUserId(orderCouponOut.getUserId());
                orderWriteOffInVo.setCreateTime(date7);
                List<OrderWriteOff> orderWriteOffList=orderWriteOffMapper.selectDailyByUserId(orderWriteOffInVo);
                if(orderWriteOffList!=null&&orderWriteOffList.size()==1&&orderWriteOffList.get(0).getOrderCouponCode().equals(orderCouponOut.getCouponCode())){

                    //1. 账户日志
                    UserAccountInVo accountInVo = new UserAccountInVo();
                    accountInVo.setUserId(orderCouponOut.getUserId());
                    accountInVo.setOccurAmount(new BigDecimal(3.7));
                    accountInVo.setType(AccountLog.TYPE_USER);
                    AccountLog accountLogInVo = new AccountLog();
                    accountLogInVo.setRemark("女神节奖励金" );
                    accountLogInVo.setOrderCouponCode(orderCouponOut.getCouponCode());
                    accountService.updateIncomeV1(accountInVo, accountLogInVo);

                    return 1;
                }

            }else if (date8Str.equals(nowStr)){
                Date date8=dateFormat.parse(date8Str);
                OrderWriteOffInVo orderWriteOffInVo=new OrderWriteOffInVo();
                orderWriteOffInVo.setCreateTime(date8);
                orderWriteOffInVo.setUserId(orderCouponOut.getUserId());
                List<OrderWriteOff> orderWriteOffList=orderWriteOffMapper.selectDailyByUserId(orderWriteOffInVo);
                if(orderWriteOffList!=null&&orderWriteOffList.size()==1&&orderWriteOffList.get(0).getOrderCouponCode().equals(orderCouponOut.getCouponCode())){
                    //1. 账户日志
                    UserAccountInVo accountInVo = new UserAccountInVo();
                    accountInVo.setUserId(orderCouponOut.getUserId());
                    accountInVo.setOccurAmount(new BigDecimal(3.8));
                    accountInVo.setType(AccountLog.TYPE_USER);
                    AccountLog accountLogInVo = new AccountLog();
                    accountLogInVo.setRemark("女神节奖励金" );
                    accountLogInVo.setOrderCouponCode(orderCouponOut.getCouponCode());
                    accountService.updateIncomeV1(accountInVo, accountLogInVo);

                    return 1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String girlsDayFirstWriteOff(OrderCouponOut orderCouponOut) {
        String result=null;
        if(orderCouponOut==null||orderCouponOut.getUserId()==null){
            return result;
        }

        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date7Str="2019-03-07";
            String date8Str="2019-03-08";
            Date now=new Date();
            String nowStr=dateFormat.format(now);

            Integer wandaDay=17;//每月17号为万达邀吃日
            Calendar nowTime = Calendar.getInstance();
            Integer nowDay= nowTime.get(Calendar.DAY_OF_MONTH);

            if(nowDay.equals(wandaDay)){
                OrderWriteOffInVo orderWriteOffInVo=new OrderWriteOffInVo();
                orderWriteOffInVo.setUserId(orderCouponOut.getUserId());
                orderWriteOffInVo.setCreateTime(now);
                List<OrderWriteOff> orderWriteOffList=orderWriteOffMapper.selectDailyByUserId(orderWriteOffInVo);
                if(orderWriteOffList!=null&&orderWriteOffList.size()==1&&orderWriteOffList.get(0).getOrderCouponCode().equals(orderCouponOut.getCouponCode())){
                    result="邀吃日奖励金5元已存入钱包,请查看";
                    return result;
                }
            }

            if(date7Str.equals(nowStr)){
                Date date7=dateFormat.parse(date7Str);
                OrderWriteOffInVo orderWriteOffInVo=new OrderWriteOffInVo();
                orderWriteOffInVo.setUserId(orderCouponOut.getUserId());
                orderWriteOffInVo.setCreateTime(date7);
                List<OrderWriteOff> orderWriteOffList=orderWriteOffMapper.selectDailyByUserId(orderWriteOffInVo);
                if(orderWriteOffList!=null&&orderWriteOffList.size()==1&&orderWriteOffList.get(0).getOrderCouponCode().equals(orderCouponOut.getCouponCode())){
                    result="女神节奖励金3.7元已存入钱包,请查看";
                    return result;
                }

            }else if (date8Str.equals(nowStr)){
                Date date8=dateFormat.parse(date8Str);
                OrderWriteOffInVo orderWriteOffInVo=new OrderWriteOffInVo();
                orderWriteOffInVo.setCreateTime(date8);
                orderWriteOffInVo.setUserId(orderCouponOut.getUserId());
                List<OrderWriteOff> orderWriteOffList=orderWriteOffMapper.selectDailyByUserId(orderWriteOffInVo);
                if(orderWriteOffList!=null&&orderWriteOffList.size()==1&&orderWriteOffList.get(0).getOrderCouponCode().equals(orderCouponOut.getCouponCode())){
                    result="女神节奖励金3.8元已存入钱包,请查看";
                    return result;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Pager<OrderCouponOut> listForVip(OrderCouponInVo inVo) {
        Pager<OrderCouponOut> result = new Pager<OrderCouponOut>();
        int total = orderCouponMapper.listTotalForVip(inVo);
        if(total > 0){
            List<OrderCouponOut> list = orderCouponMapper.listForVip(inVo);
            for (OrderCouponOut orderCouponOut : list) {
                if(orderCouponOut.getActId()!=null){
                    ActGoodsSkuInVo actGoodsSkuInVo=new ActGoodsSkuInVo();
                    actGoodsSkuInVo.setActId(orderCouponOut.getActId());
                    actGoodsSkuInVo.setSkuId(orderCouponOut.getGoodsSkuId());
                    ActGoodsSkuOut actGoodsSkuOut=actGoodsSkuMapper.selectByActId(actGoodsSkuInVo);
                    orderCouponOut.setActGoodsSkuOut(actGoodsSkuOut);
                }
                if(orderCouponOut.getShopId()!=null&&orderCouponOut.getShopId()!=0){
                    ShopInVo shopInVo=new ShopInVo();
                    shopInVo.setId(orderCouponOut.getShopId());
                    shopInVo.setLocationY(inVo.getLocationY());
                    shopInVo.setLocationX(inVo.getLocationX());
                    ShopOut shopOut= shopMapper.selectDetailById(shopInVo);
                    orderCouponOut.setShopOut(shopOut);
                }
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(total);
        return result;
    }
}
