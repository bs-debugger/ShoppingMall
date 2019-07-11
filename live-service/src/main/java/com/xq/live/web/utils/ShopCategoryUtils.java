package com.xq.live.web.utils;

import com.xq.live.model.ShopCategory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家类目工具类
 * @author 冬天的秘密 <yyimba@qq.com> 2019/4/22 16:26
 */
public class ShopCategoryUtils {

    /**
     * 把商家经营品类的类目ID转换成类目名
     * @param shopCategoryList 所有类目的集合，可从ShopCategoryService->list()获取
     * @param shopCate eg: 1-14    支持多级类目: 1-2-3|1-2-6|1-2-4-8
     * @param useRoot 是否需要根目录的描述
     * @return
     */
    public static String findShopCateMemo(List<ShopCategory> shopCategoryList, String shopCate, Boolean useRoot) {
        StringBuffer shopCateMemo = new StringBuffer();

        // 先切分为多个类目组
        String[] shopCateArray = shopCate.split("\\|");
        for (String sc : shopCateArray) {
            if (StringUtils.isBlank(sc)) {
                continue;
            }
            if (shopCateMemo.length() > 0) {
                shopCateMemo.append(",");
            }

            // 把单个类目组切分为单个的类目数组
            String[] singleCateArray = sc.split("\\-");

            // 查找类目源
            List<ShopCategory> useShopCategoryList = new ArrayList<>();
            for (int i = 0; i < singleCateArray.length; i++) {
                // 如果是根类目，则从原始类目中查找
                if (i == 0) {
                    useShopCategoryList = shopCategoryList;
                }

                if (StringUtils.isBlank(singleCateArray[i])) {
                    break;
                }

                // 单个的类目ID
                Long singleCateId = Long.parseLong(singleCateArray[i]);

                // 从类目源中找到类目ID对应的类目对象
                ShopCategory shopCategory = null;
                for (ShopCategory useShopCategory : useShopCategoryList) {
                    if (singleCateId.equals(useShopCategory.getId())) {
                        shopCategory = useShopCategory;
                        break;
                    }
                }

                // 如果没有找到类目ID对应的类目对象则退出当前类名组的查找
                if (shopCategory == null) {
                    break;
                }

                // 如果需要根目录的描述，或者不需要的时候 i>0 则追加分类名
                if (useRoot || (!useRoot && i > 0)) {
                    shopCateMemo.append(shopCategory.getCategoryName());
                }

                // 因为类目组是一个层级结构，所以找到类目后变更类目查找源为当前类目的子类目
                useShopCategoryList = shopCategory.getChildren();

                // 如果当前类目没有子类目，则退出当前类名组的查找
                if (useShopCategoryList == null || useShopCategoryList.isEmpty()) {
                    break;
                }
            }
        }

        return shopCateMemo.toString();
    }

    /**
     * 把商家经营品类的类目ID转换成类目名
     * @param shopCategoryList 所有类目的集合，可从ShopCategoryService->list()获取
     * @param shopCate eg: 1-14    支持多级类目: 1-2-3|1-2-6|1-2-4-8
     * @return
     */
    public static String findShopCateMemo(List<ShopCategory> shopCategoryList, String shopCate) {
        return findShopCateMemo(shopCategoryList, shopCate, true);
    }

}
