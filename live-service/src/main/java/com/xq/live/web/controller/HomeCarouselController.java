package com.xq.live.web.controller;

import com.alibaba.fastjson.JSON;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.HomeCarousel;
import com.xq.live.service.*;
import com.xq.live.vo.in.*;
import com.xq.live.vo.out.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lipeng on 2018/3/6.
 */
@RestController
@RequestMapping(value = "hcl")
public class HomeCarouselController {
     @Autowired
     private HomeCarouselService homeCarouselService;

    @Autowired
    private RedisCache redisCache;

     @Autowired
     private GoodsSkuService goodsSkuService;

     @Autowired
     private ShopService shopService;

     @Autowired
     private SkuService skuService;

     @Autowired
     private TopicService topicService;

    /**
     *首页轮播的五张图片
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<List<HomeCarousel>> list(){
        List<HomeCarousel> res = homeCarouselService.list();
        List<HomeCarousel> homeCarousels = null;
        if(res!=null&&res.size()>=HomeCarousel.HOME_CAROUSEL_SIZE){
             homeCarousels = res.subList(0, HomeCarousel.HOME_CAROUSEL_SIZE);
        }
        return new BaseResp<List<HomeCarousel>>(ResultStatus.SUCCESS,homeCarousels);
    }

    /**
     *首页的其他图片
     * @return
     */
    @RequestMapping(value = "/listForHomePage",method = RequestMethod.GET)
    public BaseResp<List<HomeCarousel>> listForHomePage(){
        List<HomeCarousel> res = homeCarouselService.list();
        List<HomeCarousel> homeCarousels = null;
        if(res!=null&&res.size()>HomeCarousel.HOME_CAROUSEL_SIZE){
            homeCarousels = res.subList(HomeCarousel.HOME_CAROUSEL_SIZE, res.size());
        }
        return new BaseResp<List<HomeCarousel>>(ResultStatus.SUCCESS,homeCarousels);
    }

