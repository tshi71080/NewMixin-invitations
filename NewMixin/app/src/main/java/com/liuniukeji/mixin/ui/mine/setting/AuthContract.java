package com.liuniukeji.mixin.ui.mine.setting;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.io.File;

/**
 * 实名认证
 */
public class AuthContract {

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
         */
        void submit(AuthInfo authInfo, File file1,File file2);
    }


}
