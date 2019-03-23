package com.liuniukeji.mixin.ui.pub.forward;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.CommonResult;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 转发
 */
public class ForwardActivity extends AppCompatActivity implements ForwardContract.View {


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
    @BindView(R.id.pub_pic_iv)
    ImageView pubPicIv;
    @BindView(R.id.video_play_iv)
    ImageView videoPlayIv;
    @BindView(R.id.forward_content_tv)
    TextView forwardContentTv;
    @BindView(R.id.public_sw)
    Switch publicSw;
    @BindView(R.id.sync_to_pyq_chb)
    CheckBox syncToPyqChb;
    @BindView(R.id.release_tv)
    TextView releaseTv;
    ForwardContract.Presenter presenter;
    @BindView(R.id.root_ly)
    LinearLayout rootLy;

    String forward_id;
    String cover_path;
    String forward_content;
    boolean isVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward);
        ButterKnife.bind(this);
        headTitleTv.setText("转发");

        presenter = new ForwardPresenter(this);
        presenter.attachView(this);

        forward_id = getIntent().getStringExtra("forward_id");
        cover_path = getIntent().getStringExtra("cover_path");
        forward_content = getIntent().getStringExtra("forward_content");

        isVideo = getIntent().getBooleanExtra("isVideo", false);

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

        //---------------------------------传值页面赋值--------------------------------------
        if (isVideo) {
            videoPlayIv.setVisibility(View.VISIBLE);
        } else {
            videoPlayIv.setVisibility(View.GONE);
        }
        XImage.loadImage(pubPicIv, cover_path);

        forwardContentTv.setText(forward_content);
        //---------------------------------传值页面赋值--------------------------------------
    }

    @OnClick({R.id.head_back_ly, R.id.release_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                closeKeyboard();
                break;
            case R.id.release_tv:
                //发布
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
                    ToastUtils.showShortToast("发布内容不能为空");
                    break;
                }
                presenter.forward(status, tag, content, forward_id);
                releaseTv.setEnabled(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void refreshData(CommonResult result) {
        if (EmptyUtils.isNotEmpty(result.getInfo())) {
            ToastUtils.showShortToast(result.getInfo());
        }
        EventBus.getDefault().post(Constants.UPDATE_MOMENT.ON_CHANGE);
        finish();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {
    }

    /**
     * 隐藏软键盘
     */
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootLy.getWindowToken(), 0);
    }


}
