package com.xq.live.web.controllerForWeb;
/**
 * 投票管理类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.model.ActGoodsSku;
import com.xq.live.model.User;
import com.xq.live.model.Vote;
import com.xq.live.service.*;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.in.ActLotteryInVo;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.VoteInVo;
import com.xq.live.vo.out.VoteOut;
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
import java.util.concurrent.TimeUnit;

/**
 * 投票管理类
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 **/
@RestController
@RequestMapping(value = "/website/vote")
public class VoteForWebController {
    @Autowired
    private VoteService voteService;

    @Autowired
    private SoService soService;

    @Autowired
    private CountService countService;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Autowired
    private ActLotteryService actLotteryService;


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
     * 针对新平台活动，判断是否能够投票(传入shopId的时候，sql语句中shop_id is not null,传入playerUserId的时候，sql语句中player_user_id is not null)
     * actId,beginTime,endTime,userId必填。shopId,playerUserIdId至少填一个
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/canVote", method = RequestMethod.GET)
    public BaseResp<Integer> canVote(VoteInVo inVo){
        if(inVo==null||inVo.getBeginTime()==null||inVo.getEndTime()==null||inVo.getActId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        Integer integer = voteService.canVote(inVo);
        if(integer!=null){
            return new BaseResp<Integer>(ResultStatus.error_vote_fail);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    /**
     * 针对新平台活动，判断是否能够领票(只需要传入actId,beginTime,endTime,userId)
     *
     * 注:现在的userId是当前用户，所以需要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/canGetSku", method = RequestMethod.GET)
    public BaseResp<Integer> canGetSku(VoteInVo inVo){
        User user = UserContext.getUserSession();
        inVo.setUserId(user.getId());
        if(inVo==null||inVo.getBeginTime()==null||inVo.getEndTime()==null||inVo.getActId()==null||inVo.getUserId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        Integer integer = voteService.canGetSku(inVo);
        if(integer==null){
            return new BaseResp<Integer>(ResultStatus.error_sku_fail);
        }
        SoInVo soInVo=new SoInVo();
        soInVo.setUserId(inVo.getUserId());
        soInVo.setActId(inVo.getActId());
        soInVo.setSkuId(actSkuConfig.getSkuId());
        //判断用户是否领过卷，0没有，1有过
        int i=soService.hadBeenGiven(soInVo);
        if (i!=0){
            return new BaseResp<Integer>(ResultStatus.error_so_had);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, integer);
    }



    /**
     * 新增一条记录
     *
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
        //如果活动id为37,则每次投票更新投票次数的缓存
        if(vote.getActId().equals(actSkuConfig.getActId())&&vote.getPlayerUserId()!=null){
            String keyUser = "actVoteNumsUser_" + actSkuConfig.getActId() + "_" +vote.getUserId();
            Integer i = redisCache.get(keyUser, Integer.class);
            if(i==null){
                redisCache.set(keyUser,0,1l, TimeUnit.DAYS);
            }else{
                redisCache.set(keyUser,i-1,1l,TimeUnit.DAYS);
            }
        }else if(vote.getActId().equals(actSkuConfig.getActId())&&vote.getSkuId()!=null){
            String keySku  = "actVoteNumsSku_" + actSkuConfig.getActId() + "_" +vote.getUserId();
            Integer i = redisCache.get(keySku, Integer.class);
            if(i==null){
                redisCache.set(keySku,0,1l, TimeUnit.DAYS);
            }else{
                redisCache.set(keySku,i-1,1l,TimeUnit.DAYS);
            }
        }

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

    /**
     * 每日一次免费次数投票，
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/addVoteFree",  method = RequestMethod.GET)
    public BaseResp<Integer>  addVoteFree(VoteInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null||inVo==null||inVo.getActGoodsSkuId()==null||inVo.getActId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        ActGoodsSku actGoodsSku = actGoodsSkuService.selectByPrimaryKey(inVo.getActGoodsSkuId());
        inVo.setUserId(user.getId());
        inVo.setType(Vote.VOTE_TYPE_FREE);
        inVo.setSkuId(actGoodsSku.getSkuId());
        List<Vote> voteList = voteService.getDaysList(inVo);

        if(voteList!=null&&voteList.size()>0){
            return new BaseResp<Integer>(ResultStatus.error_vote_fail);
        }
        inVo.setVoteNum(1);//免费票数为一票
        Long id  = voteService.add(inVo);

        ActGoodsSkuInVo actGoodsSkuInVo=new ActGoodsSkuInVo();
        actGoodsSkuInVo.setId(inVo.getActGoodsSkuId());
        actGoodsSkuInVo.setVoteNum(1);
        actGoodsSkuService.updateVoteNumUp(actGoodsSkuInVo);//商品活动增加一票

        ActLotteryInVo actLotteryInVo=new ActLotteryInVo();
        actLotteryInVo.setUserId(user.getId());
        actLotteryInVo.setActId(inVo.getActId());
        actLotteryInVo.setTotalNumber(1);
        actLotteryService.add(actLotteryInVo);//投票之后增加抽奖次数

        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    /**
     * 判断是否能够投票
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/canVoteToday",  method = RequestMethod.GET)
    public BaseResp<Integer>  canVoteToday(VoteInVo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null||inVo==null||inVo.getActId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        inVo.setType(Vote.VOTE_TYPE_FREE);
        List<Vote> voteList = voteService.getDaysList(inVo);
        if(voteList!=null&&voteList.size()>0){
            return new BaseResp<Integer>(ResultStatus.error_vote_fail);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    /**
     * 获取用户投票排行榜
     * @param
     * @return
     */
    @RequestMapping(value = "/approve/getUserRanking",  method = RequestMethod.GET)
    public BaseResp<Pager<VoteOut>>  getUserRanking(VoteInVo inVo){

        if(inVo.getActId()==null){
            return new BaseResp<Pager<VoteOut>>(ResultStatus.error_param_empty);
        }
        Pager<VoteOut> result= voteService.selectUserRanking(inVo);
        return new BaseResp<Pager<VoteOut>>(ResultStatus.SUCCESS,result);
    }

}
