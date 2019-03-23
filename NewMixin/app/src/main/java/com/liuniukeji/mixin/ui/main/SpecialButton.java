package com.liuniukeji.mixin.ui.main;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuniukeji.mixin.R;

/**
 */
public class SpecialButton extends NavigationButton {

    private Fragment mFragment = null;
    private Class<?> mClx;
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mDot;
    private String mTag;

    public SpecialButton(Context context) {
        super(context);
        init();
    }

    public SpecialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpecialButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpecialButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_nav_special, this, true);

        mIconView = (ImageView) findViewById(R.id.nav_iv_icon);
        mTitleView = (TextView) findViewById(R.id.nav_tv_title);
        mDot = (TextView) findViewById(R.id.nav_tv_dot);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mIconView.setSelected(selected);
        mTitleView.setSelected(selected);
    }

    @Override
    public void showRedDot(int count) {
        mDot.setVisibility(count > 0 ? VISIBLE : GONE);
        mDot.setText(String.valueOf(count));
    }

    @Override
    public void init2(@DrawableRes int resId, Class<?> clx) {
        mIconView.setImageResource(resId);
        mTitleView.setVisibility(GONE);
        mClx = clx;
        mTag = mClx.getName();
    }
    @Override
    public Class<?> getClx() {
        return mClx;
    }

    @Override
    public Fragment getFragment() {
        return mFragment;
    }

    @Override
    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override
    public String getTag() {
        return mTag;
    }


}
