package com.liuniukeji.mixin.ui.login;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class GradeContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        void liftData(List<GradeBean> beans);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        void lists();
    }



}
