package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.home.FriendUserInfo;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class MyMomentContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回用户详情资料**/
        void liftData(FriendUserInfo userInfo);

        void liftData(String info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取用户详情资料**/
        void getUserInfo(String member_id);

        /**
         * 修改封面
         *
         * @param sourcePath 封面图片本地的文件地址
         */
        void changeCover(String sourcePath);

    }


}
