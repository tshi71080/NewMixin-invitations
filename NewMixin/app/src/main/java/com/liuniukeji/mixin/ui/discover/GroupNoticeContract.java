package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;

import java.util.List;


public class GroupNoticeContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回通知列表信息**/
        void liftData(List<GroupNoticeInfo> infoList);

        void liftData(CommonResult commonResult);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取通知列表**/
        void getNoticeList(int p);

        /**
         * 申请加群处理
         * @param id 申请id
         * @param type 1同意 2拒绝
         */
        void auditMember(String id,String type);

    }


}
