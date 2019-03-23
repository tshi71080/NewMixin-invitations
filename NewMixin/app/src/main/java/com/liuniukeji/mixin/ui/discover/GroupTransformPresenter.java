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


public class GroupTransformPresenter implements GroupTransformContract.Presenter {
    GroupTransformContract.View mView;
    Context context;

    public GroupTransformPresenter(Context ctx) {
        this.context = ctx;
    }




    @Override
    public void getGroupMemberList(int p, String im_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", im_id);
        httpParams.put("p", p);
        OkGoRequest.post(UrlUtils.getGroupMemberList, context,
                httpParams, new JsonCallback<LazyResponse<List<GroupMembers>>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<List<GroupMembers>> bean, Call call, Response response) {
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
    public void changeGroupOwner(String group_im_id,String member_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("group_im_id", group_im_id);
        httpParams.put("member_id", member_id);

        OkGoRequest.post(UrlUtils.changeGroupOwner, context,
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
    public void attachView(@NonNull GroupTransformContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
