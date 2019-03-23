package com.liuniukeji.mixin.ui.multisend;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.io.File;
import java.util.List;


public class MultiSendContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {
        /***返回信息**/
        void liftData(String info);
    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {


        void sendTextMsg(int type, String im_names, String content);

        void sendPicMsg(int type, String im_names, File image);

        void sendVoiceMsg(int type, String im_names, File voice, int voice_time);

    }




}