    /**
     *首页的类目加载列表
     * @return
     *
     *{"city": "十堰市","locationX": 30.49984,"locationY": 114.34253,"rows": 12,
    "listStart": [{"type": "goodsSku","start": 3},{"type": "shop","start": 3
    },{"type": "skuMS","start": 3},{"type": "skuKJ","start": 3}]}
     */
    @RequestMapping(value = "/listForHomeObject",method = RequestMethod.POST)
    public BaseResp<Map<String,Object>> listForHomeObject(@RequestBody HomeSkuListInVo InVo){
        Map<String,Object> mapList = new HashMap<String,Object>();
        /*Integer rowsNum=InVo.getRows();
        Integer divisor = new Integer(6);
        Integer first =InVo.getRows()/divisor;*/
        if (InVo.getListStart()==null||InVo.getListStart().size()<1){

            //推荐商家
           /* ShopInVo shopInVo=new ShopInVo();
            shopInVo.setCity(InVo.getCity());
            shopInVo.setStart(0);
            shopInVo.setRows(5);
            List<ShopOut> shop=shopService.listHome(shopInVo);
            if (shop!=null&&shop.size()>0){
                shop.get(0).setHomeTotle(4);
            }
            mapList.put("shop",shop);*/
            //砍价菜
            SkuInVo skuInVo = new SkuInVo();
            skuInVo.setCity(InVo.getCity());
            skuInVo.setLocationX(InVo.getLocationX());
            skuInVo.setLocationY(InVo.getLocationY());
            skuInVo.setSkuType(new Integer(6));
            skuInVo.setStart(0);
            skuInVo.setRows(10);
            List<SkuForTscOut> skuKJ=skuService.queryKjcListHome(skuInVo);
            if (skuKJ!=null&&skuKJ.size()>0){
                skuKJ.get(0).setHomeTotle(10);
            }
            mapList.put("skuKJ",skuKJ);
            //参与砍价的商品
           /* GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
            goodsSkuInVo.setActId(new Long(41));
            goodsSkuInVo.setStart(new Integer(0));
            goodsSkuInVo.setRows(rowsNum / 4);
            List<GoodsSkuOut> goodsSku=goodsSkuService.actListHome(goodsSkuInVo);

            if (goodsSku!=null&&goodsSku.size()>0){
                goodsSku.get(0).setHomeTotle(rowsNum/4);
            }
            mapList.put("goodsSku",goodsSku);*/

            //秒杀菜
            SkuInVo skuInVos = new SkuInVo();
            skuInVos.setCity(InVo.getCity());
            skuInVos.setSkuType(new Integer(7));
            skuInVos.setLocationX(InVo.getLocationX());
            skuInVos.setLocationY(InVo.getLocationY());
            skuInVos.setStart(0);
            skuInVos.setRows(10);
            List<SkuForTscOut> skuMS=skuService.queryQgcListHome(skuInVos);
            if (skuMS!=null&&skuMS.size()>0){
                skuMS.get(0).setHomeTotle(10);
            }
            mapList.put("skuMS",skuMS);

            //视频
            TopicInVo topicInVo = new TopicInVo();
            topicInVo.setTopicType(new Integer(2));
            topicInVo.setShopType(new Integer(2));
            topicInVo.setStart(0);
            topicInVo.setRows(1);
            List<TopicForZanOut> topicVideo=topicService.listHome(topicInVo);
            if (topicVideo!=null&&topicVideo.size()>0){
                topicVideo.get(0).setHomeTotle(1);
            }
            mapList.put("topicVideo",topicVideo);
            //文章
            TopicInVo topicInVoText = new TopicInVo();
            topicInVoText.setTopicType(new Integer(1));
            topicInVoText.setShopType(new Integer(2));
            topicInVoText.setStart(new Integer(0));
            topicInVoText.setRows(1);
            List<TopicForZanOut> topicText=topicService.listHome(topicInVoText);
            if (topicText!=null&&topicText.size()>0){
                topicText.get(0).setHomeTotle(1);
            }
            mapList.put("topicText",topicText);
        }else {
            for (HomeObjectInVo home:InVo.getListStart()){
                /*if (home.getType().equals("shop")){
                    if (home.getStart()!=null&&home.getStart()>=1){
                        ShopInVo shopInVo=new ShopInVo();
                        shopInVo.setCity(InVo.getCity());
                        shopInVo.setStart(home.getStart());
                        shopInVo.setRows(4);
                        List<ShopOut> shop=shopService.listHome(shopInVo);
                        if (shop!=null&&shop.size()>0){
                            shop.get(0).setHomeTotle(home.getStart()+4);
                        }
                        mapList.put("shop",shop);
                    }
                }else*/ if (home.getType().equals("skuKJ")){

                    if (home.getStart()!=null&&home.getStart()>=1){
                        SkuInVo skuInVo = new SkuInVo();
                        skuInVo.setCity(InVo.getCity());
                        skuInVo.setSkuType(new Integer(6));
                        skuInVo.setStart(home.getStart());
                        skuInVo.setRows(10);
                        List<SkuForTscOut> skuKJ=skuService.queryKjcListHome(skuInVo);
                        if (skuKJ!=null&&skuKJ.size()>0){
                            skuKJ.get(0).setHomeTotle(home.getStart() + (10));
                        }
                        mapList.put("skuKJ",skuKJ);
                    }
                }else /*if (home.getType().equals("goodsSku")){
                    if (home.getStart()!=null&&home.getStart()>1){
                        GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
                        goodsSkuInVo.setActId(new Long(41));
                        goodsSkuInVo.setStart(home.getStart());
                        goodsSkuInVo.setRows(rowsNum / 4);
                        List<GoodsSkuOut> goodsSku=goodsSkuService.actListHome(goodsSkuInVo);
                        if (goodsSku!=null&&goodsSku.size()>0){
                            goodsSku.get(0).setHomeTotle(home.getStart()+(rowsNum/4));
                        }
                        rowsNum-=rowsNum/4;
                        mapList.put("goodsSku",goodsSku);
                    }

                }else*/ if (home.getType().equals("skuMS")){
                    if (home.getStart()!=null&&home.getStart()>=1){
                        SkuInVo skuInVo = new SkuInVo();
                        skuInVo.setCity(InVo.getCity());
                        skuInVo.setSkuType(new Integer(7));
                        skuInVo.setStart(home.getStart());
                        skuInVo.setRows(10);
                        List<SkuForTscOut> skuMS=skuService.queryQgcListHome(skuInVo);
                        if (skuMS!=null&&skuMS.size()>0){
                            skuMS.get(0).setHomeTotle(home.getStart() + (10));
                        }

                        mapList.put("skuMS",skuMS);
                    }
                }else if (home.getType().equals("topicVideo")){
                    if (home.getStart()!=null&&home.getStart()>=1){
                        TopicInVo topicInVo = new TopicInVo();
                        topicInVo.setTopicType(new Integer(2));
                        topicInVo.setShopType(new Integer(2));
                        topicInVo.setStart(home.getStart());
                        topicInVo.setRows(1);
                        List<TopicForZanOut> topicVideo=topicService.listHome(topicInVo);
                        if (topicVideo!=null&&topicVideo.size()>0){
                            topicVideo.get(0).setHomeTotle(home.getStart() + 1);
                        }
                        mapList.put("topicVideo",topicVideo);
                    }
                }else if (home.getType().equals("topicText")){
                    if (home.getStart()!=null&&home.getStart()>=1){
                        TopicInVo topicInVo = new TopicInVo();
                        topicInVo.setTopicType(new Integer(1));
                        topicInVo.setShopType(new Integer(2));
                        topicInVo.setStart(home.getStart());
                        topicInVo.setRows(1);
                        List<TopicForZanOut> topicText=topicService.listHome(topicInVo);
                        if (topicText!=null&&topicText.size()>0){
                            topicText.get(0).setHomeTotle(home.getStart()+ 1);
                        }
                        mapList.put("topicText",topicText);
                    }
                }
        }
        }
        return new BaseResp<Map<String,Object>>(ResultStatus.SUCCESS,mapList);
    }


