package com.liuniukeji.mixin.ui.pub.note;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageItem;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.OSSOperUtils;
import com.liuniukeji.mixin.util.currency.FileUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageItem;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.OSSOperUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class PublicNotesPresenter implements PubNotesContract.Presenter {
    Context context;
    PubNotesContract.View mView;

    public PublicNotesPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(@NonNull PubNotesContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }


    @Override
    public void putPhoto(String status, String tag,String content, List<ImageItem> mDataList) {
        if (AccountUtils.getUser(context) == null ||
                TextUtils.isEmpty(AccountUtils.getUser(context).getToken())) {
            return;
        }

        //----------------------------------上传OSS-----------------------------------------------
        String userId = AccountUtils.getUser(context).getId();
        String houZhui = " ";
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);

        StringBuilder sb=new StringBuilder();
        if (null!=mDataList&&mDataList.size()>0) {

            for (int i = 0; i < mDataList.size(); i++) {
                String imagePath = mDataList.get(i).sourcePath;

                houZhui = FileUtils.getFileExtension(imagePath);

                String url="Picture/" + userId + "/" + currentTime+i+ "." + houZhui;
                OSSOperUtils.newInstance(context).putObjectMethod(url, imagePath);
                //sb.append(OSSOperUtils.AliYunOSSURLFile);
                sb.append(url);
                sb.append(",");
            }
        }
        //----------------------------------上传OSS-----------------------------------------------

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        // 	0公开 1私密
        httpParams.put("status", status);
        //1,图文；2，视频
        httpParams.put("type", "1");
        //0,普通朋友圈，1，二手市场，2，学弟学妹提问
        httpParams.put("tag", tag);
        httpParams.put("lng", AccountUtils.getLocationInfo(context).getLng());
        httpParams.put("lat", AccountUtils.getLocationInfo(context).getLat());
        httpParams.put("content", content);
        httpParams.put("photoStr", sb.toString());

        OkGoRequest.post(UrlUtils.postDynamics, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.refreshData();
                        }
                    }

                    @Override
                    public void onError(Call call, okhttp3.Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                        }
                    }
                });

    }
}
