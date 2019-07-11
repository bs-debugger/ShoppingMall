package com.xq.live.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.ShopCategoryService;
import com.xq.live.service.ShopEnterService;
import com.xq.live.vo.in.ShopCashierInVo;
import com.xq.live.vo.in.ShopEnterAuditInVo;
import com.xq.live.vo.in.ShopEnterInVo;
import com.xq.live.vo.out.ShopEnterOut;
import com.xq.live.web.utils.ShopCategoryUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lipeng on 2018/3/8.
 */
@Service
public class ShopEnterServiceImpl implements ShopEnterService {

    @Autowired
    private ShopEnterMapper shopEnterMapper;

    @Autowired
    private ShopEnterAuditLogMapper shopEnterAuditLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private ShopAllocationMapper shopAllocationMapper;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Long add(ShopEnter shopEnter) {
        int res = shopEnterMapper.insert(shopEnter);
        if(res < 1){
            return null;
        }
        return shopEnter.getId();
    }

    @Override
    public ShopEnterOut searchByUserId(Long userId) {
        List<ShopEnterOut> list = shopEnterMapper.selectByUserId(userId);
        if(list==null||list.size()==0){
            return null;
        }

        return list.get(list.size()-1);//  返回最后一条数据,前面的数据无需做判断
    }

    @Override
    @Transactional
    public Integer addShop(ShopEnter shopEnter) {
        List<ShopEnterOut> list = shopEnterMapper.selectByUserId(shopEnter.getUserId());
        if(list==null||list.size()==0){
            return null;
        }
        ShopEnterOut shopEnterOut = list.get(list.size()-1);//返回最后一条数据,前面的数据无需做判断
        ShopEnter shopEnter1 = new ShopEnter();
        shopEnter1.setId(shopEnterOut.getId());
        shopEnter1.setStatus(ShopEnter.SHOP_ENTER_CAN);
        shopEnterMapper.updateByPrimaryKeySelective(shopEnter1);

        shopEnterOut = shopEnterMapper.selectByPrimaryKey(shopEnterOut.getId());
        if(shopEnterOut!=null&&shopEnterOut.getStatus()!=null&&shopEnterOut.getStatus()==1){
            Shop shop = new Shop();
            Long userId = shopEnterOut.getUserId();
            String address = shopEnterOut.getAddress();
            String businessCate = shopEnterOut.getBusinessCate();
            String city = shopEnterOut.getCity();
            BigDecimal locationX = shopEnterOut.getLocationX();
            BigDecimal locationY = shopEnterOut.getLocationY();
            String mobile = shopEnterOut.getMobile();
            String shopName = shopEnterOut.getShopName();
            String userName = shopEnterOut.getUserName();
            shop.setAddress(address);
            shop.setLocationX(locationX);
            shop.setLocationY(locationY);
            boolean flag = isMobile(mobile);//判断是否是手机号
            if(flag==true){
                shop.setMobile(mobile);
            }else{
                shop.setPhone(mobile);
            }
            shop.setShopName(shopName);
            shop.setUserId(userId);
            shop.setBusinessCate(businessCate);
            shop.setLogoUrl(shopEnterOut.getLogoPic());
            shop.setIndexUrl(shopEnterOut.getDoorPic());
            shop.setShopHours(shopEnterOut.getShopHours());
            shop.setOtherService(shopEnterOut.getOtherService());
            shop.setCity(shopEnterOut.getCity());
            int insert = shopMapper.insert(shop);
            /**
             * 判断插入shop表是否成功，失败返回-2
             */
            if(insert<1){
                throw new RuntimeException("插入shop表失败");
            }
            Long id = null;
            if(shop.getId()!=null) {
                 id = shop.getId();//插入得到的shop_id
            }
            User user = new User();
            user.setId(userId);
            user.setShopId(id);
            user.setUserType(2);

            int i = userMapper.updateUserType(user);
            /**
             * 判断更改用户状态是否成功,失败返回-1
             */
            if(i<1){
                throw new RuntimeException("更新用户状态失败");
            }
            User user1 = userMapper.selectByPrimaryKey(userId);
            //给商家加入管理员权限
            ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
            shopCashierInVo.setCashierId(user1.getId());
            shopCashierInVo.setCashierName(user1.getUserName());
            shopCashierInVo.setShopId(id);
            shopCashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_IS_ADMIN);
            shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);
            shopCashierMapper.insert(shopCashierInVo);

