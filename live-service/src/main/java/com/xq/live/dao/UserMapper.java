package com.xq.live.dao;

import com.xq.live.model.PullUser;
import com.xq.live.model.User;
import com.xq.live.vo.in.UserInVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "user")
public interface UserMapper{

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int batchInsert(User record);

    int insertSelective(User record);

    @Cacheable(value = "1h",key = "'userId_'+#p0.toString()")     //缓存时间1小时，key为自动生成
    User selectByPrimaryKey(Long id);

    @Caching(evict = {
            @CacheEvict(key = "'userId_'+#p0.id.toString()"),
            @CacheEvict(key = "'login_username_'+#p0.userName.toString()"),
            @CacheEvict(key = "#p0.userName.toString()")
    })
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Cacheable(value = "1h",key = "#p0")
    User loadUserByUserName(String userName);

    List<User> list(UserInVo inVo);

    /**
     * 查询范围内的用户
     * @param users
     * @return
     */
    List<User> selectConfineToUserId(List<PullUser> users);

    List<User> listForShopId(UserInVo inVo);

    int listTotal(UserInVo inVo);

    User findByUserNameAndPwd(UserInVo inVo);

    /**
     * 商家入驻更新企业客户类型，写入商家id
     * @param record
     * @return
     */
    @Caching(evict = {
            @CacheEvict(key = "'userId_'+#p0.id.toString()"),
            @CacheEvict(key = "'login_username_'+#p0.userName.toString()"),
            @CacheEvict(key = "#p0.userName.toString()")
    })
    int updateUserType(User record);

    //@Cacheable(value = "1h",key = "'openId_'+#p0.toString()")
    User findByOpenId(String openId);

    User findByUnionId(String unionId);

    //@Cacheable(value = "1h")
    User findByMobile(String mobile);

    //@CacheEvict(key = "'userId_'+#p0.id.toString()")
    @Caching(evict = {
            @CacheEvict(key = "'userId_'+#p0.id.toString()"),
            @CacheEvict(key = "'login_username_'+#p0.userName.toString()"),
            @CacheEvict(key = "#p0.userName.toString()")
    })
    Integer updateByOpenId(User user);

    @Caching(evict = {
            @CacheEvict(key = "'userId_'+#p0.id.toString()"),
            @CacheEvict(key = "'login_username_'+#p0.userName.toString()"),
            @CacheEvict(key = "#p0.userName.toString()")
    })
    Integer updateByMobile(User user);

    Integer batchListTotal(UserInVo inVo);

    List<User> batchList(UserInVo inVo);


    User selectByUserNameAndPassword(@Param("userName")String userName,@Param("passWord")String passWord);
}
