package com.xq.live.dao;

import com.xq.live.model.Zan;
import com.xq.live.vo.in.ZanInVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ZanMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Zan record);

    int insertSelective(Zan record);

    Zan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Zan record);

    int updateByPrimaryKey(Zan record);

    /**
     * 根据userId, refId, type取消赞
     * @param zan
     * @return
     */
    int deleteZan(Zan zan);

    /**
     * 查询点赞总数
     * @param inVo
     * @return
     */
    public int total(ZanInVo inVo);
}
