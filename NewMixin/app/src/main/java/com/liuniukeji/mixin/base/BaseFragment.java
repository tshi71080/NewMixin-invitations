package com.liuniukeji.mixin.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 */

public abstract class BaseFragment extends Fragment {
    protected View view;
    protected Context context;
    protected String TAG;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getActivity();
        TAG = this.getClass().getSimpleName();
        if (view == null) {
            view = getLayout(inflater);
            findViewById(view);
            setListener();
            processLogic();
        }
        return view;
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
        Intent intent = new Intent(context, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            ((Activity)context).finish();
        }
    }
    /**
     * @author txiuqi
     * @描述 加载布局文件
     */
    public abstract View getLayout(LayoutInflater inflater);

    /**
     * @描述 根据ID查找控件
     */
    protected  void findViewById(View view){}

    /**
     * @描述 设置监听
     */
    protected  void setListener(){}

    /**
     * @描述 处理业务
     */
    protected abstract void processLogic();

}
