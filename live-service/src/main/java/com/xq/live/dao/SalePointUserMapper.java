package com.xq.live.dao;

import com.xq.live.model.SalePointUser;
import com.xq.live.vo.in.SalePointUserInVo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SalePointUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SalePointUser record);

    int insertSelective(SalePointUser record);

    SalePointUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SalePointUser record);

    int updateByPrimaryKey(SalePointUser record);

    List<SalePointUser> selectByUserId(Long userId);

    SalePointUser selectByUserIdAndSalepint(SalePointUserInVo inVo);

    List<SalePointUser> selectByUserIdAndSalepintList(SalePointUserInVo inVo);

}
