package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GoodsRulesConfig;
import com.xq.live.service.GoodsRulesConfigService;
import com.xq.live.vo.in.GoodsRulesConfigInVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品规则配置(商家端上传商品时用到的配置)
 * Created by admin on 2019/4/13.
 */
@RestController
@RequestMapping("/app/goodsRulesConfig")
public class GoodsRulesConfigController {

    @Autowired
    private GoodsRulesConfigService goodsRulesConfigService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<GoodsRulesConfig>> listForAct(GoodsRulesConfigInVO inVo){
        Pager<GoodsRulesConfig> result = goodsRulesConfigService.list(inVo);
        return new BaseResp<Pager<GoodsRulesConfig>> (ResultStatus.SUCCESS, result);
    }

}
