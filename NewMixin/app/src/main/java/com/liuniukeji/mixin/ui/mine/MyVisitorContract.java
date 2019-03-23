package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class MyVisitorContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回访客列表信息**/
        void liftData(List<VisitorInfo> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取访客列表**/
        void getVisitorList(int p);

    }


}
