package com.xq.live.service;

import com.xq.live.model.UserBankInfo;
import com.xq.live.vo.in.BankInVo;

import java.util.List;
import java.util.Map;

/**
 * 银行卡相关服务
 * @author xuzhen
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2019/4/1515:07
 */
public interface BankService {

    /**
     * 通过userId和类型查询银行列表
     * @param userId  用户ID
     * @param type 1 用户自己的银行卡号  2 商户银行号
     * @return
     */
    List<UserBankInfo> queryListByOwnerIdAndType(Long userId,int type);

    /**
     * 校验并绑定银行卡
     * @param bankInVo
     * @return
     */
    Map<String,Object> bindBankCard(BankInVo bankInVo);

    /**
     * 解绑银行卡
     * @param bankInVo
     * @return 1 成功解绑 -1解绑失败
     */
    Integer unbindBankCard(BankInVo bankInVo);

}
