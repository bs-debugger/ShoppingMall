package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Topic;
import com.xq.live.model.User;
import com.xq.live.service.CountService;
import com.xq.live.service.TopicService;
import com.xq.live.vo.in.TopicInVo;
import com.xq.live.vo.out.TopicForZanOut;
import com.xq.live.vo.out.TopicOut;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 主题controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 18:56
 **/
@RestController
@RequestMapping(value = "/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private CountService countService;

    /**
     * 根据ID查询主题信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<TopicForZanOut> getTopicById(@PathVariable(value = "id") Long id) {
        TopicForZanOut topic = topicService.selectOne(id);
        return new BaseResp<TopicForZanOut>(ResultStatus.SUCCESS, topic);
    }

    /**
     * 注:zanUserId就是当前用户的userId,要从网关中取
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTopicByZan", method = RequestMethod.GET)
    public BaseResp<TopicForZanOut> getTopicById(TopicInVo inVo, HttpServletRequest request) {
        User user = UserContext.getUserSession();
        inVo.setZanUserId(user.getId().toString());
        if(inVo.getZanUserId()==null){
            return  new BaseResp<TopicForZanOut>(-1,"zanUserId必填",null);
        }
        inVo.setZanUserIp(IpUtils.getIpAddr(request));
        TopicForZanOut topic = topicService.selectByZan(inVo);
        Integer hits = countService.topicHits(inVo.getId());
        topic.setHitNum(hits);
        return new BaseResp<TopicForZanOut>(ResultStatus.SUCCESS, topic);
    }

    /**
     * 新增一条视频记录
     *
     * 注:现在的userId,userName是当前用户，所以需要从网关中取
     * @param topic
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Topic topic, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        topic.setUserId(user.getId());
        topic.setUserName(user.getUserName());
        Long id = topicService.add(topic);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 逻辑删除一条视频记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}",  method = RequestMethod.GET)
    public BaseResp<Integer>  delete(@PathVariable(value="id") Long id){
        int res = topicService.delete(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,res);
    }

    /**
     * 更新
     *
     * @param topic
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(Topic topic) {
        int r = topicService.update(topic);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, r);
    }

    /**
     * 其中要传的zanUserId是当前用户id,要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<TopicForZanOut>> list(TopicInVo inVo) {
        User user = UserContext.getUserSession();
        inVo.setZanUserId(user.getId().toString());
        Pager<TopicForZanOut> result = topicService.list(inVo);
        return new BaseResp<Pager<TopicForZanOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 其中要传的zanUserId是当前用户id,要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/myList", method = RequestMethod.GET)
    public BaseResp<Pager<TopicForZanOut>> myList(TopicInVo inVo) {
        User user = UserContext.getUserSession();
        inVo.setZanUserId(user.getId().toString());
        Pager<TopicForZanOut> result = topicService.myList(inVo);
        return new BaseResp<Pager<TopicForZanOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询热门主题
     * 其中要传的zanUserId是当前用户id,要从网关中取
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<TopicForZanOut>> top(TopicInVo inVo) {
        User user = UserContext.getUserSession();
        inVo.setZanUserId(user.getId().toString());
        List<TopicForZanOut> result = topicService.top(inVo);
        return new BaseResp<List<TopicForZanOut>>(ResultStatus.SUCCESS, result);
    }

    @RequestMapping(value = "/transpond",method = RequestMethod.GET)
    public BaseResp<Integer> transpond(Long topicId){
        Integer integer = countService.topicTrans(topicId);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,integer);
    }
}
