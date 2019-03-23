package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;

import java.util.List;


public class GroupMembersContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        void liftData(List<GroupMembers> membersList);

        void liftData(CommonResult result);

        void operateSucess();

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 获取群组成员列表
         *
         * @param im_id 群组环信id
         */

        void getGroupMemberList(int p, String im_id);

        /**
         * 移除群成员
         * @param group_im_id 群组环信id
         * @param member_im_name 用户环信id
         */
        void removeMember(String group_im_id,String member_im_name);
        void addToAdmin(String group_im_id,String member_im_name);
        void removeFromAdmin(String group_im_id,String member_im_name);
    }


}
