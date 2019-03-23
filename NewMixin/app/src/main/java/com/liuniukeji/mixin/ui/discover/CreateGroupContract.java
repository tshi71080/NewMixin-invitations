package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;


public class CreateGroupContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        void liftData(CommonResult result , GroupBackBean backBean);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 新建群组
         *
         * @param groupBean 群组所需
         */

        void createGroup(GroupBean groupBean);


    }


}
