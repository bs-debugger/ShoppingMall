package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.model.UserOpinion;
import com.xq.live.service.UserOpinionService;
import com.xq.live.vo.in.UserOpinionInVo;
import com.xq.live.vo.out.UserOpinionOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ss on 2018/10/24.
 * 用户提交意见
 */
@RestController
@RequestMapping(value = "/website/useropinion")
public class UserOpinionForWebController {
    @Autowired
    private UserOpinionService userOpinionService;


    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<UserOpinionOut>> list(UserOpinionInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Pager<UserOpinionOut>>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Pager<UserOpinionOut> result = userOpinionService.listForUser(inVo);
        return new BaseResp<Pager<UserOpinionOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询单条数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/getOpinion", method = RequestMethod.GET)
    public BaseResp<UserOpinion> getOpinion(Long id){
        UserOpinion userOpinion = userOpinionService.selectByPrimaryKey(id);
        return new BaseResp<UserOpinion>(ResultStatus.SUCCESS, userOpinion);
    }

    /**
     * 新增用户意见
     * @param inVo
     * @return
     * localhost:8080/useropinion/add
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(UserOpinionInVo inVo){
        if (inVo==null||inVo.getContent()==null||inVo.getPicIds()==null||inVo.getPhone()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        Long opinion = userOpinionService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, opinion);
    }

    /**
     * 逻辑删除一条意见记录
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/delete",  method = RequestMethod.POST)
    public BaseResp<Integer>  delete(UserOpinionInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setIsDeleted(UserOpinion.OPINION_IS_DELETED);
        int res = userOpinionService.delete(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,res);
    }

}
