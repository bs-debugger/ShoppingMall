package com.xq.live.service;

import com.xq.live.vo.in.VipCardInVo;
import com.xq.live.vo.out.VipCardOut;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2019/5/16.
 */
@Service
public interface VipCardService {

    /**
     * 根据会员类型获取用户的会员卡信息
     * @param vipCardInVo
     * @return
     */
    VipCardOut getByUserAndCardType(VipCardInVo vipCardInVo);

    /**
     *用户加入会员
     * @param vipCardInVo
     * @return
     */
    VipCardOut add(VipCardInVo vipCardInVo);
}
