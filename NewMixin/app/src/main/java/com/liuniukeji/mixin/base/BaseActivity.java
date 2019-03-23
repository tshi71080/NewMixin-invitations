package com.liuniukeji.mixin.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.XyqApplication;
import com.liuniukeji.mixin.util.currency.ToastUtils;


public abstract class BaseActivity extends AppCompatActivity {
    protected Context ctx;
    protected String TAG;
    /**
     *  加载框
     */
    public Dialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        XyqApplication.getInstance().addActivity(this);
        TAG = this.getClass().getSimpleName();
        ctx = this;
        setDialog("加载中");
        initView();
    }

    private void setDialog(String title) {
        /*progressDialog = new Dialog(BaseActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog_commom);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        TextView msg = (TextView) progressDialog
                .findViewById(R.id.id_tv_loadingmsg);
        msg.setText(title);*/
    }

    private void initView() {
        loadViewLayout();
        processLogic();
    }

    protected abstract void loadViewLayout();


    /**
     * @描述 处理业务
     */
    protected abstract void processLogic();

    public void showToast(String msg) {
        ToastUtils.showShortToastSafe(msg);
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            ((Activity) ctx).finish();
        }
    }


}
