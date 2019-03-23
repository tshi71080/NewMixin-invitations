package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.mine.MyCodeInfo;


public class GroupCodeContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        void liftData(GroupCodeInfo codeInfo);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        void getCode(String id);
    }



}
