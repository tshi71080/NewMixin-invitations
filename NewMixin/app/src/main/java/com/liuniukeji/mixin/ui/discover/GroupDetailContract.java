package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class GroupDetailContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        void liftData(GroupInfo groupInfo);

        void liftData(String info);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 获取群组资料
         *
         * @param im_id 群组环信id
         */

        void getGroupInfo(String im_id);

        /**
         * 申请加群
         *
         * @param group_id 群组id
         * @param password 加群密码 , 当群组开启了群密码时必传
         * @param note     申请理由
         */
        void joinInGroup(String group_id, String password, String note);



    }


}
