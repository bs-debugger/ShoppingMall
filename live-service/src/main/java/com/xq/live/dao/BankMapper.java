package com.xq.live.dao;

import com.xq.live.model.Bank;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Bank record);

    int insertSelective(Bank record);

    Bank selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Bank record);

    int updateByPrimaryKey(Bank record);

    Bank selectByParams(@Param("accountNo")String accountNo,@Param("mobile")String mobile,
                        @Param("idCard")String idCard,@Param("name")String name);
}