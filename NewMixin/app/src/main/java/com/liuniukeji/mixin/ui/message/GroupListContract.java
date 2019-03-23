package com.liuniukeji.mixin.ui.message;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class GroupListContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回分组列表信息**/
        void liftGroupData(List<GroupInfo> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {
        /*** 获取分组列表**/
        void getGroupList();
    }


}
