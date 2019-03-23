package com.liuniukeji.mixin.ui.home;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class MomentDetailContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回评论列表信息**/
        void liftData(MomentDetail momentDetail);

        void liftData(String info);

        /***返回单纯评论列表信息**/
        void liftData(List<CommentBean> beans);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 获取评论列表
         *
         * @param id 动态id
         */
        void getCommentList(String id);


        /**
         * 单独获取评论列表
         *
         * @param id 动态id
         */
        void getSingleCommentList(int p ,String id);


        /**
         * 提交评论
         *
         * @param moments_id   动态id
         * @param content      评论内容
         * @param to_member_id 被回复的人id
         */
        void submitComment(String moments_id, String content, String to_member_id);


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
