package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class ScoreDetailContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回会学分变动详情列表信息**/
        void liftData(ScoreDetail scoreDetail);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取学分变动详情列表**/
        void getScoreDetailList();

    }


}
