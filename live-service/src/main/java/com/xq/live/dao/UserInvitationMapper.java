package com.xq.live.dao;

import com.xq.live.model.UserInvitation;
import com.xq.live.vo.in.UserInvitationInVO;

import java.util.List;

public interface UserInvitationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserInvitation record);

    int insertSelective(UserInvitation record);

    UserInvitation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInvitation record);

    int updateByPrimaryKey(UserInvitation record);

    List<UserInvitation> listTodayInvitation(UserInvitationInVO userInvitationInVO);

    List<UserInvitation> list(UserInvitationInVO userInvitationInVO);
}
