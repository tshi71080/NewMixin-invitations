package com.liuniukeji.mixin.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class MomentDetailPresenter implements MomentDetailContract.Presenter {
    MomentDetailContract.View mView;
    Context context;

    public MomentDetailPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getCommentList(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("id", id);
        OkGoRequest.post(UrlUtils.momentsDetail, context,
                httpParams, new JsonCallback<LazyResponse<MomentDetail>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<MomentDetail> bean, Call call, Response response) {
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
    public void getSingleCommentList(int p, String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("p", p);
        httpParams.put("id", id);
        OkGoRequest.post(UrlUtils.commentList, context,
                httpParams, new JsonCallback<LazyResponse<List<CommentBean>>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<List<CommentBean>> bean, Call call, Response response) {
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
    public void submitComment(String moments_id, String content, String to_member_id) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("moments_id", moments_id);
        httpParams.put("content", content);
        if (EmptyUtils.isNotEmpty(to_member_id)) {
            //回复的人的id,如果为空则不需上传这个
            httpParams.put("to_member_id", to_member_id);
        }

        OkGoRequest.post(UrlUtils.submitComment, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
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
    public void addOrCancelFocus(String member_id, String type) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("member_id", member_id);
        httpParams.put("type", type);
        OkGoRequest.post(UrlUtils.addOrCancelUserFocus, context,
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
    public void likeMoments(String moments_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("moments_id", moments_id);
        OkGoRequest.post(UrlUtils.likeMoments, context,
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
    public void attachView(@NonNull MomentDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
