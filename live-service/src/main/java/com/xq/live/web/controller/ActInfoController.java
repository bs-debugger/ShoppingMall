package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActInfo;
import com.xq.live.model.User;
import com.xq.live.service.ActInfoService;
import com.xq.live.service.CountService;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.out.ActInfoOut;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台活动controller
 * @author zhangpeng32
 * @create 2018-02-07 17:14
 **/
@RestController
@RequestMapping(value = "/act")
public class ActInfoController {

    @Autowired
    private ActInfoService actInfoService;

    @Autowired
    private CountService countService;

    /**
     * 根据id查询一条活动记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<ActInfo> get(@PathVariable("id") Long id){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<ActInfo>(ResultStatus.error_param_empty);
        }
        ActInfo actInfo = actInfoService.selectOne(id);
        return new BaseResp<ActInfo>(ResultStatus.SUCCESS, actInfo);
    }


    /**
     * 新增记录
     * @param actInfo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> addUser(@Valid ActInfo actInfo, BindingResult result){
        //1、必填参数校验
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id  = actInfoService.add(actInfo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 更新
     * @param actInfo
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(ActInfo actInfo){
        Integer result = actInfoService.update(actInfo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询活动
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<Pager<ActInfoOut>> userList(ActInfoInVo inVo,HttpServletRequest request){
        inVo.setUserIp(IpUtils.getIpAddr(request));
        inVo.setActRange(1);//actRange1为小程序和客户端app，2为商家端
        Pager<ActInfoOut> result = actInfoService.list(inVo);
        return new BaseResp<Pager<ActInfoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询热门活动
     * @param inVo  page 为页数 rows 为展示行数
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<ActInfoOut>> findHotActInfos(ActInfoInVo inVo){
        List<ActInfoOut> list = actInfoService.top(inVo);
        return new BaseResp<List<ActInfoOut>>(ResultStatus.SUCCESS, list);
    }

    /**
     * 查询活动详细页，包括访问量、参与数、投票数信息
     *
     * 为了能够正常的分享，此处不加入从网关中获取，从前端选择性的传递
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public BaseResp<ActInfoOut> detail(ActInfoInVo inVo, HttpServletRequest request){
        /*if(inVo == null || inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName()) || inVo.getSourceType() == null){
            return new BaseResp<ActInfoOut>(ResultStatus.error_param_empty);
        }*/
        //userId 和 userName 也是要必填,但是为了能正常分享，此地方注释掉
        if(inVo == null || inVo.getId() == null){
            return new BaseResp<ActInfoOut>(ResultStatus.error_param_empty);
        }

        inVo.setUserIp(IpUtils.getIpAddr(request));
        ActInfoOut actInfoOut = actInfoService.detail(inVo);
        return new BaseResp<ActInfoOut>(ResultStatus.SUCCESS, actInfoOut);
    }

    /**
     * 查询用户在活动中可用的投票次数
     * (包含了选手可用投票次数和推荐菜可用投票次数)
     * 注:写此接口，而不是直接加到detail中，是为了减少代码的耦合度，方便以后的业务扩展
     * @param userId
     * @return
     */
    @RequestMapping(value = "/actVoteNums",method = RequestMethod.GET)
    public BaseResp<Map<String, Integer>> actVoteNums(Long userId,Long actId){
        Map<String, Integer> map = countService.actVoteNums(userId, actId);
        return new BaseResp<Map<String, Integer>>(ResultStatus.SUCCESS,map);
    }

    /**
     * 生成活动选手分享二维码
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/CreateCode")
    public BaseResp<Map<String,String>> CreateCode(ActInfoInVo inVo){
        if (inVo==null||inVo.getId()==null) {
            return new BaseResp<Map<String,String>>(ResultStatus.error_param_empty);
        }

        String imge=actInfoService.uploadQRCodeToCos(inVo);
        if (imge==null){
            return new BaseResp<Map<String,String>>(ResultStatus.error_shop_code);
        }
        Map<String,String> shopUrl=new HashMap<>();
        shopUrl.put("A",imge);//带背景图的二维码
        return new BaseResp<Map<String,String>>(ResultStatus.SUCCESS,shopUrl);
    }

    /**
     * 返回一个判断条件
     * @param
     * @return
     */
    @RequestMapping(value = "/flag")
    public BaseResp<Integer> getFlag(){
        //0是显示，1是不显示
        return new BaseResp<Integer>(ResultStatus.SUCCESS,0);
    }

    /**
     * 针对没有参与活动的个人主页的点赞数目
     * @param userId
     * @return
     */
    @RequestMapping(value = "/zanTotal")
    public BaseResp<Integer> zanTotal(Long userId){
        Integer integer = countService.zanTotal(userId);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,integer);
    }
}
