package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.UserConcern;
import com.xq.live.service.UserConcernService;
import com.xq.live.vo.in.UserConcernInVo;
import com.xq.live.vo.out.UserConcernOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户关注
 * Created by lipeng on 2018/7/16.
 */
@RestController
@RequestMapping(value = "/userConcern")
public class UserConcernController {
    @Autowired
    private UserConcernService userConcernService;

    /**
     * 根据主键查询一条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<UserConcern> get(@PathVariable("id") Long id) {
        UserConcern cp = userConcernService.get(id);
        return new BaseResp<UserConcern>(ResultStatus.SUCCESS, cp);
    }


    /**
     * 根据用户id查询关注列表，分页查询
     *注:入参为userId(想要查看哪个用户的userId)和type(关注类型)    ----此是一个用户关注了哪些人
     * 注:入参为type和refId   --------------此是看一个用户被哪些人关注
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<UserConcernOut>> list(UserConcernInVo inVo) {
        Pager<UserConcernOut> result = userConcernService.list(inVo);
        return new BaseResp<Pager<UserConcernOut>>(ResultStatus.SUCCESS, result);
    }


    /**
     * 添加关注
     *入参:userId,type,refId
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> put(@Valid UserConcernInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        UserConcernOut  collected = userConcernService.isCollected(inVo);
        if(collected != null){
            return new BaseResp<Long>(0, "该用户已在关注列表", null);
        }

        Long id = userConcernService.add(inVo);
        if (id == null) {
            return new BaseResp<Long>(ResultStatus.FAIL, id);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }


    /**
     * 取消关注
     *
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(@Valid UserConcernInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Integer res = userConcernService.delete(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, res);
    }

    /**
     * 查询用户个人主页中的关注数目和粉丝数目
     * 注:focus(关注数目)   fans(粉丝数目)   isCollected(是否已关注 0未关注 1关注)
     * 入参:userId(关注人userId),type,viewUserId(当前用户的userId)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/nums",method = RequestMethod.GET)
    public BaseResp<Map<String, Integer>> actVoteNums(UserConcernInVo inVo){
        if(inVo == null||inVo.getUserId() == null||inVo.getType()==null){
            return new BaseResp<Map<String, Integer>>(ResultStatus.error_param_empty);
        }
        Map<String, Integer> map = userConcernService.nums(inVo);
        return new BaseResp<Map<String, Integer>>(ResultStatus.SUCCESS,map);
    }
}
