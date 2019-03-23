package com.liuniukeji.mixin.ui.home;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class HomeContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回关注学校列表信息**/
        void liftSchoolData(List<FollowSchoolInfo> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取关注学校列表**/
        void getSchoolList();

    }




}
