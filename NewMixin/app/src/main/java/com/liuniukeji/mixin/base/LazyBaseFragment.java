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

import com.liuniukeji.mixin.util.currency.LogUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

/**
 *
 */

public abstract class LazyBaseFragment extends Fragment {
    protected View view;
    protected Context context;
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();
        if (view == null) {
            view = inflater.inflate(getLayout(), null);
            isInit = true;
            initView();

        }
        return view;
    }

    private void initView() {
        findViewById(view);
        setListener();
        processLogic();
        /**初始化的时候去加载数据**/
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            initEventAndData();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected void initEventAndData() {
    }

    /**
     * 常用在 ViewPager 和 Fragment 组合的 FragmentPagerAdapter 中使用
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e("isVisibleToUser", isVisibleToUser);
        if (isVisibleToUser) {
            //可见时执行的操作
            isCanLoadData();
        } else {
            //不可见时执行的操作
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // isResumed() 判断 避免与 onResume() 方法重复发起网络请求
        if (isVisible() && isResumed()) {
            updateUI();
        }
        if (isHidden()) {
            stopGetData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()) {
            updateUI();
        }
    }

    /**
     * 系统由于内存不足时杀掉 App 当前显示的不是第一个 Fragment，App 被杀掉再次重启时，
     * 显示这个 Fragment 时，isVisible() 的判断始终为 false，这种情况下刷新数据的操作
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            updateUI();
        }
    }

    /**
     * 当前fragment再次显示时刷新ui数据
     */
    protected void updateUI() {
    }

    /**
     * fragment 隐藏时停止加载数据
     */
    protected void stopGetData() {
    }


    public void showToast(String msg) {
        ToastUtils.showShortToast(msg);
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
        if (ex != null) {
            intent.putExtras(ex);
            startActivity(intent);
        }
        if (isCloseCurrentActivity) {
            ((Activity) context).finish();
        }
    }

    /**
     * @author txiuqi
     * @描述 加载布局文件
     */
    public abstract int getLayout();

    /**
     * @描述 根据ID查找控件
     */
    protected void findViewById(View view) {
    }

    /**
     * @描述 设置监听
     */
    protected void setListener() {
    }

    /**
     * @描述 处理业务
     */
    protected abstract void processLogic();

}
