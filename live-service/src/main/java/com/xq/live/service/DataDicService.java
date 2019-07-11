package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.vo.in.DataDicInvo;
import com.xq.live.vo.out.DataDicOut;

/**
 * 字典表service
 * Created by lipeng on 2018/4/4.
 */
public interface DataDicService {

    /**
     * 根据type分页查询数据字典
     * @param invo
     * @return
     */
    Pager<DataDicOut> listForType(DataDicInvo invo);

    /**
     * 插入一条数据字典
     * @param invo
     * @return
     */
    Long add(DataDicInvo invo);

    /**
     * 逻辑删除一条数据
     * @param invo
     * @return
     */
    Long delete(DataDicInvo invo);

    /**
     * 根据type和code来查数据
     * @param invo
     * @return
     */
    DataDicOut selectByTypeAndCode(DataDicInvo invo);
}
