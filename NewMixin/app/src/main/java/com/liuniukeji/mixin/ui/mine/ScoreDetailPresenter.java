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

import okhttp3.Call;
import okhttp3.Response;


public class ScoreDetailPresenter implements ScoreDetailContract.Presenter {
    ScoreDetailContract.View mView;
    Context context;

    public ScoreDetailPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getScoreDetailList() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("type", "3");
        OkGoRequest.post(UrlUtils.getScoreList, context,
                httpParams, new JsonCallback<LazyResponse<ScoreDetail>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<ScoreDetail> bean, Call call, Response response) {
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
    public void attachView(@NonNull ScoreDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
