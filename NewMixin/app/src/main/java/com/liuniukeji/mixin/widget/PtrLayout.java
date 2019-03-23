package com.liuniukeji.mixin.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.currency.SizeUtils;

import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 *
 */

public class PtrLayout extends CustomPtrFrameLayout {
    public PtrLayout(Context context) {
        super(context);
        initViews(context);
    }

    public PtrLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public PtrLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public void initViews(Context context) {
        final MaterialHeader header = new MaterialHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new LayoutParams(-1, -2));
        header.setPadding(0, SizeUtils.dp2px(15), 0, SizeUtils.dp2px(10));
        header.setPtrFrameLayout(this);
        setDurationToClose(200);
        setDurationToCloseHeader(200);
        setKeepHeaderWhenRefresh(true);
        setPullToRefresh(false);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setResistance(1.7f);
        setPinContent(true);
        setHeaderView(header);
        addPtrUIHandler(header);
    }
}
