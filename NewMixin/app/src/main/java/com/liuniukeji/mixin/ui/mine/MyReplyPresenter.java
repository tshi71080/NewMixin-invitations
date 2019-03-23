package com.liuniukeji.mixin.ui.mine;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class MyReplyPresenter implements MyReplyContract.Presenter {
    MyReplyContract.View mView;
    Context context;

    public MyReplyPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getReplyList(int p) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        //接口没有用到分页
        //httpParams.put("p", p);
        OkGoRequest.post(UrlUtils.getReplyList, context,
                httpParams, new JsonCallback<LazyResponse<List<ReplyInfo>>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<List<ReplyInfo>> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftData(bean.getData());
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
    public void attachView(@NonNull MyReplyContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
