package com.xq.live.service;

import com.xq.live.vo.in.BargainLogInVo;
import com.xq.live.vo.out.BargainLogOut;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 用户砍菜减价
 * Created by ss on 2018/8/11.
 */
public interface BargainLogService {

    /**
     * 查询砍菜列表和缓存
     * @param inVo
     * @return
     */
    List<BargainLogOut> skuGrouplist(BargainLogInVo inVo);

    /**
     * 查询砍缓存
     * @param inVo
     * @return
     */
    List<BargainLogOut> skuGroupRedis(BargainLogInVo inVo);


}
