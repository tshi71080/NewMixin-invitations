package com.liuniukeji.mixin.ui.multisend;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.PicassoImageLoader;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息群发
 */

public class MultiSendActivity extends AppCompatActivity implements MultiSendContract.View {

    ArrayList<String> realNameList = new ArrayList<>();
    ArrayList<String> imNameList = new ArrayList<>();
    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.send_plate_rb1)
    RadioButton sendPlateRb1;
    @BindView(R.id.send_plate_rb2)
    RadioButton sendPlateRb2;
    @BindView(R.id.send_plate_rb3)
    RadioButton sendPlateRb3;
    @BindView(R.id.send_plate_rg)
    RadioGroup sendPlateRg;
    @BindView(R.id.voice_recorder)
    EaseVoiceRecorderView voiceRecorder;
    @BindView(R.id.message_content_et)
    EditText messageContentEt;
    @BindView(R.id.message_ly)
    LinearLayout messageLy;
    @BindView(R.id.add_pic_iv)
    ImageView addPicIv;
    @BindView(R.id.pic_ly)
    LinearLayout picLy;
    @BindView(R.id.add_record_iv)
    ImageView addRecordIv;
    @BindView(R.id.voice_file_iv)
    ImageView voiceFileIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.root_ly)
    LinearLayout rootLy;
    @BindView(R.id.voice_ly)
    LinearLayout voiceLy;

    String voicePath;
    int voiceLength;

    String imagePath;

    public static boolean isPlaying = false;
    MediaPlayer mediaPlayer = null;
    private AnimationDrawable voiceAnimation = null;

    int type = 1;

    MultiSendContract.Presenter presenter;
    String im_names;
    String real_names;
    private int IMAGE_PICKER = 0x00002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_send);
        ButterKnife.bind(this);

        realNameList = getIntent().getStringArrayListExtra("realNameList");
        imNameList = getIntent().getStringArrayListExtra("imNameList");

        initImagePicker();

        presenter = new MultiSendPresenter(this);
        presenter.attachView(this);

        headTitleTv.setText("群发");

        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < realNameList.size(); i++) {
            sb1.append(realNameList.get(i));
            sb1.append(",");
        }
        real_names = sb1.toString();
        nameTv.setText(real_names);

        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < imNameList.size(); i++) {
            sb2.append(imNameList.get(i));
            sb2.append(",");
        }
        im_names = sb2.toString();

        sendPlateRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.send_plate_rb1:
                        type = 1;
                        messageLy.setVisibility(View.VISIBLE);
                        picLy.setVisibility(View.GONE);
                        voiceLy.setVisibility(View.GONE);
                        voiceRecorder.setVisibility(View.GONE);
                        break;
                    case R.id.send_plate_rb2:
                        type = 2;
                        messageLy.setVisibility(View.GONE);
                        picLy.setVisibility(View.VISIBLE);
                        voiceLy.setVisibility(View.GONE);
                        voiceRecorder.setVisibility(View.GONE);
                        break;
                    case R.id.send_plate_rb3:
                        type = 3;
                        messageLy.setVisibility(View.GONE);
                        picLy.setVisibility(View.GONE);
                        voiceLy.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });


        //触摸监听
        addRecordIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean a = false;
                voiceFileIv.setVisibility(View.GONE);

                a = voiceRecorder.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // sendVoiceMessage(voiceFilePath, voiceTimeLength);

                        //Log.e("HHHHH", "voiceFilePath:" + voiceFilePath + "voiceTimeLength:" + voiceTimeLength);

                        voicePath = voiceFilePath;
                        voiceLength = voiceTimeLength;

                        if (voiceTimeLength > 60) {
                            ToastUtils.showShortToast("请将您的语音文件控制时长在60秒以内");
                        }

                        if (EmptyUtils.isNotEmpty(voiceFilePath)) {
                            voiceFileIv.setVisibility(View.VISIBLE);
                        } else {
                            voiceFileIv.setVisibility(View.GONE);
                        }

                        voiceRecorder.setVisibility(View.GONE);
                    }
                });
                return a;
            }
        });

    }


    @OnClick({R.id.head_back_ly, R.id.tv_right_btn, R.id.add_pic_iv, R.id.voice_file_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                closeKeyboard();
                break;
            case R.id.tv_right_btn:
                //发送按钮操作
                //1文本，2图片，3语音

                switch (type) {
                    case 1:
                        String content = messageContentEt.getText().toString();
                        if (EmptyUtils.isEmpty(content)) {
                            ToastUtils.showShortToast("请输入文字内容");
                            return;
                        }
                        if (EmptyUtils.isNotEmpty(im_names)) {
                            presenter.sendTextMsg(1, im_names, content);
                        } else {
                            ToastUtils.showShortToast("发送对象为空");
                        }
                        break;
                    case 2:
                        if (EmptyUtils.isEmpty(im_names)) {
                            ToastUtils.showShortToast("发送对象为空");
                            return;
                        }
                        if (EmptyUtils.isNotEmpty(imagePath)) {
                            presenter.sendPicMsg(2, im_names, new File(imagePath));
                        } else {
                            ToastUtils.showShortToast("请选择图片文件");
                        }
                        break;
                    case 3:
                        if (EmptyUtils.isEmpty(im_names)) {
                            ToastUtils.showShortToast("发送对象为空");
                            return;
                        }
                        if (EmptyUtils.isNotEmpty(voicePath) && voiceLength > 0) {
                            if (voiceLength > 60) {
                                ToastUtils.showShortToast("语音文件超过时长限制");
                                return;
                            }
                            presenter.sendVoiceMsg(3, im_names, new File(voicePath), voiceLength);
                        } else {
                            ToastUtils.showShortToast("请先录制语音文件");
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.add_pic_iv:
                //添加图片
                ImagePicker imagePicker2 = ImagePicker.getInstance();
                imagePicker2.setMultiMode(false);
                Intent intent2 = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent2, IMAGE_PICKER);
                break;
            case R.id.voice_file_iv:
                //播放语音
                if (EmptyUtils.isNotEmpty(voicePath)) {
                    playVoice(voicePath);
                }
                break;
            default:
                break;
        }
    }

    private void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();

        if (EaseUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (null != mediaPlayer) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            mediaPlayer.start();
            showAnimation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void stopPlayVoice() {
        voiceAnimation.stop();
        voiceFileIv.setImageResource(R.drawable.voice01);
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }


    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation

        voiceFileIv.setImageResource(R.drawable.voice_play_icon);

        voiceAnimation = (AnimationDrawable) voiceFileIv.getDrawable();
        voiceAnimation.start();
    }


    @Override
    public void liftData(String info) {
        ToastUtils.showShortToast(info);
        finish();
        closeKeyboard();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> lists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                imagePath = lists.get(0).path;
                Glide.with(this).load(imagePath).into(addPicIv);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化图片加载器
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        //设置图片加载器
        imagePicker.setImageLoader(new PicassoImageLoader());
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //是否允许裁剪（单选才有效）
        imagePicker.setCrop(false);
        //是否按矩形区域保存
        imagePicker.setSaveRectangle(false);
        //选中数量限制
        imagePicker.setSelectLimit(1);
        //裁剪框的形状
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusWidth(800);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(1000);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(1000);
    }


    /**
     * 隐藏软键盘
     */
    private void closeKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootLy.getWindowToken(), 0);
    }



}
