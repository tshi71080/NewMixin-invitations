package com.liuniukeji.mixin.ui.pub.video;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

public class PubVideoContract {
    interface View extends BaseView {
        void refreshData();
        void openText();
    }

    interface Presenter extends BasePresenter<View> {
        void putVideo(String status, String tag,String content, String video);
    }


}
