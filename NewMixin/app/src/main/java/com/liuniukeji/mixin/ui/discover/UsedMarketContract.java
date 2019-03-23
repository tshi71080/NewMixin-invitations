package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.home.MomentInfo;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class UsedMarketContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回列表信息**/
        void liftData(List<MomentInfo> infoList);

        void liftData(String info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取二手市场动态列表数据**/
        void getMomentList(int p);

        /**
         * (用户)关注和取消关注
         *
         * @param member_id 对方id
         * @param type      1加关注 2取消关注
         */
        void addOrCancelFocus(String member_id, String type);

        /**
         * 点赞/取消点赞
         *
         * @param moments_id 动态id
         */
        void likeMoments(String moments_id);

    }


}
