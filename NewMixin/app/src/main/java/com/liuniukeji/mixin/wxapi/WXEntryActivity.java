package com.liuniukeji.mixin.wxapi;

import com.hyphenate.chatui.Constant;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.SimpleResponse;
import com.liuniukeji.mixin.net.StringCallback;
import com.liuniukeji.mixin.ui.login.WeChatInfoBean;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.Convert;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Constraints;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{


	public  int WX_LOGIN = 1;

	private IWXAPI iwxapi;

	private SendAuth.Resp resp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		//接收到分享以及登录的intent传递handleIntent方法，处理结果
		iwxapi.handleIntent(getIntent(), this);

	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	//请求回调结果处理
	@Override
	public void onResp(BaseResp baseResp) {
		System.out.println("------------------------" + baseResp.getType());
		//微信登录为getType为1，分享为0
		if (baseResp.getType() == WX_LOGIN){
			//登录回调
			//System.out.println("------------登陆回调------------");
			resp = (SendAuth.Resp) baseResp;
			//System.out.println("------------登陆回调的结果------------：" +  new Gson().toJson(resp));


			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					String code = String.valueOf(resp.code);
					//获取用户信息
					getAccessToken(code);
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
					Toast.makeText(WXEntryActivity.this, "用户拒绝授权", Toast.LENGTH_LONG).show();
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
					Toast.makeText(WXEntryActivity.this, "用户取消登录", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
		}else{
			//分享成功回调
			System.out.println("------------分享回调------------");
			switch (baseResp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					//分享成功
					Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_LONG).show();
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					//分享取消
					Toast.makeText(WXEntryActivity.this, "分享取消", Toast.LENGTH_LONG).show();
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
					//分享拒绝
					Toast.makeText(WXEntryActivity.this, "分享拒绝", Toast.LENGTH_LONG).show();
					break;
			}
		}
		finish();
	}

	private void getAccessToken(String code) {
		//获取授权
		String http = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constants.APP_ID + "&secret=" + Constants.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
		OkGoRequest.get(http, this, new AbsCallback<String>() {
			@Override
			public String convertSuccess(Response response) throws Exception {
				String mResponse = response.body().string();
				response.close();
				return mResponse;
			}

			@Override
			public void onSuccess(String s, Call call, Response response) {
				String access = null;
				String openId = null;
				try {
					JSONObject jsonObject = new JSONObject(s);
					access = jsonObject.optString("access_token");
					openId = jsonObject.optString("openid");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				//获取个人信息
				String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId + "";

				OkGoRequest.get(getUserInfoUrl, this, new AbsCallback<WeChatInfoBean>() {
					@Override
					public void onSuccess(WeChatInfoBean weChatInfoBean, Call call, Response response) {
						EventBus.getDefault().post(weChatInfoBean);
						finish();
					}

					@Override
					public WeChatInfoBean convertSuccess(Response response) throws Exception {
					    String mResponse = response.body().string();
						WeChatInfoBean weChatInfoBean = Convert.fromJson(mResponse, WeChatInfoBean.class);
						response.close();
						return weChatInfoBean;
					}
				});
			}

		});
	}


}