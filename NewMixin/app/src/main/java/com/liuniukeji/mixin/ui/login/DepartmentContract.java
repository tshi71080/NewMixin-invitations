package com.liuniukeji.mixin.ui.login;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class DepartmentContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        void liftData(List<DepartmentBean> beans);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        void lists(int p,  String school_id,String keyword);
    }

}
