package com.liuniukeji.mixin.ui.mine.setting;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

/**
 * 实名认证检查
 */

public class AuthCheckContract {

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
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 获取数据
         */
        void getData();
    }


}
