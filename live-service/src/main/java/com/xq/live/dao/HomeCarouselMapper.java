package com.xq.live.dao;

import com.xq.live.model.HomeCarousel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeCarouselMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HomeCarousel record);

    int insertSelective(HomeCarousel record);

    HomeCarousel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HomeCarousel record);

    int updateByPrimaryKey(HomeCarousel record);

    List<HomeCarousel> list();
}
