package com.liuniukeji.mixin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 描述：可根据item多少，自适应高度的列表
 */
public class AutoMeasureHeightListView extends ListView {

    public AutoMeasureHeightListView(Context context) {
        super(context);
    }

    public AutoMeasureHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoMeasureHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
