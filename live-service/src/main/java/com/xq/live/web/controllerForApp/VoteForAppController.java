package com.xq.live.web.controllerForApp;
/**
 * 投票管理类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.model.Vote;
import com.xq.live.service.CountService;
import com.xq.live.service.VoteService;
import com.xq.live.vo.in.VoteInVo;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 投票管理类
 * @author lipeng
 * @create 2018-01-17 19:17
 **/
@RestController
@RequestMapping(value = "/app/vote")
public class VoteForAppController {
    @Autowired
    private VoteService voteService;

    @Autowired
    private CountService countService;

    /**
     * 根据ID查询投票信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<Vote> getVoteById(@PathVariable(value = "id")  Long id){
        Vote vote = voteService.get(id);
        return new BaseResp<Vote>(ResultStatus.SUCCESS, vote);
    }

    /**
     * 新增一条记录
     * 注:现在的userId是当前用户，所以需要从网关中取
     * @param vote
     * @return
     */
    @RequestMapping(value = "/add",  method = RequestMethod.POST)
    public BaseResp<Long>  add(@Valid VoteInVo vote, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        vote.setUserId(user.getId());
        Long id  = voteService.add(vote);
        vote.setType(Vote.VOTE_ADD);
        Integer integer = countService.voteNumsNow(vote);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 取消投票
     *
     * 注:现在的userId是当前用户，所以需要从网关中取
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/delete",  method = RequestMethod.POST)
    public BaseResp<Integer>  delete(@Valid VoteInVo inVo, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        int ret  = voteService.deleteByInVo(inVo);
        inVo.setType(Vote.VOTE_DELETE);
        Integer integer = countService.voteNumsNow(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * 删除一条商家记录
     * @param vote
     * @return
     */
    @RequestMapping(value = "/update",  method = RequestMethod.PUT)
    public int  update(Vote vote){
        return 0;
    }
}
