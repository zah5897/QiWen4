package com.zhan.qiwen.model.user.event;


import com.zhan.qiwen.model.user.entity.UserInfo;

import java.util.List;

public class UserFollowingEvent {
    private List<UserInfo> userInfoList;

    public UserFollowingEvent(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }
}
