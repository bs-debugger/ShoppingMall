package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActInfo;
import com.xq.live.service.ActInfoService;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.out.ActInfoOut;
import com.xq.live.web.utils.IpUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 平台活动controller
 * @author lipeng
 * @create 2018-02-07 17:14
 **/
@RestController
@RequestMapping(value = "/app/act")
public class ActInfoForAppController {

    @Autowired
    private ActInfoService actInfoService;

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
        inVo.setActRange(2);//actRange1为小程序和客户端app，2为商家端
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
     * @param inVo
     * @param request
     * @return
     */
    @ApiOperation(value = "查询活动详细页",notes = "查询活动详细页，包括访问量、参与数、投票数信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, dataType = "Long")
    })
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public BaseResp<ActInfoOut> detail(ActInfoInVo inVo, HttpServletRequest request){
        if(inVo == null || inVo.getId() == null){
            return new BaseResp<ActInfoOut>(ResultStatus.error_param_empty);
        }

        inVo.setUserIp(IpUtils.getIpAddr(request));
        ActInfoOut actInfoOut = actInfoService.detail(inVo);
        return new BaseResp<ActInfoOut>(ResultStatus.SUCCESS, actInfoOut);
    }

    /**
     * 给予当前时间
     * @param
     * @return
     */
    @RequestMapping(value = "/getDate", method = RequestMethod.GET)
    public BaseResp<String> getDate(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println(df.format(date));
        return new BaseResp<String>(ResultStatus.SUCCESS,df.format(date));
    }
}
