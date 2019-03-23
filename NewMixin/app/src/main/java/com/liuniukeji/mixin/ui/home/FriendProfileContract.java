package com.liuniukeji.mixin.ui.home;


import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class FriendProfileContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回好友详情资料**/
        void liftData(FriendUserInfo userInfo);

        void liftData(String info);

        void liftAddBlack(String info);

        void setMarkSuccess();

        void getRemarklist(List<MyRemarkFriendBean> remarkFriendBeanList);

        void applySucess();

        void deleteSuccess();

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取好友详情资料**/
        void getFriendInfo(String member_id,String userName);

        /**
         * (用户)关注和取消关注
         *
         * @param member_id 对方id
         * @param type      1加关注 2取消关注
         */
        void addOrCancelFocus(String member_id, String type);

        /**
         * 拉黑操作
         * @param to_member_id 对方id
         * @param action 拉黑时 add ; 取消拉黑时: cancel
         */
        void addBlack(String to_member_id, String action);

        void setFriedmark(String memberId,String mark);

        void getRemarkList();

        void addFriend(String id);

        void deleteFriend(String id);

    }

}
