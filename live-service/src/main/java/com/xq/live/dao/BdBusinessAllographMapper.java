package com.xq.live.dao;

import com.xq.live.vo.in.BusinessAllographInVo;
import com.xq.live.vo.out.BusinessAllographOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BdBusinessAllographMapper {

    /**
     * 查询代签商户信息
     * @return
     */
    List<BusinessAllographOut> showList();

    int save(BusinessAllographInVo businessAllographInVo);

}