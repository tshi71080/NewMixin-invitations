package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.ui.mine.AttentionInfo;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;

import java.util.List;


public class InviteFriendGroupContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回关注列表信息**/
        void liftData(List<AttentionInfo> infoList);

        void liftData(CommonResult result);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取关注列表**/
        void getAttentionList(int p);

        /**
         * 邀请好友
         * @param member_id 好友id
         * @param group_id 群组IM id
         */
        void inviteFriend(String member_id,String group_id);


    }


}
