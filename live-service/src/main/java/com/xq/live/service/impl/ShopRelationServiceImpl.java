package com.xq.live.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xq.live.common.LocationUtils;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.BdShopRelationMapper;
import com.xq.live.exception.AppException;
import com.xq.live.model.User;
import com.xq.live.service.ShopRelationService;
import com.xq.live.vo.in.ShopRelationInVo;
import com.xq.live.vo.out.ShopRelationOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopRelationServiceImpl implements ShopRelationService {

    @Autowired
    private BdShopRelationMapper bdShopRelationMapper;

    @Override
    public PageInfo<ShopRelationOut> showList(ShopRelationInVo shopRelationInVo) {
        if (shopRelationInVo.getAllRelation()) {
            shopRelationInVo.setUserId(null);
        } else {
            User user = UserContext.getUserSession();
            if(user == null || user.getId() == null){
                throw new AppException(ResultStatus.no_login);
            }
            shopRelationInVo.setUserId(user.getId());
        }

        // 设置分页参数
        PageHelper.startPage(shopRelationInVo.getPage(), shopRelationInVo.getRows());
        // 查询列表
        List<ShopRelationOut> shopRelationOutList = bdShopRelationMapper.showList(shopRelationInVo);
        // 计算距离
        for (ShopRelationOut shopRelationOut : shopRelationOutList) {
            if (shopRelationOut.getLocationX() == null || shopRelationOut.getLocationY() == null || shopRelationInVo.getLocationX() == null || shopRelationInVo.getLocationY() == null) {
                shopRelationOut.setDistance(0);
            } else {
                double distance = LocationUtils.getDistance(shopRelationOut.getLocationX().doubleValue(), shopRelationOut.getLocationY().doubleValue(), shopRelationInVo.getLocationX().doubleValue(), shopRelationInVo.getLocationY().doubleValue());
                shopRelationOut.setDistance(Double.valueOf(distance).intValue());
            }
        }

        return new PageInfo<>(shopRelationOutList);
    }

    @Override
    public Boolean save(ShopRelationInVo shopRelationInVo) {
        int result = -1;
        // 查询商户是否被认领
        int count = bdShopRelationMapper.shopHasRelation(shopRelationInVo.getShopId());
        if (shopRelationInVo.getStatus()) {
            // 如果是认领商户操作，且该商户已被其它BD认领，则给出提示
            if (count > 0) {
                throw new AppException(ResultStatus.ERROR_SHOP_RELATION_HAS);
            }
            result = bdShopRelationMapper.save(shopRelationInVo);
        } else {
            // 如果是解除商户操作，且该商户没有被BD认领，则给出提示
            if (count <= 0) {
                throw new AppException(ResultStatus.ERROR_SHOP_RELATION_NOT_FOUND);
            }
            result = bdShopRelationMapper.remove(shopRelationInVo);
        }
        return result > 0;
    }

}
