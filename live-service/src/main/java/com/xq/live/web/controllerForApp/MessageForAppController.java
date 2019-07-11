package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.MessageService;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @package: com.xq.live.web.controller
 * @description: 站内信controller
 * @author: lipeng
 * @date: 2018/3/28 18:52
 * @version: 1.0
 */
@RestController
@RequestMapping("/app/msg")
public class MessageForAppController {
    @Autowired
    private MessageService messageService;

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
}
