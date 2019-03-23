package com.liuniukeji.mixin.ui.mine.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class FeedbackActivity extends AppCompatActivity implements FeedbackContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.feedback_content_et)
    EditText feedbackContentEt;
    @BindView(R.id.feedback_submit_tv)
    TextView feedbackSubmitTv;

    FeedbackContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        headTitleTv.setText("意见反馈");
        presenter = new FeedbackPresenter(this);
        presenter.attachView(this);

    }


    @OnClick({R.id.head_back_ly, R.id.feedback_submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.feedback_submit_tv:
                String content = feedbackContentEt.getText().toString();
                if (EmptyUtils.isNotEmpty(content)) {
                    //提交数据
                    presenter.submit(content);
                } else {
                    ToastUtils.showShortToast("反馈内容不能为空呢");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(String id) {
        if (EmptyUtils.isNotEmpty(id)) {
            //提交数据成功
            ToastUtils.showShortToast("感谢您的反馈");
            finish();
        } else {
            //提交数据失败
            ToastUtils.showShortToast("提交数据失败");
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
}
