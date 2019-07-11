package com.xq.live.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 常量配置
 * Created by lipeng on 2018/4/14.
 */
@Component
@ConfigurationProperties(prefix = "constants")
public class ConstantsConfig {
    /**
     * 域名常量
     */
    private String domainXqUrl;

    public String getDomainXqUrl() {
        return domainXqUrl;
    }

    public void setDomainXqUrl(String domainXqUrl) {
        this.domainXqUrl = domainXqUrl;
    }
}
