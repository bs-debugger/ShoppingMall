package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.HomeCarousel;
import com.xq.live.service.HomeCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lipeng on 2018/3/6.
 */
@RestController
@RequestMapping(value = "/app/hcl")
public class HomeCarouselForAppController {
     @Autowired
     private HomeCarouselService homeCarouselService;

    /**
     *首页轮播的四张图片
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
}
