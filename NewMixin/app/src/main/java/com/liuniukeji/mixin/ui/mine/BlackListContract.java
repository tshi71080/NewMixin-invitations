package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class BlackListContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回黑名单列表信息**/
        void liftData(List<BlackInfo> blackInfoList);

        void liftData(String info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取黑名单列表**/
        void getBlackList(int p);

        /**
         * 解除拉黑
         *
         * @param to_member_id 对方id
         * @param action       拉黑时 add ; 取消拉黑时: cancel
         */
        void cancelBlack(String to_member_id, String action);

    }


}
