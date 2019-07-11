package com.xq.live.dao;

import com.xq.live.model.Attachment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AttachmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);

    List<Attachment> selectByIds(Map<String, Object> paramsMap);

    /**
     * 通过图片字符串查询图片
     * @param ids
     * @return
     */
    List<Attachment> selectByImgIds(@Param("ids")String ids);
}