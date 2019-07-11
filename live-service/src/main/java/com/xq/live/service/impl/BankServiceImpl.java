package com.xq.live.service.impl;

import com.gexin.rp.sdk.base.uitls.MD5Util;
import com.xq.live.common.BankCheckUtil;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.BankService;
import com.xq.live.vo.in.BankInVo;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.out.SmsOut;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/1516:29
 */
@Service
public class BankServiceImpl implements BankService {

    private static final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private BankCheckLogMapper bankCheckLogMapper;

    @Autowired
    private UserBankInfoMapper userBankInfoMapper;

    @Autowired
    private ShopCashierMapper shopCashierMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SmsSendMapper smsSendMapper;




    @Override
    public List<UserBankInfo> queryListByOwnerIdAndType(Long userId, int type) {
        if(type==1){//查询出用户自己的银行卡
            return userBankInfoMapper.queryListByOwnerIdAndType(userId,type);
        }else{//查询出商户的银行卡
            ShopCashier shopCashier = shopCashierMapper.selectAdminByCashierId(userId);
            if(shopCashier==null){
                return new ArrayList<>();
            }
            return userBankInfoMapper.queryListByOwnerIdAndType(shopCashier.getShopId(),type);
        }
    }

    /**
     * 返回值  1 正常绑定  -1 绑定异常  2 绑定超过限制
     * @param bankInVo
     * @return
     */
    @Override
    @Transactional
    public Map<String,Object> bindBankCard(BankInVo bankInVo) {
        Map<String,Object> resultMap = new HashMap<>();
        Integer step = bankInVo.getStep();
        Integer bindType = bankInVo.getBindType();
        Long userId = bankInVo.getUserId();
        UserAccount userAccount=null;
        if(bindType==1){
            userAccount = userAccountMapper.findAccountByUserId(userId);
        }else if(bindType==2){
            userAccount = checkIsAdminAndReturnShopAccount(userId);
        }
        if(step==1){
            Integer flag = checkBandCardStepOne(bankInVo,userAccount);
            if(flag==1){
                resultMap.put("code",1);
            }else{
                resultMap.put("code",-1);
                Integer count = redisCache.get("BankCheck_"+userAccount.getId(),Integer.class);
                resultMap.put("data",count);
            }
        }else if(step==2){
            Integer flag = checkBandCardStepTwo(bankInVo,userAccount);
            if(flag==1){
                bindBankCard(bankInVo,userAccount);
                resultMap.put("code",1);
            }else{
                resultMap.put("code",flag);
            }
        }
        return resultMap;
    }


    private void insertUserbankInfo(Bank bank,BankInVo bankInVo){
        UserBankInfo userBankInfo = new UserBankInfo();
        if(bankInVo.getBindType()==2){
            ShopCashier shopCashier = shopCashierMapper.selectAdminByCashierId(bankInVo.getUserId());
            if(shopCashier==null){
                throw new RuntimeException("当前用户不是核销员");
            }
            bankInVo.setUserId(shopCashier.getShopId());
        }
        UserBankInfo existsBank = userBankInfoMapper.queryByCardNoAndUserId(bank.getAccountNo(),bankInVo.getUserId());
        if(existsBank!=null&&existsBank.getCardStatus()==1){
            throw new RuntimeException("当前银行卡该用户已经绑定");
        }
        if(existsBank!=null){
            existsBank.setCardStatus((byte)1);
            existsBank.setUpdateTime(new Date());
            userBankInfoMapper.updateByPrimaryKeySelective(existsBank);
        }else{
            userBankInfo.setBankId(bank.getId());
            userBankInfo.setBankName(bank.getBank());
            userBankInfo.setCardMobile(bank.getMobile());
            userBankInfo.setCardNo(bank.getAccountNo());
            userBankInfo.setCardStatus((byte)1);
            userBankInfo.setCardType(bank.getCardType());
            userBankInfo.setCreateTime(new Date());
            userBankInfo.setIdCard(bank.getIdCard());
            userBankInfo.setIsDeleted((byte)0);
            userBankInfo.setOwnerId(bankInVo.getUserId());
            userBankInfo.setOwnerType((byte)2);
            userBankInfo.setUpdateTime(new Date());
            userBankInfo.setCardName(bank.getCardName());
            userBankInfo.setName(bank.getName());
            userBankInfoMapper.insertSelective(userBankInfo);
        }
    }


