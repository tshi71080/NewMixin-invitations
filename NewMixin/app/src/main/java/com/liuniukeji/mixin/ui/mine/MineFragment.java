package com.liuniukeji.mixin.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.base.BaseFragment;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.ui.mine.setting.SettingActivity;
import com.liuniukeji.mixin.ui.multisend.MultiChooseActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.widget.WebViewActivity;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.ui.mine.setting.SettingActivity;
import com.liuniukeji.mixin.util.AccountUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的模块主页面
 */
public class MineFragment extends BaseFragment implements MineContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.mine_name_tv)
    TextView mineNameTv;
    @BindView(R.id.level_icon_iv)
    ImageView levelIconIv;
    @BindView(R.id.level_num_tv)
    TextView levelNumTv;
    @BindView(R.id.signature_tv)
    TextView signatureTv;
    @BindView(R.id.mine_avatar_iv)
    CircleImageView mineAvatarIv;
    @BindView(R.id.mine_attention_tv)
    TextView mineAttentionTv;
    @BindView(R.id.mine_fans_tv)
    TextView mineFansTv;
    @BindView(R.id.mine_moments_tv)
    TextView mineMomentsTv;
    @BindView(R.id.mine_score_tv)
    TextView mineScoreTv;
    @BindView(R.id.mine_visitors_rl)
    RelativeLayout mineVisitorsRl;
    @BindView(R.id.mine_moments_rl)
    RelativeLayout mineMomentsRl;
    @BindView(R.id.mine_album_rl)
    RelativeLayout mineAlbumRl;
    @BindView(R.id.mine_reply_rl)
    RelativeLayout mineReplyRl;
    @BindView(R.id.mine_self_media_rl)
    RelativeLayout mineSelfMediaRl;
    @BindView(R.id.mine_code_rl)
    RelativeLayout mineCodeRl;
    @BindView(R.id.mine_black_list_rl)
    RelativeLayout mineBlackListRl;
    @BindView(R.id.mine_member_center_rl)
    RelativeLayout mineMemberCenterRl;
    @BindView(R.id.mine_settings_rl)
    RelativeLayout mineSettingsRl;
    Unbinder unbinder;
    @BindView(R.id.mine_fans_ly)
    LinearLayout mineFansLy;
    @BindView(R.id.mine_moments_ly)
    LinearLayout mineMomentsLy;
    @BindView(R.id.mine_score_ly)
    LinearLayout mineScoreLy;
    @BindView(R.id.mine_attention_ly)
    LinearLayout mineAttentionLy;
    @BindView(R.id.visitor_iv1)
    CircleImageView visitorIv1;
    @BindView(R.id.visitor_iv2)
    CircleImageView visitorIv2;
    @BindView(R.id.visitor_iv3)
    CircleImageView visitorIv3;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    MineContract.Presenter presenter;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void processLogic() {
        presenter = new MinePresenter(getActivity());
        presenter.attachView(this);
        //setData();

        //获取访客列表
        presenter.getUserInfo();
        presenter.getVisitorList(1);
    }

    /**
     * 使用本地用户信息
     */
    private void setData() {
        UserBean user = AccountUtils.getUser(getActivity());

        if (null != user) {
            mineNameTv.setText(user.getReal_name());
            //关注
            mineAttentionTv.setText(user.getFollows());
            mineFansTv.setText(user.getFans());
            mineMomentsTv.setText(user.getMoments());
            mineScoreTv.setText(user.getPoints());
            //个性签名
            signatureTv.setText(user.getSignature());

            //处理性别显示图标&会员身份标识
            levelNumTv.setText(user.getExperience());
            String type = user.getVip_type();
            switch (user.getSex()) {
                case "1":
                    //show boy icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
                case "2":
                    //show girl icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.girl_level_color));
                    break;
                default:
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
            }

            //用户头像展示
            String logUrl = user.getPhoto_path();
            if (EmptyUtils.isNotEmpty(logUrl)) {
                XImage.loadAvatar(mineAvatarIv, logUrl);
            }
        }
    }

    /**
     * 网络获取用户信息赋值
     *
     * @param user
     */
    private void setData(UserBean user) {
        if (null != user&&EmptyUtils.isNotEmpty(user.getId())) {

            mineNameTv.setText(user.getReal_name());
            //关注
            mineAttentionTv.setText(user.getFollows());
            mineFansTv.setText(user.getFans());
            mineMomentsTv.setText(user.getMoments());
            mineScoreTv.setText(user.getPoints());
            //个性签名
            signatureTv.setText(user.getSignature());

            //处理性别显示图标&会员身份标识
            levelNumTv.setText(user.getExperience());
            String type = user.getVip_type();
            switch (user.getSex()) {
                case "1":
                    //show boy icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
                case "2":
                    //show girl icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.girl_level_color));
                    break;
                default:
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
            }

            //用户头像展示
            String logUrl = user.getPhoto_path();
            if (EmptyUtils.isNotEmpty(logUrl)) {
                XImage.loadAvatar(mineAvatarIv, logUrl);
            }

            //-----------------------保存数据聊天用户数据---------------------------
            EaseUser easeUser = new EaseUser(user.getIm_name());
            easeUser.setAvatar(user.getPhoto_path());
            easeUser.setNickname(user.getReal_name());

            DemoHelper.getInstance().saveContact(easeUser);
            //-----------------------保存数据聊天用户数据---------------------------

            //更新本地数据
            AccountUtils.saveUserCache(getActivity(),user);
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

    @OnClick({R.id.mine_avatar_iv, R.id.mine_visitors_rl, R.id.mine_moments_rl,
            R.id.mine_album_rl, R.id.mine_reply_rl, R.id.mine_self_media_rl, R.id.mine_code_rl,
            R.id.mine_black_list_rl, R.id.mine_member_center_rl, R.id.mine_settings_rl,
            R.id.mine_fans_ly, R.id.mine_moments_ly, R.id.mine_score_ly, R.id.mine_attention_ly,
            R.id.mine_base_info_ly, R.id.mine_base_info_rl,R.id.group_send_rl})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mine_base_info_ly:
            case R.id.mine_base_info_rl:
            case R.id.mine_avatar_iv:
                //修改个人资料
                intent.setClass(getActivity(), UserInfoModifyAct.class);
                startActivity(intent);
                break;
            case R.id.mine_visitors_rl:
                //我的访客
                intent.setClass(getActivity(), MyVisitorActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_moments_rl:
                //我的动态
                intent.setClass(getActivity(), MyMomentActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_album_rl:
                //我的相册
                intent.setClass(getActivity(), AlbumActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_reply_rl:
                //我的回复
                intent.setClass(getActivity(), MyReplyActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_self_media_rl:
                //申请自媒体
                //intent.setClass(getActivity(), SelfMediaActivity.class);
                String userId=AccountUtils.getUser(getActivity()).getId();
                String url= UrlUtils.applyMedia+userId;
                intent.putExtra("title", "申请自媒体");
                intent.putExtra("url", url);
                intent.setClass(getActivity(), WebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_code_rl:
                //我的二维码
                intent.setClass(getActivity(), MyCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_black_list_rl:
                //黑名单
                intent.setClass(getActivity(), BlackListActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_member_center_rl:
                //会员中心
                intent.setClass(getActivity(), VipCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_settings_rl:
                //设置
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_fans_ly:
                //粉丝
                intent.setClass(getActivity(), MyFansActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_moments_ly:
                //动态
                intent.setClass(getActivity(), MyMomentActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_score_ly:
                //学分
                intent.setClass(getActivity(), MyScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_attention_ly:
                //关注
                intent.setClass(getActivity(), AttentionActivity.class);
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

    @Override
    public void liftData(UserBean info) {
        setData(info);
    }

    @Override
    public void liftData(List<VisitorInfo> infoList) {
        //访客小头像显示
        if (null != infoList && infoList.size() > 0) {
            if (infoList.size() >= 3) {
                if (null != visitorIv1 && null != visitorIv2 && null != visitorIv3) {
                    visitorIv1.setVisibility(View.VISIBLE);
                    visitorIv2.setVisibility(View.VISIBLE);
                    visitorIv3.setVisibility(View.VISIBLE);

                    XImage.loadAvatar(visitorIv1, infoList.get(0).getPhoto_path());
                    XImage.loadAvatar(visitorIv2, infoList.get(1).getPhoto_path());
                    XImage.loadAvatar(visitorIv3, infoList.get(2).getPhoto_path());
                }

            } else {
                if (infoList.size() == 2) {
                    if (null != visitorIv1 && null != visitorIv2) {
                        visitorIv1.setVisibility(View.VISIBLE);
                        visitorIv2.setVisibility(View.VISIBLE);

                        XImage.loadAvatar(visitorIv1, infoList.get(0).getPhoto_path());
                        XImage.loadAvatar(visitorIv2, infoList.get(1).getPhoto_path());
                    }
                } else {
                    if (null != visitorIv1) {
                        visitorIv1.setVisibility(View.VISIBLE);
                        XImage.loadAvatar(visitorIv1, infoList.get(0).getPhoto_path());
                    }
                }
            }
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.USER_INFO state) {
        switch (state) {
            case UPDATE:
                //用户数据更新
                presenter.getUserInfo();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
