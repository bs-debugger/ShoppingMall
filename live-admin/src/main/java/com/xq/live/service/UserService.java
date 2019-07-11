package com.xq.live.service;

import com.xq.live.model.SysUser;
import com.xq.live.model.User;
import com.xq.live.vo.BaseVo;
import com.xq.live.vo.UserInVo;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-10 1:01
 * @copyright:hbxq
 **/
public interface UserService {

    /**
     * 分页查询数据
     * @param inVo
     * @return
     * @throws Exception
     */
    public List<User> queryWithPg(BaseVo inVo) throws Exception;

    /**
     * <p> 查询记录总数
     * <p> @param model
     * <p> @return
     * <p> @throws Exception
     * <p> User: Zhang Peng
     * <p> Date: 2015年11月16日
     */
    public int queryWithCount(BaseVo inVo)throws Exception;

    /**
     * 根据id查询记录
     * @param inVo
     * @return
     */
    User queryUserById(UserInVo inVo)throws Exception;;

    /**
     * 新增
     * @param inVo
     * @return
     * @throws Exception
     */
    int addUser(UserInVo inVo)throws Exception;

    /**
     * 更新
     * @param inVo
     * @return
     * @throws Exception
     */
    int updateUser(UserInVo inVo)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    int delete(Long[] ids)throws Exception;
}
