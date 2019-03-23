package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class MyFansContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回粉丝列表信息**/
        void liftData(List<FansInfo> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取粉丝列表**/
        void getFansList(int p);

    }


}
