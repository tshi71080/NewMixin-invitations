package com.liuniukeji.mixin.ui.message;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class FriendEditContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回分组列表信息**/
        void liftGroupData(List<GroupInfo> infoList);

        void liftMoveInfo(String info);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {
        /*** 获取分组列表**/
        void getGroupList();

        /**
         * 移动用户到指定分组
         *
         * @param member_id       对方id
         * @param friend_group_id 我的分组id
         * @param is_quietly      是否悄悄关注: 0不是 1是
         */
        void moveToGroup(String member_id, String friend_group_id, String is_quietly);
    }


}
