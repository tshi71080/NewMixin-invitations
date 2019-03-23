package com.liuniukeji.mixin.ui.main;


import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.base.MyBaseFragment;
import com.liuniukeji.mixin.ui.discover.DiscoverFragment;
import com.liuniukeji.mixin.ui.home.HomeFragment;
import com.liuniukeji.mixin.ui.message.MessageFragment;
import com.liuniukeji.mixin.ui.message.MyContactListFragment;
import com.liuniukeji.mixin.ui.mine.MineFragment;
import com.liuniukeji.mixin.util.currency.ConvertUtils;
import com.liuniukeji.mixin.base.MyBaseFragment;
import com.liuniukeji.mixin.ui.discover.DiscoverFragment;
import com.liuniukeji.mixin.ui.message.MessageFragment;
import com.liuniukeji.mixin.ui.mine.MineFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * 首页底部导航
 */
public class NavFragment extends MyBaseFragment implements View.OnClickListener,
        View.OnLongClickListener {

    @BindView(R.id.nav_item_news)
    NavigationButton mNavNews;
    @BindView(R.id.nav_item_tweet)
    NavigationButton mNavTweet;
    @BindView(R.id.nav_item_explore)
    NavigationButton mNavExplore;
    @BindView(R.id.nav_item_me)
    NavigationButton mNavMe;
    @BindView(R.id.nav_item_tweet_pub)
    ImageView mNavPub;
    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnNavigationReselectListener mOnNavigationReselectListener;

    public NavFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nav;
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(0, 1, 0, 0)));
        lineDrawable.getPaint().setColor(getResources().getColor(R.color.list_divider_color));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        root.setBackgroundDrawable(layerDrawable);

        mNavNews.init(R.drawable.tab_icon_message,
                R.string.main_tab_name_message,
                MessageFragment.class
                );

        //DiscoverFragment.class

        mNavTweet.init(R.drawable.tab_icon_contact,
                R.string.main_tab_name_connect,
                MyContactListFragment.class);

        /*mNavPub.init2(R.drawable.ic_nav_add_selector,
                PubFragment.class);*/

        mNavExplore.init(R.drawable.tab_icon_discover,
                R.string.main_tab_name_discover,
                HomeFragment.class);

        mNavMe.init(R.drawable.tab_icon_mine,
                R.string.main_tab_name_mine,
                MineFragment.class);

    }

    @OnClick({R.id.nav_item_news, R.id.nav_item_tweet, R.id.nav_item_explore, R.id.nav_item_me,
            R.id.nav_item_tweet_pub})
    @Override
    public void onClick(View v) {
        if (v instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) v;
            doSelect(nav);
        }

        if (v.getId() == R.id.nav_item_tweet_pub)
            PubActivity.show(getContext());
        // PubActivity.show(getContext());
        //TweetPublishActivity.show(getContext(), mRoot.findViewById(R.id.nav_item_tweet_pub));
//            showWindow();
//        }
    }

    @OnLongClick({R.id.nav_item_tweet_pub})
    @Override
    public boolean onLongClick(View v) {
        //TweetPublishActivity.show(getContext(), mRoot.findViewById(R.id.nav_item_tweet_pub));
        return false;
    }

    public void setup(Context context, FragmentManager fragmentManager,
                      int contentId, OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavNews);
    }

    public void select(int index) {
        if (mNavMe != null) {
            doSelect(mNavMe);
        }
    }

    @SuppressWarnings("RestrictedApi")
    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0) {
            return;
        }
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this && fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit) {
            transaction.commitNow();
        }
    }

    private void doSelect(NavigationButton newNavButton) {
        // If the new navigation is me info fragment, we intercept it
        /*
        if (newNavButton == mNavMe) {
            if (interceptMessageSkip())
                return;
        }
        */

        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }


    private void onReselect(NavigationButton navigationButton) {
        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }


    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showWindow() {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.activity_pub, null, false);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        WindowManager.LayoutParams d = getActivity().getWindow().getAttributes();


        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels;
        int result = 0;
        int resourceId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getActivity().getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("NAV", "HE" + width + "//" + height + "//" + result);

        int ss = ConvertUtils.dp2px(result);

        final PopupWindow window = new PopupWindow(contentView, width, height - ss, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        window.showAsDropDown(mNavPub, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        window.showAtLocation(mNavPub, Gravity.NO_GRAVITY, 0, 0);


        contentView.findViewById(R.id.pub_close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });


    }

    public void showMsgDot(){
        mNavNews.showRedPointDot();
    }

    public void hideMsgDot(){
        mNavNews.hiddenPointDot();
    }


}
