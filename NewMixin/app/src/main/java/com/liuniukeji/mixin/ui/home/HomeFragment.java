package com.liuniukeji.mixin.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.hyphenate.chatui.popwindow.AddPopWindow;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.base.BaseFragment;
import com.liuniukeji.mixin.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 首页面
 */
public class HomeFragment extends BaseFragment implements HomeContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Unbinder unbinder;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"推荐","好友"};

    private List<String> titleList = new ArrayList<>();

    private MyPagerAdapter mAdapter;

    @BindView(R.id.vp)
    ViewPager viewpager;

    @BindView(R.id.tl_2)
    SlidingTabLayout tabLayout_2;

    @BindView(R.id.nav_add_ly)
    RelativeLayout navAddLy;

    HomeContract.Presenter presenter;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View getLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        //依附页面
        unbinder = ButterKnife.bind(this, view);
        presenter = new HomePresenter(getContext());
        presenter.attachView(this);
        return view;
    }

    @Override
    protected void processLogic() {
        //前三个标题数据初始化
        List<String> titles = Arrays.asList(mTitles);
        titleList.clear();
        titleList.addAll(titles);
        //获取关注学校列表
        //presenter.getSchoolList();

        for (String title : titleList) {
            String typeId = "";
            if (title.equals(mTitles[0])) {
                typeId = "1";
            } else if (title.equals(mTitles[1])) {
                typeId = "3";
            }
            mFragments.add(HomeMomentFragment.newInstance("1", typeId));
        }

        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(mAdapter);
        tabLayout_2.setViewPager(viewpager);
        //mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void liftSchoolData(List<FollowSchoolInfo> infoList) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.ADD_CIRCLE state) {
        switch (state) {
            case ON_CHANGE:
                //更新数据
                //前三个标题数据初始化
                List<String> titles = Arrays.asList(mTitles);
                titleList.clear();
                titleList.addAll(titles);
                presenter.getSchoolList();
                break;
            default:
                break;
        }
    }

    /**
     * 加载前3个固定页面
     */
    /*private void addCommonPages() {
        //添加之前，先清空
        mFragments.clear();
        for (String title : titleList) {
            String typeId = "";
            if (title.equals(mTitles[0])) {
                typeId = "1";
            } else if (title.equals(mTitles[1])) {
                typeId = "3";
            }
            mFragments.add(HomeMomentFragment.newInstance("1", typeId));
        }
        mAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(mAdapter);
        tabLayout_2.setViewPager(viewpager);
        mAdapter.notifyDataSetChanged();
    }*/

    @Override
    public void onEmpty() {
        //加载固定页面

    }

    @Override
    public void onNetError() {
        //加载固定页面

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.nav_add_ly)
    public void onViewClicked() {
        //添加关注学校
        //startActivity(new Intent().setClass(getContext(), AddCircleActivity.class));
        AddPopWindow popWindow = new AddPopWindow(getActivity());
        popWindow.showPopupWindow(navAddLy);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (titleList.size() <= position) {
                return "";
            } else {
                return titleList.get(position);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //阻止回收页面
             super.destroyItem(container, position, object);
        }
    }

}
