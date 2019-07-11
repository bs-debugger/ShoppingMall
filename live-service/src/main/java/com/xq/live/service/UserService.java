package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.User;
import com.xq.live.model.UserAccount;
import com.xq.live.vo.in.SkuForNewUserInVo;
import com.xq.live.vo.in.UpdatePassWordInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.vo.out.SecondsKillGoodsSkuOut;
import com.xq.live.vo.out.SkuForNewUserOut;
import com.xq.live.vo.out.UserOut;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
public interface UserService {
    public User getUserById(@Param("id") Long id);

    public Long add(User user);

    Long batchAdd(User user);

    public User findByUsername(@Param("userName") String userName);

    public User findByOpenId(String openId);

    public User findByUnionId(String unionId);

    public User findByMobile(String mobile);

    public Pager<User> list(UserInVo inVo);

    public List<User> listForShopId(UserInVo inVo);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public Integer update(User user);

    public List<User> top(UserInVo inVo);

    /**
     * 根据订单ID查询用户信息中的余额
     * @param userID
     * @return
     */
    public List<UserAccount> findAccountByUserId(Long userID);

    /**
     * 根据活动UserID查询用户信息中的余额
     * @param userID
     * @return
     */
    public List<UserAccount> fingAccountByID(Long userID);

    /**
     * 根据入参查询用户信息
     * @param inVo
     * @return
     */
    public User findByUserNameAndPwd(UserInVo inVo);

    /**
     * 更新用户登录信息
     * @param user
     * @return
     */
    public Integer updateLoginInfo(User user);

    /**
     * 使用openid更新用户信息
     * @param user
     * @return
     */
    public Integer updateByOpenId(User user);

    /**
     * 根据userID修改userAccount
     * @param
     * @return
     */
    public Integer updateByUserID(UserAccountInVo accountInVo);

    /**
     * 使用mobile更新用户信息
     * @param user
     * @return
     */
    public Integer updateByMobile(User user);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     */
    String login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 操作结果
     */
    Long register(User user);

    /**
     * 刷新密钥
     *
     * @param oldToken 原密钥
     * @return 新密钥
     */
    String refreshToken(String oldToken);


    /**
     * 通过商家端app注册用户（用微信登陆）,适合客户端app登陆，但是通过openId和mobile
     *
     * 适用于用openId和mobile来增加用户,
     * 如果通过openId查出数据,这里面的openId查出来的手机号是为空的，让用户输入手机号发送验证码,
     * 如果输入的手机号在user表中存在记录,则更新该记录放入openId，返回该记录id,
     * 如果输入的手机号不存在记录，则直接在查出来的openId的记录中放入手机号,返回该记录id,
     * 如果openId查出来的数据含有手机号，则不走此接口，直接通过findByOpenId接口返回，方便以后扩展。
     *
     * 如果openId查不出数据，则用户没有登录过小程序，
     * 如果输入的手机号在user表中存在记录,则更新该记录放入openId，返回该记录id,
     * 如果输入的手机号不存在记录，则直接插入一条数据
     * @param user
     * @return
     */
    Long addAppUser(User user);

    /**
     * 新人注册成功后修改缓存人数
     * @param inVo
     * @return
     */
    public SkuForNewUserOut upPeopleNum(SkuForNewUserInVo inVo);

    /**
     * 抢购邀请新人注册
     * @param inVo
     * @return
     */
    public SkuForNewUserOut invite(SkuForNewUserInVo inVo);

    /**
     * 秒杀商品创建邀请新人注册
     * @param inVo
     * @return
     */
    public SecondsKillGoodsSkuOut inviteNewPeople(SkuForNewUserInVo inVo);

    /**
     * 查看邀请的新人信息
     * @param inVo
     * @return
     */
    public List<SkuForNewUserOut> getNewUser(SkuForNewUserInVo inVo);

    /**
     * 查看商品邀请的新人信息
     * @param inVo
     * @return
     */
    public List<SecondsKillGoodsSkuOut> selectQuerySecondsKill(SkuForNewUserInVo inVo);


    /**
     * 商品新人注册成功后修改缓存人数
     * @param inVo
     * @return
     */
    public SecondsKillGoodsSkuOut updatePeopleNumNew(SkuForNewUserInVo inVo);

    Integer batchUpdate(User user);

    List<User> batchList(UserInVo inVo);

    /**
     * 通过code获取用户信息
     * @param code
     * @param ip
     * @return
     */
    public UserOut wXUserInfo(String code,String ip);

    /**
     * 删除用户
     * @param user
     * @return
     */
    public Integer deleteUserInfo(User user);

    /**
     * 通过账号密码查询用户账号信息
     * @param userName
     * @param passWord
     * @return
     */
    public User queryByUserNameAndPassWord(String userName,String passWord);

    /**
     * 修改用户登录密码
     * @param command
     * @return
     */
    public Integer updatePassWord(UpdatePassWordInVo command);


}
