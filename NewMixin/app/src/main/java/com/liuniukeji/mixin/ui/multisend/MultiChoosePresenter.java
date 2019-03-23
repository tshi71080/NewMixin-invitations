package com.liuniukeji.mixin.ui.multisend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class MultiChoosePresenter implements MultiChooseContract.Presenter {
    MultiChooseContract.View mView;
    Context context;

    public MultiChoosePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void friendGroupMember() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.friendGroupMember, context,
                httpParams, new JsonCallback<LazyResponse<List<GroupFriendBean>>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<List<GroupFriendBean>> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftGroupFriendData(bean.getData());
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
    public void attachView(@NonNull MultiChooseContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
