package com.xq.live.service;

import com.xq.live.model.TopNotice;
import com.xq.live.vo.in.TopNoticeVo;

import java.util.List;

/**
 * Created by ss on 2019/3/28.
 */

public interface TopNoticeService {

    /**
     * 不分页查询列表
     * @param inVo
     * @return
     */
    List<TopNotice> listTop(TopNoticeVo inVo);

}
