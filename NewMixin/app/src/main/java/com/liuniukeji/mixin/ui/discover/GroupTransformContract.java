package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.ui.mine.AttentionInfo;

import java.util.List;


public class GroupTransformContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回群成员列表信息**/
        void liftData(List<GroupMembers> infoList);

        void liftData(CommonResult result);
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
         * 群组转让
         *
         * @param group_im_id 群组的环信ID
         * @param member_id   好友id
         */
        void changeGroupOwner(String group_im_id, String member_id);


    }


}
