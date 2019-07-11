package com.xq.live.web.controllerForWeb;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.*;
import com.xq.live.model.SmsSend;
import com.xq.live.model.User;
import com.xq.live.model.UserBlacklist;
import com.xq.live.service.SmsSendService;
import com.xq.live.service.UserBlacklistService;
import com.xq.live.service.UserService;
import com.xq.live.service.WeiXinUserLoginService;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.vo.in.WeixinPhoneInvo;
import com.xq.live.vo.out.SmsOut;
import com.xq.live.vo.out.UserOut;
import com.xq.live.web.utils.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @package: com.xq.live.web.controller
 * @description: 授权controller
 * @author: zhangpeng32
 * @date: 2018/4/20 15:55
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/webApprove/auth")
public class AuthForWebController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserBlacklistService userBlacklistService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private WeiXinUserLoginService weiXinUserLoginService;

    @Value("${jwt.password.salt}")
    private String salt;

    /**
     * 该方法是注册用户的方法，默认放开访问控制
     * @param in
     */
    @PostMapping("/signup")
    @ResponseBody
    public BaseResp<Long> signup(User in) throws AuthenticationException{
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<Long>(ResultStatus.error_user_exist);
        }
        User user = userService.findByUsername(in.getUserName());
        if(user != null){
            return new BaseResp<Long>(ResultStatus.error_user_exist);
        }
        Long id  = userService.register(in);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    @RequestMapping(value="/h5",method= RequestMethod.GET)
    public String getHost(Model model){
        return "h5";
    }

    @RequestMapping(value="/actdic",method= RequestMethod.GET)
    public String getActDic(Model model){
        return "actdic";
    }



    /**
     * 登录认证
     * @param in
     * @return
     * @throws AuthenticationException
     */
    @PostMapping("/login")
    @ResponseBody
    public BaseResp<?> login(User in) throws AuthenticationException{
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<String>(ResultStatus.error_para_user_empty);
        }
        User user = userService.findByUsername(in.getUserName());
        if(user == null){
            throw new BadCredentialsException(in.getUserName());
        }
        UserBlacklist userBlacklist = userBlacklistService.selectUserByUserName(in.getUserName());
        if (userBlacklist!=null){
            return new BaseResp<String>(ResultStatus.FAIL,"您暂时无法使用该应用,如有疑问请联系客服!");
        }
        userService.update(user);
        String token = userService.login(in.getUserName(), in.getPassword());//这里的password现在没用了，默认用userName + salt
        return new BaseResp<String>(ResultStatus.SUCCESS, token);
    }

    /**
     * 刷新密钥
     *
     * @param authorization 原密钥
     * @return 新密钥
     * @throws AuthenticationException 错误信息
     */
    @GetMapping(value = "/refresh")
    @ResponseBody
    public BaseResp<?> refreshToken(@RequestHeader String authorization) throws AuthenticationException {
        String result =  userService.refreshToken(authorization);
        return new BaseResp<String>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<User> getUserbyId(@PathVariable Long id) {
        User user = user = userService.getUserById(id);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 根据微信code查询用户信息
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/wXUserInfo", method = RequestMethod.GET)
    public BaseResp<UserOut> wXUserInfo(String code,HttpServletRequest request) {
        String ip= IpUtils.getIpAddr(request);
        UserOut userOut = userService.wXUserInfo(code,ip);
        if (userOut!=null&&userOut.getErrcode()!=null){
            return new BaseResp<UserOut>(ResultStatus.FAIL,userOut);
        }
        return new BaseResp<UserOut>(ResultStatus.SUCCESS,userOut);
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
        Long add = userService.add(user);
        User re = null;
        if(add!=null){
            re = userService.getUserById(add);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS,re);
    }


    /**
     * 新增用户
     * @param code
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<User> addUser(String code,HttpServletRequest request){
        //获取openId
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<User>(ResultStatus.error_weixin_user_code_empty);
        }

        /*if(StringUtils.isEmpty(unionId)){
            return new BaseResp<Long>(ResultStatus.error_weixin_user_unionid_empty);
        }*/
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
                return new BaseResp<User>(errcode, jsonObject.getString("errmsg"), null);
            }
            String openId = jsonObject.getString("openid");
            String unionId = jsonObject.getString("unionid");
            /*
            通过unionid查询唯一用户，如果没有查到再查询openId，如果也没有查到证明是新用户或者是仅仅通过手机号在商家端里面注册的用户，直接插入数据即可,
            如果通过unionid没查到，通过openId查到了，则证明是之前的老用户，没有unionId，则更新用户，返回userId给前端,
            如果通过unionid查到了用户，则里面必有openId,再比较其中的openId，如果openId相等的话，则不用更新用户，不相等的话更新用户(更新openId)
            */
            User byUnionId = userService.findByUnionId(unionId);
            User user = userService.findByOpenId(openId);
            if(byUnionId==null){
                if(user!=null){
                    user.setUnionId(unionId);
                    Integer update = userService.update(user);
                    User userById = userService.getUserById(user.getId());
                    return new BaseResp<User>(ResultStatus.error_user_exist,userById);
                }
            }else if(byUnionId!=null){
                if(!StringUtils.equals(byUnionId.getOpenId(),openId)){
                    byUnionId.setOpenId(openId);
                    Integer update = userService.update(byUnionId);
                    User userById = userService.getUserById(byUnionId.getId());
                    return new BaseResp<User>(ResultStatus.error_user_exist,userById);
                }
                return new BaseResp<User>(ResultStatus.error_user_exist,byUnionId);
            }

            /*User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }*/
            User userNew = new User();
            userNew.setOpenId(openId);
            userNew.setUnionId(unionId);
            userNew.setUserIp(IpUtils.getIpAddr(request));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
            userNew.setUserName("xq_" + sdf.format(date));
            /*userNew.setPassword(RandomStringUtil.getRandomCode(6,3));
            userNew.setPassword(DigestUtils.md5DigestAsHex(userNew.getPassword().getBytes()));*/
            userNew.setSourceType(1);  //来源小程序
            Long id  = userService.add(userNew);
            User userById = null;
            if(id!=null) {
                 userById = userService.getUserById(id);
            }
            return new BaseResp<User>(ResultStatus.SUCCESS, userById);
        }
        return new BaseResp<User>(ResultStatus.FAIL);
    }

    /**
     * 通过openId和unionId新增用户
     * @param openId
     * @param unionId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUserUnionId", method = RequestMethod.POST)
    public BaseResp<User> addUserUnionId(String openId,String unionId,HttpServletRequest request){
        //获取openId
        if (StringUtils.isEmpty(openId)||StringUtils.isEmpty(unionId)) {
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
            /*
            通过unionid查询唯一用户，如果没有查到再查询openId，如果也没有查到证明是新用户或者是仅仅通过手机号在商家端里面注册的用户，直接插入数据即可,
            如果通过unionid没查到，通过openId查到了，则证明是之前的老用户，没有unionId，则更新用户，返回userId给前端,
            如果通过unionid查到了用户，则里面必有openId,再比较其中的openId，如果openId相等的话，则不用更新用户，不相等的话更新用户(更新openId)
            */
        User byUnionId = userService.findByUnionId(unionId);
        User user = userService.findByOpenId(openId);
        if(byUnionId==null){
            if(user!=null){
                user.setUnionId(unionId);
                Integer update = userService.update(user);
                User userById = userService.getUserById(user.getId());
                return new BaseResp<User>(ResultStatus.error_user_exist,userById);
            }
        }else if(byUnionId!=null){
            if(!StringUtils.equals(byUnionId.getOpenId(),openId)){
                byUnionId.setOpenId(openId);
                Integer update = userService.update(byUnionId);
                User userById = userService.getUserById(byUnionId.getId());
                return new BaseResp<User>(ResultStatus.error_user_exist,userById);
            }
            return new BaseResp<User>(ResultStatus.error_user_exist,byUnionId);
        }

            /*User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }*/
        User userNew = new User();
        userNew.setOpenId(openId);
        userNew.setUnionId(unionId);
        userNew.setUserIp(IpUtils.getIpAddr(request));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
        userNew.setUserName("xq_" + sdf.format(date));
            /*userNew.setPassword(RandomStringUtil.getRandomCode(6,3));
            userNew.setPassword(DigestUtils.md5DigestAsHex(userNew.getPassword().getBytes()));*/
        userNew.setSourceType(1);  //来源小程序
        Long id  = userService.add(userNew);
        User userById = null;
        if(id!=null) {
            userById = userService.getUserById(id);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS, userById);
    }



    /**
     * 注意:批量执行的时候,循环条件一定要在controller里面,因为如果放到service里面,使用的是同一个dao,则会一直用一个事物提交,
     * 哪怕你没有写事物注解,但是dao层会把所有批量数据当做一个事物,放到controller之后,@Autowird注解是单例的,每次创建都会new一个新的出来,
     * 这个地方一定要注意,否则在并发量高的时候会引起表锁
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/batchUpdate",method = RequestMethod.POST)
    public BaseResp<Integer> batchUpdate(UserInVo inVo,Date beginTime,Date endTime){
        List<User> list = userService.batchList(inVo);
        for (User user : list) {
            if(beginTime!=null&&endTime!=null) {
                user.setCreateTime(GetUserInfoUtil.randomDate(beginTime, endTime));
            }
            Integer re = userService.batchUpdate(user);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS,null);
    }



    /**
     * 通过encrypData和ivData和sessionKey新增用户
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addUserUnionIdAndPhoneAES", method = RequestMethod.POST)
    public BaseResp<User> addUserUnionIdAndPhoneAES(@Valid WeixinPhoneInvo inVo,BindingResult result,HttpServletRequest request){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<User>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        byte[] encrypData = Base64.decodeBase64(inVo.getEncrypData());
        byte[] ivData = Base64.decodeBase64(inVo.getIvData());
        byte[] sessionKey = Base64.decodeBase64(inVo.getSessionKey());

        String decrypt = null;
        try {
            decrypt = AESDecodeUtils.decrypt(sessionKey, ivData, encrypData);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<User>(ResultStatus.FAIL);
        }
        JSONObject jsonObject = JSONObject.parseObject(decrypt);
        String openId = null;
        String unionId = null;
        if (jsonObject != null) {
            openId = jsonObject.getString("openId");
            unionId = jsonObject.getString("unionId");
        }
        //获取openId
        if (StringUtils.isEmpty(openId)||StringUtils.isEmpty(unionId)) {
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
            /*
            通过unionid查询唯一用户，如果没有查到再查询openId，如果也没有查到证明是新用户或者是仅仅通过手机号在商家端里面注册的用户，直接插入数据即可,
            如果通过unionid没查到，通过openId查到了，则证明是之前的老用户，没有unionId，则更新用户，返回userId给前端,
            如果通过unionid查到了用户，则里面必有openId,再比较其中的openId，如果openId相等的话，则不用更新用户，不相等的话更新用户(更新openId)
            */
        User byUnionId = userService.findByUnionId(unionId);
        User user = userService.findByOpenId(openId);
        if(byUnionId==null){
            if(user!=null){
                user.setUnionId(unionId);
                Integer update = userService.update(user);
                User userById = userService.getUserById(user.getId());
                return new BaseResp<User>(ResultStatus.error_user_exist,userById);
            }
        }else if(byUnionId!=null){
            if(!StringUtils.equals(byUnionId.getOpenId(),openId)){
                byUnionId.setOpenId(openId);
                Integer update = userService.update(byUnionId);
                User userById = userService.getUserById(byUnionId.getId());
                return new BaseResp<User>(ResultStatus.error_user_exist,userById);
            }
            return new BaseResp<User>(ResultStatus.error_user_exist,byUnionId);
        }

            /*User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }*/
        User userNew = new User();
        userNew.setOpenId(openId);
        userNew.setUnionId(unionId);
        userNew.setUserIp(IpUtils.getIpAddr(request));
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
        userNew.setUserName("xq_" + sdf.format(date));
            /*userNew.setPassword(RandomStringUtil.getRandomCode(6,3));
            userNew.setPassword(DigestUtils.md5DigestAsHex(userNew.getPassword().getBytes()));*/
        userNew.setSourceType(1);  //来源小程序
        Long id  = userService.add(userNew);
        User userById = null;
        if(id!=null) {
            userById = userService.getUserById(id);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS, userById);
    }

    /**
     * 通过code获取用户信息
     * @param code
     * @return
     */
    @RequestMapping(value = "/findByCode",method = RequestMethod.GET)
    @ResponseBody
    public BaseResp<User> findByCode(String code){
        //获取openId
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<User>(ResultStatus.error_weixin_user_code_empty);
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
                return new BaseResp<User>(errcode, jsonObject.getString("errmsg"), null);
            }
            String openId = jsonObject.getString("openid");
            User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<User>(ResultStatus.SUCCESS,user);
            }
            user = new User();
            user.setOpenId(openId);
            return new BaseResp<User>(ResultStatus.SUCCESS,user);

        }
        return new BaseResp<User>(ResultStatus.FAIL);
    }

    /**
     * @Description: 获取openId
     * @param: code
     * @Author: zhangpeng32
     * @Date: 2018/3/11 17:39
     * @Version: 1.0.0
     */
    @ResponseBody
    @RequestMapping(value = "/getOpenId", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> getOpenId(String code, HttpServletRequest request) throws Exception{
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_weixin_user_code_empty);
        }
        //获取openId
        String param = "?grant_type=" + PaymentConfig.GRANT_TYPE + "&appid=" + PaymentConfig.APPID + "&secret=" + PaymentConfig.APP_SECRET + "&js_code=" + code;
        System.out.println(PaymentConfig.GET_OPEN_ID_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.GET_OPEN_ID_URL, "GET", param);
        Map<String, String> result = new HashMap<String, String>();
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return new BaseResp<Map<String, String>>(errcode, jsonObject.getString("errmsg"), null);
            }
            result.put("openId", jsonObject.getString("openid"));
            result.put("sessionKey", jsonObject.getString("session_key"));
            User user = userService.findByOpenId(jsonObject.getString("openid"));
            String unionid = jsonObject.getString("unionid");
            if(StringUtils.isNotBlank(unionid)) {
                result.put("unionId", unionid);
            }else{
                if(user!=null){
                    result.put("unionId", user.getUnionId());
                }
            }
        }
        return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, result);
    }

    /**
     *获取微信用户的手机号
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/phoneAES",method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<String> phoneAES(@Valid WeixinPhoneInvo inVo,BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<String>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        byte[] encrypData = Base64.decodeBase64(inVo.getEncrypData());
        byte[] ivData = Base64.decodeBase64(inVo.getIvData());
        byte[] sessionKey = Base64.decodeBase64(inVo.getSessionKey());

        String decrypt = null;
        try {
            decrypt = AESDecodeUtils.decrypt(sessionKey, ivData, encrypData);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<String>(ResultStatus.FAIL,e.getMessage());
        }

        return new BaseResp<String>(ResultStatus.SUCCESS,decrypt);
    }

    /**
     * 用户登录发送验证码
     * @param inVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendForShopAppRegister", method = RequestMethod.POST)
    //@CrossOrigin
    public BaseResp<SmsOut> sendForShopAppRegister(SmsSendInVo inVo, HttpServletResponse response) {
        if(inVo==null||inVo.getShopMobile()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }

        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        SmsOut smsOut = smsSendService.redisVerifyForShopApp(inVo);
        //判断缓存是否存在，并且在10分钟以内
        if(smsOut!=null){
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            String msg = MessageFormat.format(Constants.WINNER_LOOK_REGISTER, randomCode);
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("userCode", Constants.WINNER_LOOK_USER_NAME);
            paramMap.put("userPass", Constants.WINNER_LOOK_PASSWORD);
            paramMap.put("DesNo", inVo.getShopMobile());
            paramMap.put("Msg", msg);
            String s = HttpRequestUtil.httpsPost(Constants.WINNER_LOOK_HTTPS_SEND_MESSAGE_URL, paramMap);
            Document dom= DocumentHelper.parseText(s);
            Element root=dom.getRootElement();
            String ret=root.getText();
            if (ret.length() > 10) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
                inVo.setRemark("OK");
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
                inVo.setRemark(ret);
            }
            inVo.setSmsContent(randomCode);
            Long id = smsSendService.create(inVo);
            SmsOut smsOutForNew = smsSendService.redisVerifyForShopApp(inVo);//再把数据缓存
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS, smsOutForNew);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<SmsOut>(ResultStatus.FAIL);
        }
    }

    /**
     *判断验证码是否正确,并且更新user表,如果user表中含有手机号,先比较手机号是否相同，如果不同则算新加入用户
     * @param inVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isVerifyForShopApp",method = RequestMethod.GET)
    public BaseResp<Integer> isVerifyForShopApp(SmsSendInVo inVo){
        if(inVo==null||inVo.getShopMobile()==null||inVo.getSmsContent()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        Integer verify = smsSendService.isVerifyForShopApp(inVo);
        if(verify==null||verify==-1){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,verify);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS,verify);
    }

    /**
     * 通过mobile查询用户信息,如果查到了用户信息,则直接用userName来完成登陆获取到token
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserByMobile",method = RequestMethod.GET)
    //@CrossOrigin
    public BaseResp<User> findUserByMobile(String mobile){
        if(mobile==null||"".equals(mobile)){
            return new BaseResp<User>(ResultStatus.error_param_empty);
        }
        User byMobile = userService.findByMobile(mobile);
        return new BaseResp<User>(ResultStatus.SUCCESS,byMobile);
    }

}
