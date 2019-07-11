package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.TopNotice;
import com.xq.live.service.TopNoticeService;
import com.xq.live.vo.in.TopNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ss on 2019/3/28.
 */
@RestController
@RequestMapping(value = "/app/notice")
public class TopNoticeForAppController {

    @Autowired
    private TopNoticeService topNoticeService;


    /**
     * 查询公告
     * @param inVo
     * @return
     * /notice/Top?type=1
     */
    @RequestMapping(value = "/Top", method = RequestMethod.GET)
    public BaseResp<List<TopNotice>> myList(TopNoticeVo inVo) {
        List<TopNotice> list = topNoticeService.listTop(inVo);
        return new BaseResp<List<TopNotice>>(ResultStatus.SUCCESS, list);
    }
}