            //给商家配置收款方式，先配置已删除,为平台代收的数据，后期可以根据业务来修改
            ShopAllocation shopAllocation = new ShopAllocation();
            shopAllocation.setShopId(id);
            shopAllocation.setIsDelete(ShopAllocation.SHOP_ALLOCATION_IS_DELETED);
            shopAllocation.setPaymentMethod(ShopAllocation.SHOP_ALLOCATION_DS);
            shopAllocationMapper.insert(shopAllocation);
        }else {
            throw new RuntimeException("用户尚未入驻或审批未通过");//判断用户是否入驻且审批通过
        }
        return 0;
    }

    @Override
    public ShopEnterOut selectByUserIdAndShopName(ShopEnter shopEnter) {
        ShopEnterOut shopEnterOut = shopEnterMapper.selectByUserIdAndShopName(shopEnter);
        return shopEnterOut;
    }

    /**
     * 先不做，按具体需求在来改
     */
    @Override
    public Integer deleteShopEnterForStatus(ShopEnter shopEnter) {
        /*ShopEnterOut out = shopEnterMapper.selectByUserId(shopEnter.getUserId());
        if(out==null){
            return -2;//商家还未入驻
        }
        if(out.getStatus()==2){

        }*/
        return null;
    }

    @Override
    public Shop searchByShopName(String shopName) {
        Shop shop = shopMapper.selectByShopName(shopName);
        return shop;
    }

    @Override
    public PageInfo<ShopEnterOut> showList(ShopEnterInVo shopEnterInVo) {
        // 设置分页参数
        PageHelper.startPage(shopEnterInVo.getPage(), shopEnterInVo.getRows());
        // 根据分页参数查询商家入驻列表
        List<ShopEnterOut> shopEnterOutList = shopEnterMapper.showList(shopEnterInVo);
        // 获取商家类目
        List<ShopCategory> shopCategoryList = shopCategoryService.list();
        // 把商家类目ID转换为类目名
        for (ShopEnterOut shopEnterOut : shopEnterOutList) {
            if (StringUtils.isBlank(shopEnterOut.getShopCate())) {
                continue;
            }
            shopEnterOut.setShopCate(ShopCategoryUtils.findShopCateMemo(shopCategoryList, shopEnterOut.getShopCate()));
        }

        return new PageInfo<>(shopEnterOutList);
    }

    @Override
    @Transactional
    public Boolean operationById(ShopEnterAuditInVo shopEnterAuditInVo) {
        // 查询商家入驻信息
        ShopEnter shopEnter = shopEnterMapper.selectById(shopEnterAuditInVo.getShopEnterId());
        if (shopEnter == null) {
            throw new RuntimeException("商家入驻信息不存在");
        }
        if (shopEnter.getStatus() != ShopEnter.SHOP_ENTER_WAIT) {
            throw new RuntimeException("此商家入驻信息已被审核");
        }

        // 当前时间
        Date nowDate = new Date();

        // 商家入驻审核记录
        ShopEnterAuditLog shopEnterAuditLog = new ShopEnterAuditLog();
        shopEnterAuditLog.setShopEnterId(shopEnterAuditInVo.getShopEnterId());
        shopEnterAuditLog.setCreateTime(nowDate);

        if (shopEnterAuditInVo.getStatus()) {   // 通过入驻
            shopEnter.setStatus(ShopEnter.SHOP_ENTER_CAN);
            shopEnter.setUpdateTime(nowDate);
            shopEnterMapper.updateByPrimaryKeySelective(shopEnter);

            Shop shop = new Shop();
            shop.setUserId(shopEnter.getUserId());
            shop.setShopName(shopEnter.getShopName());
            shop.setCity(shopEnter.getCity());
            shop.setAddress(shopEnter.getAddress());
            shop.setMobile(shopEnter.getMobile());
            shop.setShopCate(shopEnter.getShopCate());
            shop.setBusinessCate(shopEnter.getBusinessCate());
            shop.setSmallLogoUrl(shopEnter.getSmallLogoPic());
            shop.setLocationX(shopEnter.getLocationX());
            shop.setLocationY(shopEnter.getLocationY());
            shop.setLogoUrl(shopEnter.getLogoPic());
            shop.setIndexUrl(shopEnter.getDoorPic());
            shop.setShopHours(shopEnter.getShopHours());
            shop.setOtherService(shopEnter.getOtherService());

            // 插入店铺信息，并返回插入的店铺ID
            int shopId = shopMapper.insertSelective(shop);

            if (shopId < 1) {
                throw new RuntimeException("插入shop表失败");
            }

            DecimalFormat mFormat = new DecimalFormat("00000000"); //确定格式，把1转换为00000001
            String shopCode = mFormat.format(shopId);
            shop.setShopCode(shopCode);
            shopMapper.updateShopCodeByShopId(Long.valueOf(shopId), shopCode);

            User user = new User();
            user.setId(shopEnter.getUserId());
            user.setShopId(Long.valueOf(shopId));
            user.setUserType(User.USER_TYPE_SJ);
            if (userMapper.updateByPrimaryKeySelective(user) < 1) {
                throw new RuntimeException("更新用户状态失败");
            }

            User userInfo = userMapper.selectByPrimaryKey(shopEnter.getUserId());

            // 给商家加入管理员权限
            ShopCashierInVo shopCashier = new ShopCashierInVo();
            shopCashier.setCashierId(userInfo.getId());
            shopCashier.setCashierName(userInfo.getUserName());
            shopCashier.setShopId(Long.valueOf(shopId));
            shopCashier.setIsAdmin(ShopCashier.SHOP_CASHIER_IS_ADMIN);
            shopCashier.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);
            shopCashierMapper.insertSelective(shopCashier);

            // 给当前店铺增加店铺账号
            UserAccount userAccount = new UserAccount();
            userAccount.setAccountAmount(BigDecimal.ZERO);
            userAccount.setIsDeleted(0);
            userAccount.setShopId(Long.valueOf(shopId));
            userAccount.setUserAccountType(1);
            userAccount.setUserId(0L); //当为店铺账号时 userId 赋值为0
            userAccount.setCreateTime(nowDate);
            userAccount.setUpdateTime(nowDate);
            userAccount.setAccountType(UserAccount.ACCOUNT_TYPE_XQ);
            userAccountMapper.insertSelective(userAccount);

            // 绑定当前用户到当前店铺
            userAccountMapper.updateShopIdByUserId(userInfo.getId(), Long.valueOf(shopId));

            // 删除缓存
            redisCache.del("userId_" + userInfo.getId());
            redisCache.del(userInfo.getMobile());
            redisCache.del("login_username_" + userInfo.getMobile());

            // 给商家配置收款方式，先配置已删除,为平台代收的数据，后期可以根据业务来修改
            ShopAllocation shopAllocation = new ShopAllocation();
            shopAllocation.setShopId(Long.valueOf(shopId));
            shopAllocation.setIsDelete(ShopAllocation.SHOP_ALLOCATION_IS_DELETED);
            shopAllocation.setPaymentMethod(ShopAllocation.SHOP_ALLOCATION_DS);
            shopAllocationMapper.insertSelective(shopAllocation);

            shopEnterAuditLog.setStatus(true);
        } else {                                // 驳回入驻
            shopEnter.setStatus(ShopEnter.SHOP_ENTER_NO_CAN);
            shopEnter.setUpdateTime(nowDate);
            shopEnterMapper.updateByPrimaryKeySelective(shopEnter);

            shopEnterAuditLog.setStatus(false);
            shopEnterAuditLog.setMemo(shopEnterAuditInVo.getMemo());
            shopEnterAuditLog.setPictures(shopEnterAuditInVo.getPictures());
            shopEnterAuditLog.setSendMsg(shopEnterAuditInVo.getSendMsg());

            // todo 发送短信通知
            if (shopEnterAuditInVo.getSendMsg()) {

            }
        }
        shopEnterAuditLogMapper.insertSelective(shopEnterAuditLog);

        return true;
    }

    /**
     * 判断是否是手机号
     * @param str
     * @return
     */
    public boolean isMobile(String str){
        String regExp = "^[1][3,4,5,7,8][0-9]{9}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(str);
        return m.find();
    }
}
