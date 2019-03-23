package com.liuniukeji.mixin.ui.mine.setting;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class SettingContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /**
         * 传回数据
         *
         * @param info 接口返回信息
         */
        void liftData(AuthInfo info);

        void liftData(VersionInfo info);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 获取数据
         */
        void getData();

        /**
         * 获取数据
         */
        void checkVersion();
    }


}
