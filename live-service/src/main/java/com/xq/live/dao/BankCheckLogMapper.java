package com.xq.live.dao;

import com.xq.live.model.BankCheckLog;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCheckLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BankCheckLog record);

    int insertSelective(BankCheckLog record);

    BankCheckLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BankCheckLog record);

    int updateByPrimaryKey(BankCheckLog record);
}