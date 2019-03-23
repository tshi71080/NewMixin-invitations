package com.liuniukeji.mixin.ui.message;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class NewFriendsContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        /***返回信息**/
        void showdata(List<NewFriendsBean> newFriendsBeanList);

        void addSuccess();

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        void getNewFriendsList();
        void addFriend(String id);

    }


}
