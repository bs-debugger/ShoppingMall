package com.xq.live.dao;

import com.xq.live.model.PullUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PullUserMapper {
    int deleteByPrimaryKey(Long id);

    /**
     * 配置邀请
     * @return
     */
    int insert(PullUser record);

    int insertSelective(PullUser record);

    PullUser selectByPrimaryKey(Long id);

    PullUser selectByUserId(Long userId);

    int updateByPrimaryKeySelective(PullUser record);

    int updateByPrimaryKey(PullUser record);

    int updatePullNums(PullUser record);

    /**
     * 清空邀请螃蟹人数
     * @return
     */
    int updatePullNumsPx(PullUser record);

    /**
     * 邀请螃蟹人数加一
     * @return
     */
    int updatePullNumsUp(PullUser record);


    /**
     * 查看邀请螃蟹人数
     * @return
     */
    PullUser getByUserIdPX(Long id);

    /**
     * 查看邀请新人人数
     * @return
     */
    PullUser getByUserId(PullUser InVo);

    /**
     * 查看邀请新人信息
     * @return
     */
    List<PullUser> selectUserByParentId(PullUser inVo);


}
