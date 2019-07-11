package com.xq.live.dao;

import com.xq.live.model.GoodsSpu;
import com.xq.live.vo.in.GoodsSpuInVo;
import com.xq.live.vo.out.GoodsSpuOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsSpuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsSpuInVo record);

    int insertSelective(GoodsSpuInVo record);

    GoodsSpu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsSpuInVo record);

    int updateByPrimaryKey(GoodsSpuInVo record);

    int listTotal(GoodsSpuInVo inVo);

    List<GoodsSpuOut> list(GoodsSpuInVo inVo);

    Long countTotal();

    GoodsSpuOut selectDetailById(Long id);
}
