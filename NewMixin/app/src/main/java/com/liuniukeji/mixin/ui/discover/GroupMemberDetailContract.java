package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;


public class GroupMemberDetailContract {

    /**
     * 界面处理逻辑
     */
    public interface View extends BaseView {

        void liftData(GroupInfo groupInfo);

        void liftData(CommonResult result);

        void liftUpdateData(GroupBackBean bean,CommonResult result);

        void muteSucess();

        void getStatusSucess(String status);

    }

    /***业务处理逻辑**/
    public interface Presenter extends BasePresenter<View> {

        /**
         * 获取群组资料
         *
         * @param im_id 群组环信id
         */

        void getGroupInfo(String im_id);

        /**
         * 退出群组
         * @param group_im_id 群组环信id
         */
        void exitGroup(String group_im_id);

        /**
         * 解散群组
         * @param im_id 群组环信id
         */
        void dropGroup(String im_id);

        /**
         * 修改群组资料（逐项修改）
         * @param groupBean 群资料
         */
        void updateGroup(GroupBean groupBean);

        //群禁言
        void muteAll(String group_im_id,String type);

        //获取禁言状态
        void getMuteStatus(String group_im_id);
    }


}
