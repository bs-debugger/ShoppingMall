package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.UserConcernMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.model.UserConcern;
import com.xq.live.service.UserConcernService;
import com.xq.live.vo.in.UserConcernInVo;
import com.xq.live.vo.out.UserConcernOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户关注serviceImpl
 * Created by lipeng on 2018/7/16.
 */
@Service
public class UserConcernServiceImpl implements UserConcernService{
    @Autowired
    private UserConcernMapper userConcernMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserConcern get(Long id) {
        return userConcernMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pager<UserConcernOut> list(UserConcernInVo inVo) {
        Pager<UserConcernOut> result = new Pager<UserConcernOut>();
        int total = userConcernMapper.listTotal(inVo);
        if(total > 0){
            List<UserConcernOut> list = userConcernMapper.list(inVo);
            List<UserConcernOut> userConcernOuts = new ArrayList<UserConcernOut>();
            for (UserConcernOut concernOut:list){
                User user=userMapper.selectByPrimaryKey(concernOut.getRefId());
                concernOut.setUserName(user.getUserName());
                concernOut.setNickName(user.getNickName());
                concernOut.setIconUrl(user.getIconUrl());
                concernOut.setSex(user.getSex());
                userConcernOuts.add(concernOut);
            }
            result.setList(userConcernOuts);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public UserConcernOut isCollected(UserConcernInVo inVo) {
        UserConcernOut result = userConcernMapper.isCollected(inVo);
        return result;
    }

    @Override
    public Long add(UserConcernInVo inVo) {
        int res = userConcernMapper.insert(inVo);
        if(res < 1){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Integer delete(UserConcernInVo inVo) {
        UserConcernOut re= userConcernMapper.isCollected(inVo);
        if(re==null||"".equals(re.getId())){
            return null;
        }
        Integer result= userConcernMapper.deleteByPrimaryKey(re.getId());
        if(result<1){
            return null;
        }
        return result;
    }

    @Override
    public Map<String, Integer> nums(UserConcernInVo inVo) {
        int focus = userConcernMapper.listTotal(inVo);//通过userId和type查询关注的数目
        UserConcernInVo userConcernInVo = new UserConcernInVo();
        userConcernInVo.setType(inVo.getType());
        userConcernInVo.setRefId(inVo.getUserId());
        int fans = userConcernMapper.listTotal(userConcernInVo);//通过refId和type查询粉丝的数目
        UserConcernInVo viewInVo = new UserConcernInVo();
        viewInVo.setUserId(inVo.getViewUserId());
        viewInVo.setType(inVo.getType());
        viewInVo.setRefId(inVo.getUserId());
        UserConcernOut collected = null;
        if(inVo.getViewUserId()!=null) {
            collected = userConcernMapper.isCollected(viewInVo);//查询是否已经关注
        }
        Integer isCollected = collected==null?0:1;
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("focus",focus);
        map.put("fans",fans);
        map.put("isCollected",isCollected);
        return map;
    }
}
