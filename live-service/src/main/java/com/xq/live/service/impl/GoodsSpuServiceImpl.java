package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.GoodsSpuMapper;
import com.xq.live.model.GoodsSku;
import com.xq.live.model.GoodsSpu;
import com.xq.live.service.GoodsSpuService;
import com.xq.live.vo.in.GoodsSpuInVo;
import com.xq.live.vo.out.GoodsSpuOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商城系统Sku的ServiceImpl
 * Created by lipeng on 2018/8/29.
 */
@Service
public class GoodsSpuServiceImpl implements GoodsSpuService {
    @Autowired
    private GoodsSpuMapper goodsSpuMapper;

    @Override
    public GoodsSpu selectOne(Long id) {
        return goodsSpuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(GoodsSpuInVo inVo) {
        inVo.setSpuCode(RandomStringUtil.getRandomCode(8, 0));
        inVo.setIsDeleted(GoodsSku.GOODS_SKU_NO_DELETED);//未删除
        int res = goodsSpuMapper.insert(inVo);

        if(res < 1){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Integer update(GoodsSpuInVo inVo) {
        return goodsSpuMapper.updateByPrimaryKeySelective(inVo);
    }

    @Override
    public Pager<GoodsSpuOut> list(GoodsSpuInVo inVo) {
        Pager<GoodsSpuOut> result = new Pager<GoodsSpuOut>();
        int listTotal = goodsSpuMapper.listTotal(inVo);
        if(listTotal > 0){
            List<GoodsSpuOut> list = goodsSpuMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(listTotal);
        return result;
    }

    @Override
    public List<GoodsSpuOut> top(GoodsSpuInVo inVo) {
        return goodsSpuMapper.list(inVo);
    }
}
