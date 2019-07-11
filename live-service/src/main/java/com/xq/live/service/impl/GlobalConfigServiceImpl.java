package com.xq.live.service.impl;

import com.xq.live.dao.GlobalConfigMapper;
import com.xq.live.model.GlobalConfig;
import com.xq.live.service.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GlobalConfigServiceImpl implements GlobalConfigService {
    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    @Override
    public Map<String,GlobalConfig> list(GlobalConfig globalConfig) {
        List<GlobalConfig> list = globalConfigMapper.list(globalConfig);
        Map<String,GlobalConfig> map = new HashMap<String,GlobalConfig>();
        for (GlobalConfig config : list) {
            map.put(config.getConfigKey(),config);
        }
        return map;
    }

    @Override
    public Integer delete(Long id) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setId(id);
        globalConfig.setIsDeleted(1);
        int i = globalConfigMapper.updateByPrimaryKeySelective(globalConfig);
        return i;
    }

    @Override
    public Long add(GlobalConfig globalConfig) {
        int re = 0;
        if(globalConfig!=null&&globalConfig.getId()!=null){
            re = globalConfigMapper.updateByPrimaryKeySelective(globalConfig);
        }else {
            re = globalConfigMapper.insert(globalConfig);
        }
        if(re > 0){
            return globalConfig.getId();
        }
        return null;
    }
}
