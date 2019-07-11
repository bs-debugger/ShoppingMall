package com.xq.live.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.gexin.rp.sdk.base.uitls.MD5Util;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.AccountService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.AccountLogOut;
import com.xq.live.vo.out.SmsOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * com.xq.live.service.impl
 * 账户操作service
 * @author zhangpeng32
 * Created on 2018/5/5 下午4:29
 * @Description:
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private AccountLogMapper accountLogMapper;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsSendMapper smsSendMapper;

    @Override
    @Transactional
    public Integer update(UserAccountInVo userAccount) {
        UserAccount adminAccount = checkIsAdminAndReturnShopAccount(userAccount.getUserId());
        if(adminAccount!=null){//如果是管理员账户  需要修改门店的绑定账号信息
            userAccountMapper.updateBankDetailByShopId(adminAccount.getShopId(),userAccount.getAccountName(),
                    userAccount.getCardBankname(),userAccount.getAccountCardholder());
        }
        return userAccountMapper.updateByUserID(userAccount);
    }

    @Override
    @Transactional
    public Integer income(UserAccountInVo inVo, String remark) {
        //查询用户账户信息
        UserAccount checkAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        //如果账户不存在，则新增一个(主要是历史数据没创建账户信息)
        if(checkAccount == null){
            User user = userMapper.selectByPrimaryKey(inVo.getUserId());
            if(user != null){
                this.createAccountByUser(user);
            }else{
                return 0;
            }
        }

        UserAccount adminAccount = checkIsAdminAndReturnShopAccount(inVo.getUserId());
        UserAccount userAccount = fillUserAccount(adminAccount,inVo.getUserId());

        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        inVo.setShopId(userAccount.getShopId());
        //更新账户余额
        Integer ret = userAccountMapper.incomeForAdmin(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLog(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_INCOME, remark);
        }
        return ret;
    }

    @Override
    @Transactional
    public Integer payout(UserAccountInVo inVo, String remark) {
        //查询用户账户信息
        UserAccount adminAccount = checkIsAdminAndReturnShopAccount(inVo.getUserId());
        UserAccount userAccount = fillUserAccount(adminAccount,inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        inVo.setShopId(userAccount.getShopId());
        //更新商家账户余额
        Integer ret = userAccountMapper.payoutForAdmin(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLog(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_PAYOUT, remark);
        }
        return ret;
    }

    /**
     * 账户金币收入
     * @param inVo
     * @param remark  备注
     * @return
     */
    @Transactional
    @Override
    public Integer incomegold(UserAccountInVo inVo, String remark) {
        UserAccount userAccount = userAccountMapper.findAccountByUserId(inVo.getUserId());
        if (userAccount!=null) {
            //修改用户信息和获取日志
            AccountLog accountLog = inCustom(userAccount, remark, inVo.getOccurGold());
            Integer dogold = accountLogMapper.insert(accountLog);
            if (dogold<1){
                throw new RuntimeException("用户金币日志添加信息失败");
            }else {
                return dogold;
            }
        }
        return null;
    }

    /**
     * 账户金币支出
     * @param inVo
     * @return
     */
    @Transactional
    @Override
    public Integer payoutgold(UserAccountInVo inVo,Integer gold, String remark) {
        UserAccount userAccount = userAccountMapper.findAccountByUserId(inVo.getUserId());
        if (userAccount!=null) {
            //修改用户信息和获取日志
            AccountLog accountLog = outCustom(userAccount, remark, gold);
            Integer dogold = accountLogMapper.insert(accountLog);
            if (dogold<1){
                throw new RuntimeException("用户金币日志添加信息失败");
            }
        }
        return null;
    }

    /*
    * 用户收入金币方法
    *
    * */
    public AccountLog inCustom(UserAccount userAccount,String remark,Integer gold)  throws RuntimeException{
        AccountLog accountLog = new AccountLog();
        UserAccount account = new UserAccount();

        accountLog.setAccountId(userAccount.getId());
        accountLog.setUserName(userAccount.getUserName());
        accountLog.setPreGold(userAccount.getGold());//操作前金币

        accountLog.setOperateType(AccountLog.OPERATE_TYPE_INCOME);//加钱
        accountLog.setAfterGold(userAccount.getGold() + gold);//操作后金币


        accountLog.setOperateGold(gold);//操作金币
        accountLog.setRemark(remark);//备注
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setAccountName(userAccount.getAccountName());

        accountLog.setType(AccountLog.TYPE_GOLD);

        //更改用户金币
        account.setUserId(userAccount.getUserId());
        account.setVersionNo(userAccount.getVersionNo());
        account.setGold(userAccount.getGold() + gold);//更改用户金币

        Integer i=userAccountMapper.goldByUserId(account);
        if (i < 1) {
            throw new RuntimeException("账户金币修改失败!");
        }
        return accountLog;
    }

    /*
    * 用户支出金币方法
    *
    * */
    public AccountLog outCustom(UserAccount userAccount,String remark,Integer gold)  throws RuntimeException{
        AccountLog accountLog = new AccountLog();
        UserAccount account = new UserAccount();

        accountLog.setAccountId(userAccount.getId());
        accountLog.setUserName(userAccount.getUserName());
        if (userAccount.getGold() == null){
            userAccount.setGold(0);
        }
        accountLog.setPreGold(userAccount.getGold());//操作前金币
        accountLog.setOperateType(AccountLog.OPERATE_TYPE_PAYOUT);//支出
        accountLog.setAfterGold(userAccount.getGold() - gold);//操作后金币


        accountLog.setOperateGold(gold);//操作金币
        accountLog.setRemark(remark);//备注
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setAccountName(userAccount.getAccountName());

        //更改用户金币
        account.setUserId(userAccount.getUserId());
        account.setVersionNo(userAccount.getVersionNo());
        account.setGold(userAccount.getGold() - gold);//更改用户金币

        accountLog.setType(AccountLog.TYPE_GOLD);

        Integer i=userAccountMapper.goldByUserId(account);
        if (i < 1) {
            throw new RuntimeException("账户金币修改失败!");
        }
        return accountLog;
    }

    @Override
    public UserAccount findAccountByUserId(Long userId) {
        UserAccount adminAccount = checkIsAdminAndReturnShopAccount(userId);
        UserAccount userAccount = userAccountMapper.findAccountByUserId(userId);
        //如果当前用户为管理员账户 需要将店铺信息进行冗余  以适应前期功能
        if(adminAccount!=null){
            userAccount.setAccountName(adminAccount.getAccountName());
            userAccount.setAccountIdentityCard(adminAccount.getAccountIdentityCard());
            userAccount.setAccountCardholder(adminAccount.getAccountCardholder());
            userAccount.setAccountAmount(adminAccount.getAccountAmount());
            userAccount.setAccountType(adminAccount.getAccountType());
            userAccount.setAccountStatus(adminAccount.getAccountStatus());
            userAccount.setCardBankname(adminAccount.getCardBankname());
            userAccount.setSecurityCode(adminAccount.getSecurityCode());
        }
        return userAccount;
    }

    @Override
    public Pager<AccountLogOut> findAccountLogs(AccountLogInVo inVo) {
        Pager<AccountLogOut> result = new Pager<AccountLogOut>();
        int listTotal = accountLogMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<AccountLogOut> list = accountLogMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public BigDecimal balance(Long userId) {
        BigDecimal res = BigDecimal.ZERO;
        //校验当前用户是不是管理员
        UserAccount adminAccount = checkIsAdminAndReturnShopAccount(userId);
        if(adminAccount!=null){
            if(adminAccount.getAccountAmount()!=null){
                res = adminAccount.getAccountAmount();
            }
        }else{
            //查询用户账户信息
            UserAccount userAccount =  userAccountMapper.findAccountByUserId(userId);
            if(userAccount != null && userAccount.getAccountAmount() != null){
                res = userAccount.getAccountAmount();
            }
        }

        return res.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Integer findGoldByUserId(Long userId) {
        UserAccount userAccount= userAccountMapper.findGoldByUserId(userId);
        if (userAccount==null){
            return  null;
        }
        return userAccount.getGold();
    }

    /**
     * 根据shopid和需要改变的两个人的userid修改两个人的信息
     * type=1是管理员和核销员互换，type=2是管理员变普通用户，核销员变管理员，type=3是管理员变普通用户，普通用户边管理员
     * @param userId
     * @param thUserId
     * @param type
     * userId是之前管理员的userid，thUserId是核销员或着用户的userid
     * @return
     */
    @Override
    @Transactional
    public Integer changeUserByshop(Long userId, Long thUserId, Integer type) {
        UserAccount getadmin=userAccountMapper.findAccountByUserId(userId);
        UserAccount getuser=userAccountMapper.findAccountByUserId(thUserId);
        Integer ref=this.changetAccount(getadmin, getuser, type);
        return ref;
    }


    public UserAccount create(UserAccount account){
        int ret = userAccountMapper.insert(account);
        if(ret > 0){
            return account;
        }
        return null;
    }

    /**
     * 账户操作日志
     * @param userAccount
     * @param occurAmount
     * @param operateType
     * @return
     */
    private Integer addAccountLog(UserAccount userAccount, BigDecimal occurAmount, int operateType, String remark){
        //校验当前用户是不是管理员
        UserAccount adminAccount = checkIsAdminAndReturnShopAccount(userAccount.getUserId());

        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setUserName(userAccount.getUserName());
        if(adminAccount!=null){
            accountLog.setAccountId(adminAccount.getId());
        }else{
            accountLog.setAccountId(userAccount.getId());
        }
        accountLog.setAccountName(userAccount.getAccountName());
        accountLog.setPreAmount(userAccount.getAccountAmount());
        accountLog.setOperateType(operateType);
        accountLog.setRemark(remark);
        BigDecimal aferAmount = BigDecimal.ZERO;
        if(operateType == AccountLog.OPERATE_TYPE_INCOME){  //收入
            aferAmount = userAccount.getAccountAmount().add(occurAmount);
        }else {
            aferAmount = userAccount.getAccountAmount().subtract(occurAmount);  //支出
        }
        accountLog.setAfterAmount(aferAmount);
        accountLog.setOperateAmount(occurAmount);
        accountLog.setType(AccountLog.TYPE_SHOP);
        Integer ret = accountLogMapper.insert(accountLog);
        return ret;
    }

    /**
     * 根据user信息创建用户账户
     * @param user
     * @return
     */
    private UserAccount createAccountByUser(User user){
        UserAccount account = new UserAccount();
        account.setUserId(user.getId());
        account.setUserName(user.getUserName());
        account.setAccountName(user.getUserName());
        account.setAccountType(UserAccount.ACCOUNT_TYPE_XQ);
        account.setAccountAmount(BigDecimal.ZERO);
        account.setVersionNo(0);
        account.setGold(0);
        return this.create(account);
    }

    /**
     * 根据type修改用户信息
     * type=1是管理员和核销员互换，type=2是管理员变普通用户，核销员变管理员，type=3是管理员变普通用户，普通用户变管理员
     * @param user
     * @return
     */
    private Integer changetAccount(UserAccount admin,UserAccount user ,Integer type){
        Integer status=null;
        if (type==1){
            status=this.exchange(admin, user);
        }else  if (type==2){
            status=this.identity(admin, user);
        }else {
            status=this.reversal(admin, user);
        }
        return status;
    }

    /**
     * 管理员和核销员互换
     * @param admin
     * @param user
     * @return
     */
    private Integer exchange(UserAccount admin,UserAccount user){

          /*修改核销员的账户余额*/
//        AccountLog userLog = custom(user,admin.getAccountAmount(),1);
//        Integer usLog = accountLogMapper.insert(userLog);
//        if (usLog<1){
//            throw new RuntimeException("账户日志添加失败!");
//        }

        /*修改管理员的账户余额*/
//        AccountLog adminLog = custom(admin, admin.getAccountAmount(), 2);
//        Integer adLog=accountLogMapper.insert(adminLog);
//        if (adLog<1){
//            throw new RuntimeException("账户日志添加失败!");
//        }

        User useradmin=userMapper.selectByPrimaryKey(admin.getUserId());
        User usercahier=userMapper.selectByPrimaryKey(user.getUserId());

        if (useradmin.getShopId()==null){
            throw new RuntimeException("第一个用户不是商家!");
        }

        /*判断表中是否有配置*/
        ShopCashier adminCashier=getFlag(useradmin,1);
        if (adminCashier==null){
            if (getFlag(useradmin,2)!=null){
                ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
                shopCashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_IS_ADMIN);
                shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_IS_DELETED);//将删除状态变为1
                Integer updateUser= shopCashierMapper.updateForShop(shopCashierInVo);
                if (updateUser<1){
                    throw new RuntimeException("修改管理员失败!");
                }
            }else {
                ShopCashierInVo cashierInVo=new ShopCashierInVo();
                cashierInVo.setShopId(useradmin.getShopId());
                cashierInVo.setCashierId(useradmin.getId());
                cashierInVo.setCashierName(useradmin.getUserName());
                cashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_IS_ADMIN);
                Integer careateUser= createCashier(cashierInVo);
                if (careateUser<1){
                    throw new RuntimeException("创建管理员失败!");
                }
            }

        }

        usercahier.setShopId(useradmin.getShopId());
        ShopCashier userCashier=getFlag(usercahier,1);
        if (userCashier==null){
            if (getHave(usercahier,1)!=null){
                throw new RuntimeException("此用户已经是其他商家的店员!");
            }
            if (getFlag(usercahier,2)!=null){
                ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
                shopCashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_NO_ADMIN);
                shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);//将删除状态变为0
                Integer updateUser= shopCashierMapper.updateForShop(shopCashierInVo);
                if (updateUser<1){
                    throw new RuntimeException("修改核销员失败!");
                }
            }else{
                ShopCashierInVo cashierInVo=new ShopCashierInVo();
                cashierInVo.setShopId(useradmin.getShopId());
                cashierInVo.setCashierId(usercahier.getId());
                cashierInVo.setCashierName(usercahier.getUserName());
                cashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_NO_ADMIN);
                cashierInVo.setParentId(useradmin.getId());
                Integer careateUser= createCashier(cashierInVo);
                if (careateUser<1){
                    throw new RuntimeException("创建核销员失败!");
                }
            }
        }

        /*将该商家所有人设置成核销员*/
        ShopCashierInVo shopadminInVo = new ShopCashierInVo();
        shopadminInVo.setShopId(useradmin.getShopId());
        shopadminInVo.setParentId(user.getUserId());
        Integer allByshop=changeCashier(shopadminInVo);
        if (allByshop<1){
            throw new RuntimeException("修改用户权限失败!");
        }

        /*将核销员设置成管理员*/
        ShopCashierInVo shopuserInVo = new ShopCashierInVo();
        shopuserInVo.setCashierId(user.getUserId());
        shopuserInVo.setShopId(usercahier.getShopId());
        Integer adminByuser=changeAdmin(shopuserInVo);
        if (adminByuser<1){
            throw new RuntimeException("修改用户权限失败!");
        }
        return  1;
    }

    /**
     * 管理员变普通用户，核销员变管理员
     * @param admin
     * @param user
     * @return
     */
    private Integer identity(UserAccount admin,UserAccount user){
        /*修改核销员的账户余额*/
//        AccountLog userLog = custom(user,admin.getAccountAmount(),1);
//        Integer usLog = accountLogMapper.insert(userLog);
//        if (usLog<1){
//            throw new RuntimeException("账户日志添加失败!");
//        }

        /*修改管理员的账户余额*/
//        AccountLog adminLog = custom(admin, admin.getAccountAmount(), 2);
//        Integer adLog=accountLogMapper.insert(adminLog);
//        if (adLog<1){
//            throw new RuntimeException("账户日志添加失败!");
//        }

        User useradmin=userMapper.selectByPrimaryKey(admin.getUserId());
        User usercahier=userMapper.selectByPrimaryKey(user.getUserId());

        if (useradmin.getShopId()==null){
            throw new RuntimeException("第一个用户不是商家!");
        }
        if (usercahier.getShopId()==null){
            throw new RuntimeException("第二个用户不是商家!");
        }
        ShopCashier adminCashier=getFlag(useradmin, 1);
        if (adminCashier==null){
            ShopCashierInVo cashierInVo=new ShopCashierInVo();
            cashierInVo.setShopId(useradmin.getShopId());
            cashierInVo.setCashierId(useradmin.getId());
            cashierInVo.setCashierName(useradmin.getUserName());
            cashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_IS_ADMIN);
            Integer careateUser= createCashier(cashierInVo);
            if (careateUser<1){
                throw new RuntimeException("创建管理员失败!");
            }
        }

        usercahier.setShopId(useradmin.getShopId());
        ShopCashier userCashier=getFlag(usercahier, 1);
        if (userCashier==null){
            if (getHave(usercahier,1)!=null){
                throw new RuntimeException("此用户已经是其他商家的店员!");
            }
            if (getFlag(usercahier,2)!=null){
                ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
                shopCashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_NO_ADMIN);
                shopCashierInVo.setShopId(useradmin.getShopId());
                shopCashierInVo.setCashierId(usercahier.getId());
                shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);//将删除状态变为0
                Integer updateUser= shopCashierMapper.updateForShop(shopCashierInVo);
                if (updateUser<1){
                    throw new RuntimeException("修改核销员失败!");
                }
            }else{
                ShopCashierInVo cashierInVo=new ShopCashierInVo();
                cashierInVo.setShopId(useradmin.getShopId());
                cashierInVo.setCashierId(usercahier.getId());
                cashierInVo.setCashierName(usercahier.getUserName());
                cashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_NO_ADMIN);
                cashierInVo.setParentId(useradmin.getId());
                Integer careateUser= createCashier(cashierInVo);
                if (careateUser<1){
                    throw new RuntimeException("创建核销员失败!");
                }
            }
        }

        /*将商家的核销员或者管理员修改成用户*/
        User getPt=new User();
        getPt.setUserName(useradmin.getUserName());
        getPt.setId(useradmin.getId());
        getPt.setUserType(User.USER_TYPE_PT);
        Integer changeUser=changeUser(getPt);
        if (changeUser<1){
            throw new RuntimeException("用户修改失败!");
        }else {
            String key = "userId_"+useradmin.getId();
            redisCache.del(key);
        }

        /*将商家的核销员或者管理员删除*/
        ShopCashierInVo delInVo=new ShopCashierInVo();
        delInVo.setShopId(useradmin.getShopId());
        delInVo.setCashierId(useradmin.getId());
        Integer delcash=deleteCashier(delInVo);
        if (delcash<1){
            throw new RuntimeException("删除店员失败!");
        }

        /*将该商家所有人设置成核销员*/
        ShopCashierInVo shopadminInVo = new ShopCashierInVo();
        shopadminInVo.setShopId(useradmin.getShopId());
        shopadminInVo.setParentId(user.getUserId());
        Integer allByshop=changeCashier(shopadminInVo);
        if (allByshop<1){
            throw new RuntimeException("修改用户权限失败!");
        }

        /*将核销员设置成管理员*/
        ShopCashierInVo shopuserInVo = new ShopCashierInVo();
        shopuserInVo.setCashierId(user.getUserId());
        shopuserInVo.setShopId(useradmin.getShopId());
        Integer adminByuser=changeAdmin(shopuserInVo);
        if (adminByuser<1){
            throw new RuntimeException("修改用户权限失败!");
        }
        return 1;
    }

    /**
     * 管理员变普通用户，普通用户变管理员
     * @param admin
     * @param user
     * @return
     */
    private Integer reversal(UserAccount admin,UserAccount user){
         /*修改普通用户的账户余额*/
//        AccountLog userLog = custom(user,admin.getAccountAmount(),1);
//        Integer usLog = accountLogMapper.insert(userLog);
//        if (usLog<1){
//            throw new RuntimeException("账户日志添加失败!");
//        }

        /*修改管理员的账户余额*/
//        AccountLog adminLog = custom(admin, admin.getAccountAmount(), 2);
//        Integer adLog=accountLogMapper.insert(adminLog);
//        if (adLog<1){
//            throw new RuntimeException("账户日志添加失败!");
//        }

        User useradmin=userMapper.selectByPrimaryKey(admin.getUserId());
        User usercahier=userMapper.selectByPrimaryKey(user.getUserId());

        if (useradmin.getShopId()==null){
            throw new RuntimeException("第一个用户不是商家!");
        }

        ShopCashier adminCashier=getFlag(useradmin,1);
        if (adminCashier!=null){
            /*将商家的核销员或者管理员删除*/
            ShopCashierInVo delInVo=new ShopCashierInVo();
            delInVo.setShopId(useradmin.getShopId());
            delInVo.setCashierId(useradmin.getId());
            Integer delcash=deleteCashier(delInVo);
            if (delcash<1){
                throw new RuntimeException("删除店员失败!");
            }
        }else {
            if (getHave(useradmin,1)!=null){
                throw new RuntimeException("此用户已经是其他商家的店员!");
            }
            ShopCashierInVo cashierInVo=new ShopCashierInVo();
            cashierInVo.setShopId(useradmin.getShopId());
            cashierInVo.setCashierId(useradmin.getId());
            cashierInVo.setCashierName(useradmin.getUserName());
            cashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_NO_ADMIN);
            cashierInVo.setParentId(usercahier.getId());
            Integer careateUser= createCashier(cashierInVo);
            if (careateUser<1){
                throw new RuntimeException("创建核销员失败!");
            }
        }

        usercahier.setShopId(useradmin.getShopId());
        ShopCashier userCashier=getFlag(usercahier,1);
        if (userCashier==null){
            if (getHave(usercahier,1)!=null){
                throw new RuntimeException("此用户已经是其他商家的店员!");
            }
            if (getFlag(usercahier,2)!=null){
                ShopCashierInVo shopCashierInVo = new ShopCashierInVo();
                shopCashierInVo.setShopId(useradmin.getShopId());
                shopCashierInVo.setCashierId(usercahier.getId());
                shopCashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_NO_ADMIN);
                shopCashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);//将删除状态变为0
                Integer updateUser= shopCashierMapper.updateForShop(shopCashierInVo);
                if (updateUser<1){
                    throw new RuntimeException("修改店员状态失败!");
                }
            }else{
                ShopCashierInVo cashierInVo=new ShopCashierInVo();
                cashierInVo.setShopId(useradmin.getShopId());
                cashierInVo.setCashierId(usercahier.getId());
                cashierInVo.setCashierName(usercahier.getUserName());
                cashierInVo.setIsAdmin(ShopCashier.SHOP_CASHIER_IS_ADMIN);
                Integer careateUser= createCashier(cashierInVo);
                if (careateUser<1){
                    throw new RuntimeException("创建管理员失败!");
                }
            }
        }

        /*将用户修改成商家*/
        User getSj=new User();
        getSj.setUserName(usercahier.getUserName());
        getSj.setShopId(useradmin.getShopId());
        getSj.setId(usercahier.getId());
        getSj.setUserType(User.USER_TYPE_SJ);
        Integer changeShop=changeUser(getSj);
        if (changeShop<1){
            throw new RuntimeException("用户修改失败!");
        }else {
            String key = "userId_"+usercahier.getId();
            redisCache.del(key);
        }

         /*将商家的核销员或者管理员修改成用户*/
        User getPt=new User();
        getPt.setUserName(useradmin.getUserName());
        getPt.setId(useradmin.getId());
        getPt.setUserType(User.USER_TYPE_PT);
        Integer changeUser=changeUser(getPt);
        if (changeUser<1){
            throw new RuntimeException("用户修改失败!");
        }else {
            String key = "userId_"+useradmin.getId();
            redisCache.del(key);
        }

        /*将该商家所有人设置成核销员*/
        ShopCashierInVo shopadminInVo = new ShopCashierInVo();
        shopadminInVo.setShopId(useradmin.getShopId());
        shopadminInVo.setParentId(user.getUserId());
        Integer allByshop=changeCashier(shopadminInVo);
        if (allByshop<1){
            throw new RuntimeException("修改用户权限失败!");
        }
        /*将该核销员(上面已经从用户变为核销员)修改成该商家的管理员*/
        ShopCashierInVo shopuserInVo = new ShopCashierInVo();
        shopuserInVo.setCashierId(user.getUserId());
        shopuserInVo.setShopId(useradmin.getShopId());
        Integer adminByuser=changeAdmin(shopuserInVo);
        if (adminByuser<1){
            throw new RuntimeException("修改用户权限失败!");
        }
        return 1;
    }

    /**
     * 将该商家所有人设置成核销员
     * @param shopCashierInVo
     * @return
     */
    private Integer changeCashier(ShopCashierInVo shopCashierInVo){
        return shopCashierMapper.updateAllByshop(shopCashierInVo);
    }

    /**
     * 将该核销员设置成管理员
     * @param shopCashierInVo
     * @return
     */
    private Integer changeAdmin(ShopCashierInVo shopCashierInVo){
        return shopCashierMapper.updateAdminByUser(shopCashierInVo);
    }

    /**
     * 将商家的核销员或者管理员修改成用户
     * @param user
     * @return
     */
    private Integer changeUser(User user){
        System.out.println(user.toString());
        Integer i=userMapper.updateUserType(user);
        return i;
    }

    /**
     * 修改店员信息
     * @param shopCashierInVo
     * @return
     */
    private Integer deleteCashier(ShopCashierInVo shopCashierInVo){
        return shopCashierMapper.deleteforUser(shopCashierInVo);
        //updateForShop
    }



    /**
     * 判断用户是否在shopCashier中此商家有配置
     * @param user
     * @return
     */
    private ShopCashier getFlag(User user,Integer type){
        ShopCashierInVo cashierInVo = new ShopCashierInVo();
        cashierInVo.setCashierId(user.getId());
        cashierInVo.setShopId(user.getShopId());
        if(type==1){
            cashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);
        }
        ShopCashier shopCashier = shopCashierMapper.selectBycashier(cashierInVo);
        return shopCashier;
    }

    /**
     * 判断用户是否在shopCashier中有配置
     * @param user
     * @return
     */
    private ShopCashier getHave(User user,Integer type){
        ShopCashierInVo cashierInVo = new ShopCashierInVo();
        cashierInVo.setCashierId(user.getId());
        cashierInVo.setShopId(user.getShopId());
        if(type==1){
            cashierInVo.setIsDeleted(ShopCashier.SHOP_CASHIER_NO_DELETED);
        }
        ShopCashier shopCashier = shopCashierMapper.gethave(cashierInVo);
        return shopCashier;
    }

    /**
     * 加入管理表
     * 需要shopid，cashier_id，cashier_name,is_admin,parent_id(看情况)
     * @param cashierInVo
     * @return
     */
    private Integer createCashier(ShopCashierInVo cashierInVo){
        cashierInVo.setUpdatorId(new Long(57));
        cashierInVo.setCreatorId(new Long(57));
        Integer i=shopCashierMapper.insert(cashierInVo);
        return i;
    }

    /**
     * 修改用户余额
     * type =1加钱，type=2减钱  (由于现在商家信息单独维护，所以在更换用户角色时，不需要转移其账户余额)
     * @param
     * @return
     */
    @Deprecated
    private AccountLog custom(UserAccount userAccount,BigDecimal amount, Integer type){
        AccountLog accountLog = new AccountLog();
        accountLog.setAccountId(userAccount.getId());
        accountLog.setUserName(userAccount.getUserName());
        accountLog.setPreAmount(userAccount.getAccountAmount());
        accountLog.setOperateAmount(amount);
        if (type==1){//type =1加钱
            accountLog.setAfterAmount(accountLog.getPreAmount().add(accountLog.getOperateAmount()));
            accountLog.setOperateType(AccountLog.OPERATE_TYPE_INCOME);
        }else {//type=2减钱
            accountLog.setAfterAmount(accountLog.getPreAmount().subtract(accountLog.getOperateAmount()));
            accountLog.setOperateType(AccountLog.OPERATE_TYPE_PAYOUT);
        }
        StringBuilder remark = new StringBuilder();
        remark.append("账户转移");//标题
        remark.append("交易对象:"+userAccount.getUserName());//交易对象
        remark.append("交易金额:" + accountLog.getOperateAmount());//金额
        accountLog.setRemark(remark.toString());
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setAccountName(userAccount.getAccountName());
        String re="账户转移"+"交易对象:"+userAccount.getUserName()+"交易金额:" + accountLog.getOperateAmount();

        //更改用户余额
        UserAccountInVo userAccountInVo = new UserAccountInVo();
        userAccountInVo.setUserId(userAccount.getUserId());
        userAccountInVo.setOccurAmount(amount);//操作金额
        userAccountInVo.setVersionNo(userAccount.getVersionNo());
        Integer i=0;
        if (type==1){//type =1加钱
            i =userAccountMapper.updateByoccur(userAccountInVo);
        }else {//type=2减钱
            i=userAccountMapper.payout(userAccountInVo);
        }
        if (i < 1) {
            throw new RuntimeException("账户余额修改失败!");
        }
        return accountLog;
    }

    @Override
    @Transactional
    public Integer updateIncome(UserAccountInVo inVo,String remark, Long cashApplyId,Long orderId) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        //如果账户不存在，则新增一个(主要是历史数据没创建账户信息)
        if(userAccount == null){
            User user = userMapper.selectByPrimaryKey(inVo.getUserId());
            if(user != null){
                userAccount = this.createAccountByUser(user);
            }else{
                return 0;
            }
        }

        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.updateIncome(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLogNew(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_INCOME, inVo.getType(), remark, cashApplyId, orderId);
        }
        return ret;
    }

    /**
     * 账户操作日志通用版本(金币不适合)
     * @param inVo
     * @param accountLogInVo
     * @return
     */
    @Override
    @Transactional
    public Integer updateIncomeV1(UserAccountInVo inVo, AccountLog accountLogInVo) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        //如果账户不存在，则新增一个(主要是历史数据没创建账户信息)
        if(userAccount == null){
            User user = userMapper.selectByPrimaryKey(inVo.getUserId());
            if(user != null){
                userAccount = this.createAccountByUser(user);
            }else{
                return 0;
            }
        }

        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.updateIncome(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLogV1(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_INCOME, inVo.getType(), accountLogInVo);
        }
        return ret;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Integer updatePayout(UserAccountInVo inVo,String remark, Long cashApplyId,Long orderId) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.updatePayout(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLogNew(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_PAYOUT, inVo.getType(), remark, cashApplyId, orderId);
        }
        return ret;
    }

    /**
     * 账户操作日志通用版本(金币不适合)
     * @param inVo
     * @param accountLogInVo
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Integer
    updatePayoutV1(UserAccountInVo inVo,AccountLog accountLogInVo) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.payout(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLogV1(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_PAYOUT, inVo.getType(), accountLogInVo);
        }
        return ret;
    }

    /**
     * 修改账户用户提现绑定银行卡信息
     * @param msg
     * @param inVo
     * @return
     */
    @Override
    public Integer updatePayBinding(String msg, UserAccountInVo inVo) {
        String key = "redisPayBinding" + inVo.getBankCardPhone();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSendInVo smsSendInVo = new SmsSendInVo();
        smsSendInVo.setUserId(inVo.getUserId());
        smsSendInVo.setSmsType(SmsSend.SMS_TYPE_BINDING);
        smsSendInVo.setShopMobile(inVo.getBankCardPhone());
        SmsSend smsSend = smsSendMapper.selectByMobile(smsSendInVo);
        if(smsSend==null||smsSend.getSmsContent()==null){
            return null;
        }
        if (smsOut == null) {
            if (smsSend!=null&&smsSend.getId()!=null){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            }
            return null;
        }
        if(StringUtils.equals(msg, smsSend.getSmsContent())){
            //删除缓存 和 数据库信息  使短信只有一次有效
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);

            return userAccountMapper.updatePayBinding(inVo);
        }
        return null;
    }

    /**
     * 账户操作日志
     * @param userAccount
     * @param occurAmount
     * @param operateType
     * @return
     */
    private Integer addAccountLogNew(UserAccount userAccount, BigDecimal occurAmount, int operateType, Integer type,String remark, Long cashApplyId,Long orderId){
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setUserName(userAccount.getUserName());
        accountLog.setAccountId(userAccount.getId());
        accountLog.setAccountName(userAccount.getAccountName());

        accountLog.setOperateType(operateType);
        accountLog.setRemark(remark);
        accountLog.setCashApplyId(cashApplyId);
        accountLog.setOrderId(orderId);
        accountLog.setType(type);
        BigDecimal aferAmount = BigDecimal.ZERO;
        if(userAccount.getUserAmount()==null){
            userAccount.setUserAmount( BigDecimal.ZERO);
        }
        if(userAccount.getFailAmount()==null){
            userAccount.setFailAmount( BigDecimal.ZERO);
        }
        if(userAccount.getPassedAmount()==null){
            userAccount.setPassedAmount( BigDecimal.ZERO);
        }
        if(userAccount.getReviewAmount()==null){
            userAccount.setReviewAmount( BigDecimal.ZERO);
        }

        if(operateType == AccountLog.OPERATE_TYPE_INCOME){  //收入
            if(type==AccountLog.TYPE_USER){//用户余额
                accountLog.setPreAmount(userAccount.getUserAmount());
                aferAmount = userAccount.getUserAmount().add(occurAmount);
            }else if(type==AccountLog.TYPE_USER_FAIL){//用户获取失败的奖励金
                accountLog.setPreAmount(userAccount.getFailAmount());
                aferAmount = userAccount.getFailAmount().add(occurAmount);
            }else if (type==AccountLog.TYPE_USER_PASSED){//用户已获得的奖励金
                accountLog.setPreAmount(userAccount.getPassedAmount());
                aferAmount = userAccount.getPassedAmount().add(occurAmount);
            }else if (type==AccountLog.TYPE_USER_REVIEW){//用户审核中的奖励金
                accountLog.setPreAmount(userAccount.getReviewAmount());
                aferAmount = userAccount.getReviewAmount().add(occurAmount);
            }

        }else {
            if(type==AccountLog.TYPE_USER){//用户余额
                aferAmount = userAccount.getUserAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getUserAmount());
            }else if(type==AccountLog.TYPE_USER_FAIL){//用户获取失败的奖励金
                aferAmount = userAccount.getFailAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getFailAmount());
            }else if (type==AccountLog.TYPE_USER_PASSED){//用户已获得的奖励金
                aferAmount = userAccount.getPassedAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getPassedAmount());
            }else if (type==AccountLog.TYPE_USER_REVIEW){//用户审核中的奖励金
                aferAmount = userAccount.getReviewAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getReviewAmount());
            }
        }
        accountLog.setAfterAmount(aferAmount);
        accountLog.setOperateAmount(occurAmount);

        Integer ret = accountLogMapper.insert(accountLog);
        return ret;
    }

    /**
     * 账户操作日志通用版本(金币不适合)
     * @param userAccount
     * @param occurAmount
     * @param operateType
     * @return
     */
    private Integer addAccountLogV1(UserAccount userAccount, BigDecimal occurAmount, int operateType, Integer type,AccountLog accountLogInVo){
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setUserName(userAccount.getUserName());
        accountLog.setAccountId(userAccount.getId());
        accountLog.setAccountName(userAccount.getAccountName());

        accountLog.setOperateType(operateType);
        accountLog.setRemark(accountLogInVo.getRemark());
        accountLog.setCashApplyId(accountLogInVo.getCashApplyId());
        accountLog.setOrderId(accountLogInVo.getOrderId());
        accountLog.setOrderCouponCode(accountLogInVo.getOrderCouponCode());
        accountLog.setType(type);
        BigDecimal aferAmount = BigDecimal.ZERO;
        if(userAccount.getAccountAmount()==null){
            userAccount.setAccountAmount( BigDecimal.ZERO);
        }
        if(userAccount.getUserAmount()==null){
            userAccount.setUserAmount( BigDecimal.ZERO);
        }
        if(userAccount.getFailAmount()==null){
            userAccount.setFailAmount( BigDecimal.ZERO);
        }
        if(userAccount.getPassedAmount()==null){
            userAccount.setPassedAmount( BigDecimal.ZERO);
        }
        if(userAccount.getReviewAmount()==null){
            userAccount.setReviewAmount( BigDecimal.ZERO);
        }

        if(operateType == AccountLog.OPERATE_TYPE_INCOME){  //收入
            if(type==AccountLog.TYPE_USER){//用户余额
                accountLog.setPreAmount(userAccount.getUserAmount());
                aferAmount = userAccount.getUserAmount().add(occurAmount);
            }else if(type==AccountLog.TYPE_USER_FAIL){//用户获取失败的奖励金
                accountLog.setPreAmount(userAccount.getFailAmount());
                aferAmount = userAccount.getFailAmount().add(occurAmount);
            }else if (type==AccountLog.TYPE_USER_PASSED){//用户已获得的奖励金
                accountLog.setPreAmount(userAccount.getPassedAmount());
                aferAmount = userAccount.getPassedAmount().add(occurAmount);
            }else if (type==AccountLog.TYPE_USER_REVIEW){//用户审核中的奖励金
                accountLog.setPreAmount(userAccount.getReviewAmount());
                aferAmount = userAccount.getReviewAmount().add(occurAmount);
            }else if (type==AccountLog.TYPE_SHOP){//用户审核中的奖励金
                accountLog.setPreAmount(userAccount.getAccountAmount());
                aferAmount = userAccount.getAccountAmount().add(occurAmount);
            }

        }else {
            if(type==AccountLog.TYPE_USER){//用户余额
                aferAmount = userAccount.getUserAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getUserAmount());
            }else if(type==AccountLog.TYPE_USER_FAIL){//用户获取失败的奖励金
                aferAmount = userAccount.getFailAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getFailAmount());
            }else if (type==AccountLog.TYPE_USER_PASSED){//用户已获得的奖励金
                aferAmount = userAccount.getPassedAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getPassedAmount());
            }else if (type==AccountLog.TYPE_USER_REVIEW){//用户审核中的奖励金
                aferAmount = userAccount.getReviewAmount().subtract(occurAmount);  //支出
                accountLog.setPreAmount(userAccount.getReviewAmount());
            }else if (type==AccountLog.TYPE_SHOP){//用户审核中的奖励金
                accountLog.setPreAmount(userAccount.getAccountAmount());
                aferAmount = userAccount.getAccountAmount().subtract(occurAmount);
            }
        }
        accountLog.setAfterAmount(aferAmount);
        accountLog.setOperateAmount(occurAmount);

        Integer ret = accountLogMapper.insert(accountLog);
        return ret;
    }

    /**
     * 通过userId 判断当前用户是否是店铺管理员并返回当前店铺的店铺账户信息
     * @param userId
     * @return
     */
    public UserAccount checkIsAdminAndReturnShopAccount(long userId){
        ShopCashier shopCashier = shopCashierMapper.selectAdminByCashierId(userId);
        if(shopCashier!=null){
            Long shopId = shopCashier.getShopId();
            UserAccount userAccount = userAccountMapper.selectShopAccountByShopId(shopId);
            return userAccount;
        }
        return null;
    }

    /**
     * 新增商户账号信息后  加钱逻辑修改
     * @param inVo
     * @param accountLogInVo
     * @return
     */
    @Override
    @Transactional
    public Integer updateIncomeV2(UserAccountInVo inVo, AccountLog accountLogInVo) {
        //查询用户账户信息
        UserAccount adminAccount =  checkIsAdminAndReturnShopAccount(inVo.getUserId());
        UserAccount userAccount = fillUserAccount(adminAccount,inVo.getUserId());
        if(userAccount == null){
           return 0;
        }
        userAccount.setUserId(inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        inVo.setShopId(userAccount.getShopId());
        //更新账户余额
        Integer ret = userAccountMapper.incomeForAdmin(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLogV1(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_INCOME, inVo.getType(), accountLogInVo);
        }
        return ret;
    }

    /**
     * 账户操作日志通用版本(金币不适合)
     * @param inVo
     * @param accountLogInVo
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public Integer updatePayoutV2(UserAccountInVo inVo,AccountLog accountLogInVo) {
        //查询用户账户信息
        UserAccount adminAccount =  checkIsAdminAndReturnShopAccount(inVo.getUserId());
        UserAccount userAccount = fillUserAccount(adminAccount,inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        inVo.setShopId(userAccount.getShopId());
        //更新账户余额
        Integer ret = userAccountMapper.payoutForAdmin(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLogV1(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_PAYOUT, inVo.getType(), accountLogInVo);
        }
        return ret;
    }

    @Override
    public Integer addOrUpdateSecurityCode(SecurityCodeInVo vo) {
        String securityCode = vo.getSecurityCode();
        Integer type = vo.getType();
        Long userId = vo.getUserId();
        if(org.apache.commons.lang3.StringUtils.isBlank(securityCode)){
            return -1;
        }
        UserAccount userAccount=null;
        if(type==1){
            userAccount = userAccountMapper.findAccountByUserId(userId);
        }else if(type==2){
            userAccount = checkIsAdminAndReturnShopAccount(userId);
        }
        if(userAccount==null){
            return -1;
        }
        UserAccount updateAccount = new UserAccount();
        updateAccount.setId(userAccount.getId());
        updateAccount.setUpdateTime(new Date());
        updateAccount.setSecurityCode(vo.getSecurityCode());
        Integer ret = userAccountMapper.updateSecurityCodeById(updateAccount);
        if(ret<1){
            return -1;
        }
        return 1;
    }

    @Override
    public Integer forgetSecurityCode(SecurityCodeInVo vo) {
        String key = "redisSecurityCode" + vo.getMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSendInVo smsSendInVo = new SmsSendInVo();
        smsSendInVo.setUserId(vo.getUserId());
        smsSendInVo.setSmsType(SmsSend.SMS_TYPE_FORGET);
        smsSendInVo.setShopMobile(vo.getMobile());
        SmsSend smsSend = smsSendMapper.selectByMobile(smsSendInVo);
        if(smsSend==null||smsSend.getSmsContent()==null){
            return -2;
        }
        if(org.apache.commons.lang3.StringUtils.equals(vo.getCode(), smsSend.getSmsContent())){
            //删除缓存 和 数据库信息  使短信只有一次有效
            redisCache.del(key);
        }else{
            return -2;
        }

        if (smsOut == null) {
            return -3;
        }

        UserAccount userAccount=null;
        Integer type = vo.getType();
        Long userId = vo.getUserId();
        if(type==1){
            userAccount = userAccountMapper.findAccountByUserId(userId);
        }else if(type==2){
            userAccount = checkIsAdminAndReturnShopAccount(userId);
        }
        String securityCode = vo.getSecurityCode();
        UserAccount updateAccount = new UserAccount();
        updateAccount.setId(userAccount.getId());
        updateAccount.setUpdateTime(new Date());
        updateAccount.setSecurityCode(securityCode);
        userAccountMapper.updateSecurityCodeById(updateAccount);
        return 1;
    }

    @Override
    public Pager<AccountLogOut> findAccountLogsForShop(AccountLogInVo inVo) {
        Pager<AccountLogOut> result = new Pager<AccountLogOut>();
        UserAccount userAccount = checkIsAdminAndReturnShopAccount(inVo.getUserId());
        inVo.setUserId(null);
        inVo.setAccountId(userAccount.getId());
        int listTotal = accountLogMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<AccountLogOut> list = accountLogMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public UserAccount findAdminAccountByUserId(Long userId) {
        ShopCashier shopCashier = shopCashierMapper.selectAdminByCashierId(userId);
        if(shopCashier!=null){
            Long shopId = shopCashier.getShopId();
            UserAccount userAccount = userAccountMapper.selectShopAccountByShopId(shopId);
            return userAccount;
        }
        return null;
    }

    /**
     * 商家账户信息填充
     * @param adminAccount
     * @return
     */
    private UserAccount fillUserAccount(UserAccount adminAccount,Long userId){
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(userId);
        if(userAccount==null){
            return new UserAccount();
        }
        userAccount.setAccountName(adminAccount.getAccountName());
        userAccount.setAccountIdentityCard(adminAccount.getAccountIdentityCard());
        userAccount.setAccountCardholder(adminAccount.getAccountCardholder());
        userAccount.setAccountAmount(adminAccount.getAccountAmount());
        userAccount.setAccountType(adminAccount.getAccountType());
        userAccount.setAccountStatus(adminAccount.getAccountStatus());
        userAccount.setCardBankname(adminAccount.getCardBankname());
        userAccount.setVersionNo(adminAccount.getVersionNo());
        userAccount.setShopId(adminAccount.getShopId());
        userAccount.setId(adminAccount.getId());
        return userAccount;
    }

}
