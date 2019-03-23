package com.liuniukeji.mixin.ui.message;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class PhoneFriendContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回手机通讯录好友列表信息**/
        void liftFriendData(List<PhoneFriendInfo> infoList);

        void liftData(String info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {
        /**
         * 获取手机通讯录好友列表
         * @param phone 手机号; 用英文逗号拼接的字符串
         */
        void getFriendList(String phone);

        /**
         * (用户)关注和取消关注
         *
         * @param member_id 对方id
         * @param type      1加关注 2取消关注
         */
        void addOrCancelFocus(String member_id, String type);
    }


}
