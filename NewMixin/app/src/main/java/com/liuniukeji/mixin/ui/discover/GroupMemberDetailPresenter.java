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


public class GroupMemberDetailPresenter implements GroupMemberDetailContract.Presenter {
    GroupMemberDetailContract.View mView;
    Context context;
    boolean extension = false;
    int change_type=0;

    public GroupMemberDetailPresenter(Context ctx) {
        this.context = ctx;
    }


    @Override
    public void getGroupInfo(String im_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", im_id);
        OkGoRequest.post(UrlUtils.getGroupInfo, context,
                httpParams, new JsonCallback<LazyResponse<GroupInfo>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<GroupInfo> bean, Call call, Response response) {
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
    public void exitGroup(String group_im_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("group_im_id", group_im_id);

        OkGoRequest.post(UrlUtils.quitGroup, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            CommonResult result = new CommonResult(bean.getStatus(), bean.getInfo());
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
    public void dropGroup(String im_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", im_id);
        OkGoRequest.post(UrlUtils.dropGroup, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            CommonResult result = new CommonResult(bean.getStatus(), bean.getInfo());
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
    public void updateGroup(GroupBean groupBean) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", groupBean.getIm_id());

        //修改的类型： 1群头像 2群名称 3群简介 4 群密码 5屏蔽群消息 6群审批
        switch (groupBean.getChangeType()) {
            case 1:
                httpParams.put("logo", groupBean.getLogo());
                extension = true;
                change_type=1;
                break;
            case 2:
                httpParams.put("name", groupBean.getName());
                extension = true;
                change_type=2;
                break;
            case 3:
                httpParams.put("description", groupBean.getDescription());
                break;
            case 4:
                //群密码
                if (EmptyUtils.isNotEmpty(groupBean.getIs_pwd())) {
                    httpParams.put("password", groupBean.getPassword());
                    httpParams.put("is_pwd", groupBean.getIs_pwd());
                }
                break;
            case 5:
                //屏蔽群消息
                httpParams.put("reminder", groupBean.getReminder());
                break;
            case 6:
                //群审批
                httpParams.put("membersonly", groupBean.getMembersonly());
                break;
            default:
                break;
        }
        OkGoRequest.post(UrlUtils.createOrUpdateGroup, context,
                httpParams, new JsonCallback<LazyResponse<GroupBackBean>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<GroupBackBean> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            CommonResult result = new CommonResult(bean.getStatus(), bean.getInfo());
                            GroupBackBean backBean = bean.getData();
                            backBean.setExtension(extension);
                            backBean.setChange_type(change_type);
                            mView.liftUpdateData(backBean, result);
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
     * 禁言
     * @param group_im_id
     * @param type
     */
    @Override
    public void muteAll(String group_im_id, String type) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("group_im_id", group_im_id);
        httpParams.put("type", type);
        OkGoRequest.post(UrlUtils.muteAllUrl,context,httpParams,new JsonCallback<LazyResponse<Void>>(context,false){
            @Override
            public void onSuccess(LazyResponse<Void> voidLazyResponse, Call call, Response response) {
                super.onSuccess(voidLazyResponse, call, response);
                if (mView != null) {
                    mView.muteSucess();
                }
            }
        });
    }

    /**
     * 获取禁言状态
     * @param group_im_id
     */
    @Override
    public void getMuteStatus(String group_im_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("group_im_id", group_im_id);
        OkGoRequest.post(UrlUtils.getMuteStatus,context,httpParams,new JsonCallback<LazyResponse<String>>(context,false){
            @Override
            public void onSuccess(LazyResponse<String> voidLazyResponse, Call call, Response response) {
                super.onSuccess(voidLazyResponse, call, response);
                if (mView != null) {
                    mView.getStatusSucess(voidLazyResponse.getData());
                }
            }
        });
    }

    @Override
    public void attachView(@NonNull GroupMemberDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }


}
