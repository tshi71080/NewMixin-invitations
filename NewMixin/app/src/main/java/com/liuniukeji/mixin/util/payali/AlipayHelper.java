package com.liuniukeji.mixin.util.payali;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;

import com.alipay.sdk.app.PayTask;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description 支付宝支付辅助
 * @Author wanghaijun QQ:1819005139
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date 2018/1/23
 * @CreateBy Android Studio
 * @ModifiedBy // 修改作者, 联系方式, 修改日期 [无修改作者, 可为空]
 */
public class AlipayHelper {

    private Context mContext;
    public ProgressDialog progressDialog;// 加载框

    public AlipayHelper(Context context) {
        mContext = context;
        setPro();
    }

    private void setPro() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("请求网络中...");
    }

    /**
     * 购买学分
     *
     * @param id 学分购买选项ID
     *           pay_code 付款方式1 支付宝 2 微信
     */
    public void buyScore(String id) {

        String token = AccountUtils.getUserToken(mContext);
        if (EmptyUtils.isNotEmpty(token)) {
            progressDialog.show();
            HttpParams httpParams = new HttpParams();
            httpParams.put("token", token);
            httpParams.put("id", id);
            httpParams.put("pay_code", '1');

            OkGoRequest.post(UrlUtils.rechargePoints, mContext, httpParams, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.optInt("status") == 1) {
                            String data = jsonObject.optString("data");
                            aliPay(data);
                        } else {
                            ToastUtils.showLongToastSafe(jsonObject.optString("info"));
                            progressDialog.dismiss();
                            EventBus.getDefault().post(Constants.ALI_PAY_STATE.ON_SUCCESS);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    progressDialog.dismiss();
                    EventBus.getDefault().post(Constants.ALI_PAY_STATE.ON_ERROR);
                }
            });
        } else {
            ToastUtils.showShortToastSafe("请先登录");
            EventBus.getDefault().post(Constants.ALI_PAY_STATE.OTHER);

        }

    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private void aliPay(String data) {
        // 订单信息
        final String orderInfo = data;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
                String result = alipay.pay(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
        progressDialog.dismiss();
    }

    @SuppressWarnings("all")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtils.showShortToastSafe("支付成功");


                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                        // 最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.showShortToastSafe("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.showShortToastSafe("支付失败,请稍后重试");
                        }
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj,
                            true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") &&
                            TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        ToastUtils.showShortToastSafe("授权成功\n" +
                                String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        ToastUtils.showShortToastSafe("授权失败" +
                                String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

}
