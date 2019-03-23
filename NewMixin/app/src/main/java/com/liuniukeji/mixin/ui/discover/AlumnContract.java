package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class AlumnContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回校友录信息**/
        void liftData(AlumnInfo info);

        /***返回校友录定位到我信息**/
        void liftData(AlumnMe info);

        /***返回校友录院系人员信息**/
        void liftData(List<AlumnMember> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /*** 获取校友录数据**/
        void getAlumnInfo();

        /*** 校友录定位到我**/
        void locationMe();

        /*** 校友录院系人员查询**/
        void getMember(String school_class, String school_department_id);


    }


}
