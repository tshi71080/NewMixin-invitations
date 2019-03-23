package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class InterestGroupContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回列表信息**/
        void liftData(List<InterestGroupInfo> infoList);
        void liftData(String  noticeNum);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取列表数据**/
        void getInterestGroupList(int p);

        /*** 获取群组通知数量**/
        void getGroupNoticeNum();

    }


}
