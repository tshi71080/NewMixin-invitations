package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.io.File;


public class UserInfoModifyContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回信息**/
        void liftData(String info);

        void liftData(UserInfo info);

        void updateSucess(UserInfo info);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 修改头像
         * @param photo_path 头像文件
         */
        void updateAvatar(File photo_path);

        /*** 获取用户信息**/
        void getUserInfo();

        void updateUserSex(String sex);
    }


}
