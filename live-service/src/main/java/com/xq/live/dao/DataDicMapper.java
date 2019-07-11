package com.xq.live.dao;

import com.xq.live.model.DataDic;
import com.xq.live.vo.in.DataDicInvo;
import com.xq.live.vo.out.DataDicOut;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典表
 */
@Repository
public interface DataDicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataDicInvo invo);

    int insertSelective(DataDicInvo invo);

    DataDic selectByPrimaryKey(Long id);

    DataDicOut selectByTypeAndCode(DataDicInvo invo);

    int updateByPrimaryKeySelective(DataDicInvo invo);

    int updateByPrimaryKey(DataDicInvo invo);

    List<DataDicOut> listForType(DataDicInvo invo);
}
