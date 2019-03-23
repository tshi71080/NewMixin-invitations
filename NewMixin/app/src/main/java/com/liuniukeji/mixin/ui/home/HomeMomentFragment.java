package com.liuniukeji.mixin.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.danikula.videocache.HttpProxyCacheServer;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.XyqApplication;
import com.liuniukeji.mixin.base.BaseFragment;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.pub.forward.ForwardActivity;
import com.liuniukeji.mixin.ui.pub.video.VideoPlayActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.ImageLoader;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.widget.WebViewActivity;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 首页动态列表
 * 包含：推荐、校园、
 * 关注、其他学校
 */
public class HomeMomentFragment extends BaseFragment implements HomeMomentContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    Unbinder unbinder;

    /***动态类型**/
    private String type;

    private String typeId;

    HomeMomentContract.Presenter presenter;

    int p = 1;
    int mCurrentCounter = 0;

    private MomentAdapter mAdapter;
    private List<MomentInfo> dataList;


    public HomeMomentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeMomentFragment.
     */
    public static HomeMomentFragment newInstance(String param1, String param2) {
        HomeMomentFragment fragment = new HomeMomentFragment();
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
            type = getArguments().getString(ARG_PARAM1);
            typeId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View getLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_home_moment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void processLogic() {
        presenter = new HomeMomentPresenter(getContext());
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(manager);
        // 解决数据加载完成后, 没有停留在顶部的问题
        rvList.setFocusable(false);
        //rvList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new MomentAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);


        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                getData();
            }
        });

        //设置加载更多监听
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurrentCounter < Constants.COUNT_SIZE * p) {
                    mAdapter.loadMoreEnd();
                    return;
                }
                p++;
                getData();
            }
        }, rvList);

        //获取页面数据
        getData();

        //点击进入详情
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                String type = dataList.get(position).getType();
                if (type.equals("3")) {
                    //自媒体跳转web页面
                    String id = dataList.get(position).getId();
                    String url = UrlUtils.momentArticle + id;
                    Intent intent = new Intent();
                    intent.putExtra("title", "文章详情");
                    intent.putExtra("url", url);
                    intent.setClass(getActivity(), WebViewActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("MomentInfo", dataList.get(position));
                    intent.putExtras(bundle);
                    intent.setClass(getContext(), MomentDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        EventBus.getDefault().register(this);
    }

    /**
     * 获取页面列表数据
     */
    private void getData() {
        switch (type) {
            case "1":
                if (typeId.equals("1")) {
                    //推荐
                    presenter.getRecommendMomentList(p);
                } else if (typeId.equals("2")) {
                    //校园
                    presenter.getSchoolMomentList(p);
                } else if (typeId.equals("3")) {
                    //关注
                    presenter.getFocusMomentList(p);
                }
                break;
            case "2":
                //其他关注学校动态
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void liftData(List<MomentInfo> infoList) {
        if (this.dataList == null || infoList == null || infoList.size() == 0) {
            if (p == 1) {
                mAdapter.setEmptyView(R.layout.empty_layout);
                mAdapter.notifyDataSetChanged();
                if (null != swipeLayout) {
                    swipeLayout.setRefreshing(false);
                }
            }
            if (null != mAdapter) {
                mAdapter.loadMoreEnd();
            }
            return;
        }
        if (p == 1) {
            if (null != swipeLayout) {
                swipeLayout.setRefreshing(false);
            }
            this.dataList.clear();
            this.dataList.addAll(infoList);
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        } else {
            if (null != swipeLayout) {
                swipeLayout.setRefreshing(false);
            }
            this.dataList.addAll(infoList);
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        }
    }

    @Override
    public void liftData(String info) {
        //返回结果，刷新数据
        p=1;
        getData();
    }


    @Override
    public void onEmpty() {
        mAdapter.setEmptyView(R.layout.empty_layout);
        mAdapter.notifyDataSetChanged();
        mAdapter.loadMoreEnd();
    }

    @Override
    public void onNetError() {
        mAdapter.setEmptyView(R.layout.empty_layout);
        mAdapter.notifyDataSetChanged();
        mAdapter.loadMoreEnd();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.UPDATE_MOMENT state) {
        switch (state) {
            case ON_CHANGE:
                //刷新数据
                p = 1;
                getData();
                break;
            default:
                break;
        }
    }


    class MomentAdapter extends BaseQuickAdapter<MomentInfo, BaseViewHolder> {

        public MomentAdapter(@Nullable List<MomentInfo> data) {
            super(R.layout.moment_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final MomentInfo item) {

            //发布内容
            TextView tweetContentTv = helper.getView(R.id.tweet_content_tv);
            TextView forwardContentTv = helper.getView(R.id.forward_content_tv);

            ImageView forwardBgIv = helper.getView(R.id.forward_bg_iv);


            //-----------------------------公共-------------------------------
            //设置昵称
            helper.setText(R.id.user_name_tv, item.getReal_name());
            TextView nameTv = helper.getView(R.id.user_name_tv);
            String vipType = AccountUtils.getUser(getActivity()).getVip_type();
            //0,普通用户 1，VIP
            boolean vip = vipType.equals("1") ? true : false;
            if (vip) {
                if (EmptyUtils.isNotEmpty(item.getColor())) {
                    //设置昵称字体颜色
                    nameTv.setTextColor(Color.parseColor(item.getColor()));
                }
            }

            //显示头像
            ImageView avatarIv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(avatarIv, item.getPhoto_path());

            //用户等级
            ImageView levelIconIv = helper.getView(R.id.level_icon_iv);
            TextView levelNumTv = helper.getView(R.id.level_num_tv);

            levelNumTv.setText(item.getExperience());
            String type = item.getVip_type();
            switch (item.getSex()) {
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

            //处理关注状态
            TextView followTv = helper.getView(R.id.follow_state_tv);
            //0没关注 1已关注
            final boolean isFocus = item.getIs_focus().equals("1") ? true : false;

            if (isFocus) {
                //灰色
                followTv.setTextColor(Color.parseColor("#ACACAC"));
                followTv.setText("已关注");
                followTv.setBackground(getResources().getDrawable(R.drawable.attention_nor_bg));
            } else {
                //黄色
                followTv.setTextColor(Color.parseColor("#FFD71B"));
                followTv.setText("关注");
                followTv.setBackground(getResources().getDrawable(R.drawable.attention_pre_bg));

            }

            //添加or取消关注
            followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFocus) {
                        //取消关注操作
                        presenter.addOrCancelFocus(item.getMember_id(), "2");
                    } else {
                        //添加关注操作
                        presenter.addOrCancelFocus(item.getMember_id(), "1");
                    }
                }
            });

            //时间
            TextView dateTv = helper.getView(R.id.date_tv);
            dateTv.setText(item.getTime_ago());

            //院系
            //TextView departmentTv = helper.getView(R.id.department_tv);
            String school_class = item.getSchool_class() + "届/";

            //departmentTv.setText(school_class + item.getSchool_department_name());


            //发布视频封面图片
            ImageView coverIv = helper.getView(R.id.tweet_video_cover_iv);
            RelativeLayout coverLy = helper.getView(R.id.tweet_video_cover_ly);
            //发布图片
            GridView grv = helper.getView(R.id.tweet_pic_grv);


            String mType = item.getType();
            // 0纯文字，1,图文；2，视频；3，文章
            //-----------------------------自媒体----------------------------
            FrameLayout mediaLy = helper.getView(R.id.media_fly);
            ImageView mediaPicIv = helper.getView(R.id.media_pic_iv);
            ImageView mediaAvatarIv = helper.getView(R.id.media_avatar_iv);
            TextView mediaContentTv = helper.getView(R.id.media_content_tv);
            TextView mediaTitleTv = helper.getView(R.id.media_title_tv);
            //-----------------------------自媒体----------------------------

            //转发
            ImageView forwardIv = helper.getView(R.id.forward_iv);
            TextView forwardTv = helper.getView(R.id.forward_num_tv);
            forwardTv.setText(item.getForward_count());

            LinearLayout forwardLy = helper.getView(R.id.forward_ly);
            final String forwardId = item.getForward_id();

            forwardLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //-----------------------------传值数据处理-------------------------------
                    boolean isVideo=false;
                    String coverPath = "";
                    String forwardContent = "";
                    String fId;

                    if (!forwardId.equals("0")) {
                        //是转发
                        MomentInfo.ForwardMoment moment1 = item.getForward_moments();
                        List<MomentInfo.MomentPhoto> list1 = moment1.getMoments_photo();
                        fId=forwardId;
                        forwardContent = moment1.getContent();
                        // 备注：0纯文字，1,图文；2，视频；3，文章
                        String type1 = moment1.getType();
                        switch (type1) {
                            case "0":
                                isVideo = false;
                                coverPath = "";
                                break;
                            case "1":
                                isVideo = false;
                                if (null != list1 && list1.size() > 0) {
                                    coverPath = list1.get(0).getThumb();
                                } else {
                                    coverPath = "";
                                }
                                break;
                            case "2":
                                isVideo = true;
                                coverPath = moment1.getVideo_cover_path();
                                break;
                            case "3":
                                isVideo = false;
                                if (null != list1 && list1.size() > 0) {
                                    coverPath = list1.get(0).getThumb();
                                } else {
                                    coverPath = "";
                                }
                                break;
                            default:
                                break;
                        }

                    } else {
                        //原创内容
                        List<MomentInfo.MomentPhoto> list1 = item.getMoments_photo();
                        forwardContent = item.getContent();
                        fId=item.getId();
                        // 备注：0纯文字，1,图文；2，视频；3，文章
                        String type1 = item.getType();
                        switch (type1) {
                            case "0":
                                isVideo = false;
                                coverPath = "";
                                break;
                            case "1":
                                isVideo = false;
                                if (null != list1 && list1.size() > 0) {
                                    coverPath = list1.get(0).getThumb();
                                } else {
                                    coverPath = "";
                                }
                                break;
                            case "2":
                                isVideo = true;
                                coverPath = item.getVideo_cover_path();
                                break;
                            case "3":
                                isVideo = false;
                                if (null != list1 && list1.size() > 0) {
                                    coverPath = list1.get(0).getThumb();
                                } else {
                                    coverPath = "";
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    //-----------------------------传值数据处理-------------------------------
                    Intent intent = new Intent();
                    intent.putExtra("forward_id", fId);
                    intent.putExtra("cover_path", coverPath);
                    intent.putExtra("forward_content", forwardContent);
                    intent.putExtra("isVideo", isVideo);
                    intent.setClass(mContext, ForwardActivity.class);
                    startActivity(intent);
                }
            });

            // 0没点赞 1已点赞
            boolean isLike = item.getIs_like().equals("1") ? true : false;
            ImageView likeIv = helper.getView(R.id.like_iv);
            TextView likeTv = helper.getView(R.id.like_num_tv);
            LinearLayout likeLy = helper.getView(R.id.like_ly);
            likeTv.setText(item.getLike_count());

            if (isLike) {
                likeIv.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_s));
                likeTv.setTextColor(getResources().getColor(R.color.color_main));
            } else {
                likeIv.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_n));
                likeTv.setTextColor(Color.parseColor("#AEAEAE"));
            }

            likeLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //喜欢/取消喜欢
                    presenter.likeMoments(item.getId());
                }
            });

            //评论
            ImageView commentIv = helper.getView(R.id.comment_iv);
            TextView commentTv = helper.getView(R.id.comment_num_tv);
            commentTv.setText(item.getComment_count());

            String memberId = item.getMember_id();
            //处理隐藏内容
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals("0")) {
                levelNumTv.setVisibility(View.INVISIBLE);
                levelIconIv.setVisibility(View.INVISIBLE);
                //departmentTv.setVisibility(View.INVISIBLE);
                //followTv.setVisibility(View.INVISIBLE);
            }else{
                levelNumTv.setVisibility(View.VISIBLE);
                levelIconIv.setVisibility(View.VISIBLE);
                //departmentTv.setVisibility(View.VISIBLE);
                //followTv.setVisibility(View.VISIBLE);
            }

            String userId = AccountUtils.getUser(getContext()).getId();
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals(userId)) {
                //隐藏关注按钮
                //followTv.setVisibility(View.INVISIBLE);
            } else {
                //followTv.setVisibility(View.VISIBLE);
            }
            //0,普通朋友圈，1，二手市场，2，学弟学妹提问
            boolean anonymous = item.getTag().equals("2");
            if (anonymous) {
                //followTv.setVisibility(View.GONE);
            }

            //-----------------------------公共-------------------------------


            //-------------------------------转发处理-----------------------------
            String forward_id = item.getForward_id();
            if (forward_id.equals("0")) {

                List<MomentInfo.MomentPhoto> list = item.getMoments_photo();

                //不是转发
                forwardBgIv.setVisibility(View.GONE);
                tweetContentTv.setText(item.getContent());
                forwardContentTv.setVisibility(View.GONE);

                if (mType.equals("3")) {
                    grv.setVisibility(View.GONE);
                    coverLy.setVisibility(View.GONE);
                    mediaLy.setVisibility(View.VISIBLE);

                    //加载自媒体封面
                    if (null != list && list.size() > 0) {
                        XImage.loadImage(mediaPicIv, list.get(0).getOrigin());
                    }

                    XImage.loadAvatar(mediaAvatarIv, item.getPhoto_path());
                    mediaContentTv.setText(item.getReal_name());
                    mediaTitleTv.setText(item.getTitle());
                } else {
                    mediaLy.setVisibility(View.GONE);
                    grv.setVisibility(View.GONE);

                    if (null != list && list.size() > 0) {
                        GridViewAdapter adapter = new GridViewAdapter(getContext(), list);
                        grv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        grv.setVisibility(View.VISIBLE);
                        coverLy.setVisibility(View.GONE);

                    } else {
                        grv.setVisibility(View.GONE);
                        if (EmptyUtils.isNotEmpty(item.getVideo_path())) {
                            grv.setVisibility(View.GONE);
                            coverLy.setVisibility(View.VISIBLE);
                            coverIv.setVisibility(View.VISIBLE);
                            //加载封面图片
                            XImage.loadImage(coverIv, item.getVideo_cover_path());

                            coverLy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ToastUtils.showShortToast("加载视频");
                                    //做视频缓存
                                    HttpProxyCacheServer proxy = XyqApplication.getProxy(getActivity());
                                    String proxyUrl = proxy.getProxyUrl( item.getVideo_path());
                                    Intent intent = new Intent();
                                    intent.putExtra("path", proxyUrl);
                                    intent.putExtra("cover", item.getVideo_cover_path());
                                    intent.setClass(mContext, VideoPlayActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //给视频缩略图加载默认封面
                            //coverIv.setImageResource(R.mipmap.default_image);
                            //解决复用引起的bug
                            coverIv.setVisibility(View.GONE);
                            grv.setVisibility(View.GONE);
                        }
                    }
                }

            } else {
                //是转发
                final MomentInfo.ForwardMoment item1 = item.getForward_moments();
                List<MomentInfo.MomentPhoto> list = item1.getMoments_photo();

                forwardBgIv.setVisibility(View.VISIBLE);
                //显示内容
                tweetContentTv.setText(item.getContent());

                //显示原来的转发内容
                forwardContentTv.setVisibility(View.VISIBLE);
                forwardContentTv.setText(item1.getReal_name() + "：" +
                        item1.getContent());


                if (mType.equals("3")) {
                    grv.setVisibility(View.GONE);
                    coverLy.setVisibility(View.GONE);
                    mediaLy.setVisibility(View.VISIBLE);

                    //加载自媒体封面
                    if (null != list && list.size() > 0) {
                        XImage.loadImage(mediaPicIv, list.get(0).getOrigin());
                    }
                    XImage.loadAvatar(mediaAvatarIv, item.getPhoto_path());
                    mediaContentTv.setText(item.getReal_name());
                    mediaTitleTv.setText(item.getTitle());
                } else {
                    mediaLy.setVisibility(View.GONE);
                    grv.setVisibility(View.GONE);

                    if (null != list && list.size() > 0) {
                        GridViewAdapter adapter = new GridViewAdapter(getContext(), list);
                        grv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        grv.setVisibility(View.VISIBLE);
                        coverLy.setVisibility(View.GONE);

                    } else {
                        grv.setVisibility(View.GONE);
                        if (EmptyUtils.isNotEmpty(item1.getVideo_path())) {
                            grv.setVisibility(View.GONE);
                            coverLy.setVisibility(View.VISIBLE);
                            coverIv.setVisibility(View.VISIBLE);
                            //加载封面图片
                            XImage.loadImage(coverIv, item1.getVideo_cover_path());
                            coverLy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ToastUtils.showShortToast("加载视频");
                                    Intent intent = new Intent();
                                    intent.putExtra("path", item1.getVideo_path());
                                    intent.putExtra("cover", item1.getVideo_cover_path());
                                    intent.setClass(mContext, VideoPlayActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //给视频缩略图加载默认封面
                            //coverIv.setImageResource(R.mipmap.default_image);
                            //解决复用引起的bug
                            coverIv.setVisibility(View.GONE);
                            grv.setVisibility(View.GONE);
                        }
                    }
                }
            }

            avatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //好友详情主页跳转
                    Intent intent = new Intent();
                    intent.putExtra("userId", item.getMember_id());
                    intent.setClass(getActivity(), FriendProfileActivity.class);
                    startActivity(intent);
                }
            });
            //-------------------------------转发处理-----------------------------
        }
    }


    /**
     * GridView图片适配器
     */
    private class GridViewAdapter extends BaseAdapter {

        private Context mContext;
        private List<MomentInfo.MomentPhoto> mdatas;
        private LayoutInflater inflater;

        public GridViewAdapter(Context context, List<MomentInfo.MomentPhoto> data) {
            mContext = context;
            mdatas = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (null != mdatas) {
                return mdatas.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            GridViewAdapter.ViewHolder holder = null;
            if (convertView == null) {
                //convertView = View.inflate(mContext, R.layout.grid_item_layout, null);
                convertView = inflater.inflate(R.layout.pic_grid_item, null);
                holder = new GridViewAdapter.ViewHolder();
                holder.iv = convertView.findViewById(R.id.pic_iv);
                convertView.setTag(holder);
            } else {
                holder = (GridViewAdapter.ViewHolder) convertView.getTag();
            }
            //holder.iv;
            XImage.loadImage(holder.iv, mdatas.get(position).getThumb());

            List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
            for (int i = 0; i < mdatas.size(); i++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(mdatas.get(i).getThumb());
                info.setBigImageUrl(mdatas.get(i).getOrigin());
                imageInfos.add(info);
            }

            //赋值备用
            final List<ImageInfo> mimageInfos = imageInfos;
            final int mposition = position;

            NineGridView.setImageLoader(new NineGridView.ImageLoader() {
                @Override
                public void onDisplayImage(Context context, ImageView imageView, String url) {
                    ImageLoader.loadImageLocal(context, imageView, url);
                }

                @Override
                public Bitmap getCacheImage(String url) {
                    return null;
                }
            });

            //点击查看大图片
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) mimageInfos);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, mposition);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        public final class ViewHolder {
            public ImageView iv;
        }
    }


}
