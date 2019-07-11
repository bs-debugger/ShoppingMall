package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActTopicMapper;
import com.xq.live.service.ActTopicService;
import com.xq.live.vo.in.ActTopicInVo;
import com.xq.live.vo.out.ActTopicOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 主题活动Impl
 * Created by lipeng on 2018/6/29.
 */
@Service
public class ActTopicServiceImpl implements ActTopicService{
    @Autowired
    private ActTopicMapper actTopicMapper;

    @Override
    public Long add(ActTopicInVo inVo) {
        int i = actTopicMapper.countByActId(inVo.getActId());
        /*DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
        String s = mFormat.format(i+1);*/
        String s =(i + 1)+"";
        inVo.setActTopicCode(s);
        int insert = actTopicMapper.insert(inVo);//插入数据
        if(insert<1){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Pager<ActTopicOut> listForNewAct(ActTopicInVo inVo) {
        Pager<ActTopicOut> result = new Pager<ActTopicOut>();
        int listTotal = actTopicMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if(listTotal>0){
            List<ActTopicOut> list = actTopicMapper.listForNewAct(inVo);
            Integer zanTotal = 0;
            Integer actHitNum = 0;
            for (ActTopicOut actTopicOut : list) {
                zanTotal = zanTotal + actTopicOut.getZan();
                actHitNum = actHitNum + actTopicOut.getHitNum();
            }
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public ActTopicOut zanAndHitTotal(ActTopicInVo inVo) {
        return actTopicMapper.zanAndHitTotal(inVo);
    }
}
