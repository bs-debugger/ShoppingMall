package com.xq.live.dao;

import com.xq.live.model.SalepointTopPic;
import com.xq.live.vo.out.SalepointTopPicOut;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SalepointTopPicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SalepointTopPic record);

    int insertSelective(SalepointTopPic record);

    SalepointTopPic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SalepointTopPic record);

    int updateByPrimaryKey(SalepointTopPic record);

    /**
     * 根据salepoint_id查询图片列表
     * @param salepointId
     * @return
     */
    List<SalepointTopPicOut> selectBySalepointId(Long salepointId);
}
