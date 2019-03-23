package com.liuniukeji.mixin.ui.pub.forward;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;


public class ForwardContract {

    interface View extends BaseView {
        void refreshData(CommonResult commonResult);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * 转发动态
         *
         * @param status     0公开 1私密
         * @param tag        0,普通朋友圈，1，二手市场，2，学弟学妹提问
         * @param content    内容
         * @param forward_id 动态转发的来源ID
         */
        void forward(String status, String tag, String content, String forward_id);
    }


}
