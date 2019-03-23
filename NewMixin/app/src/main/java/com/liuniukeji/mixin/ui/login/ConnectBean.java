package com.liuniukeji.mixin.ui.login;

import java.util.List;

/**
 * 联系人以及群组
 */
public class ConnectBean {
    private List<ConnectFriendBean> friends;
    private List<ConnectGroupBean> groups;

    public List<ConnectFriendBean> getFriends() {
        return friends;
    }

    public void setFriends(List<ConnectFriendBean> friends) {
        this.friends = friends;
    }

    public List<ConnectGroupBean> getGroups() {
        return groups;
    }

    public void setGroups(List<ConnectGroupBean> groups) {
        this.groups = groups;
    }
}
