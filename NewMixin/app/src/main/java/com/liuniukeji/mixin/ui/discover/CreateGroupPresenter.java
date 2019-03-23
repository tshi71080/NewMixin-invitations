package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


public class CreateGroupPresenter implements CreateGroupContract.Presenter {
    CreateGroupContract.View mView;
    Context context;

    public CreateGroupPresenter(Context ctx) {
        this.context = ctx;
    }


    @Override
    public void createGroup(GroupBean groupBean) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", groupBean.getIm_id());
        httpParams.put("name", groupBean.getName());
        httpParams.put("description", groupBean.getDescription());

        httpParams.put("logo", groupBean.getLogo());
        httpParams.put("lng", groupBean.getLng());
        httpParams.put("lat", groupBean.getLat());

        httpParams.put("membersonly", groupBean.getMembersonly());

        if(EmptyUtils.isNotEmpty(groupBean.getIs_pwd())){
            httpParams.put("is_pwd", groupBean.getIs_pwd());
            httpParams.put("password", groupBean.getPassword());
        }

        OkGoRequest.post(UrlUtils.createOrUpdateGroup, context,
                httpParams, new JsonCallback<LazyResponse<GroupBackBean>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<GroupBackBean> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            CommonResult result=new CommonResult(bean.getStatus(),bean.getInfo());
                            mView.liftData(result,bean.getData());
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
    public void attachView(@NonNull CreateGroupContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
