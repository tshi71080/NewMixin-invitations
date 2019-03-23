package com.liuniukeji.mixin.ui.home;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class HomeMomentContract {

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

        /**
         * 获取其他关注学校动态列表数据
         *
         * @param p         分页标识
         * @param school_id 学校id
         */

        void getOtherSchoolMomentList(int p, String school_id);

        /*** 获取推荐动态列表数据**/
        void getRecommendMomentList(int p);

        /*** 获取校园动态列表数据**/
        void getSchoolMomentList(int p);

        /*** 获取关注动态列表数据**/
        void getFocusMomentList(int p);

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
