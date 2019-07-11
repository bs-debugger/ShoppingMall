package com.xq.live.service.impl;

import com.xq.live.dao.SkuDao;
import com.xq.live.model.Sku;
import com.xq.live.model.User;
import com.xq.live.service.SkuService;
import com.xq.live.utils.RandomStringUtil;
import com.xq.live.vo.BaseVo;
import com.xq.live.vo.UserInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-12 13:42
 * @copyright:hbxq
 **/
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuDao skuDao;

    @Override
    public List<Sku> queryWithPg(BaseVo inVo) throws Exception {
        Integer rowCount = queryWithCount(inVo);
        inVo.getPager().setRowCount(rowCount);
        return skuDao.queryWithPg(inVo);
    }

    @Override
    public int queryWithCount(BaseVo inVo) throws Exception {
        return skuDao.queryWithCount(inVo);
    }

    @Override
    public Sku queryById(Long id) throws Exception {
        return skuDao.selectByPrimaryKey(id);
    }

    @Override
    public int add(Sku sku) throws Exception {
        sku.setSkuCode(RandomStringUtil.getRandomCode(8,0));
        return skuDao.insert(sku);
    }
    @Override
    public int update(Sku sku) throws Exception {
        sku.setUpdateTime(new Date());
        return skuDao.updateByPrimaryKeySelective(sku);
    }

    @Override
    public int delete(Long[] ids) throws Exception {
        int result = 0;
        for (Long id : ids) {
            // 删除用户
            skuDao.deleteByPrimaryKey(id);
            result++;
        }
        return result;
    }
}
