package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class VipCenterContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回会员月数列表信息**/
        void liftData(List<VipMonth> list);

        void liftData(String info);

        void liftData(UserInfo info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 学分购买会员
         * @param id 会员列表id
         */
        void buyVip(String id);

        /*** 获取VIP会员月数列表**/
        void getVipMonth();

        /*** 获取用户信息**/
        void getUserInfo();
    }


}
