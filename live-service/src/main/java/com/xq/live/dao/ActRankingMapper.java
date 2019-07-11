package com.xq.live.dao;

import com.xq.live.model.ActRanking;
import com.xq.live.vo.in.ActRankingInVo;
import com.xq.live.vo.out.ActRankingOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActRankingMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActRanking record);

    int insertSelective(ActRanking record);

    ActRanking selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActRanking record);

    int updateByPrimaryKey(ActRanking record);

    /**
     * 查询获奖商品列表
     * @param inVo
     * @return
     */
    List<ActRankingOut> selectActInfoList(ActRankingInVo inVo);

    /**
     * 查询获奖商品列表条数
     * @param inVo
     * @return
     */
    Integer selectActInfoTotal(ActRankingInVo inVo);

    /**
     * 查询单个获奖商品
     * @param inVo
     * @return
     */
    List<ActRankingOut> selectActInfo(ActRankingInVo inVo);


}