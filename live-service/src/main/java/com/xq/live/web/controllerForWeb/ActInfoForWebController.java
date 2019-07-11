package com.xq.live.web.controllerForWeb;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActInfo;
import com.xq.live.service.ActInfoService;
import com.xq.live.service.CountService;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.out.ActInfoOut;
import com.xq.live.web.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 平台活动controller
 * @author zhangpeng32
 * @create 2018-02-07 17:14
 **/
@RestController
@RequestMapping(value = "/webApprove/act")
public class ActInfoForWebController {

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
        ActInfo actInfo = actInfoService.selectOne(id);
        return new BaseResp<ActInfo>(ResultStatus.SUCCESS, actInfo);
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
