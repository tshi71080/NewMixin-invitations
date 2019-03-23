package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class RecommendContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        /***返回搜索到的用户信息**/
        void liftData(List<RecommendUser> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 推荐用户列表
         */
        void getRecommendUser();

    }

}
