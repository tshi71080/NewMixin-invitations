package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.multisend.MultiChooseActivity;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 发现
 */
public class DiscoverFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;
    @BindView(R.id.nearby_people_rl)
    RelativeLayout nearbyPeopleRl;
    @BindView(R.id.nearby_group_rl)
    RelativeLayout nearbyGroupRl;
    @BindView(R.id.nearby_moment_rl)
    RelativeLayout nearbyMomentRl;
    @BindView(R.id.interest_group_rl)
    RelativeLayout interestGroupRl;
    @BindView(R.id.recommend_friend_rl)
    RelativeLayout recommendFriendRl;
    @BindView(R.id.alumni_record_rl)
    RelativeLayout alumniRecordRl;
    @BindView(R.id.second_hand_market_rl)
    RelativeLayout secondHandMarketRl;
    @BindView(R.id.bro_sis_question_rl)
    RelativeLayout broSisQuestionRl;
    Unbinder unbinder;
    @BindView(R.id.search_content_et)
    EditText searchContentEt;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String keyword;

    public DiscoverFragment() {
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
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        unbinder = ButterKnife.bind(this, view);

        //软键盘搜索监听
        searchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    goSearch();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(searchContentEt.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    /**
     * 搜索
     */
    private void goSearch() {
        keyword = searchContentEt.getText().toString();
        if (EmptyUtils.isNotEmpty(keyword)) {
            //跳转到搜索结果列表
            Intent intent = new Intent();
            intent.putExtra("keyword", keyword);
            intent.setClass(getActivity(), SearchUserActivity.class);
            startActivity(intent);

        } else {
            ToastUtils.showShortToast("搜索内容不能为空");
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.search_rl, R.id.nearby_people_rl, R.id.nearby_group_rl, R.id.nearby_moment_rl,
            R.id.interest_group_rl, R.id.recommend_friend_rl, R.id.alumni_record_rl,
            R.id.second_hand_market_rl, R.id.bro_sis_question_rl, R.id.group_send_rl})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.search_rl:
                //搜索
                goSearch();
                break;
            case R.id.nearby_people_rl:
                //附近的人
                intent.setClass(getActivity(), PeopleNearbyActivity.class);
                startActivity(intent);
                break;
            case R.id.nearby_group_rl:
                //附近的组
                intent.setClass(getActivity(), GroupNearbyActivity.class);
                startActivity(intent);
                break;
            case R.id.nearby_moment_rl:
                //附近的动态
                intent.setClass(getActivity(), MomentNearActivity.class);
                startActivity(intent);
                break;
            case R.id.interest_group_rl:
                //兴趣组
                intent.setClass(getActivity(), InterestGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.recommend_friend_rl:
                //推荐好友
                intent.setClass(getActivity(), RecommendFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.alumni_record_rl:
                //校友录
                intent.setClass(getActivity(), AlumnActivity.class);
                startActivity(intent);
                break;
            case R.id.second_hand_market_rl:
                //二手市场
                intent.setClass(getActivity(), UsedMarketActivity.class);
                startActivity(intent);
                break;
            case R.id.bro_sis_question_rl:
                //学弟学妹提问
                intent.setClass(getActivity(), BroSisActivity.class);
                startActivity(intent);
                break;

            case R.id.group_send_rl:
                //群发助手
                intent.setClass(getActivity(), MultiChooseActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
