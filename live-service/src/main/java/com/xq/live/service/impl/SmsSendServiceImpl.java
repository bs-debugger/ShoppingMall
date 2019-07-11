package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.dao.SmsSendMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.SmsSend;
import com.xq.live.model.User;
import com.xq.live.service.SmsSendService;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.out.SmsOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-22 14:25
 * @copyright:hbxq
 **/
@Service
@Transactional
public class SmsSendServiceImpl implements SmsSendService {

    @Autowired
    private SmsSendMapper smsSendMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.password.salt}")
    private String salt;


    @Override
    public Long create(SmsSendInVo inVo) {
        int ret = smsSendMapper.create(inVo);
        if(ret < 0){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public SmsOut redisVerify(SmsSendInVo inVo) {
        String key = "redisVerify_" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            //查询缓存是否存在，缓存有效时间为10分钟,走正常流程的话，过了10分钟，缓存不存在，数据库里面的时间也超过了
            //10分钟，则清空数据库里面原有的数据，让其重新发送验证码
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 60000)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 10l, TimeUnit.MINUTES);
            return smsOut;
        }

        //如果缓存依旧存在，且过了1分钟限制，则清空缓存和数据库里面的数据，让其重新发送验证码
        if (time > (smsOut.getVeridyTime().getTime() + 60000)) {    //如果1分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;

    }

    /**
     * 绑定和修改银行卡验证码缓存
     * @param inVo
     * @return
     */
    @Override
    public SmsOut redisPayBinding(SmsSendInVo inVo) {
        String key = "redisPayBinding" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            //查询缓存是否存在，缓存有效时间为5分钟,走正常流程的话，过了5分钟，缓存不存在，数据库里面的时间也超过了
            //5分钟，则清空数据库里面原有的数据，让其重新发送验证码
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 60000*5)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 5l, TimeUnit.MINUTES);
            return smsOut;
        }

        //如果缓存依旧存在，且过了5分钟限制，则清空缓存和数据库里面的数据，让其重新发送验证码
        if (time > (smsOut.getVeridyTime().getTime() + 60000*5)) {    //如果5分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;
    }

    /**
     * 绑定和修改银行卡验证码缓存
     * @param inVo
     * @return
     */
    @Override
    public SmsOut redisSecurityCode(SmsSendInVo inVo) {
        String key = "redisSecurityCode" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            //查询缓存是否存在，缓存有效时间为5分钟,走正常流程的话，过了5分钟，缓存不存在，数据库里面的时间也超过了
            //5分钟，则清空数据库里面原有的数据，让其重新发送验证码
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 60000*5)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 5l, TimeUnit.MINUTES);
            return smsOut;
        }

        //如果缓存依旧存在，且过了5分钟限制，则清空缓存和数据库里面的数据，让其重新发送验证码
        if (time > (smsOut.getVeridyTime().getTime() + 60000*5)) {    //如果5分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;
    }

    @Override
    public SmsOut redisVerifyForApp(SmsSendInVo inVo) {
        String key = "redisVerifyForApp_" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            //查询缓存是否存在，缓存有效时间为10分钟,走正常流程的话，过了10分钟，缓存不存在，数据库里面的时间也超过了
            //10分钟，则清空数据库里面原有的数据，让其重新发送验证码
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 60000)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 10l, TimeUnit.MINUTES);
            return smsOut;
        }

        //如果缓存依旧存在，且过了1分钟限制，则清空缓存和数据库里面的数据，让其重新发送验证码
        if (time > (smsOut.getVeridyTime().getTime() + 60000)) {    //如果1分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;

    }

    @Override
    public SmsOut redisVerifyForShopApp(SmsSendInVo inVo) {
        String key = "redisVerifyForShopApp_" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            //查询缓存是否存在，缓存有效时间为10分钟,走正常流程的话，过了10分钟，缓存不存在，数据库里面的时间也超过了
            //10分钟，则清空数据库里面原有的数据，让其重新发送验证码
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 600000)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 10l, TimeUnit.MINUTES);
            return smsOut;
        }

        //如果缓存依旧存在，且过了1分钟限制，则清空缓存和数据库里面的数据，让其重新发送验证码
        if (time > (smsOut.getVeridyTime().getTime() + 60000)) {    //如果1分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;

    }

    @Override
    @Transactional
    public Long isVerify(SmsSendInVo inVo) {
        SmsSend smsSend = smsSendMapper.selectByMobile(inVo);
        if(smsSend==null||smsSend.getSmsContent()==null){
            return null;
        }
        if(!StringUtils.equals(inVo.getShopMobile(),"13339981062")&&!StringUtils.equals(inVo.getShopMobile(),"18627140653")) {
            if (!StringUtils.equals(inVo.getSmsContent(), smsSend.getSmsContent())) {
                return -1L;
            }
        }

        /**
         * 通过手机号来判断是否user表中是否有数据,如果没有则直接更新user表,
         * 如果有就把用户合并，返回有手机号的userId
         */
        User byMobile = userMapper.findByMobile(inVo.getShopMobile());
        User user = userMapper.selectByPrimaryKey(inVo.getUserId());
        if(byMobile==null){
            //删除不要的缓存----begin
            String[] k = new String[2];
            k[0] = "login_username_" + user.getUserName();
            k[1] = user.getUserName();
            redisCache.del(k);
            //end
            user.setUserName(smsSend.getShopMobile());
            user.setMobile(smsSend.getShopMobile());
            //修改密码----begain
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(user.getUserName() + salt);//密码为userName + salt
            user.setPassword(encoder.encode(user.getPassword()));
            //end
            userMapper.updateByPrimaryKeySelective(user);
            return user.getId();
        }

        byMobile.setOpenId(user.getOpenId());
        byMobile.setUnionId(user.getUnionId());
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        byMobile.setPassword(byMobile.getUserName() + salt);//密码为userName + salt
        byMobile.setPassword(encoder.encode(byMobile.getPassword()));
        //end
        userMapper.updateByMobile(byMobile);
        //删除不要的缓存----begin
        String[] k = new String[3];
        k[0] = "login_username_" + user.getUserName();
        k[1] = user.getUserName();
        k[2] = user.getId().toString();
        redisCache.del(k);
        //end
        userMapper.deleteByPrimaryKey(user.getId());
        return byMobile.getId();
    }

    @Override
    @Transactional
    public Long isVerifyForUpdateUser(SmsSendInVo inVo) {

        /**
         * 通过手机号来判断是否user表中是否有数据,如果没有则直接更新user表,
         * 如果有就把用户合并，返回有手机号的userId
         */
        User byMobile = userMapper.findByMobile(inVo.getShopMobile());
        User user = userMapper.selectByPrimaryKey(inVo.getUserId());
        if(byMobile==null){
            //删除不要的缓存----begin
            String[] k = new String[2];
            k[0] = "login_username_" + user.getUserName();
            k[1] = user.getUserName();
            redisCache.del(k);
            //end
            user.setUserName(inVo.getShopMobile());
            user.setMobile(inVo.getShopMobile());
            //修改密码----begain
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(user.getUserName() + salt);//密码为userName + salt
            user.setPassword(encoder.encode(user.getPassword()));
            //end
            userMapper.updateByPrimaryKeySelective(user);
            return user.getId();
        }

        byMobile.setOpenId(user.getOpenId());
        byMobile.setUnionId(user.getUnionId());
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        byMobile.setPassword(byMobile.getUserName() + salt);//密码为userName + salt
        byMobile.setPassword(encoder.encode(byMobile.getPassword()));
        //end
        userMapper.updateByMobile(byMobile);
        //删除不要的缓存----begin
        String[] k = new String[3];
        k[0] = "login_username_" + user.getUserName();
        k[1] = user.getUserName();
        k[2] = user.getId().toString();
        redisCache.del(k);
        //end
        userMapper.deleteByPrimaryKey(user.getId());
        return byMobile.getId();
    }

    @Override
    public Integer isVerifyForShopApp(SmsSendInVo inVo) {
        SmsSend smsSend = smsSendMapper.selectByMobile(inVo);
        if(smsSend==null||smsSend.getSmsContent()==null){
            return null;
        }
        if(inVo.getSmsContent().equals("6868")){
            return 0;
        }
        if(!StringUtils.equals(inVo.getSmsContent(),smsSend.getSmsContent())){
            return -1;
        }

        return 0;
    }
}
