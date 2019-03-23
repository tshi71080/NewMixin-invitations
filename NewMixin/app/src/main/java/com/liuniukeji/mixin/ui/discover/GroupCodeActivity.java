package com.liuniukeji.mixin.ui.discover;

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
import com.liuniukeji.mixin.util.FileUtil;
import com.liuniukeji.mixin.util.FileUtils;
import com.liuniukeji.mixin.util.ImageLoader;
import com.liuniukeji.mixin.util.WxShareAndLoginUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群二维码
 */

public class GroupCodeActivity extends AppCompatActivity implements GroupCodeContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.iv_qr_code)
    ImageView myQrCode;
    @BindView(R.id.tv_right_btn)
    TextView btnSaveImgCode;
    @BindView(R.id.ll_saved_img)
    LinearLayout llSavedImg;

    GroupCodeContract.Presenter presenter;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.expire_date_tv)
    TextView expireDateTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_code);
        ButterKnife.bind(this);

        headTitleTv.setText("群二维码");
        btnSaveImgCode.setVisibility(View.GONE);
        presenter = new GroupCodePresenter(this);
        presenter.attachView(this);

        String groupImId = getIntent().getStringExtra("groupImId");

        if (EmptyUtils.isNotEmpty(groupImId)) {
            presenter.getCode(groupImId);
        }
        String groupName = getIntent().getStringExtra("groupName");

        //群组名显示
        userNameTv.setText(groupName);
    }


    @OnClick({R.id.head_back_ly, R.id.save_tv, R.id.share_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.save_tv:
                //保存图片
                //FileUtil.saveBitmap(this, createViewBitmap(llSavedImg));

                Bitmap bitmap = createViewBitmap(llSavedImg);
                String path = FileUtils.saveBitmapPath(bitmap);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + FileUtils.SDPATHIMG + path)));
                ToastUtils.showShortToast("保存成功！");
                break;
            case R.id.share_tv:
                //分享给微信好友
                WxShareAndLoginUtils.WxBitmapShare(this, createViewBitmap(llSavedImg), 0);
            default:
                break;
        }
    }

    @Override
    public void liftData(GroupCodeInfo codeInfo) {
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
