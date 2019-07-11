package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GoodsSku;
import com.xq.live.model.GoodsSpu;
import com.xq.live.service.GoodsSpuService;
import com.xq.live.vo.in.GoodsSpuInVo;
import com.xq.live.vo.out.GoodsSpuOut;
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
 * 商城系统产品spu的controller
 * Created by lipeng on 2018/8/30.
 */
@RestController
@RequestMapping("/goodsSpu")
public class GoodsSpuController {
    @Autowired
    private GoodsSpuService goodsSpuService;

    /**
     * 根据id查询一条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<GoodsSpu> selectOne(@PathVariable("id") Long id){
        GoodsSpu re = goodsSpuService.selectOne(id);
        return new BaseResp<GoodsSpu>(ResultStatus.SUCCESS, re);
    }

    /**
     * 新增
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid GoodsSpuInVo inVo, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = goodsSpuService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 更新(可以通过此接口来更改用户的上架和下架)
     * 注:入参基本上与add接口相同,并且再多传递一个id
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(GoodsSpuInVo inVo){
        Integer result = goodsSpuService.update(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 逻辑删除
     * 注:入参 id
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(GoodsSpuInVo inVo){
        inVo.setIsDeleted(GoodsSku.GOODS_SKU_IS_DELETED);
        Integer result = goodsSpuService.update(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询记录列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<GoodsSpuOut>> list(GoodsSpuInVo inVo){
        Pager<GoodsSpuOut> result = goodsSpuService.list(inVo);
        return new BaseResp<Pager<GoodsSpuOut>> (ResultStatus.SUCCESS, result);
    }

    /**
     * 查看热门
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<GoodsSpuOut>> top(GoodsSpuInVo inVo){
        List<GoodsSpuOut> result = goodsSpuService.top(inVo);
        return new BaseResp<List<GoodsSpuOut>> (ResultStatus.SUCCESS, result);
    }
}