    /**
     *首页的类目加载列表（现在使用）
     * @return
     *
     *{"city": "十堰市","locationX": 30.49984,"locationY": 114.34253,"rows": 12,
    "listStart": [{"type": "goodsSku","start": 3},{"type": "shop","start": 3
    },{"type": "skuMS","start": 3},{"type": "skuKJ","start": 3}]}
     */
    @RequestMapping(value = "/listForHomeObjectLv",method = RequestMethod.POST)
    public BaseResp<Map<String,Object>> listForHomeObjectLv(@RequestBody HomeSkuListInVo InVo){
        Map<String,Object> mapList = new HashMap<String,Object>();
        //Map<Object,String> keyArrays = redisCache.hmget("listForHomeObjectLv");
        if (InVo.getListStart()==null||InVo.getListStart().size()<1){
            //参与砍价的商品
            GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
            goodsSkuInVo.setCity(InVo.getCity());
            goodsSkuInVo.setActId(new Long(41));
            goodsSkuInVo.setStart(new Integer(0));
            goodsSkuInVo.setRows(10);
            List<GoodsSkuOut> goodsSku=goodsSkuService.actListHome(goodsSkuInVo);
            if (goodsSku!=null&&goodsSku.size()>0){
                goodsSku.get(0).setHomeTotle(10);
            }
            mapList.put("goodsSku",goodsSku);
            //参与秒杀的商品
            GoodsSkuInVo goodsSkuInVoMs = new GoodsSkuInVo();
            goodsSkuInVoMs.setActId(new Long(44));
            goodsSkuInVoMs.setStart(new Integer(0));
            goodsSkuInVoMs.setCity(InVo.getCity());
            goodsSkuInVoMs.setRows(10);
            List<GoodsSkuOut> goodsSkuMs=goodsSkuService.actListHome(goodsSkuInVoMs);
            if (goodsSkuMs!=null&&goodsSkuMs.size()>0){
                goodsSkuMs.get(0).setHomeTotle(10);
            }
            mapList.put("skuMS",goodsSkuMs);
            //视频
            TopicInVo topicInVo = new TopicInVo();
            topicInVo.setTopicType(new Integer(2));
            topicInVo.setShopType(new Integer(2));
            topicInVo.setStart(0);
            topicInVo.setRows(1);
            List<TopicForZanOut> topicVideo=topicService.listHome(topicInVo);
            if (topicVideo!=null&&topicVideo.size()>0){
                topicVideo.get(0).setHomeTotle(1);
            }
            mapList.put("topicVideo",topicVideo);
            //文章
            TopicInVo topicInVoText = new TopicInVo();
            topicInVoText.setTopicType(new Integer(1));
            topicInVoText.setShopType(new Integer(2));
            topicInVoText.setStart(new Integer(0));
            topicInVoText.setRows(1);
            List<TopicForZanOut> topicText=topicService.listHome(topicInVoText);
            if (topicText!=null&&topicText.size()>0){
                topicText.get(0).setHomeTotle(1);
            }
            mapList.put("topicText",topicText);
        }else {
            for (HomeObjectInVo home:InVo.getListStart()){
              if (home.getType().equals("goodsSku")){
                    if (home.getStart()!=null&&home.getStart()>1){
                        GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
                        goodsSkuInVo.setCity(InVo.getCity());
                        goodsSkuInVo.setActId(new Long(41));
                        goodsSkuInVo.setStart(home.getStart());
                        goodsSkuInVo.setRows(10);
                        List<GoodsSkuOut> goodsSku=goodsSkuService.actListHome(goodsSkuInVo);
                        if (goodsSku!=null&&goodsSku.size()>0){
                            goodsSku.get(0).setHomeTotle(home.getStart()+(10));
                        }
                        mapList.put("goodsSku",goodsSku);
                    }
                }else if (home.getType().equals("skuMS")){
                    if (home.getStart()!=null&&home.getStart()>1){
                        GoodsSkuInVo goodsSkuInVo = new GoodsSkuInVo();
                        goodsSkuInVo.setCity(InVo.getCity());
                        goodsSkuInVo.setActId(new Long(44));
                        goodsSkuInVo.setStart(home.getStart());
                        goodsSkuInVo.setRows(10);
                        List<GoodsSkuOut> goodsSkuMs=goodsSkuService.actListHome(goodsSkuInVo);
                        if (goodsSkuMs!=null&&goodsSkuMs.size()>0){
                            goodsSkuMs.get(0).setHomeTotle(home.getStart()+(10));
                        }
                        mapList.put("skuMS",goodsSkuMs);
                    }
                }else if (home.getType().equals("topicVideo")){
                    if (home.getStart()!=null&&home.getStart()>=1){
                        TopicInVo topicInVo = new TopicInVo();
                        topicInVo.setTopicType(new Integer(2));
                        topicInVo.setShopType(new Integer(2));
                        topicInVo.setStart(home.getStart());
                        topicInVo.setRows(1);
                        List<TopicForZanOut> topicVideo=topicService.listHome(topicInVo);
                        if (topicVideo!=null&&topicVideo.size()>0){
                            topicVideo.get(0).setHomeTotle(home.getStart() + 1);
                        }
                        mapList.put("topicVideo",topicVideo);
                    }
                }else if (home.getType().equals("topicText")){
                    if (home.getStart()!=null&&home.getStart()>=1){
                        TopicInVo topicInVo = new TopicInVo();
                        topicInVo.setTopicType(new Integer(1));
                        topicInVo.setShopType(new Integer(2));
                        topicInVo.setStart(home.getStart());
                        topicInVo.setRows(1);
                        List<TopicForZanOut> topicText=topicService.listHome(topicInVo);
                        if (topicText!=null&&topicText.size()>0){
                            topicText.get(0).setHomeTotle(home.getStart()+ 1);
                        }
                        mapList.put("topicText",topicText);
                    }
                }
            }
        }
        return new BaseResp<Map<String,Object>>(ResultStatus.SUCCESS,mapList);
    }
}
