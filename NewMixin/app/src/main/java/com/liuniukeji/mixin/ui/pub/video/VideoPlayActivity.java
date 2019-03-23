package com.liuniukeji.mixin.ui.pub.video;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.XyqApplication;
import com.liuniukeji.mixin.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 视频播放
 */

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;

    String url, cover;
    @BindView(R.id.btnLeft)
    ImageView btnLeft;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    private String proxyUrl;


    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        tvTitle.setText("视频播放");
        if (getIntent() != null) {
            url = getIntent().getStringExtra("path");
            cover = getIntent().getStringExtra("cover");
        }
    }

    @Override
    public void onBackPressed() {
        if (videoplayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoplayer.releaseAllVideos();
    }

    @Override
    protected void processLogic() {
        videoplayer.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

        videoplayer.thumbImageView.setAdjustViewBounds(true);
        videoplayer.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //Glide.with(VideoPlayActivity.this).load(UrlUtils.APIHTTP + cover).into(videoplayer.thumbImageView);
        Glide.with(VideoPlayActivity.this).load(cover).into(videoplayer.thumbImageView);
        videoplayer.startVideo();

    }


    @OnClick(R.id.btnLeft)
    public void onViewClicked() {
        finish();
    }
}
