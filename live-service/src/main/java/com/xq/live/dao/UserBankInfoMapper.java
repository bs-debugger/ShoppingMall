package com.xq.live.dao;

import com.xq.live.model.UserBankInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBankInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserBankInfo record);

    int insertSelective(UserBankInfo record);

    UserBankInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserBankInfo record);

    int updateByPrimaryKey(UserBankInfo record);

    List<UserBankInfo> queryListByOwnerIdAndType(@Param("ownerId")Long owner,@Param("ownerType")Integer ownerType);

    UserBankInfo queryByCardNoAndUserId(@Param("cardNo")String cardNo,@Param("ownerId")Long ownerId);
}