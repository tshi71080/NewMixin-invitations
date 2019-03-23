package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.mine.MyCodeContract;
import com.liuniukeji.mixin.ui.mine.MyCodeInfo;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


public class GroupCodePresenter implements GroupCodeContract.Presenter {
    GroupCodeContract.View mView;
    Context context;

    public GroupCodePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getCode(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", id);
        OkGoRequest.post(UrlUtils.getGroupCode, context,
                httpParams, new JsonCallback<LazyResponse<GroupCodeInfo>>() {
                    @Override
                    public void onSuccess(LazyResponse<GroupCodeInfo> bean, Call call, Response response) {
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
    public void attachView(@NonNull GroupCodeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
