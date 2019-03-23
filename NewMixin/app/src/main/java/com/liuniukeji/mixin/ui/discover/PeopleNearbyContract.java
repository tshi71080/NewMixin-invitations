package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class PeopleNearbyContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回列表信息**/
        void liftData(List<PeopleNearbyInfo> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取列表数据**/
        void getPeopleNearbyList(int p, String lng, String lat);

    }


}
