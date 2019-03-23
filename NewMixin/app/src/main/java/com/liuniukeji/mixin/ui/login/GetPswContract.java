package com.liuniukeji.mixin.ui.login;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

public class GetPswContract {

    interface View extends BaseView {
        /**
         * 验证码
         */
        void phoneCodeState(boolean state);

        void liftData(String state);
    }


    interface Presenter extends BasePresenter<View> {

        /**
         * 密码找回
         *
         * @param phone    手机号
         * @param code     验证码
         * @param password 新密码
         */
        void getBack(String phone, String code, String password,String area_code);

        /**
         * 获取验证码
         *
         * @param phone 手机号
         * @param type  1.注册;2找回密码;3修改手机号
         */
        void getCode(String phone, int type);

        /**
         * 验证手机验证码
         */
        void checkCode(String user_phone, String sms_code);

    }
}
