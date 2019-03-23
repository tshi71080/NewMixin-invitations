package com.liuniukeji.mixin.ui.mine;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.home.MomentInfo;
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


public class MomentPresenter implements MomentContract.Presenter {
    MomentContract.View mView;
    Context context;

    public MomentPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getSomeoneMomentList(int p, String member_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("member_id", member_id);
        httpParams.put("p", p);
        OkGoRequest.post(UrlUtils.getSomebodyMoments, context,
                httpParams, new JsonCallback<LazyResponse<List<MomentInfo>>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<List<MomentInfo>> bean, Call call, Response response) {
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
    public void delMoment(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("id", id);
        OkGoRequest.post(UrlUtils.delMoments, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
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
    public void attachView(@NonNull MomentContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }


}
