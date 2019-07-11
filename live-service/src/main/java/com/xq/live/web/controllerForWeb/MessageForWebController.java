package com.xq.live.web.controllerForWeb;

import com.alibaba.fastjson.JSON;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.WeiXinTeamplateMsg;
import com.xq.live.model.User;
import com.xq.live.service.MessageService;
import com.xq.live.service.UserService;
import com.xq.live.service.WeiXinPushService;
import com.xq.live.vo.in.FormIdInVo;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.in.WeiXinPushInVo;
import com.xq.live.vo.out.MessageOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * @package: com.xq.live.web.controller
 * @description: 站内信controller
 * @author: zhangpeng32
 * @date: 2018/3/28 18:52
 * @version: 1.0
 */
@RestController
@RequestMapping("/website/msg")
public class MessageForWebController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @Autowired
    private WeiXinPushService weiXinPushService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 消息读取状态
     * @param receiverId
     * @return
     */
    @RequestMapping(value = "/msgStatus",method = RequestMethod.GET)
    public BaseResp<Map<String, Object>> msgStatus(Long receiverId){
        return null;
    }

    /**
     * 查询列表
     * @param inVo
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<MessageOut>> list(@Valid MessageInVo inVo, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Pager<MessageOut>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        inVo.setIsDeleted(0);   //查询未删除的消息
        Pager<MessageOut> result = messageService.list(inVo);
        return new BaseResp<Pager<MessageOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询消息详情，并更新记录
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public BaseResp<MessageOut> detail(@Valid MessageInVo inVo, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<MessageOut>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        if(inVo.getMessageTextId() == null){
            return new BaseResp<MessageOut>(ResultStatus.error_param_message_text_id_empty);
        }
        MessageOut res = messageService.detail(inVo);
        return new BaseResp<MessageOut>(ResultStatus.SUCCESS, res);
    }

    /**
     * 删除一条消息
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResp<Integer> delete(@Valid MessageInVo inVo, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        if(inVo.getMessageTextId() == null){
            return new BaseResp<Integer>(ResultStatus.error_param_message_text_id_empty);
        }
        Integer ret = messageService.delete(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * 微信推送
     * @return
     */
    @RequestMapping(value = "/getPush",method = RequestMethod.GET)
    public BaseResp<Integer> getPush(WeiXinPushInVo inVo) throws Exception {
        String page="www.baidu.com";
        String templateId= WeiXinTeamplateMsg.templateId_TYPE;
        String keyWords="第一个,第二个,第三个,第四个,第五个";
        String fromId="1539741992524";
        Long userId= new Long(1400);
        User user =userService.getUserById(inVo.getUserId());
        //User user =userService.getUserById(userId);
        //Integer res =weiXinPushService.push(templateId, page, keyWords, fromId, user);
        Integer res =weiXinPushService.push(inVo.getTemplate_id(), inVo.getPage(), inVo.getKeyWords(), inVo.getForm_id(), user);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,res);
    }

    /**
     * 获取活动列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listAct",method = RequestMethod.GET)
    public BaseResp<Pager<MessageOut>> listAct( MessageInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<MessageOut>>(ResultStatus.error_param_empty);
        }
        inVo.setReceiverId(user.getId());
        Pager<MessageOut> result = messageService.listAct(inVo);
        return new BaseResp<Pager<MessageOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 获取当前用户未读消息数量以及第一条消息
     * @return
     */
    @RequestMapping(value = "/unreadMessageTotal",method = RequestMethod.GET)
    public BaseResp<List<MessageOut>> unreadMessageTotal(MessageInVo inVo ){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<List<MessageOut>>(ResultStatus.error_param_empty);
        }
        inVo.setReceiverId(user.getId());
        List<MessageOut> result=messageService.unreadMessageTotal(inVo);
        return new BaseResp<List<MessageOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 标记所有系统通知消息为已读
     * @return
     */
    @RequestMapping(value = "/readAllNotic",method = RequestMethod.GET)
    public BaseResp<Integer> readAllNotic( ){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        int result=messageService.readAllNotic(user.getId());
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 标记活动通知消息为已读
     * @return
     */
    @RequestMapping(value = "/readAct",method = RequestMethod.GET)
    public BaseResp<Integer> readAct( Long messageTextId){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        MessageInVo inVo=new MessageInVo();
        inVo.setMessageTextId(messageTextId);
        inVo.setReceiverId(user.getId());
        int result=messageService.readAct(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 缓存用户的formId
     * 用于后面发送消息
     * @return
     */
    @RequestMapping(value = "/addFormIdCache",method = RequestMethod.GET)
    public BaseResp<String> addFormIdCache( String formId){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<String>(ResultStatus.error_param_empty);
        }

        List<FormIdInVo> formIdInVos=messageService.getFormIdList(user.getId());
        if(formIdInVos!=null&&formIdInVos.size()>50){
            return new BaseResp<String>(ResultStatus.SUCCESS,"缓存已达到上限");//当前用户已缓存数量超过50条之后不做缓存
        }else{
            FormIdInVo formIdInVo=new FormIdInVo();
            formIdInVo.setFormId(formId);
            formIdInVo.setCerateTime(new Date());
            formIdInVo.setUserId(user.getId());
            redisCache.hset("formId_"+user.getOpenId(), formId, JSON.toJSONString(formIdInVo));
        }

        return new BaseResp<String>(ResultStatus.SUCCESS);
    }

}
