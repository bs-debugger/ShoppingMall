package com.xq.live.dao;

import com.xq.live.model.ShopCashier;
import com.xq.live.vo.in.ShopCashierInVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCashierMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopCashierInVo record);

    int insertSelective(ShopCashierInVo record);

    ShopCashier selectByPrimaryKey(Long id);

    ShopCashier adminByShopId(Long shopId);

    ShopCashier isHave(ShopCashierInVo inVo);

    int updateByPrimaryKeySelective(ShopCashierInVo record);

    int updateByPrimaryKey(ShopCashierInVo record);

    /**
     * 将此商家所有员工修改为核销员
     * @param record
     * @return
     */
    Integer updateAllByshop(ShopCashierInVo record);

    /**
     * 将核销员修改为管理员
     * @param record
     * @return
     */
    Integer updateAdminByUser(ShopCashierInVo record);

    /**
     * 将该店员删除
     * @param record
     * @return
     */
    Integer deleteforUser(ShopCashierInVo record);

    /**
     * 修改店员状态
     * @param record
     * @return
     */
    Integer updateForShop(ShopCashierInVo record);


    /**
     * 查询此用户是否在shopCashier中有配置
     * @param record
     * @return
     */
    ShopCashier selectBycashier(ShopCashierInVo record);

    /**
     * 查询此用户是否在shopCashier中有配置
     * @param record
     * @return
     */
    ShopCashier gethave(ShopCashierInVo record);

    /**
     * 通过cashierId查询当前用户是否是管理员账户
     * @param cashierId
     * @return
     */
    ShopCashier selectAdminByCashierId(@Param("cashierId")Long cashierId);
}
