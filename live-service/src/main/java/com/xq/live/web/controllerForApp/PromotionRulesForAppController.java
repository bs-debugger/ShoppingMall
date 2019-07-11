package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.PromotionRules;
import com.xq.live.model.ShopPromotionRules;
import com.xq.live.service.PromotionRulesService;
import com.xq.live.vo.out.PromotionRulesOut;
import com.xq.live.vo.out.ShopPromotionRulesOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 平台卷与商家规则
 * Created by lipeng on 2018/3/30.
 */
@RestController
@RequestMapping("/app/pnr")
public class PromotionRulesForAppController {

    @Autowired
    private PromotionRulesService promotionRulesService;

    /**
     * 根据shopId查询商家所需的卷规则----老接口,以后要删除
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectByShopId",method = RequestMethod.GET)
    public BaseResp<List<PromotionRulesOut>> selectByShopId(Long shopId){
        if(shopId==null){
            return new BaseResp<List<PromotionRulesOut>>(ResultStatus.error_param_empty);
        }
        List<PromotionRulesOut> promotionRulesOuts = promotionRulesService.selectByShopId(shopId);
        return new BaseResp<List<PromotionRulesOut>>(ResultStatus.SUCCESS,promotionRulesOuts);
    }

    /**
     * 根据shopId查询商家所需的卷规则(新)
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectByShopIdNew",method = RequestMethod.GET)
    public BaseResp<List<ShopPromotionRulesOut>> selectByShopIdNew(Long shopId){
        if(shopId==null){
            return new BaseResp<List<ShopPromotionRulesOut>>(ResultStatus.error_param_empty);
        }
        List<ShopPromotionRulesOut> promotionRulesOuts = promotionRulesService.selectByShopIdNew(shopId);
        return new BaseResp<List<ShopPromotionRulesOut>>(ResultStatus.SUCCESS,promotionRulesOuts);
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public BaseResp<Integer> deleteById(@PathVariable("id")Long id){
        int result = promotionRulesService.deleteById(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 更新一条商家促销规则
     *
     * @param rules
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(PromotionRules rules) {
        int result = promotionRulesService.update(rules);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 新增一条促销规则
     * @param rules
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid PromotionRules rules, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long skuId = promotionRulesService.add(rules);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    @RequestMapping(value = "/deleteByIdNew/{id}",method = RequestMethod.GET)
    public BaseResp<Integer> deleteByIdNew(@PathVariable("id")Long id){
        int result = promotionRulesService.deleteByIdNew(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 更新一条商家促销规则
     *
     * @param rules
     * @return
     */
    @RequestMapping(value = "/updateNew", method = RequestMethod.POST)
    public BaseResp<Integer> updateNew(ShopPromotionRules rules) {
        int result = promotionRulesService.updateNew(rules);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 新增一条促销规则
     * @param rules
     * @return
     */
    @RequestMapping(value = "/addNew", method = RequestMethod.POST)
    public BaseResp<Long> addNew(@Valid ShopPromotionRules rules, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long skuId = promotionRulesService.addNew(rules);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }
}
