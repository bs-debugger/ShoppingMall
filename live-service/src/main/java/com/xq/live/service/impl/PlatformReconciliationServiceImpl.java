package com.xq.live.service.impl;

import com.xq.live.common.ListObjConverter;
import com.xq.live.common.Pager;
import com.xq.live.dao.PlatformReconciliationMapper;
import com.xq.live.service.PlatformReconciliationService;
import com.xq.live.vo.in.PlatformReconciliationInVo;
import com.xq.live.vo.out.PlatformReconciliationOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlatformReconciliationServiceImpl implements PlatformReconciliationService {

    @Autowired
    private PlatformReconciliationMapper platformReconciliationMapper;
    @Override
    public Pager<PlatformReconciliationOut> getList(PlatformReconciliationInVo platformReconciliationInVo) {
        //创建分页对象
        Pager<PlatformReconciliationOut> result = new Pager<>();
        //查询列表
        List<PlatformReconciliationOut> list = platformReconciliationMapper.getList(platformReconciliationInVo);
        //转为输出实例
        List<PlatformReconciliationOut> accountOuts = ListObjConverter.convert(list, PlatformReconciliationOut.class);
        result.setTotal(platformReconciliationMapper.getListCount(platformReconciliationInVo));
        result.setPage(platformReconciliationInVo.getPage());
        result.setList(accountOuts);
        return result;
    }
}
