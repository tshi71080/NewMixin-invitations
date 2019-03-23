package com.liuniukeji.mixin.widget;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.base.BaseActivity;
import com.liuniukeji.mixin.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.wv)
    WebView wv;
    @BindView(R.id.pb)
    ProgressBar progressbar;
    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
    }

    @Override
    protected void processLogic() {
        wv.setWebViewClient(new MyWebViewClient());
        wv.setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = wv.getSettings();
        webSettings.setAppCacheEnabled(true);
        // 设置支持javascript脚本
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setBuiltInZoomControls(true);// 设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setUseWideViewPort(true);// 图片被缩放后显示不全的问题
        //自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        wv.loadUrl(getIntent().getStringExtra("url"));
        String title = getIntent().getStringExtra("title");

        headTitleTv.setText(title);
    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
