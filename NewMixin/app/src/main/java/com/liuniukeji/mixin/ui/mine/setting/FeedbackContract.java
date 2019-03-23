package com.liuniukeji.mixin.ui.mine.setting;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;


public class FeedbackContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /**
         * 传回数据
         * @param id 反馈id
         */
        void liftData(String id);
    }

    /**
     * 业务处理逻辑
     */
    interface Presenter extends BasePresenter<View> {
        /**
         * 提交反馈数据
         * @param content 反馈内容
         */
        void submit(String content);
    }



}
