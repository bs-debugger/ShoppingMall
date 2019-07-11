package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.UserOpinion;
import com.xq.live.vo.in.UserOpinionInVo;
import com.xq.live.vo.out.UserOpinionOut;

import java.util.List;

/**
 * Created by ss on 2018/10/24.
 *
 */
public interface UserOpinionService {

    /**
     * 查看
     * @param id
     * @return
     */
    UserOpinion selectByPrimaryKey(Long id);

    /**
     * 新增
     * @param inVo
     * @return
     */
    Long add(UserOpinionInVo inVo);

    /**
     * 删除
     * @param inVo
     * @return
     */
    Integer delete(UserOpinionInVo inVo);

    /**
     * 查看
     * @param inVo
     * @return
     */
    Pager<UserOpinionOut> listForUser(UserOpinionInVo inVo);

}
