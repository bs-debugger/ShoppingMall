package com.xq.live.service.impl;

import com.xq.live.dao.HomeCarouselMapper;
import com.xq.live.model.HomeCarousel;
import com.xq.live.service.HomeCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lipeng on 2018/3/6.
 */
@Service
public class HomeCarouselServiceImpl implements HomeCarouselService {
    @Autowired
    private HomeCarouselMapper homeCarouselMapper;

    @Override
    public List<HomeCarousel> list() {
        List<HomeCarousel> list = homeCarouselMapper.list();
        return list;
    }
}
