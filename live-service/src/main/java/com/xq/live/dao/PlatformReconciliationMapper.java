package com.xq.live.dao;

import com.xq.live.vo.in.PlatformReconciliationInVo;
import com.xq.live.vo.out.PlatformReconciliationOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformReconciliationMapper {

    List<PlatformReconciliationOut> getList(PlatformReconciliationInVo platformReconciliationInVo);

    int getListCount(PlatformReconciliationInVo platformReconciliationInVo);
}
