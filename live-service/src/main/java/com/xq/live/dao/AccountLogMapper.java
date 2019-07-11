package com.xq.live.dao;

import com.xq.live.model.AccountLog;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.ShopAccountInVo;
import com.xq.live.vo.out.AccountLogOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AccountLog record);

    int insertSelective(AccountLog record);

    AccountLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountLog record);

    int updateByPrimaryKey(AccountLog record);

    int listTotal(AccountLogInVo inVo);

    List<AccountLogOut> list(AccountLogInVo inVo);

    /**
     * 查询商家一段时间内的非订单产生的日志（补偿的费用已经扣除的费用等）
     * @param inVo
     * @return
     */
    List<AccountLog> selectSpecialLog(ShopAccountInVo inVo);

    /**
     * 总数(供统计)
     * @param inVo
     * @return
     */
    int selectSpecialLogTotal(ShopAccountInVo inVo);

}
