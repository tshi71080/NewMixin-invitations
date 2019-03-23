package com.liuniukeji.mixin.ui.chat;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.mine.UserInfo;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.mine.UserInfo;


public class UserProfileContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回信息**/

        void liftData(UserInfo info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取用户信息**/
        void getUserInfo(String im_name);
    }

}
