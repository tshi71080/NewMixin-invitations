package com.liuniukeji.mixin.ui.home;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.io.File;


public class ReportContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /**
         * 传回数据
         * @param info 返回信息
         */
        void liftData(String info);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 提交举报数据
         * @param content 举报内容
         * @param to_member_id 对方id
         * @param photo_path 举报截图文件
         */
        void submit(String content,String to_member_id,File photo_path);
    }



}
