package com.liuniukeji.mixin.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.FileUtil;
import com.liuniukeji.mixin.util.FileUtils;
import com.liuniukeji.mixin.util.ImageLoader;
import com.liuniukeji.mixin.util.WxShareAndLoginUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的邀请码
 */

public class MyCodeActivity extends AppCompatActivity implements MyCodeContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.base_center_line)
    View baseCenterLine;
    @BindView(R.id.code_tv)
    TextView codeTv;
    @BindView(R.id.invite_friend_tv)
    TextView inviteFriendTv;
    @BindView(R.id.iv_qr_code)
    ImageView myQrCode;
    @BindView(R.id.tv_right_btn)
    TextView btnSaveImgCode;
    @BindView(R.id.ll_saved_img)
    LinearLayout llSavedImg;

    MyCodeContract.Presenter presenter;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.expire_date_tv)
    TextView expireDateTv;
    @BindView(R.id.save_tv)
    TextView saveTv;
    @BindView(R.id.share_tv)
    TextView shareTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_code);
        ButterKnife.bind(this);

        headTitleTv.setText("我的二维码");
        btnSaveImgCode.setVisibility(View.GONE);
        presenter = new MyCodePresenter(this);
        presenter.attachView(this);
        presenter.getCode();

        //用户名显示
        userNameTv.setText(AccountUtils.getUser(this).getReal_name());
    }


    @OnClick({R.id.head_back_ly, R.id.invite_friend_tv, R.id.save_tv, R.id.share_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.invite_friend_tv:
                //邀请好友
                break;
            case R.id.save_tv:
                //保存图片
                //FileUtil.saveBitmap(MyCodeActivity.this, createViewBitmap(llSavedImg));

                Bitmap bitmap = createViewBitmap(llSavedImg);
                String path = FileUtils.saveBitmapPath(bitmap);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + FileUtils.SDPATHIMG + path)));
                ToastUtils.showShortToast("保存成功！");

                break;
            case R.id.share_tv:
                //分享给微信好友
                WxShareAndLoginUtils.WxBitmapShare(this, createViewBitmap(llSavedImg), 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(MyCodeInfo codeInfo) {
        if (null != codeInfo) {
            //codeTv.setText(code);
            ImageLoader.loadImage(this, myQrCode, codeInfo.getQr_code());

            //二维码有效期
            expireDateTv.setText("有效期至 " + codeInfo.getDate());

        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }

    private Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }


}
