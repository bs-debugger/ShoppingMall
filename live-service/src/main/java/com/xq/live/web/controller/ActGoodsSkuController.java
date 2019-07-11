package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.GoldConfig;
import com.xq.live.model.ActLotteryCategory;
import com.xq.live.model.OrderInfo;
import com.xq.live.model.PullUser;
import com.xq.live.model.User;
import com.xq.live.service.ActGoodsSkuService;
import com.xq.live.service.PullUserService;
import com.xq.live.vo.in.ActGoodsSkuInVo;
import com.xq.live.vo.in.ActLotteryCategoryInVo;
import com.xq.live.vo.out.ActGoodsSkuOut;
import com.xq.live.vo.out.PullUserOut;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.UserContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * Created by ss on 2018/11/3.
 * 参与活动商品
 */
@RestController
@RequestMapping("/actGoodsSku")
public class ActGoodsSkuController {

    @Autowired
    private ActGoodsSkuService actGoodsSkuService;

    @Autowired
    private PullUserService pullUserService;

    @Autowired
    private GoldConfig goldConfig;

    /**
     * 查询参加活动的商品类目列表
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getSpuList", method = RequestMethod.GET)
    public BaseResp<List<ActGoodsSkuOut>> top(ActGoodsSkuInVo inVo){
        if(inVo.getActId()==null){
            return new BaseResp<List<ActGoodsSkuOut>>(-1,"ActId必填", null);
        }
        List<ActGoodsSkuOut> result = actGoodsSkuService.selectByGroupSpu(inVo);
        return new BaseResp<List<ActGoodsSkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 活动奖池
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/jackpotList", method = RequestMethod.GET)
    public BaseResp<Pager<ActLotteryCategory>> top(ActLotteryCategoryInVo inVo){
        if(inVo.getActId()==null){
            return new BaseResp<Pager<ActLotteryCategory>>(-1,"ActId必填", null);
        }
        Pager<ActLotteryCategory> result = actGoodsSkuService.ActLotteryList(inVo);
        return new BaseResp<Pager<ActLotteryCategory>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 通过订单查询参加活动的团
     *
     * @param inVo
     * @return
     */
    @ApiOperation(value = "查询活动订单相关",notes = "通过订单查询参加活动的团")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/getActSkuListAndOrder", method = RequestMethod.GET)
    public BaseResp<ActGoodsSkuOut> getActSkuListAndOrder(OrderInfo inVo){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_param_empty);
        }
        inVo.setUserId(user.getId());
        if(inVo.getId()==null){
            return new BaseResp<ActGoodsSkuOut>(-1,"订单id必填", null);
        }
        ActGoodsSkuOut result = actGoodsSkuService.getActSkuListAndOrder(inVo);
        return new BaseResp<ActGoodsSkuOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 抽奖
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/lottery", method = RequestMethod.GET)
    public BaseResp<ActGoodsSkuOut> lottery(ActGoodsSkuInVo inVo,HttpServletRequest request) throws ParseException {
        if(inVo.getActId()==null||inVo.getType()==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_param_empty);
        }
        Long userId=user.getId();
        String userName=user.getUserName();
        String userIp=IpUtils.getIpAddr(request);
        PullUser pullUser= new PullUser();
        pullUser.setUserId(userId);
        pullUser.setType(inVo.getType());
        PullUserOut pullUserOut =pullUserService.getForUserList(pullUser);
         BeanUtils.copyProperties(pullUserOut, pullUser);
        if(pullUserOut==null||pullUserOut.getHaveNum()==null||pullUserOut.getPullNum()<goldConfig.getLynum()){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_lottery_hasNumber);
        }

        ActGoodsSkuOut result=actGoodsSkuService.lottery(inVo, userId, userName, userIp, pullUser);
        if(result==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_lottery_fail);
        }
        return new BaseResp<ActGoodsSkuOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 专区活动抽奖
     *
     * @param inVo
     * @return
     */
    @ApiOperation(value = "专区活动抽奖",notes = "根据活动id专区活动抽奖")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actId", value = "活动id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, dataType = "string")
    })
    @RequestMapping(value = "/zoneLottery", method = RequestMethod.POST)
    public BaseResp<ActGoodsSkuOut> zoneLottery(ActGoodsSkuInVo inVo,HttpServletRequest request) throws ParseException {
        if(inVo.getActId()==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_param_empty);
        }
        //inVo.setActId(new Long(42));//抽奖活动
        Long userId=user.getId();
        String userName=user.getUserName();
        String userIp=IpUtils.getIpAddr(request);

        ActGoodsSkuOut result=actGoodsSkuService.zoneLottery(inVo, userId, userName, userIp);
        if(result==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_order_lottery_THANKS);
        }
        return new BaseResp<ActGoodsSkuOut>(ResultStatus.SUCCESS, result);
    }

    /**
     * 长期抽奖-邀请新人-核销-支付运费
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/aloneLottery", method = RequestMethod.POST)
    public BaseResp<ActGoodsSkuOut> aloneLottery(ActGoodsSkuInVo inVo,HttpServletRequest request) throws ParseException {
        if(inVo.getActId()==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_param_empty);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_param_empty);
        }
        //inVo.setActId(new Long(42));//抽奖活动
        Long userId=user.getId();
        String userName=user.getUserName();
        String userIp=IpUtils.getIpAddr(request);

        ActGoodsSkuOut result=actGoodsSkuService.aloneLottery(inVo, userId, userName, userIp);
        if(result==null){
            return new BaseResp<ActGoodsSkuOut>(ResultStatus.error_order_lottery_THANKS);
        }
        return new BaseResp<ActGoodsSkuOut>(ResultStatus.SUCCESS, result);
    }


    /*修改排序*/
    @RequestMapping(value = "/updateSku",method = RequestMethod.GET)
    public BaseResp<Integer> updateSku(){
        Integer re = actGoodsSkuService.updateSku();
        return new BaseResp<Integer>(ResultStatus.SUCCESS,re);
    }

}
