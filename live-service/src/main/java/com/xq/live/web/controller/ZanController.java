package com.xq.live.web.controller;/**
 * 点赞控制类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:10
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.model.Zan;
import com.xq.live.service.CountService;
import com.xq.live.service.ZanService;
import com.xq.live.vo.in.ZanInVo;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 点赞控制类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:10
 **/
@RestController
@RequestMapping(value = "/zan")
public class ZanController {

    @Autowired
    private ZanService zanService;

    @Autowired
    private CountService countService;
    /**
     * 查询一条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Zan getZanById(@PathVariable(value = "id") Long id) {
        return new Zan();
    }

    /**
     * 新增一条记录
     * 注:现在的userId是当前用户，所以需要从网关中取
     * @param zan
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Transactional
    public BaseResp<Long> add(@Valid Zan zan, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        zan.setUserId(user.getId());
        Long id = zanService.add(zan);
        if(zan.getType()==Zan.ZAN_TOPIC){
            zan.setZanType(Zan.ZAN_ADD);
            Integer integer = countService.zanNumsNow(zan);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 取消赞
     * 注:现在的userId是当前用户，所以需要从网关中取
     * @param zan
     * @return
     */
    /*@RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(Long id){
        if(id == null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty_id);
        }
        int res = zanService.delete(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, res);
    }*/
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @Transactional
    public BaseResp<Integer> delete(@Valid Zan zan, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        zan.setUserId(user.getId());
        int res = zanService.deleteByZan(zan);
        if(zan.getType()==Zan.ZAN_TOPIC){
            zan.setZanType(Zan.ZAN_DELETE);
            Integer integer = countService.zanNumsNow(zan);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, res);
    }

    /**
     * 查询点赞总数
     * refId与type必填
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public BaseResp<Integer> total(@Valid ZanInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        int total = zanService.total(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, total);
    }
}
