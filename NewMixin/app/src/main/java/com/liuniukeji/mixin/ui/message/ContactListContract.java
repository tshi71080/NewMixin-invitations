package com.liuniukeji.mixin.ui.message;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class ContactListContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回列表信息**/
        //void liftGroupData(List<GroupInfo> infoList);

        //void liftMemberData(List<GroupMemberInfo> infoList);

        void liftGroupFriendData(List<GroupFriendInfo> infoList);

        void liftMoveInfo(String info);

        void liftBuildGroupInfo(String info);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取分组列表**/
        void getGroupList();

        /*** 获取分组成员列表**/
        void getMemberList(String friend_group_id);

        /*** 获取分组及分组下好友**/
        void friendGroupMember();


        /**
         * 移动用户到指定分组
         *
         * @param member_id       对方id
         * @param friend_group_id 我的分组id
         * @param is_quietly      是否悄悄关注: 0不是 1是
         */
        void moveToGroup(String member_id, String friend_group_id, String is_quietly);


        /***新建分组**/
        void buildGroup(String name);



    }


}
