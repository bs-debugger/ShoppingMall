package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Zhibo;
import com.xq.live.service.ZhiboService;
import com.xq.live.vo.in.ZhiboInVo;
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
 *直播活动controller
 *@author lipeng
 *@create 2018-02-07 18:04
 **/
@RestController
@RequestMapping("/app/zb")
public class ZhiboForAppController {

    @Autowired
    private ZhiboService zhiboService;

    /**
     * 根据id查询一条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<Zhibo> selectOne(@PathVariable("id") Long id){
        Zhibo zb = zhiboService.selectOne(id);
        return new BaseResp<Zhibo>(ResultStatus.SUCCESS, zb);
    }

    /**
     * 新增
     * @param zb
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Zhibo zb, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
//            for (ObjectError error : list) {
//                System.out.println(error.getCode() + "---" + error.getArguments()+ "---" + error.getDefaultMessage());
//            }
        }
        Long id = zhiboService.add(zb);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 更新
     * @param zb
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(Zhibo zb){
        Integer result = zhiboService.update(zb);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询记录列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<Zhibo>> update(ZhiboInVo inVo){
        Pager<Zhibo> result = zhiboService.list(inVo);
        return new BaseResp<Pager<Zhibo>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 查看热门
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<Zhibo>> top(ZhiboInVo inVo){
        List<Zhibo> result = zhiboService.top(inVo);
        return new BaseResp<List<Zhibo>> (ResultStatus.SUCCESS, result);
    }
}
