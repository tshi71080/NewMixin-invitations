package com.hyphenate.chatui.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.hyphenate.util.EMLog;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.mabeijianxi.smallvideorecord2.model.MediaRecorderConfig;

import java.util.List;

import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

/**
 * 录制视频
 */

public class NewRecordVideoActivity extends AppCompatActivity {


    private IntentFilter intentFilter;
    private MediaPickerActivity.MyBroadcastReceiver myBroadcastReceiver;
    private MediaPickerActivity mediaPickerActivity;

    private String videourl;
    private String imageUrl;
    private String videoScreenshot;
    private static final int REQUEST_CODE_VIDEO = 1001;//录制视频返回结果
    private static final int REQUEST_CODE_CHOOSE = 1002;//选择视频返回结果

    String localPath = "";// path to save recorded video


    private int previewWidth = 360;
    private int previewHeight = 480;

    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //进入视频拍摄界面
        takeVideo();
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
                    localPath = videourl;
                    if (EmptyUtils.isNotEmpty(videourl)) {
                        sendVideo();
                    } else {
                        finish();
                        ToastUtils.showShortToast("拍摄视频返回数据异常");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 录制视频
     */
    public void takeVideo() {

//        /// 获取照相机参数，设置需要的参数，其余缺省
//        Camera camera = Camera.open();
//        Camera.Parameters parameters = camera.getParameters();
//
//        //获取预览的各种分辨率
//        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
//        //获取摄像头支持的各种分辨率
//        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
//
//
//        for (int i=0; i<supportedPictureSizes.size();i++) {
//            Log.e("CCCCCTTTT","h:"+supportedPictureSizes.get(i).height);
//            Log.e("CCCCCTTTT","w:"+supportedPictureSizes.get(i).width);
//        }

        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                .fullScreen(true)

                .smallVideoWidth(640)
                .smallVideoHeight(480)

                .recordTimeMax(15000)
                .recordTimeMin(5000)
                .maxFrameRate(20)
                .videoBitrate(600000)
                .captureThumbnailsTime(1)
                .build();
        Intent intent = new Intent(NewRecordVideoActivity.this, MediaRecorderActivity.class);
        intent.putExtra(MediaRecorderActivity.MEDIA_RECORDER_CONFIG_KEY, config);
        startActivityForResult(intent, REQUEST_CODE_VIDEO);


    }


    MediaScannerConnection msc = null;
    ProgressDialog progressDialog = null;

    private void sendVideo() {
        if (TextUtils.isEmpty(localPath)) {
            EMLog.e("Recorder", "recorder fail please try again!");
            return;
        }

        msc = new MediaScannerConnection(this,
                new MediaScannerConnection.MediaScannerConnectionClient() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        msc.disconnect();
                        progressDialog.dismiss();
                        setResult(RESULT_OK, getIntent().putExtra("uri", uri));
                        finish();
                    }

                    @Override
                    public void onMediaScannerConnected() {
                        msc.scanFile(localPath, "video/*");
                    }
                });


        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        msc.connect();

    }


}




