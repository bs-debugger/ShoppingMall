package com.xq.live.web.controllerForApp;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.*;
import com.xq.live.model.User;
import com.xq.live.service.AccessLogService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.PayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lipeng on 2017/12/14.
 * 用户信息相关controller
 */

@RestController
@RequestMapping(value = "/app/user")
public class UserForAppController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessLogService accessLogService;

    @Value("${jwt.password.salt}")
    private String salt;

    @Autowired
    private RedisCache redisCache;

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<User> getUserbyId(@PathVariable Long id) {
        User user = user = userService.getUserById(id);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 通过mobile查询用户信息
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/findUserByMobile",method = RequestMethod.GET)
    //@CrossOrigin
    public BaseResp<User> findUserByMobile(String mobile){
        if(mobile==null||"".equals(mobile)){
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
        User byMobile = userService.findByMobile(mobile);
        return new BaseResp<User>(ResultStatus.SUCCESS,byMobile);
    }

    /**
     * 新增用户(小程序)
     * @param code
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> addUser(String code, HttpServletRequest request){
        //获取openId
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<Long>(ResultStatus.error_weixin_user_code_empty);
        }
        //获取openId
        String param = "?grant_type=" + PaymentConfig.GRANT_TYPE + "&appid=" + PaymentConfig.APPID + "&secret=" + PaymentConfig.API_KEY + "&js_code=" + code;
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.GET_OPEN_ID_URL, "GET", param);
        Map<String, String> result = new HashMap<String, String>();
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return new BaseResp<Long>(errcode, jsonObject.getString("errmsg"), null);
            }
            String openId = jsonObject.getString("openid");
            User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }
            user = new User();
            user.setOpenId(openId);
            user.setUserIp(IpUtils.getIpAddr(request));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
            user.setUserName("xq_" + sdf.format(date));
            /*user.setPassword(RandomStringUtil.getRandomCode(6,3));
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));*/
            user.setSourceType(1);  //来源小程序
            Long id  = userService.add(user);
            return new BaseResp<Long>(ResultStatus.SUCCESS, id);
        }
        return new BaseResp<Long>(ResultStatus.FAIL);
    }

    /**
     * 通过商家端注册用户（仅仅是通过手机号注册用户）
     *
     * 注:现在返回的是个user对象
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addShopAppUser",method =RequestMethod.POST )
    public BaseResp<User> addShopAppUser(User user, HttpServletRequest request){
        if(user==null||user.getMobile()==null){
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
        user.setUserIp(IpUtils.getIpAddr(request));
        user.setUserName(user.getMobile());
        /*user.setPassword(RandomStringUtil.getRandomCode(6, 3));
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));*/
        Long add = userService.add(user);
        User re = null;
        if(add!=null){
            re = userService.getUserById(add);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS,re);
    }

    /**
     * 通过商家端app注册用户（用微信登陆）,适合客户端app登陆，但是通过openId和mobile
     *
     * 适用于用openId和mobile来增加用户,
     * 如果通过openId查出数据,这里面的openId查出来的手机号是为空的，让用户输入手机号发送验证码,
     * 如果输入的手机号在user表中存在记录,则更新该记录放入openId，返回该记录id,并删除原来含有openId记录的数据
     * 如果输入的手机号不存在记录，则直接在查出来的openId的记录中放入手机号,返回该记录id,
     * 如果openId查出来的数据含有手机号，则不走此接口，直接通过findByOpenId接口返回，方便以后扩展。
     *
     * 如果openId查不出数据，则用户没有登录过小程序，
     * 如果输入的手机号在user表中存在记录,则更新该记录放入openId，返回该记录id,
     * 如果输入的手机号不存在记录，则直接插入一条数据
     *
     * 注:现在返回的是个user对象
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAppUser",method =RequestMethod.POST )
    public BaseResp<User> addAppUser(User user, HttpServletRequest request){
        //为了保证，线上正常，此地方的unionId不显示的传，但是是要必传的
        if(user==null||user.getMobile()==null||user.getOpenId()==null){
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
        user.setUserIp(IpUtils.getIpAddr(request));
        user.setUserName(user.getMobile());
        /*user.setPassword(RandomStringUtil.getRandomCode(6, 3));
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));*/
        Long add = userService.addAppUser(user);
        User re = null;
        if(add!=null){
            re = userService.getUserById(add);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS,re);
    }


    /**
     * 通过shopId和mobile添加核销员
     * @param user
     * @return
     */
    @RequestMapping(value = "/addHxUser",method = RequestMethod.POST)
    public BaseResp<Integer> addHxUser(User user){
         if(user==null||user.getShopId()==null||user.getMobile()==null){
             return new BaseResp<Integer>(ResultStatus.error_param_empty);
         }
        User byMobile = userService.findByMobile(user.getMobile());
        if(byMobile==null){
            return new BaseResp<Integer>(ResultStatus.error_para_user_empty);
        }
        byMobile.setShopId(user.getShopId());
        byMobile.setUserType(User.USER_TYPE_SJ);//更改状态为商家状态
        Integer res = userService.updateByMobile(byMobile);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,res);
    }

    /**
     * 通过shopId和mobile删除核销员
     * @param user
     * @return
     */
    @RequestMapping(value = "/deleteHxUser",method = RequestMethod.POST)
    public BaseResp<Integer> deleteHxUser(User user){
        if(user==null||user.getShopId()==null||user.getMobile()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        User byMobile = userService.findByMobile(user.getMobile());
        if(byMobile==null){
            return new BaseResp<Integer>(ResultStatus.error_para_user_empty);
        }
        byMobile.setUserType(User.USER_TYPE_PT);//更改状态为普通用户
        byMobile.setShopId(0L);
        Integer res = userService.updateByMobile(byMobile);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,res);
    }

    /**
     * 查询用户列表信息
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public BaseResp<Pager<User>> userList(UserInVo inVo){
        Pager<User> result = userService.list(inVo);
        return new BaseResp<Pager<User>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据shopId查询核销员列表
     * @return
     */
    @RequestMapping("/listForShopId")
    public BaseResp<List<User>> listForShopId(UserInVo inVo){
        if(inVo==null||inVo.getShopId()==null){
            return new BaseResp<List<User>>(ResultStatus.error_param_empty);
        }
        List<User> result = userService.listForShopId(inVo);
        return new BaseResp<List<User>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 该方法是注册用户的方法，默认放开访问控制
     * @param in
     */
//    @PostMapping("/signup")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public BaseResp<Long> signUp(User in) {
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<Long>(ResultStatus.error_user_exist);
        }
        User user = userService.findByUsername(in.getUserName());
        if(user != null){
            return new BaseResp<Long>(ResultStatus.error_para_user_empty);
        }
