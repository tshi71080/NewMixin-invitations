package com.liuniukeji.mixin.ui.mine.setting;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class PasswordContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /**
         * 传回数据
         *
         * @param info 接口返回信息
         */
        void liftData(String info);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 提交数据
         *
         * @param old_password 原密码
         * @param password 新密码
         * @param re_password 新密码确认
         */
        void submit(String old_password, String password,String re_password);
    }


}
