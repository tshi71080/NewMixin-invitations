package com.liuniukeji.mixin.ui.pub.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.OSSOperUtils;
import com.liuniukeji.mixin.util.currency.FileUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.OSSOperUtils;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


public class PublicVideoPresenter implements PubVideoContract.Presenter {
    Context context;
    PubVideoContract.View mView;

    public PublicVideoPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(@NonNull PubVideoContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void putVideo(String status, String tag, String content, String video) {
        if (AccountUtils.getUser(context) == null || TextUtils.isEmpty(AccountUtils.getUserToken(context))) {
            return;
        }
        //--------------------------------------上传OSS---------------------------------------
        String userId = AccountUtils.getUser(context).getId();
        String houZhui = " ";
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
        if (!TextUtils.isEmpty(video)) {
            houZhui = FileUtils.getFileExtension(video);

            OSSOperUtils.newInstance(context).putObjectMethod("Video/" + userId + "/" + currentTime + "." + houZhui, video);
            //OSSOperUtils.newInstance(context).putObjectMethod("Picture/" + userId + "/" + currentTime + ".jpg", photo);
        }
        //--------------------------------------上传OSS---------------------------------------


        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        // 	0公开 1私密
        httpParams.put("status", status);
        //1,图文；2，视频
        httpParams.put("type", "2");
        //0,普通朋友圈，1，二手市场，2，学弟学妹提问
        httpParams.put("tag", tag);
        httpParams.put("lng", AccountUtils.getLocationInfo(context).getLng());
        httpParams.put("lat", AccountUtils.getLocationInfo(context).getLat());
        httpParams.put("content", content);
        httpParams.put("video_path", "Video/" + userId + "/" + currentTime + "." + houZhui);

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
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                            mView.openText();
                        }
                    }
                });
    }

}
