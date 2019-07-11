package com.xq.live.service;

import com.xq.live.model.GlobalConfig;

import java.util.List;
import java.util.Map;

public interface GlobalConfigService {
    Map<String,GlobalConfig> list(GlobalConfig globalConfig);

    Integer delete(Long id);

    Long add(GlobalConfig globalConfig);
}
