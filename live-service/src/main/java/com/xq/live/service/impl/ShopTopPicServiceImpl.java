package com.xq.live.service.impl;

import com.xq.live.dao.ShopTopPicMapper;
import com.xq.live.model.ShopTopPic;
import com.xq.live.service.ShopTopPicService;
import com.xq.live.vo.out.ShopTopPicOut;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家顶部图片接口
 * Created by lipeng on 2018/7/3.
 */
@Service
public class ShopTopPicServiceImpl implements ShopTopPicService{
    @Autowired
    private ShopTopPicMapper shopTopPicMapper;

    /**
     * 通过shopId查询商家的顶部图片
     * @param shopId
     * @return
     */
    @Override
    public List<Triplet<String, String,Long>> selectByShopId(Long shopId) {
        List<ShopTopPicOut> shopTopPicOuts = shopTopPicMapper.selectByShopId(shopId);
        List<Triplet<String, String,Long>> list = new ArrayList<>();
        for (ShopTopPicOut shopTopPicOut : shopTopPicOuts) {
            list.add(new Triplet<String, String,Long>(shopTopPicOut.getAttachment().getSmallPicUrl(),shopTopPicOut.getAttachment().getPicUrl(),shopTopPicOut.getId()));
        }
        return list;
    }

    @Override
    public Long add(ShopTopPic shopTopPic) {
        int insert = shopTopPicMapper.insert(shopTopPic);
        if(insert < 1){
            return null;
        }
        return shopTopPic.getId();
    }

    @Override
    public Integer delete(Long id) {
        int i = shopTopPicMapper.deleteByPrimaryKey(id);
        if(i < 1){
            return null;
        }
        return i;
    }
}
