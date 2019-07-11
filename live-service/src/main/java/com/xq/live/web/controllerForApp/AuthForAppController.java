package com.xq.live.web.controllerForApp;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Constants;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.SmsSend;
import com.xq.live.model.User;
import com.xq.live.model.UserBlacklist;
import com.xq.live.service.SmsSendService;
import com.xq.live.service.UserBlacklistService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.in.UpdatePassWordInVo;
import com.xq.live.vo.out.SmsOut;
import com.xq.live.web.utils.HttpRequestUtil;
import com.xq.live.web.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @package: com.xq.live.web.controllerForApp
 * @description: 授权controller
 * @author: lipeng
 * @date: 2018/8/3 15:55
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/app/auth")
public class AuthForAppController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserBlacklistService userBlacklistService;

    @Autowired
    private SmsSendService smsSendService;

    @RequestMapping(value="/h5",method= RequestMethod.GET)
    public String getHost(Model model){
        return "h5";
    }

    @RequestMapping(value="/actdic",method= RequestMethod.GET)
    public String getActDic(Model model){
        return "actdic";
    }

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
            return new BaseResp<String>(ResultStatus.FAIL,"您暂时无法使用该APP,如有疑问请联系客服!");
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
     * app用户注册发送验证码
     * @param inVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendForRegister", method = RequestMethod.POST)
    public BaseResp<SmsOut> sendForRegister(SmsSendInVo inVo) {

        if(inVo==null||inVo.getShopMobile()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }

        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        SmsOut smsOut = smsSendService.redisVerifyForApp(inVo);
        //判断缓存是否存在，并且在10分钟以内
        if(smsOut!=null){
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            /*SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            params.add(randomCode);
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_VERIFY_SUCCESS, params, "", "", "");*/
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
            SmsOut smsOutForNew = smsSendService.redisVerifyForApp(inVo);//再把数据缓存
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS, smsOutForNew);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<SmsOut>(ResultStatus.FAIL);
        }
    }

    /**
     * 商家端app用户登录发送验证码
     * @param inVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendForShopAppRegister", method = RequestMethod.POST)
    //@CrossOrigin
    public BaseResp<SmsOut> sendForShopAppRegister(SmsSendInVo inVo,HttpServletResponse response) {
        /*response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.addHeader("Access-Control-Max-Age", "1800");*/

        if(inVo==null||inVo.getShopMobile()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }
       /* *//**
         * 发送验证码的手机如果不在user表中，且用户的状态不为商家，则不让登录
         *//*
        User byMobile = userService.findByMobile(inVo.getShopMobile());
        if(byMobile==null||byMobile.getUserType()==1){
            return new BaseResp<SmsOut>(ResultStatus.error_para_cashier_user_type);
        }*/

        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        SmsOut smsOut = smsSendService.redisVerifyForShopApp(inVo);
        //判断缓存是否存在，并且在10分钟以内
        if(smsOut!=null){
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            /*SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            params.add(randomCode);
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_VERIFY_SUCCESS, params, "", "", "");*/
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
     *判断商家验证码是否正确,并且更新user表,如果user表中含有手机号,先比较手机号是否相同，如果不同则算新加入用户
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
            return new BaseResp<Integer>(ResultStatus.error_user_fail,verify);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS,verify);
    }

    /**
     * 根据openId和unionId查询用户信息
     * @param openId
     * @return
     */
    @ResponseBody
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
     * 通过账户密码登录认证
     * @param in
     * @return
     * @throws AuthenticationException
     */
    @PostMapping("/loginByPassword")
    @ResponseBody
    public BaseResp<?> loginByPassword(User in) throws AuthenticationException{
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<String>(ResultStatus.error_para_user_empty);
        }
        User user = userService.queryByUserNameAndPassWord(in.getUserName(),in.getPassword());
        if(user == null){
            throw new RuntimeException("账号或者密码错误");
        }
        UserBlacklist userBlacklist = userBlacklistService.selectUserByUserName(in.getUserName());
        if (userBlacklist!=null){
            return new BaseResp<String>(ResultStatus.FAIL,"您暂时无法使用该APP,如有疑问请联系客服!");
        }
        userService.update(user);
        String token = userService.login(in.getUserName(), in.getPassword());//这里的password现在没用了，默认用userName + salt
        return new BaseResp<String>(ResultStatus.SUCCESS, token);
    }


    /**
     * 通过账户密码登录认证
     * @param in
     * @return
     * @throws AuthenticationException
     */
    @PostMapping("/updatePassWord")
    @ResponseBody
    public BaseResp<Integer> updatePassWord(UpdatePassWordInVo in) throws AuthenticationException{
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<Integer>(ResultStatus.error_para_user_empty);
        }
        User user = userService.findByUsername(in.getUserName());
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        Integer ref = userService.updatePassWord(in);
        if (ref ==-2){
            return new BaseResp<Integer>(ResultStatus.error_user_security_code);
        }else if(ref ==-1){
            return new BaseResp<Integer>(ResultStatus.error_user_update_no);
        }else if(ref ==-3){
            return new BaseResp<Integer>(ResultStatus.error_user_security_EXPIRE);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ref);
    }

    /**
     * 校验当前用户是不是首次修改密码
     * @param in
     * @return
     * @throws AuthenticationException
     */
    @PostMapping("/checkIsFirstUpdatePassWord")
    @ResponseBody
    public BaseResp<Integer> checkIsFirstUpdatePassWord(User in) throws AuthenticationException{
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<Integer>(ResultStatus.error_para_user_empty);
        }
        User user = userService.findByUsername(in.getUserName());
        if(user == null){
            throw new RuntimeException("用户不存在");
        }

        if("91BECDF6F8EC365C5B272D124D0FEAC0".equals(user.getPassword())){
            return new BaseResp<Integer>(ResultStatus.SUCCESS, 1);
        }else{
            return new BaseResp<Integer>(ResultStatus.SUCCESS, -1);
        }
    }
}
