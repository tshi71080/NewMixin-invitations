package com.liuniukeji.mixin.ui.home;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.login.SchoolBean;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.login.SchoolBean;

import java.util.List;


public class AddSchoolContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        void liftData(List<SchoolBean> beans);

        void liftData(String  info);

    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 获取学校列表
         * @param p
         * @param keyword
         */
        void lists(int p, String keyword);

        /**
         * 添加关注学校
         * @param school_id
         * @param type
         */
        void follow(String school_id, String type);


    }

}
