package com.xq.live.web.controllerForWeb;

import com.alibaba.fastjson.JSON;
import com.xq.live.common.BaseResp;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.GoodsSku;
import com.xq.live.model.OrderCart;
import com.xq.live.model.User;
import com.xq.live.service.GoodsSkuService;
import com.xq.live.service.OrderCartService;
import com.xq.live.vo.out.GoodsSkuOut;
import com.xq.live.vo.out.OrderCartListOut;
import com.xq.live.vo.out.OrderCartOut;
import com.xq.live.web.utils.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * 购物车接口
 *
 * 注意:里面涉及到了一个排序，hash是无序的，所以没有选择用hash，但是用hash的性能比较高一点，这里没有做多的探究
 * Created by lipeng on 2018/10/4.
 */
@RestController
@RequestMapping(value = "/website/orderCart")
public class OrderCartForWebController {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private OrderCartService orderCartService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    /**
     * redisTemplate中hash结构用法举例
     * @return
     */
    @RequestMapping(value = "/kkk",method = RequestMethod.GET)
    public BaseResp<Integer> kkk(){
        User user = new User();
        user.setId(1L);
        user.setUserName("aaa");
        redisCache.hset("lipenTest1", "nihaoma", JSON.toJSONString(user));
        user.setId(2L);
        user.setUserName("bbb");
        redisCache.hset("lipenTest1", "nihaoma111", JSON.toJSONString(user));
        user.setId(3L);
        user.setUserName("ccc");
        redisCache.hset("lipenTest1", "nihaoma222", JSON.toJSONString(user));
        user.setId(4L);
        user.setUserName("ddd");
        redisCache.hset("lipenTest1", "hhhnihaoma333", JSON.toJSONString(user));
        Map<Object, String> lipengTest = redisCache.hmget("lipenTest");
        String o = lipengTest.get("3");
        User user1 = JSON.parseObject(o, User.class);
        ScanOptions.ScanOptionsBuilder sacanBuider=  new ScanOptions.ScanOptionsBuilder();
        sacanBuider.match("*nihaoma*");
        ScanOptions so =sacanBuider.build();
        System.out.println("so.toOptionString():" + so.toOptionString());
        Cursor<Map.Entry<String, String>> lipenTest = redisCache.hselAll("lipenTest1", sacanBuider.build());
        while (lipenTest.hasNext()){
            Map.Entry<String, String> map = lipenTest.next();
            String key = map.getKey();
            String value = map.getValue();
            System.out.println("key:"+ key +"||||" +"value:" +value);
        }

        String hget = (String)redisCache.hget("lipenTest1", "nihaoma");
        User user2 = JSON.parseObject(hget, User.class);

        Set set = redisCache.hmkeyget("lipenTest1");
        /*boolean aa = redisCache.hHasKey("lipenTest", "4");
        redisCache.hdel("lipenTest","4");
        redisCache.hdel("lipenTest", "1");
        boolean bb = redisCache.hHasKey("lipenTest", "2");
        boolean cc = redisCache.hHasKey("lipenTest", "4");*/
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    /**
     * 添加购物项到购物车
     * 入参:goodsSkuId,goodsSpuId,userId,shopId,goodsSkuName,num,(price)
     * 注意:price这个是入库的价格，及在购物项最开始的加入的时候传递进来
     *
     * 注意:新增的时候，我的想法是先插入缓存，再插入数据库,之所以插入数据库是方便以后记录用户的购物习惯
     * 删除的时候，是物理删除缓存的购物项,逻辑删除数据库的购物项(下单等同于删除)
     * 查询的时候，是仅仅查询缓存就够了
     *
     * 注意:这里的userId是当前用户userId,从网关中获取
     * @param orderCart
     * @return
     */
    @RequestMapping(value = "/addOrderCart",method = RequestMethod.POST)
    @Transactional
    public BaseResp<Integer> addOrderCart(@Valid OrderCart orderCart, BindingResult result){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        orderCart.setUserId(user.getId());
        GoodsSkuOut goodsSkuOut = goodsSkuService.selectDetailBySkuId(orderCart.getGoodsSkuId());
        if(goodsSkuOut==null||goodsSkuOut.getIsDeleted()== GoodsSku.GOODS_SKU_IS_DELETED
                ||goodsSkuOut.getStatus()==GoodsSku.STATUS_XJ){
            return new BaseResp<Integer>(ResultStatus.error_no_goods_sku);
        }
        //查询购物车信息
        String key = "orderCart_" + orderCart.getUserId();
        OrderCartListOut orderCartListOut = redisCache.get(key, OrderCartListOut.class);
        orderCartListOut = orderCartListOut!=null?orderCartListOut:new OrderCartListOut();
        List<OrderCartOut> items = orderCartListOut.getItems();
        if(items.size()>100){
            return new BaseResp<Integer>(ResultStatus.error_order_cart_limit);
        }
        OrderCartOut orderCartOut = new OrderCartOut();
        BeanUtils.copyProperties(orderCart, orderCartOut);
        if(items.contains(orderCartOut)) {
            //遍历购物车
            for (OrderCartOut item : items) {
                //判断是否包含商品项
                if (item.getGoodsSkuId().equals(orderCart.getGoodsSkuId())) {
                    item.setNum(orderCart.getNum());
                    item.setUpdateTime(new Date());
                    item.setIsChecked(OrderCartOut.ORDER_CART_IS_CHECKED);//添加商品被选中
                    //item.setGoodsSkuOut(goodsSkuOut);//商品详情是在查询的时候展现，而不是在添加
                    Integer update = orderCartService.updateByGoodsSkuIdAndUserId(orderCart);//更新购物车项的内容
                }
            }
        }else{
            orderCartOut.setCreateTime(new Date());
            orderCartOut.setUpdateTime(new Date());
            orderCartOut.setIsChecked(OrderCartOut.ORDER_CART_IS_CHECKED);//添加商品被选中
            //orderCartOut.setGoodsSkuOut(goodsSkuOut);//商品详情是在查询的时候展现，而不是在添加
            items.add(orderCartOut);
            //为防止出问题,特意在此查询数据库中是否存在未删除的购物项
            OrderCartOut oc = orderCartService.findByGoodsSkuIdAndUserId(orderCart);
            if(oc==null){
                Long add = orderCartService.add(orderCart);//新增购物项
            }else{
                Integer update = orderCartService.updateByGoodsSkuIdAndUserId(orderCart);//更新购物车项的内容
            }
        }
        orderCartListOut.setItems(items);
        redisCache.set(key,orderCartListOut);
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    /**
     * 删除购物项
     *
     * 入参:goodsSkuId,userId
     *
     * 注意:这里的userId是当前用户userId,从网关中获取
     * @param orderCart
     * @return
     */
    @RequestMapping(value = "/deleteOrderCart",method = RequestMethod.POST)
    @Transactional
    public BaseResp<Integer> deleteOrderCart(OrderCart orderCart){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        orderCart.setUserId(user.getId());
        if(orderCart==null||orderCart.getGoodsSkuId()==null||orderCart.getUserId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //查询购物车信息
        String key = "orderCart_" + orderCart.getUserId();
        OrderCartListOut orderCartListOut = redisCache.get(key, OrderCartListOut.class);
        orderCartListOut = orderCartListOut!=null?orderCartListOut:new OrderCartListOut();
        List<OrderCartOut> items = orderCartListOut.getItems();

        Iterator<OrderCartOut> sListIterator = items.iterator();
        while (sListIterator.hasNext()) {
            OrderCartOut str = sListIterator.next();
            if (str.getGoodsSkuId().equals(orderCart.getGoodsSkuId())) {
                sListIterator.remove();
            }
        }
        orderCart.setIsDeleted(OrderCart.ORDER_CART_IS_DELETED);
        Integer update = orderCartService.updateByGoodsSkuIdAndUserId(orderCart);//更新购物车项的内容
        orderCartListOut.setItems(items);
        redisCache.set(key, orderCartListOut);
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    /**
     * 查询购物车列表,并排序
     *
     * 入参:userId
     * 注意:查询的是缓存,需要前端去判断对应的商品详情的是否存在,is_deleted=0,status!=2
     *
     * 注意:这里的userId是当前用户userId,从网关中获取
     * @param orderCart
     * @return
     */
    @RequestMapping(value = "/selectOrderCartList",method = RequestMethod.GET)
    public BaseResp<OrderCartListOut> selectOrderCartList(OrderCart orderCart){
        User user = UserContext.getUserSession();
        if(user==null||user.getId()==null){
            return new BaseResp<OrderCartListOut>(ResultStatus.error_param_empty);
        }
        orderCart.setUserId(user.getId());
        if(orderCart==null||orderCart.getUserId()==null){
            return new BaseResp<OrderCartListOut>(ResultStatus.error_param_empty);
        }
        //查询购物车信息
        String key = "orderCart_" + orderCart.getUserId();
        OrderCartListOut orderCartListOut = redisCache.get(key, OrderCartListOut.class);
        orderCartListOut = orderCartListOut!=null?orderCartListOut:new OrderCartListOut();
        List<OrderCartOut> items = orderCartListOut.getItems();
        List<GoodsSkuOut> goodsSkuOuts = goodsSkuService.selectListBySkuId(items);//查询商品列表详情
        for (OrderCartOut item : items) {
            for (GoodsSkuOut goodsSkuOut : goodsSkuOuts) {
              if(item.getGoodsSkuId().equals(goodsSkuOut.getId())){
                  StringBuilder builder = new StringBuilder();
                  if(goodsSkuOut.getStockNum()<=0){
                      builder.append(goodsSkuOut.getSkuName()+"商品库存不足!");
                  }
                  if(goodsSkuOut.getStatus()==GoodsSku.STATUS_XJ
                          ||goodsSkuOut.getIsDeleted()==GoodsSku.GOODS_SKU_IS_DELETED){
                      builder.append(goodsSkuOut.getSkuName()+"商品已下架或已被删除!");
                  }
                  item.setErrorMessage(builder.toString());
                  item.setGoodsSkuOut(goodsSkuOut);
              }
            }
        }
        //目前先按照创建时间来排序,后期可以在此扩展改变
        Collections.sort(items, new Comparator<OrderCartOut>() {
            public int compare(OrderCartOut o1, OrderCartOut o2) {
                if (o1.getCreateTime().before(o2.getCreateTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        orderCartListOut.setItems(items);
        return new BaseResp<OrderCartListOut>(ResultStatus.SUCCESS,orderCartListOut);
    }
}
