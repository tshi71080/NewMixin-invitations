package com.liuniukeji.mixin.ui.areacode;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.mine.AlbumContract;
import com.liuniukeji.mixin.ui.mine.AlbumInfo;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class AreaCodePresenter implements AreaCodeContract.Presenter {
    AreaCodeContract.View mView;
    Context context;

    public AreaCodePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void attachView(@NonNull AreaCodeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }


    @Override
    public void getAreacodeList() {
        OkGoRequest.get(UrlUtils.getAreaCodeUrl, context, new JsonCallback<LazyResponse<List<AreaCodeBean>>>(context, true) {
            @Override
            public void onSuccess(LazyResponse<List<AreaCodeBean>> listLazyResponse, Call call, Response response) {
                super.onSuccess(listLazyResponse, call, response);
                if (mView != null) {
                    mView.showAreacodeList(listLazyResponse.data);
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
