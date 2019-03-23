package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.mine.AttentionInfo;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class InviteFriendGroupPresenter implements InviteFriendGroupContract.Presenter {
    InviteFriendGroupContract.View mView;
    Context context;

    public InviteFriendGroupPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getAttentionList(int p) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("p", p);
        OkGoRequest.post(UrlUtils.getAttentionList, context,
                httpParams, new JsonCallback<LazyResponse<List<AttentionInfo>>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<List<AttentionInfo>> bean, Call call, Response response) {
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
    public void inviteFriend(String member_id, String group_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("member_id", member_id);
        httpParams.put("group_id", group_id);

        OkGoRequest.post(UrlUtils.inviteFriends, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                            if (mView != null) {
                            CommonResult result=new CommonResult(bean.getStatus(),bean.getInfo());
                            mView.liftData(result);
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
    public void attachView(@NonNull InviteFriendGroupContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