    @Override
    public Integer unbindBankCard(BankInVo bankInVo) {
        String securityCode = bankInVo.getSecurityCode();
        Long id = bankInVo.getId();
        UserBankInfo userBankInfo = userBankInfoMapper.selectByPrimaryKey(id);
        if(userBankInfo==null){
            throw new RuntimeException("当前银行卡不存在");
        }
        if(StringUtils.isBlank(securityCode)){
            throw new RuntimeException("参数异常");
        }
        byte bindType = userBankInfo.getOwnerType();
        Long userId = bankInVo.getUserId();
        UserAccount userAccount=null;
        if(bindType==1){
            userAccount = userAccountMapper.findAccountByUserId(userId);
        }else if(bindType==2){
            userAccount = checkIsAdminAndReturnShopAccount(userId);
        }
        if(userAccount!=null){
            if(!securityCode.equals(userAccount.getSecurityCode())){
                return -1;
            }
        }
        UserBankInfo updateInfo = new UserBankInfo();
        updateInfo.setId(id);
        updateInfo.setUpdateTime(new Date());
        updateInfo.setCardStatus((byte)2);
        userBankInfoMapper.updateByPrimaryKeySelective(updateInfo);
        return 1;
    }


    /**
     * 校验银行卡四要素
     * @param bankInVo
     * @return
     */
    private Integer checkBandCardStepOne(BankInVo bankInVo,UserAccount userAccount){
        Integer count = redisCache.get("BankCheck_"+userAccount.getId(),Integer.class);
        int unUse = count==null?3:count;
        if(unUse<=0){
            return -1;
        }
        Bank bank;
        String accountNo = bankInVo.getAccountNo();
        String name = bankInVo.getName();
        String idCard = bankInVo.getIdCard();
        String mobile = bankInVo.getMobile();
        Bank existBank = bankMapper.selectByParams(accountNo,null,idCard,name);
        if(existBank==null){//如果本地存在当前银行卡校验信息
            Map<String,Object> result = BankCheckUtil.checkBankCode(accountNo,idCard,mobile,name);
            DateTime dateTime = new DateTime().millisOfDay().withMaximumValue();
            long seconds = new Duration(new DateTime(), dateTime).getStandardSeconds();
            redisCache.set("BankCheck_"+userAccount.getId(),unUse-1,seconds, TimeUnit.SECONDS);
            BankCheckLog bankCheckLog = new BankCheckLog();
            bankCheckLog.setAccountNo(accountNo);
            bankCheckLog.setIdCard(idCard);
            bankCheckLog.setCreateTime(new Date());
            bankCheckLog.setMobile(mobile);
            bankCheckLog.setName(name);
            bankCheckLog.setStatus(result.get("code").toString());
            bankCheckLog.setUpdateTime(new Date());
            bankCheckLog.setUserId(bankInVo.getUserId());
            bankCheckLogMapper.insertSelective(bankCheckLog);
            if("SUCC".equals(result.get("code"))){
                bank = (Bank) result.get("data");
                bankMapper.insertSelective(bank);//插入银行卡信息
            }else{
                return -1;
            }
        }
        return 1;
    }

    /**
     * 校验短信验证码
     * @param bankInVo
     * @param userAccount
     * @return
     */
    private Integer checkBandCardStepTwo(BankInVo bankInVo,UserAccount userAccount){
        String key = "redisPayBinding" + bankInVo.getMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSendInVo smsSendInVo = new SmsSendInVo();
        smsSendInVo.setUserId(bankInVo.getUserId());
        smsSendInVo.setSmsType(SmsSend.SMS_TYPE_BINDING);
        smsSendInVo.setShopMobile(bankInVo.getMobile());
        SmsSend smsSend = smsSendMapper.selectByMobile(smsSendInVo);
        if(smsSend==null||smsSend.getSmsContent()==null){
            return -2;
        }
        if(StringUtils.equals(bankInVo.getCode(), smsSend.getSmsContent())){
            //删除缓存 和 数据库信息  使短信只有一次有效
            redisCache.del(key);
        }else{
            return -2;
        }
        if (smsOut == null) {
            return -3;
        }
        return 1;
    }

    private Integer bindBankCard(BankInVo bankInVo,UserAccount userAccount){
        if(StringUtils.isBlank(bankInVo.getSecurityCode())||!bankInVo.getSecurityCode().equals(userAccount.getSecurityCode())){
            throw new RuntimeException("账户安全码错误");
        }
        String accountNo = bankInVo.getAccountNo();
        String name = bankInVo.getName();
        String idCard = bankInVo.getIdCard();
        String mobile = bankInVo.getMobile();
        Bank bank = bankMapper.selectByParams(accountNo,null,idCard,name);
        if(bank==null){
            return -1;
        }
        //绑定银行卡
        insertUserbankInfo(bank,bankInVo);
        return 1;
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
}
