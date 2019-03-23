package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class MyScoreContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回学分列表信息**/
        void liftData(List<ScoreInfo> list);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取学分信息**/
        void getScoreInfo();
    }


}
