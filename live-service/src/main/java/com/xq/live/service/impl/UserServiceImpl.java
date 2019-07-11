package com.xq.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.xq.live.common.Constants;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.UserService;
import com.xq.live.service.WeiXinUserLoginService;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import com.xq.live.web.utils.GetUserInfoUtil;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.JwtTokenUtil;
import com.xq.live.web.utils.RedEnvelope;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private WeiXinUserLoginService weiXinUserLoginService;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.password.salt}")
    private String salt;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SmsSendMapper smsSendMapper;

    @Override
    public User getUserById(@Param("id") Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(User user) {
        user.setIconUrl(Constants.DEFAULT_ICON_URL);
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName()+salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        int ret = userMapper.insert(user);
        if(ret > 0){
            //新增用户成功后，新增一条账户信息
            this.addUserAccount(user);

            return user.getId();
        }
        return null;
    }

    @Override
    public Long batchAdd(User user) {
        user.setIconUrl(Constants.DEFAULT_ICON_URL);
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName()+salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        int ret = 0;
        try {
            ret = userMapper.batchInsert(user);
        }catch (Exception e){
            ret = 0;
        }
        if(ret > 0){
            //新增用户成功后，新增一条账户信息
            this.batchAddUserAccount(user);

            return user.getId();
        }
        return null;
    }

    @Override
    public User findByUsername(@org.apache.ibatis.annotations.Param("userName") String userName) {
        return userMapper.loadUserByUserName(userName);
    }

    @Override
    public User findByOpenId(String openId) {
        return userMapper.findByOpenId(openId);
    }

    @Override
    public User findByUnionId(String unionId) {
        return userMapper.findByUnionId(unionId);
    }

    @Override
    public User findByMobile(String mobile) {
        User byMobile = userMapper.findByMobile(mobile);
        return byMobile;
    }

    @Override
    public Pager<User> list(UserInVo inVo) {
        Pager<User> result= new Pager<User>();
        int total = userMapper.listTotal(inVo);
        result.setTotal(total);
        if(total > 0 ){
            List<User> list = userMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<User> listForShopId(UserInVo inVo) {
        return userMapper.listForShopId(inVo);
    }

    @Override
    public Integer update(User user) {
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName()+salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> top(UserInVo inVo){
        return userMapper.list(inVo);
    }

    //通过订单ID得到UserID获取用户余额
    @Override
    public List<UserAccount> findAccountByUserId(Long id) {
        List<UserAccount> list = new ArrayList<UserAccount>();
        SoInVo inVo = new SoInVo();
        inVo.setId(id);
        Long userId=soMapper.getUserIDBySoId(inVo);
        UserAccount userAccount=userAccountMapper.findAccountByUserId(userId);
        list.add(0,userAccount);
        return list;
    }

    //通过活动UserID获取用户余额
    @Override
    public List<UserAccount> fingAccountByID(Long userID) {
        List<UserAccount> list = new ArrayList<UserAccount>();
        UserAccount userAccount=userAccountMapper.findAccountByUserId(userID);
        list.add(0, userAccount);
        return list;
    }
    //通过UserId获取用户余额

    @Override
    public User findByUserNameAndPwd(UserInVo inVo){
        return userMapper.findByUserNameAndPwd(inVo);
    }

    @Override
    public Integer updateLoginInfo(User user){
        Date now = new Date();
        //1、更新用户表登录ip，登录次数等
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName() + salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer updateByOpenId(User user) {
        Date now = new Date();
        //1、更新用户表登录ip，登录次数等
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        if(user!=null&&user.getMobile()!=null){
            /*Map<String, String> rmp = SignUtil.encryNameAndMobile(user.getMobile());
            user.setUserName(rmp.get("mobile"));*/
            user.setUserName(user.getMobile());
        }
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName() + salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        return userMapper.updateByOpenId(user);
    }

    //在用户短信验证通过后修改account_name名称为用户电话号码
    @Override
    public Integer updateByUserID(UserAccountInVo accountInVo) {
        int i=userAccountMapper.updateByUserID(accountInVo);
        //如果成功返回1，失败返回0
        if (i>0){
            return 1;
        }
        return 0;
    }

    @Override
    public Integer updateByMobile(User user){
        Date now = new Date();
        //1、更新用户表登录ip，登录次数等
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        if(user!=null&&user.getMobile()!=null){
            user.setUserName(user.getMobile());
        }
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName()+salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        String aa = user.getPassword();
        String bb = user.getUserName()+salt;
        boolean kk = encoder.matches(bb, aa);
        Integer integer = userMapper.updateByMobile(user);
        return integer;
    }


    @Override
    public String login(String username, String password){
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, username+salt);
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //boolean kk = encoder.matches("1999900099abc", "$2a$10$I3hlDXQqWI9S9XA8kKcj6uLNUc2NO1uAuSjTO6aqWfvaZXD0cmfha");
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public Long register(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));
//        rawPassword = DigestUtils.md5DigestAsHex((rawPassword).getBytes());
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }


    @Override
    public Long addAppUser(User user) {
        User byOpenId = userMapper.findByOpenId(user.getOpenId());
        User byUnionId = userMapper.findByUnionId(user.getUnionId());
        User byMobile = userMapper.findByMobile(user.getMobile());
        /*
        商家端中用微信登录必有openId，且openId应该都是不同的
        byUnionId如果为空,byMobile也为空,则证明要么没用户，要么没有有效用户(该用户要被合并)，则直接插入一条数据,返回userId
        byUnionId如果为空,byMobile不为空，则到byMobile
         */
        if(byUnionId==null){
            if(byMobile==null){
                //修改密码----begain
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(user.getUserName() + salt);//密码为userName + salt
                user.setPassword(encoder.encode(user.getPassword()));
                //end
                    int i = userMapper.insert(user);
                    if (i < 1) {
                        return null;
                    }
                    //新增用户成功后，新增一条账户信息
                    this.addUserAccount(user);
                    return user.getId();
            }
            byMobile.setOpenId(user.getOpenId());
            byMobile.setUnionId(user.getUnionId());
            //修改密码----begain
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            byMobile.setPassword(byMobile.getUserName() + salt);//密码为userName + salt
            byMobile.setPassword(encoder.encode(byMobile.getPassword()));
            //end
            Integer k = userMapper.updateByMobile(byMobile);
            if(k<1){
                return null;
            }
            return byMobile.getId();
        }else {
            if(byUnionId.getMobile()!=null&&!StringUtils.equals("",byUnionId.getMobile())){
                if(!StringUtils.equals(byUnionId.getOpenId(), user.getOpenId())){
                    byUnionId.setOpenId(user.getOpenId());
                    //修改密码----begain
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    byUnionId.setPassword(byUnionId.getUserName() + salt);//密码为userName + salt
                    byUnionId.setPassword(encoder.encode(byUnionId.getPassword()));
                    //end
                    userMapper.updateByPrimaryKeySelective(byUnionId);
                }
                return byUnionId.getId();
            }
            if(byMobile==null){
                byUnionId.setMobile(user.getMobile());
                //删除不要的缓存----begin
                String[] k = new String[2];
                k[0] = "login_username_" + byUnionId.getUserName();
                k[1] = byUnionId.getUserName();
                redisCache.del(k);
                //end
                byUnionId.setUserName(user.getMobile());
                //修改密码----begain
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                byUnionId.setPassword(byUnionId.getUserName() + salt);//密码为userName + salt
                byUnionId.setPassword(encoder.encode(byUnionId.getPassword()));
                //end
                Integer j = userMapper.updateByPrimaryKeySelective(byUnionId);
                if (j < 1) {
                    return null;
                }
                return byUnionId.getId();
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
            k[0] = "login_username_" + byUnionId.getUserName();
            k[1] = byUnionId.getUserName();
            k[2] = byUnionId.getId().toString();
            redisCache.del(k);
            //end
            userMapper.deleteByPrimaryKey(byUnionId.getId());
            return byMobile.getId();
        }


        /*if(byOpenId==null) {
            if (byMobile == null) {
                int i = userMapper.insert(user);
                if (i < 1) {
                    return null;
                }
                //新增用户成功后，新增一条账户信息
                this.addUserAccount(user);
                return user.getId();
            }
            byMobile.setOpenId(user.getOpenId());
            Integer k = userMapper.updateByMobile(byMobile);
            if(k<1){
                return null;
            }
            return byMobile.getId();
        }else {
            if(byOpenId.getMobile()!=null){
                return byOpenId.getId();
            }
            if(byMobile==null){
                byOpenId.setMobile(user.getMobile());
                Integer j = userMapper.updateByOpenId(byOpenId);
                if (j < 1) {
                    return null;
                }
                return byOpenId.getId();
            }
            byMobile.setOpenId(user.getOpenId());
            userMapper.updateByMobile(byMobile);
            userMapper.deleteByPrimaryKey(byOpenId.getId());
            return byMobile.getId();
        }*/
    }

    /**
     * 新人注册成功后修改缓存人数
     * @param inVo
     * @return
     */
    @Override
    public SkuForNewUserOut upPeopleNum(SkuForNewUserInVo inVo) {
        String key="Qgc_"+"skuId_"+inVo.getSkuId()+"_parentId_"+inVo.getParentId();
        SkuForNewUserOut newUserOut=redisCache.get(key, SkuForNewUserOut.class);
        if (newUserOut==null){
            return null;
        }

        if (newUserOut.getNewUser()!=null){
            for (User user:newUserOut.getNewUser()){
                if (user.getId()==inVo.getNewUser()){
                    return null;
                }
            }
        }

        if (newUserOut.getPeoPleNum()==null){
            newUserOut.setPeoPleNum(1);
        }else if (newUserOut.getPeoPleNum()>=2){
            return null;
        }else {
            newUserOut.setPeoPleNum(newUserOut.getPeoPleNum()+1);
        }
        User user=userMapper.selectByPrimaryKey(inVo.getNewUser());
        List<User> newUser=newUserOut.getNewUser();
        if (newUser==null){
            newUser= new ArrayList<User>();
            newUser.add(user);
            newUserOut.setNewUser(newUser);
        }else {
            newUser.add(user);
            newUserOut.setNewUser(newUser);
        }

        //跟新缓存
        Date date= new Date();
        Long redisout= RedEnvelope.getDatePoor(newUserOut.getEndTime(), date);
        if (redisout.equals(0) || redisout.intValue() < 0) {
            redisCache.del(key);
            return null;
        }
        redisCache.set(key, newUserOut, redisout, TimeUnit.SECONDS);
        return newUserOut;
    }

    /**
     * 抢购邀请新人注册
     * @param inVo
     * @return
     */
    @Override
    public SkuForNewUserOut invite(SkuForNewUserInVo inVo){
        String key = "Qgc_" + "skuId_" + inVo.getSkuId()+"_parentId_"+inVo.getParentId();
        SkuForNewUserOut newUserOut = redisCache.get(key, SkuForNewUserOut.class);
        if (newUserOut!=null){
            return newUserOut;
        }
        SkuForNewUserOut skuForNewUserOut= this.createNewRedis(inVo);

        redisCache.set(key, skuForNewUserOut,1l, TimeUnit.HOURS);
        return skuForNewUserOut;
    }

    /**
     * 秒杀商品创建邀请新人注册
     * @param inVo
     * @return
     */
    @Override
    public SecondsKillGoodsSkuOut inviteNewPeople(SkuForNewUserInVo inVo) {
        Date time = new Date();
        String valueKey="ActId_"+inVo.getActId()+"MsGoodsSku_"+"sku_id_"+inVo.getSkuId()+"parent_id_"+inVo.getParentId();
        String totleKey="ActId_"+inVo.getActId()+"MsGoodsSku_"+"parent_id_"+inVo.getParentId();
       /*获取缓存*/
        String hget = (String)redisCache.hget(totleKey,valueKey);

        if (hget==null||hget.length()<1){
            SecondsKillGoodsSkuOut secondsKillGoodsSkuOut=this.createSecondsSKill(inVo);
            secondsKillGoodsSkuOut.setTotleKey(totleKey);
            secondsKillGoodsSkuOut.setValueKey(valueKey);
            redisCache.hset(totleKey, valueKey, JSON.toJSONString(secondsKillGoodsSkuOut));
            return secondsKillGoodsSkuOut;//创建新的缓存
        }else {
            SecondsKillGoodsSkuOut secondsKillRedis=JSON.parseObject(hget, SecondsKillGoodsSkuOut.class);
            if (time.getTime()>=secondsKillRedis.getEndTime().getTime()){
                redisCache.hdel(totleKey, valueKey);
            }
            SecondsKillGoodsSkuOut secondsKillGoodsSkuOut=this.createSecondsSKill(inVo);
            secondsKillGoodsSkuOut.setTotleKey(totleKey);
            secondsKillGoodsSkuOut.setValueKey(valueKey);
            redisCache.hset(totleKey, valueKey, JSON.toJSONString(secondsKillGoodsSkuOut));
            return secondsKillGoodsSkuOut;//创建新的缓存
        }
    }

    /**
     * 查看商品邀请的新人信息
     * @param inVo
     * @return
     */
    @Override
    public List<SecondsKillGoodsSkuOut> selectQuerySecondsKill(SkuForNewUserInVo inVo) {
        Date time = new Date();
        List<SecondsKillGoodsSkuOut> killGoodsSkuOuts = new ArrayList<SecondsKillGoodsSkuOut>();
        String totleKey = null;
        String valueKey = null;
        if (inVo.getSkuId()!=null){
            valueKey="ActId_"+inVo.getActId()+"MsGoodsSku_"+"sku_id_"+inVo.getSkuId()+"parent_id_"+inVo.getParentId();
            totleKey="ActId_"+inVo.getActId()+"MsGoodsSku_"+"parent_id_"+inVo.getParentId();
            String hget = (String)redisCache.hget(totleKey,valueKey);
            if(hget==null){
                return null;
            }
            SecondsKillGoodsSkuOut secondsKillRedis=JSON.parseObject(hget, SecondsKillGoodsSkuOut.class);
            if(time.getTime()>=secondsKillRedis.getEndTime().getTime()){
                redisCache.hdel(totleKey.toString(),valueKey);//删除超时秒杀
            }else {
                killGoodsSkuOuts.add(secondsKillRedis);
                //目前先按照创建时间来排序,后期可以在此扩展改变
                Collections.sort(killGoodsSkuOuts, new Comparator<SecondsKillGoodsSkuOut>() {
                    public int compare(SecondsKillGoodsSkuOut o1, SecondsKillGoodsSkuOut o2) {
                        if (o1.getCreateTime().before(o2.getCreateTime())) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
            }
        }else {
            totleKey="ActId_"+inVo.getActId()+"MsGoodsSku_"+"parent_id_"+inVo.getParentId();
            Map<Object, String> values = redisCache.hmget(totleKey);
            if(values==null||values.size()==0){
                return null;
            }
            for(Map.Entry<Object, String> a: values.entrySet() ){
                Object key =a.getKey();
                String value=a.getValue();
                SecondsKillGoodsSkuOut secondsKillRedis=JSON.parseObject(value, SecondsKillGoodsSkuOut.class);
                if(time.getTime()>=secondsKillRedis.getEndTime().getTime()){
                    redisCache.hdel(key.toString(),value);//删除超时秒杀
                }else{
                    secondsKillRedis.setValueKey(valueKey);
                    secondsKillRedis.setTotleKey(totleKey);
                    killGoodsSkuOuts.add(secondsKillRedis);
                    //目前先按照创建时间来排序,后期可以在此扩展改变
                    Collections.sort(killGoodsSkuOuts, new Comparator<SecondsKillGoodsSkuOut>() {
                        public int compare(SecondsKillGoodsSkuOut o1, SecondsKillGoodsSkuOut o2) {
                            if (o1.getCreateTime().before(o2.getCreateTime())) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                }
            }
        }

        return killGoodsSkuOuts;
    }


    /**
     * 商品新人注册成功后修改缓存人数
     * @param inVo
     * @return
     */
    @Override
    public SecondsKillGoodsSkuOut updatePeopleNumNew(SkuForNewUserInVo inVo) {
        Date time = new Date();
        SecondsKillGoodsSkuOut secondsKillGoodsSkuOut=null;
        String valueKey="ActId_"+inVo.getActId()+"MsGoodsSku_"+"sku_id_"+inVo.getSkuId()+"parent_id_"+inVo.getParentId();
        String totleKey="ActId_"+inVo.getActId()+"MsGoodsSku_"+"parent_id_"+inVo.getParentId();
        String hget = (String)redisCache.hget(totleKey,valueKey);
        if (hget==null||hget.length()<1){
            return secondsKillGoodsSkuOut;
        }
        secondsKillGoodsSkuOut=JSON.parseObject(hget, SecondsKillGoodsSkuOut.class);
        if(time.getTime()>=secondsKillGoodsSkuOut.getEndTime().getTime()){
            redisCache.hdel(totleKey,valueKey);//删除超时秒杀
        }else{
            if (secondsKillGoodsSkuOut.getNewUser()!=null){
                for (User user:secondsKillGoodsSkuOut.getNewUser()){
                    if (user.getId()==inVo.getNewUser()){
                        return null;
                    }
                }
            }
            if (secondsKillGoodsSkuOut.getPeoPleNum()==null||secondsKillGoodsSkuOut.getPeoPleNum()==0){
                secondsKillGoodsSkuOut.setPeoPleNum(1);
            }else {
                secondsKillGoodsSkuOut.setPeoPleNum(secondsKillGoodsSkuOut.getPeoPleNum()+1);
            }
            User user=userMapper.selectByPrimaryKey(inVo.getNewUser());
            List<User> newUser=secondsKillGoodsSkuOut.getNewUser();
            if (newUser==null){
                newUser= new ArrayList<User>();
                newUser.add(user);
                secondsKillGoodsSkuOut.setNewUser(newUser);
            }else {
                newUser.add(user);
                secondsKillGoodsSkuOut.setNewUser(newUser);
            }
            secondsKillGoodsSkuOut.setTotleKey(totleKey);
            secondsKillGoodsSkuOut.setValueKey(valueKey);
            redisCache.hset(totleKey, valueKey, JSON.toJSONString(secondsKillGoodsSkuOut));
            return secondsKillGoodsSkuOut;
        }
        return  secondsKillGoodsSkuOut;
    }

    /*
    * 创建缓存信息*/
    private SecondsKillGoodsSkuOut createSecondsSKill(SkuForNewUserInVo inVo){
        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);

        SecondsKillGoodsSkuOut secondsKillGoodsSkuOut =new SecondsKillGoodsSkuOut();
        secondsKillGoodsSkuOut.setBeginTime(nowDate);
        secondsKillGoodsSkuOut.setEndTime(endtime);
        secondsKillGoodsSkuOut.setCreateTime(nowDate);
        //添加商品信息
        this.insertGoodsSku(secondsKillGoodsSkuOut, inVo);
        //添加商家信息
        this.insertShopInfo(secondsKillGoodsSkuOut,inVo);
        return  secondsKillGoodsSkuOut;
    }

    /*添加商品信息*/
    private void insertGoodsSku(SecondsKillGoodsSkuOut secondsKillGoodsSkuOut,SkuForNewUserInVo inVo){
        GoodsSkuOut goodsSkuOut = goodsSkuMapper.selectDetailBySkuId(inVo.getSkuId());
        secondsKillGoodsSkuOut.setGoodsSkuOut(goodsSkuOut);
    }

    /* 添加商家信息*/
    private void insertShopInfo(SecondsKillGoodsSkuOut secondsKillGoodsSkuOut,SkuForNewUserInVo inVo){
        Shop shop = shopMapper.selectByPrimaryKey(inVo.getShopId());
        secondsKillGoodsSkuOut.setShop(shop);
    }

    /**
     * 查看对一道菜邀请的新人信息
     * @param inVo
     * @return
     */
    @Override
    public List<SkuForNewUserOut> getNewUser(SkuForNewUserInVo inVo) {
        List<SkuForNewUserOut> logOuts=new ArrayList<SkuForNewUserOut>();
        String key=null;
        if (inVo.getSkuId()!=null){
            key="Qgc_"+"skuId_"+inVo.getSkuId()+"_parentId_"+inVo.getParentId();
            SkuForNewUserOut newUserOut=redisCache.get(key, SkuForNewUserOut.class);
            logOuts.add(newUserOut);
            return logOuts;
        }else {
            key="Qgc_"+"skuId_"+"*"+"_parentId_"+inVo.getParentId();
        }
        //获取缓存
        Set keyValue=redisCache.selAll(key);
        if (keyValue==null){
            return null;
        }
        Object[] keylist=keyValue.toArray();

        //添加菜信息
        for (Object value:keylist){
            SkuForNewUserOut newUserOut=redisCache.get((String)value, SkuForNewUserOut.class);
            Sku sku = skuMapper.selectByskuId(newUserOut.getSkuId());
            newUserOut.setSkuName(sku.getSkuName());
            newUserOut.setSkuPic(sku.getPicUrl());
            if (newUserOut!=null){
                logOuts.add(newUserOut);
            }
        }

        //目前先按照创建时间来排序,后期可以在此扩展改变
        Collections.sort(logOuts, new Comparator<SkuForNewUserOut>() {
            public int compare(SkuForNewUserOut o1, SkuForNewUserOut o2) {
                if (o1.getCreateTime().before(o2.getCreateTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return logOuts;
    }

    @Override
    public Integer batchUpdate(User user) {

            try {
                User user1 = new User();
                user1.setId(user.getId());
                user1.setUserName(GetUserInfoUtil.getTelPhoneNew());
                user1.setMobile(user1.getUserName());
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user1.setPassword(user1.getUserName() + salt);//密码为userName + salt
                user1.setPassword(encoder.encode(user1.getPassword()));
                user1.setCreateTime(GetUserInfoUtil.changeNightTime(user.getCreateTime()));
                user1.setUpdateTime(GetUserInfoUtil.changeNightTime(user.getCreateTime()));
                userMapper.updateByPrimaryKeySelective(user1);
                UserAccount userAccount = new UserAccount();
                userAccount.setUserId(user.getId());
                userAccount.setUserName(user1.getUserName());
                userAccount.setAccountName(user1.getUserName());
                userAccount.setCreateTime(GetUserInfoUtil.changeNightTime(user.getCreateTime()));
                userAccount.setUpdateTime(GetUserInfoUtil.changeNightTime(user.getCreateTime()));
                userAccountMapper.batchUpdateByPrimaryKeySelective(userAccount);
            }catch (Exception e){
                e.printStackTrace();
            }
        return null;
    }

    @Override
    public List<User> batchList(UserInVo inVo) {
        List<User> users = userMapper.batchList(inVo);
        return users;
    }

    /**
     * 通过code获取用户信息
     * @param code
     * @return
     */
    @Override
    public UserOut wXUserInfo(String code,String ip) {
        //获取token
        WeiXinOauth weiXinOauth=weiXinUserLoginService.getAccessToken(code);
        UserOut userOut = new UserOut();
        if (weiXinOauth!=null){
            if (weiXinOauth.getErrcode()==null){
                //获取用户opind和unionid
                userOut = weiXinUserLoginService.getUserInfo(weiXinOauth.getAccessToken(),weiXinOauth.getOpenid(),"zh-CN");
                User byUnionId = userMapper.findByUnionId(userOut.getUnionId());
                User user = userMapper.findByOpenId(userOut.getOpenId());
                //判断是否是新用户
                 /*
            通过unionid查询唯一用户，如果没有查到再查询openId，如果也没有查到证明是新用户或者是仅仅通过手机号在商家端里面注册的用户，直接插入数据即可,
            如果通过unionid没查到，通过openId查到了，则证明是之前的老用户，没有unionId，则更新用户，返回userId给前端,
            如果通过unionid查到了用户，则里面必有openId,再比较其中的openId，如果openId相等的话，则不用更新用户，不相等的话更新用户(更新openId)
            */
                if (byUnionId==null||byUnionId.getId()==null){
                        if(user!=null||user.getId()!=null){
                            //更新unionid
                            user.setUnionId(userOut.getUnionId());
                            this.update(user);
                            BeanUtils.copyProperties(user, userOut);
                            return userOut;
                        }else {
                            //注册新用户
                            User userNew = new User();
                            userNew.setOpenId(userOut.getOpenId());
                            userNew.setUnionId(userOut.getUnionId());
                            userNew.setUserIp(ip);
                            userNew.setNickName(userOut.getNickName());
                            Date date = new Date();
                            userNew.setIconUrl(userOut.getIconUrl());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
                            userNew.setUserName("xq_" + sdf.format(date));
                            userNew.setSourceType(2);  //来源小程序
                            userNew.setSex(userOut.getSex());
                            Long id  = this.add(userNew);
                            User userById = null;
                            if(id!=null) {
                                userById = this.getUserById(id);
                            }
                            BeanUtils.copyProperties(userById, userOut);
                            return userOut;
                        }
                }else if(byUnionId!=null){
                    if(!StringUtils.equals(byUnionId.getOpenId(),userOut.getOpenId())){
                        //更新openid
                        byUnionId.setOpenId(userOut.getOpenId());
                        this.update(byUnionId);
                        BeanUtils.copyProperties(byUnionId, userOut);
                        return userOut;
                    }
                    BeanUtils.copyProperties(byUnionId, userOut);
                    return userOut;
                }
                return userOut;
            }
            userOut.setErrcode(weiXinOauth.getErrcode());
        }
        return userOut;
    }

    /**
     * 删除用户
     * @param user
     * @return
     */
    @Override
    public Integer deleteUserInfo(User user) {

        return null;
    }

    @Override
    public User queryByUserNameAndPassWord(String userName, String passWord) {
        return userMapper.selectByUserNameAndPassword(userName,passWord);
    }

    @Override
    public Integer updatePassWord(UpdatePassWordInVo command) {
        User user = userMapper.loadUserByUserName(command.getUserName());
        if(user==null){
            return -1;
        }
        if(command.getType()==2){
            String key = "redisSecurityCode" + command.getMobile();
            SmsOut smsOut = redisCache.get(key, SmsOut.class);
            SmsSendInVo smsSendInVo = new SmsSendInVo();
            smsSendInVo.setUserId(user.getId());
            smsSendInVo.setSmsType(SmsSend.SMS_TYPE_FORGET);
            smsSendInVo.setShopMobile(command.getMobile());
            SmsSend smsSend = smsSendMapper.selectByMobile(smsSendInVo);
            if(smsSend==null||smsSend.getSmsContent()==null){
                return -2;
            }
            if(org.apache.commons.lang3.StringUtils.equals(command.getCode(), smsSend.getSmsContent())){
                //删除缓存 和 数据库信息  使短信只有一次有效
                redisCache.del(key);
            }else{
                return -2;
            }

            if (smsOut == null) {
                return -3;
            }

        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUpdateTime(new Date());
        updateUser.setPassword(command.getPassword());
        userMapper.updateByPrimaryKeySelective(updateUser);
        return 1;
    }

    /**
     * 创建缓存对象
     * @param inVo
     * @return
     */
    public SkuForNewUserOut createNewRedis(SkuForNewUserInVo inVo){
        //生成失效时间
        Long outdate= new Long(60*60*1000);
        Date nowDate=new Date();
        Date endtime=new Date(nowDate.getTime()+outdate);

        SkuForNewUserOut skuForNewUserOut =new SkuForNewUserOut();
        skuForNewUserOut.setBeginTime(nowDate);
        skuForNewUserOut.setEndTime(endtime);
        skuForNewUserOut.setCreateTime(inVo.getCreateTime());
        skuForNewUserOut.setSkuId(inVo.getSkuId());
        skuForNewUserOut.setParentId(inVo.getParentId());
        skuForNewUserOut.setShopId(inVo.getShopId());
        return skuForNewUserOut;
    }

    /**
     * 新增用户账户记录
     * @param user
     * @return
     */
    private Integer addUserAccount(User user){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(user.getId());
        userAccount.setUserName(user.getUserName());
        userAccount.setAccountName(user.getMobile());   //暂时把手机号作为用户账号
        userAccount.setAccountType(UserAccount.ACCOUNT_TYPE_XQ);
        userAccount.setAccountAmount(BigDecimal.ZERO);
        userAccount.setAccountStatus(UserAccount.ACCOUNT_STATUS_ACTIVE);
        userAccount.setGold(0);
        userAccount.setPassedAmount(BigDecimal.ZERO);
        userAccount.setFailAmount(BigDecimal.ZERO);
        userAccount.setReviewAmount(BigDecimal.ZERO);
        userAccount.setUserAmount(BigDecimal.ZERO);
        return userAccountMapper.insert(userAccount);
    }

    /**
     * 新增用户账户记录
     * @param user
     * @return
     */
    private Integer batchAddUserAccount(User user){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(user.getId());
        userAccount.setUserName(user.getUserName());
        userAccount.setAccountName(user.getMobile());   //暂时把手机号作为用户账号
        userAccount.setAccountType(UserAccount.ACCOUNT_TYPE_XQ);
        userAccount.setAccountAmount(BigDecimal.ZERO);
        userAccount.setAccountStatus(UserAccount.ACCOUNT_STATUS_ACTIVE);
        userAccount.setGold(0);
        userAccount.setPassedAmount(BigDecimal.ZERO);
        userAccount.setFailAmount(BigDecimal.ZERO);
        userAccount.setReviewAmount(BigDecimal.ZERO);
        userAccount.setUserAmount(BigDecimal.ZERO);
        userAccount.setCreateTime(user.getCreateTime());
        return userAccountMapper.batchInsert(userAccount);
    }


}
