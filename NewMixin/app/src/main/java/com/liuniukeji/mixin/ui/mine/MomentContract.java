package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.home.MomentInfo;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class MomentContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        /**
         * 返回列表信息
         *
         * @param infoList 列表数据
         */
        void liftData(List<MomentInfo> infoList);

        /**
         * 返回接口信息
         *
         * @param info 信息
         */
        void liftData(String info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 获取某人动态列表
         *
         * @param p         分页标识
         * @param member_id 对方id
         */

        void getSomeoneMomentList(int p, String member_id);


        /**
         * 删除动态
         *
         * @param id 动态id
         */
        void delMoment(String id);


    }


}
