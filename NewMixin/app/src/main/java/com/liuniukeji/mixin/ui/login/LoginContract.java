package com.liuniukeji.mixin.ui.login;


import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;

public class LoginContract {
    interface View extends BaseView {

        void toMain(UserBean userBean);//跳转到主页面

        void findPassword();//忘记密码

        void toRegister();//注册

        void getConnectSuccess(ConnectBean connectBean);

        void getRemarklist(List<MyRemarkFriendBean> remarkFriendBeanList);
    }


    interface Presenter extends BasePresenter<View> {
        void login(String mobile, String pwd,String area_code);
        void loginByWx(String openId,String userLogo,String nick_name,String lng,String lat);
        void getAllConnect();
        void getRemarkList();
    }
}
