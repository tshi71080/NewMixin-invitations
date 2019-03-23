package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class MyCodeContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        void liftData(MyCodeInfo codeInfo);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        void getCode();
    }



}
