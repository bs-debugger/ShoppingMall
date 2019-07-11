package com.xq.live.service.impl;

import com.xq.live.dao.TopNoticeMapper;
import com.xq.live.model.TopNotice;
import com.xq.live.service.TopNoticeService;
import com.xq.live.vo.in.TopNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ss on 2019/3/28.
 */
@Service
public class TopNoticeServiceImpl implements TopNoticeService{

    @Autowired
    private TopNoticeMapper topNoticeMapper;

    /**
     * 不分页查询列表
     * @param inVo
     * @return
     */
    @Override
    public List<TopNotice> listTop(TopNoticeVo inVo) {
        List<TopNotice> list = topNoticeMapper.selectBetweenTime(inVo);
        return list;
    }
}
