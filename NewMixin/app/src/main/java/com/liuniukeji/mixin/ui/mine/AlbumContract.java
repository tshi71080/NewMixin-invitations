package com.liuniukeji.mixin.ui.mine;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;

import java.util.List;


public class AlbumContract {

    /**
     * 界面处理逻辑
     */
    interface View extends BaseView {

        /**
         * 返回列表信息
         *
         * @param infoList 列表数据
         */
        void liftData(List<AlbumInfo> infoList);

    }

    /***业务处理逻辑**/
    interface Presenter extends BasePresenter<View> {

        /**
         * 获取某人相册列表
         *
         * @param p         分页标识
         * @param member_id 对方id
         */

        void getSomeoneAlbumList(int p, String member_id);




    }


}
