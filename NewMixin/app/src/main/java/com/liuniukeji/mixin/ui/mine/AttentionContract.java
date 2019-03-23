package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class AttentionContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回关注列表信息**/
        void liftData(List<AttentionInfo> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取关注列表**/
        void getAttentionList(int p);

    }


}
