package com.liuniukeji.mixin.ui.areacode;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class AreaCodeContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        void showAreacodeList(List<AreaCodeBean> beans);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        void getAreacodeList();
    }

}
