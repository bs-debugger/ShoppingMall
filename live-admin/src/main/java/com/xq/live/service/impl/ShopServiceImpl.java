package com.xq.live.service.impl;

import com.xq.live.dao.ShopDao;
import com.xq.live.model.Shop;
import com.xq.live.service.ShopService;
import com.xq.live.vo.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-13 16:43
 * @copyright:hbxq
 **/
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    public List<Shop> queryWithPg(BaseVo inVo) throws Exception {
        Integer rowCount = queryWithCount(inVo);
        inVo.getPager().setRowCount(rowCount);
        return shopDao.queryWithPg(inVo);
    }

    @Override
    public int queryWithCount(BaseVo inVo) throws Exception {
        return shopDao.queryWithCount(inVo);
    }

    @Override
    public Shop queryById(Long id) throws Exception {
        return shopDao.selectByPrimaryKey(id);
    }

    @Override
    public int add(Shop shop) throws Exception {
        return shopDao.insert(shop);
    }

    @Override
    public int update(Shop shop) throws Exception {
        shop.setUpdateTime(new Date());
        return shopDao.updateByPrimaryKeySelective(shop);
    }

    @Override
    public int delete(Long[] ids) throws Exception {
        int result = 0;
        for (Long id : ids) {
            // 删除用户
            shopDao.deleteByPrimaryKey(id);
            result++;
        }
        return result;
    }
}
