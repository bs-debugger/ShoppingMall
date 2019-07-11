package com.xq.live.service;

import com.github.pagehelper.PageInfo;
import com.xq.live.vo.in.BusinessAllographInVo;
import com.xq.live.vo.out.BusinessAllographOut;

public interface BusinessAllographService {

    /**
     * 分页查询代签商户信息
     * @param businessAllographInVo
     * @return
     */
    PageInfo<BusinessAllographOut> showList(BusinessAllographInVo businessAllographInVo);

    /**
     * 保存代签商户信息
     * @param businessAllographInVo
     */
    Boolean save(BusinessAllographInVo businessAllographInVo);

}
