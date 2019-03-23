package com.liuniukeji.mixin.ui.mine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.home.FriendUserInfo;
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


public class MyMomentPresenter implements MyMomentContract.Presenter {
    MyMomentContract.View mView;
    Context context;

    public MyMomentPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getUserInfo(String member_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("member_id", member_id);
        OkGoRequest.post(UrlUtils.getSomebodyInfo, context,
                httpParams, new JsonCallback<LazyResponse<FriendUserInfo>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<FriendUserInfo> bean, Call call, Response response) {
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
    public void changeCover(String sourcePath) {

        if (AccountUtils.getUser(context) == null ||
                TextUtils.isEmpty(AccountUtils.getUser(context).getToken())) {
            return;
        }

        //----------------------------------AliOSS-----------------------------------------------
        String userId = AccountUtils.getUser(context).getId();
        String houZhui = " ";
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);

        houZhui = FileUtils.getFileExtension(sourcePath);

        String url = "Picture/" + userId + "/" + currentTime + "." + houZhui;
        OSSOperUtils.newInstance(context).putObjectMethod(url, sourcePath);
        //sb.append(OSSOperUtils.AliYunOSSURLFile);

        //----------------------------------AliOSS-----------------------------------------------

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("cover_path", url);
        OkGoRequest.post(UrlUtils.updateCover, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true)

                {
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
    public void attachView(@NonNull MyMomentContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