//        String pwd = bCryptPasswordEncoder.encode(in.getPassword());
        in.setPassword(RandomStringUtil.getRandomCode(6,3));
        in.setPassword(DigestUtils.md5DigestAsHex(in.getPassword().getBytes()));
        Long id  = userService.add(in);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    @RequestMapping(value = "/findUserByName/{userName}", method = RequestMethod.GET)
    public BaseResp<User> findUserByName(@PathVariable("userName") String userName){
        User user = userService.findByUsername(userName);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 根据openId查询用户信息
     * @param openId
     * @return
     */
    @RequestMapping(value = "/findByOpenId/{openId}", method = RequestMethod.GET)
    public BaseResp<User> findByOpenId(@PathVariable("openId") String openId){
        User user = userService.findByOpenId(openId);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 根据openId和unionId查询用户信息
     * @param openId
     * @return
     */
    @RequestMapping(value = "/findByOpIdAndUnId", method = RequestMethod.GET)
    public BaseResp<User> findByOpIdAndUnId(String openId,String unionId){
        User byUnionId = userService.findByUnionId(unionId);
        User user = userService.findByOpenId(openId);
        User userById = new User();
        if(byUnionId==null){
            if(user!=null){
                user.setUnionId(unionId);
                Integer update = userService.update(user);
                userById = userService.getUserById(user.getId());
                return new BaseResp<User>(ResultStatus.SUCCESS,userById);
            }
        }else if(byUnionId!=null){
            if(!StringUtils.equals(byUnionId.getOpenId(),openId)){
                byUnionId.setOpenId(openId);
                Integer update = userService.update(byUnionId);
                userById = userService.getUserById(byUnionId.getId());
                return new BaseResp<User>(ResultStatus.SUCCESS,userById);
            }
            userById = byUnionId;
        }
        return new BaseResp<User>(ResultStatus.SUCCESS, userById);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(User user){
        if(user == null || user.getOpenId() == null){
            return new BaseResp<Integer>(ResultStatus.error_param_open_id_empty);
        }
        if(user.getId() == null){
            return new BaseResp<Integer>(ResultStatus.error_input_user_id);
        }

        User u = userService.findByOpenId(user.getOpenId());
        if(u == null){
            return new BaseResp<Integer>(ResultStatus.error_param_open_id);
        }
        user.setUserName(u.getUserName());
        //删除不要的缓存----begin
        String[] k = new String[2];
        k[0] = "login_username_" + user.getUserName();
        k[1] = user.getUserName();
        redisCache.del(k);
        //end
        Integer result = userService.updateByOpenId(user);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 通过手机号来修改用户信息(不需要放入到/app/auth/**里面)
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateByMobile",method = RequestMethod.POST)
    public BaseResp<Integer> updateByMobile(User user){
        if(user==null||user.getMobile()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_mobile_empty);
        }

        User byMobile = userService.findByMobile(user.getMobile());//通过mobile查询数据，则userName必定与mobile相同
        if(byMobile==null){
            return new BaseResp<>(ResultStatus.error_para_user_empty);
        }
        //删除不要的缓存----begin
        String[] k = new String[2];
        k[0] = "login_username_" + byMobile.getUserName();
        k[1] = byMobile.getUserName();
        redisCache.del(k);
        //end
        user.setId(byMobile.getId());//为了能正确的清空缓存，带上id
        user.setUserName(byMobile.getUserName());
        Integer integer = userService.updateByMobile(user);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,integer);
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @ResponseBody
    public BaseResp<List<User>> top(UserInVo inVo){
        List<User> result = userService.top(inVo);
        return new BaseResp<List<User>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 登录身份验证
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResp<User> login(UserInVo inVo, HttpServletRequest request){
        if(inVo == null || inVo.getUserName() == null || inVo.getPassword() == null){
            return new BaseResp<User>(ResultStatus.error_para_user_empty);
        }
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        inVo.setPassword(inVo.getUserName()+salt);//密码为userName + salt
        inVo.setPassword(encoder.encode(inVo.getPassword()));
        //end
        User user = userService.findByUserNameAndPwd(inVo);
        if(user != null){
            //更新登录时间，更新登录次数
            user.setUserIp(IpUtils.getIpAddr(request));
            user.setLoginTimes(user.getLoginTimes() + 1);   //登录次数+1
            userService.updateLoginInfo(user);
        }else{
            return new BaseResp<User>(ResultStatus.error_para_user_login);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }
}
