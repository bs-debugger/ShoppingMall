package com.xq.live.vo.out;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车出参
 * Created by lipeng on 2018/10/4.
 */
public class OrderCartListOut {
    private List<OrderCartOut> items = new ArrayList<OrderCartOut>();//购物车项结果集

    //添加购物车
    public void addItem(OrderCartOut item){
        //判断是否包含同款
        if(items.contains(item)){
            //追加数量
            for (OrderCartOut orderCartOut : items) {
                if(orderCartOut.equals(item)){
                    orderCartOut.setNum(item.getNum());//前端直接传最后某个商品的数量
                }
            }
        }else {
            items.add(item);
        }
    }

    public List<OrderCartOut> getItems() {
        return items;
    }

    public void setItems(List<OrderCartOut> items) {
        this.items = items;
    }
}
