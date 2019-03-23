package com.liuniukeji.mixin.ui.multisend;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class MultiChooseContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回列表信息**/
        void liftGroupFriendData(List<GroupFriendBean> infoList);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取分组及分组下好友**/
        void friendGroupMember();

    }


}
