package com.xq.live.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.BdBusinessAllographMapper;
import com.xq.live.exception.AppException;
import com.xq.live.model.User;
import com.xq.live.service.BusinessAllographService;
import com.xq.live.vo.in.BusinessAllographInVo;
import com.xq.live.vo.out.BusinessAllographOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BusinessAllographServiceImpl implements BusinessAllographService {

    @Autowired
    private BdBusinessAllographMapper businessAllographMapper;

    @Override
    public PageInfo<BusinessAllographOut> showList(BusinessAllographInVo businessAllographInVo) {
        // 设置分页参数
        PageHelper.startPage(businessAllographInVo.getPage(), businessAllographInVo.getRows());
        return new PageInfo<>(businessAllographMapper.showList());
    }

    @Override
    public Boolean save(BusinessAllographInVo businessAllographInVo) {
        User user = UserContext.getUserSession();
        if(user == null || user.getId() == null){
            throw new AppException(ResultStatus.no_login);
        }
        Date nowDate = new Date();
        businessAllographInVo.setCreateUserId(user.getId());
        businessAllographInVo.setCreateTime(nowDate);
        businessAllographInVo.setUpdateTime(nowDate);
        int result = businessAllographMapper.save(businessAllographInVo);
        return result > 0;
    }

}
