package com.liuniukeji.mixin.ui.pub.note;


import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageItem;
import com.liuniukeji.mixin.base.BasePresenter;
import com.liuniukeji.mixin.base.BaseView;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageItem;

import java.util.List;


public class PubNotesContract {
    interface View extends BaseView {
        void refreshData();
    }

    interface Presenter extends BasePresenter<View> {
        void putPhoto(String status, String tag,String content, List<ImageItem> mDataList);
    }


}
