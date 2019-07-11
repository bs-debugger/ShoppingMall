package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.vo.in.PlatformReconciliationInVo;
import com.xq.live.vo.out.PlatformReconciliationOut;

public interface PlatformReconciliationService {

    Pager<PlatformReconciliationOut> getList(PlatformReconciliationInVo platformReconciliationInVo);

}
