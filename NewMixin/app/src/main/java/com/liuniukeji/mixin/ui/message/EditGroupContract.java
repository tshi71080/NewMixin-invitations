package com.liuniukeji.mixin.ui.message;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class EditGroupContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        /***返回信息**/
        void liftBuildGroupInfo(String info);
        void liftDelGroupInfo(String info);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {


        /**
         * 新建分组 or 编辑分组
         *
         * @param type  1新建 2编辑
         * @param name  分组名称
         * @param color 分组颜色值
         * @param id    分组id
         */
        void buildGroup(int type, String name, String color, String id);

        void deleteGroup(String id);

    }


}
