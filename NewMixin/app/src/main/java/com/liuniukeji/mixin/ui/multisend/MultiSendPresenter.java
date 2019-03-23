package com.liuniukeji.mixin.ui.multisend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class MultiSendPresenter implements MultiSendContract.Presenter {
    MultiSendContract.View mView;
    Context context;

    public MultiSendPresenter(Context ctx) {
        this.context = ctx;
    }


    @Override
    public void attachView(@NonNull MultiSendContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void sendTextMsg(int type, String im_names, String content) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("type", type);
        httpParams.put("im_names", im_names);
        httpParams.put("content", content);
        OkGoRequest.post(UrlUtils.sendMultiMsg, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftData(bean.getInfo());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                        }
                    }
                });
    }

    @Override
    public void sendPicMsg(int type, String im_names, File image) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("type", type);
        httpParams.put("im_names", im_names);
        httpParams.put("image", image);
        OkGoRequest.post(UrlUtils.sendMultiMsg, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftData(bean.getInfo());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                        }
                    }
                });
    }

    @Override
    public void sendVoiceMsg(int type, String im_names, File voice, int voice_time) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("type", type);
        httpParams.put("im_names", im_names);
        httpParams.put("voice", voice);
        httpParams.put("voice_time", voice_time);
        OkGoRequest.post(UrlUtils.sendMultiMsg, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftData(bean.getInfo());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                        }
                    }
                });
    }
}
