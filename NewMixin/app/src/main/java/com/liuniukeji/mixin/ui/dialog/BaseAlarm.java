package com.liuniukeji.mixin.ui.dialog;

import android.content.Context;

/**
 *
 */

public class BaseAlarm implements IAlarm {
    private AlarmListener mAlarmListener;

    protected boolean mIsShowing;

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void show() {
        mIsShowing = true;
    }

    @Override
    public void show(long keepTime) {
        mIsShowing = true;
    }

    @Override
    public void dismiss() {
        mIsShowing = false;
    }

    @Override
    public void cancel() {
        mIsShowing = false;
    }

    @Override
    public void setAlarmListener(AlarmListener listener) {
        mAlarmListener = listener;
    }

    /**
     * 返回监听器
     *
     * @return
     */
    public AlarmListener getAlarmListener() {
        return mAlarmListener;
    }

    /**
     * 是否正在显示中
     *
     * @return
     */
    public boolean isShowing() {
        return mIsShowing;
    }
}
