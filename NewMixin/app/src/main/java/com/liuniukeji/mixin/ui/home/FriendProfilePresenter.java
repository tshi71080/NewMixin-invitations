package com.liuniukeji.mixin.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class FriendProfilePresenter implements FriendProfileContract.Presenter {
    FriendProfileContract.View mView;
    Context context;

    public FriendProfilePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getFriendInfo(String member_id,String username) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        if(EmptyUtils.isNotEmpty(member_id)){
            httpParams.put("member_id", member_id);
        }
        if(EmptyUtils.isNotEmpty(username)){
            httpParams.put("im_name", username);
        }
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
    public void addBlack(String to_member_id, String action) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("to_member_id", to_member_id);
        httpParams.put("action", action);
        OkGoRequest.post(UrlUtils.addOrCancelBlack, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftAddBlack(bean.getInfo());
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

    /**
     * 修改好友备注
     * @param mark
     */
    @Override
    public void setFriedmark(String memberId,String mark) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("friend_id", memberId);
        httpParams.put("remark", mark);
        OkGoRequest.post(UrlUtils.changeRemarkUrl, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.setMarkSuccess();
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
    public void getRemarkList() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.getRemarkList, context, httpParams, new JsonCallback<LazyResponse<List<MyRemarkFriendBean>>>(context,false) {
            @Override
            public void onSuccess(LazyResponse<List<MyRemarkFriendBean>> lazyResponse, Call call, Response response) {
                super.onSuccess(lazyResponse, call, response);
                if(mView != null && lazyResponse.getStatus() == 1) {
                    mView.getRemarklist(lazyResponse.getData());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });

    }

    /**
     * 申请添加好友
     * @param id
     */
    @Override
    public void addFriend(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("member_id", id);
        OkGoRequest.post(UrlUtils.applyAddNewFriends, context, httpParams, new JsonCallback<LazyResponse<String>>(context,false) {
            @Override
            public void onSuccess(LazyResponse<String> lazyResponse, Call call, Response response) {
                super.onSuccess(lazyResponse, call, response);
                if(mView != null && lazyResponse.getStatus() == 1) {
                    mView.applySucess();
                }
                ToastUtils.showLongToast(lazyResponse.getInfo());
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    /**
     * 删除好友
     * @param id
     */
    @Override
    public void deleteFriend(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("member_id", id);
        OkGoRequest.post(UrlUtils.deleteFriends, context, httpParams, new JsonCallback<LazyResponse<String>>(context,false) {
            @Override
            public void onSuccess(LazyResponse<String> lazyResponse, Call call, Response response) {
                super.onSuccess(lazyResponse, call, response);
                if(mView != null && lazyResponse.getStatus() == 1) {
                    mView.deleteSuccess();
                }
                ToastUtils.showLongToast(lazyResponse.getInfo());
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }


    @Override
    public void attachView(@NonNull FriendProfileContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
