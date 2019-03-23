package com.liuniukeji.mixin.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.liuniukeji.mixin.R;


/**
 *
 */

public abstract class DialogAlarm extends BaseAlarm {
    Dialog mDialog;
    Context context;

    public DialogAlarm(Context context) {
        this(context, 0);
    }

    public DialogAlarm(Context context, int themeId) {
        this.context = context;
        if (themeId != 0) {
            mDialog = new Dialog(context, themeId);
        } else {
            mDialog = new Dialog(context, R.style.progress_dialog);
        }
    }

    public Dialog getDialog() {
        return mDialog;
    }


    public Context getContext() {
        return context;
    }


    public void show() {
        super.show();
        if (mDialog != null) {
            View mContent = new FrameLayout(getContext());
            mContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.cancel();
                }
            });
            View view = getView((ViewGroup) mContent);
            if (view != null) {
                onViewCreated(view);

                mDialog.setContentView(mContent, new ViewGroup.LayoutParams(-1, -1));
                mDialog.show();
                mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        mIsShowing = true;
                        onDialogShowed();
                        if (getAlarmListener() != null) {
                            getAlarmListener().onShow(DialogAlarm.this);
                        }
                    }
                });

                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mIsShowing = false;
                        onDialogDismissed();
                        if (getAlarmListener() != null) {
                            getAlarmListener().onDismiss(DialogAlarm.this);
                        }
                    }
                });

                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mIsShowing = false;
                        onDialogCanceled();
                        if (getAlarmListener() != null) {
                            getAlarmListener().onCancel(DialogAlarm.this);
                        }
                    }
                });
            }
        }
    }

    public abstract View getView(ViewGroup parent);

    public abstract void onViewCreated(View view);

    protected void onDialogShowed() {

    }

    protected void onDialogDismissed() {

    }

    protected void onDialogCanceled() {

    }

    @Override
    public boolean isShowing() {
        return super.isShowing() && (mDialog != null && mDialog.isShowing());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mDialog != null) {
            mDialog.cancel();
        }
    }

    /**
     * 是否支持取消
     *
     * @param enable
     */
    public void cancelable(boolean enable) {
        if (mDialog != null) {
            mDialog.setCancelable(enable);
            mDialog.setCanceledOnTouchOutside(enable);
        }
    }
}

