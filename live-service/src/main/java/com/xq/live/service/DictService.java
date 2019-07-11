package com.xq.live.service;

import com.xq.live.model.DictArea;
import com.xq.live.model.DictCity;
import com.xq.live.model.DictCounty;
import com.xq.live.model.DictProvince;
import com.xq.live.vo.in.DictInVo;

import java.util.List;

/**
 * Created by lipeng on 2018/3/23.
 */
public interface DictService {

    /**
     * 查询省份列表
     * @return
     */
    List<DictProvince> findDictProvience();

    /**
     * 查询地市列表
     * @param inVo
     * @return
     */
    List<DictCity> findDictCity(DictInVo inVo);

    /**
     * 查询县区列表
     * @param inVo
     * @return
     */
    List<DictCounty> findDictCounty(DictInVo inVo);

    /**
     * 查询街道列表
     * @param inVo
     * @return
     */
    List<DictArea> findDictArea(DictInVo inVo);
}
