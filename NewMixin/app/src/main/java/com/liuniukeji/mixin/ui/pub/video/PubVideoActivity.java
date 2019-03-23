package com.liuniukeji.mixin.ui.pub.video;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TakePhotoOptions;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.mabeijianxi.smallvideorecord2.model.MediaRecorderConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

import static vn.tungdx.mediapicker.activities.MediaPickerActivity.getMediaItemSelected;
import static vn.tungdx.mediapicker.activities.MediaPickerActivity.open;

/**
 * 发布视频
 */

public class PubVideoActivity extends AppCompatActivity implements PubVideoContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.pub_plate_rb1)
    RadioButton pubPlateRb1;
    @BindView(R.id.pub_plate_rb2)
    RadioButton pubPlateRb2;
    @BindView(R.id.pub_plate_rb3)
    RadioButton pubPlateRb3;
    @BindView(R.id.pub_plate_rg)
    RadioGroup pubPlateRg;
    @BindView(R.id.pub_content_et)
    EditText pubContentEt;
    @BindView(R.id.pub_add_pic_iv)
    ImageView pubAddPicIv;
    @BindView(R.id.txt_hint)
    TextView txtHint;
    @BindView(R.id.public_sw)
    Switch publicSw;
    @BindView(R.id.sync_to_pyq_chb)
    CheckBox syncToPyqChb;
    @BindView(R.id.release_tv)
    TextView releaseTv;

    private IntentFilter intentFilter;
    private MediaPickerActivity.MyBroadcastReceiver myBroadcastReceiver;
    private MediaPickerActivity mediaPickerActivity;

    private String videourl;
    private String imageUrl;
    private String videoScreenshot;
    private static final int REQUEST_CODE_VIDEO = 1001;//录制视频返回结果
    private static final int REQUEST_CODE_CHOOSE = 1002;//选择视频返回结果

    PubVideoContract.Presenter presenter;
    @BindView(R.id.root_ly)
    LinearLayout rootLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_video);
        ButterKnife.bind(this);

        headTitleTv.setText("发布");

        presenter = new PublicVideoPresenter(this);
        presenter.attachView(this);

        //字体颜色跟随选中变化
        pubPlateRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.pub_plate_rb1:
                        pubPlateRb1.setTextColor(getResources().getColor(R.color.white));
                        pubPlateRb2.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb3.setTextColor(getResources().getColor(R.color.color_main));
                        break;
                    case R.id.pub_plate_rb2:
                        pubPlateRb1.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb2.setTextColor(getResources().getColor(R.color.white));
                        pubPlateRb3.setTextColor(getResources().getColor(R.color.color_main));
                        break;
                    case R.id.pub_plate_rb3:
                        pubPlateRb1.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb2.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb3.setTextColor(getResources().getColor(R.color.white));
                        break;
                    default:
                        break;
                }
            }
        });

        txtHint.setText(String.format(getString(R.string.pub_hint3), "15"));
    }

    @OnClick({R.id.head_back_ly, R.id.release_tv, R.id.pub_add_pic_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                closeKeyboard();
                break;
            case R.id.release_tv:
                //发布,提交数据
                String tag;
                switch (pubPlateRg.getCheckedRadioButtonId()) {
                    case R.id.pub_plate_rb1:
                        tag = "0";
                        break;
                    case R.id.pub_plate_rb2:
                        tag = "1";
                        break;
                    case R.id.pub_plate_rb3:
                        tag = "2";
                        break;
                    default:
                        tag = "0";
                        break;
                }

                String status = publicSw.isChecked() ? "0" : "1";
                String content = pubContentEt.getText().toString();

                if (EmptyUtils.isEmpty(content)) {
                    ToastUtils.showShortToast("分享内容不能为空");
                    break;
                }

                if (TextUtils.isEmpty(videourl)) {
                    ToastUtils.showShortToast("请选择视频");
                } else {
                    presenter.putVideo(status, tag, content, videourl);
                    //只能点一次
                    releaseTv.setEnabled(false);
                }

                break;
            case R.id.pub_add_pic_iv:
                closeKeyboard();
                showDialog();
                break;
            default:
                break;
        }
    }

    private List<MediaItem> mMediaSelectedList;
    private static final int REQUEST_MEDIA = 100;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //获取拍照视频返回
            case REQUEST_CODE_VIDEO:
                if (data != null) {
                    videourl = data.getStringExtra(MediaRecorderActivity.VIDEO_URI);
                    videoScreenshot = data.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);
                    Bitmap bitmap = BitmapFactory.decodeFile(videoScreenshot);
                    pubAddPicIv.setImageBitmap(bitmap);
                }
                break;
            case REQUEST_CODE_CHOOSE:
                if (resultCode == RESULT_OK) {
                    mMediaSelectedList = getMediaItemSelected(data);
                    if (mMediaSelectedList != null) {
                        for (MediaItem mediaItem : mMediaSelectedList) {
                            if (mediaItem.getUriCropped() == null) {
                                Glide.with(this).load(mediaItem.getUriOrigin()).into(pubAddPicIv);
                            } else {
                                Glide.with(this).load(mediaItem.getUriCropped()).into(pubAddPicIv);
                            }
                            videourl = mediaItem.getPathOrigin(this);
                        }
                    }

                }
                break;
            default:
                break;
        }
    }

    // 录制视频
    public void takeVideo() {

        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()

                .fullScreen(true)
                .smallVideoWidth(360)
                .smallVideoHeight(480)
                .recordTimeMax(15000)
                .recordTimeMin(5000)
                .maxFrameRate(20)
                .videoBitrate(600000)
                .captureThumbnailsTime(1)
                .build();
        Intent intent = new Intent(PubVideoActivity.this, MediaRecorderActivity.class);
        intent.putExtra(MediaRecorderActivity.MEDIA_RECORDER_CONFIG_KEY, config);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }

    // 选择视频
    public void selectVideo() {

        MediaOptions.Builder builder = new MediaOptions.Builder();
        MediaOptions options = builder.selectVideo().canSelectMultiPhoto(true).build();
        if (options != null) {
            open(this, REQUEST_CODE_CHOOSE, options);
        }

    }

    /**
     * 选择对话框
     */

    private void showDialog() {
        final TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setCorrectImage(true);
        PhotoUploadDialog dialog = new PhotoUploadDialog(this, "录制视频", "选择视频");
        dialog.setPhotoUpListener(new PhotoUploadDialog.PhotoUpLisenter() {
            @Override
            public void tackPhoto() {
                //录制
                takeVideo();
                //ToastUtils.showShortToast("拍照");
            }

            @Override
            public void joinPhoto() {
                //选择视频
                selectVideo();
            }

            @Override
            public void cancel() {

            }
        });
        dialog.show();
    }

    @Override
    public void refreshData() {
        ToastUtils.showShortToast("发布视频成功");
        //通知发布的页面关闭
        setResult(0x112);
        finish();
        releaseTv.setEnabled(true);

    }

    @Override
    public void openText() {
        releaseTv.setEnabled(true);
    }

    @Override
    public void onEmpty() {
        releaseTv.setEnabled(true);

    }

    @Override
    public void onNetError() {
        releaseTv.setEnabled(true);
    }

    /**
     * 隐藏软键盘
     */
    private void closeKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootLy.getWindowToken(), 0);
    }


}
