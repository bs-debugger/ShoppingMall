package com.xq.live.service.impl;

import com.xq.live.dao.DictAreaMapper;
import com.xq.live.dao.DictCityMapper;
import com.xq.live.dao.DictCountyMapper;
import com.xq.live.dao.DictProvinceMapper;
import com.xq.live.model.DictArea;
import com.xq.live.model.DictCity;
import com.xq.live.model.DictCounty;
import com.xq.live.model.DictProvince;
import com.xq.live.service.DictService;
import com.xq.live.vo.in.DictInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lipeng on 2018/3/23.
 */
@Service
public class DictServiceImpl implements DictService{

    @Autowired
    private DictProvinceMapper dictProvinceMapper;

    @Autowired
    private DictCityMapper dictCityMapper;

    @Autowired
    private DictCountyMapper dictCountyMapper;

    @Autowired
    private DictAreaMapper dictAreaMapper;


    @Override
    public List<DictProvince> findDictProvience() {
        return dictProvinceMapper.list();
    }

    @Override
    public List<DictCity> findDictCity(DictInVo inVo) {

        return dictCityMapper.list(inVo);
    }

    @Override
    public List<DictCounty> findDictCounty(DictInVo inVo) {

        return dictCountyMapper.list(inVo);
    }

    @Override
    public List<DictArea> findDictArea(DictInVo inVo) {

        return dictAreaMapper.list(inVo);
    }
}
