package com.liuniukeji.mixin.ui.pub.forward;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


public class ForwardPresenter implements ForwardContract.Presenter {
    Context context;
    ForwardContract.View mView;

    public ForwardPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(@NonNull ForwardContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void forward(String status, String tag, String content, String forward_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        // 	0公开 1私密
        httpParams.put("status", status);
        //1,图文；2，视频
        //httpParams.put("type", "1");

        //0,普通朋友圈，1，二手市场，2，学弟学妹提问
        httpParams.put("tag", tag);
        httpParams.put("lng", AccountUtils.getLocationInfo(context).getLng());
        httpParams.put("lat", AccountUtils.getLocationInfo(context).getLat());
        httpParams.put("content", content);
        httpParams.put("forward_id", forward_id);

        OkGoRequest.post(UrlUtils.postDynamics, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            CommonResult result=new CommonResult(bean.getStatus(),bean.getInfo());
                            mView.refreshData(result);
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
