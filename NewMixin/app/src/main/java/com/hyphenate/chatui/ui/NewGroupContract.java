package com.hyphenate.chatui.ui;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.io.File;


public class NewGroupContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {


        void liftData(String info);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 新建群组
         *
         * @param im_id 群组环信id
         */

        void createGroup(String im_id, String name, String description, File logo,
                         String membersonly, String is_pwd);


    }


}
