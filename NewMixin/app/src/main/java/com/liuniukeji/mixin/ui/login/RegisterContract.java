package com.liuniukeji.mixin.ui.login;


import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.net.CommonResult;

import java.util.List;

public class RegisterContract {

    interface View extends BaseView {
        /**
         * 验证码
         */
        void phoneCodeState(boolean state);

        void liftData(UserBean userBean);

        void getConnectSuccess(ConnectBean connectBean);

        void getRemarklist(List<MyRemarkFriendBean> remarkFriendBeanList);

    }


    interface Presenter extends BasePresenter<View> {
        /**
         * 注册
         *
         * @param info 注册所需信息
         */
        void register(RegisterInfo info);

        /**
         * 获取验证码
         *
         * @param phone 手机号
         * @param type  1.注册;2找回密码;3修改手机号
         */
        void getCode(String phone, int type,String area_code);

        /**
         * 验证手机验证码
         */
        void checkCode(String user_phone, String sms_code);

        void getAllConnect();
        void getRemarkList();

    }
}
