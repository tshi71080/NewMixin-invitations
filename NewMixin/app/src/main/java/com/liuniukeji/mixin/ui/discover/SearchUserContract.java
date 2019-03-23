package com.liuniukeji.mixin.ui.discover;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class SearchUserContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        /***返回搜索到的用户信息**/
        void liftData(List<SearchUser> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 搜索用户查询
         *
         * @param p    分页数
         * @param name 搜索用户姓名
         */
        void getSearchUser(int p, String name);

    }


}
