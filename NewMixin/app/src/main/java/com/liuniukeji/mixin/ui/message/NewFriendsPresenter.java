package com.liuniukeji.mixin.ui.message;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class NewFriendsPresenter implements NewFriendsContract.Presenter {
    NewFriendsContract.View mView;
    Context context;

    public NewFriendsPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void attachView(@NonNull NewFriendsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        OkGo.getInstance().cancelTag(context);
    }

    @Override
    public void getNewFriendsList() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.getNewFriedsApply, context,
                httpParams, new JsonCallback<LazyResponse<List<NewFriendsBean>>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<List<NewFriendsBean>> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.showdata(bean.getData());
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
    public void addFriend(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("id", id);
        OkGoRequest.post(UrlUtils.addNewFriends, context,
                httpParams, new JsonCallback<LazyResponse<String>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<String> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.addSuccess();
                        }
                        ToastUtils.showLongToast(bean.getInfo());
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
