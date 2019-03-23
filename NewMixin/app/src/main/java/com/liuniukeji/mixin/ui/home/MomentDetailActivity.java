package com.liuniukeji.mixin.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.pub.forward.ForwardActivity;
import com.liuniukeji.mixin.ui.pub.video.VideoPlayActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.ImageLoader;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.widget.AutoMeasureHeightGridView;
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
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 动态详情
 */

public class MomentDetailActivity extends AppCompatActivity implements MomentDetailContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.avatar_iv)
    CircleImageView avatarIv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.level_icon_iv)
    ImageView levelIconIv;
    @BindView(R.id.level_num_tv)
    TextView levelNumTv;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.department_tv)
    TextView departmentTv;
    @BindView(R.id.follow_state_tv)
    TextView followStateTv;
    @BindView(R.id.tweet_content_tv)
    TextView tweetContentTv;
    @BindView(R.id.tweet_pic_grv)
    AutoMeasureHeightGridView tweetPicGrv;
    @BindView(R.id.tweet_video_cover_iv)
    ImageView tweetVideoCoverIv;
    @BindView(R.id.tweet_video_play_iv)
    ImageView tweetVideoPlayIv;
    @BindView(R.id.tweet_video_cover_ly)
    RelativeLayout tweetVideoCoverLy;

    @BindView(R.id.user_rl)
    RelativeLayout userRl;

    @BindView(R.id.forward_iv)
    ImageView forwardIv;
    @BindView(R.id.forward_num_tv)
    TextView forwardNumTv;
    @BindView(R.id.like_iv)
    ImageView likeIv;
    @BindView(R.id.like_num_tv)
    TextView likeNumTv;
    @BindView(R.id.like_ly)
    LinearLayout likeLy;
    @BindView(R.id.comment_iv)
    ImageView commentIv;
    @BindView(R.id.comment_num_tv)
    TextView commentNumTv;


    @BindView(R.id.auto_list_view)
    ListView autoList;

    @BindView(R.id.comment_content_et)
    EditText commentContentEt;
    @BindView(R.id.comment_sub_tv)
    TextView commentSubTv;
    @BindView(R.id.comment_ly)
    LinearLayout commentLy;

    MomentDetailContract.Presenter presenter;

    @BindView(R.id.root_ly)
    RelativeLayout rootLy;

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    View headerView;

    private ListViewAdapter mAdapter;
    private List<MomentDetail.MomentComment> dataList;

    private List<CommentBean> commentDataList;

    String momentId;

    String memberId;
    String toMemberId;

    CommentAdapter commentAdapter;
    int p = 1;
    int mCurrentCounter = 0;

    MomentInfo momentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moment_detail);
        ButterKnife.bind(this);
        headTitleTv.setText("详情");

        presenter = new MomentDetailPresenter(this);
        presenter.attachView(this);

        EventBus.getDefault().register(this);

        //加载基本信息
        momentInfo = (MomentInfo) getIntent().getParcelableExtra("MomentInfo");
        momentId = momentInfo.getId();
        memberId = momentInfo.getMember_id();

        //弃用上个列表页面的传值操作
        //setBaseInfo(momentInfo);

        //Log.e("MMMMMM","memberId"+memberId);

        //getData();

        getCommentData();


        dataList = new ArrayList<>();

        mAdapter = new ListViewAdapter(this, dataList);
        autoList.setAdapter(mAdapter);


        //----------------------------------新列表-------------------------------------
        commentDataList = new ArrayList<>();
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
//        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        commentAdapter = new CommentAdapter(commentDataList);
        commentAdapter.bindToRecyclerView(rvList);
        commentAdapter.setAutoLoadMoreSize(1);
        commentAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        commentAdapter.setEnableLoadMore(true);


        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                getCommentData();
            }
        });

        //设置加载更多监听
        commentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurrentCounter < Constants.COUNT_SIZE * p) {
                    commentAdapter.loadMoreEnd();
                    return;
                }
                p++;
                getCommentData();
            }
        }, rvList);

        //添加头部
        addHeadView();
        setHeadData(momentInfo);

        //----------------------------------新列表-------------------------------------

        //评论点击处理
        autoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = commentDataList.get(position).getReal_name();

                //再次点击取消回复这个人的状态
                String hint = commentContentEt.getHint().toString();
                if (EmptyUtils.isNotEmpty(hint) && hint.contains(name)) {
                    toMemberId = "";
                    commentContentEt.setHint("");
                } else {
                    //初次点击
                    toMemberId = commentDataList.get(position).getMember_id();
                    commentContentEt.setHint("回复：" + name);
                }
            }
        });

        //评论点击回复处理
        commentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                String name = commentDataList.get(position).getReal_name();

                //再次点击取消回复这个人的状态
                String hint = commentContentEt.getHint().toString();
                if (EmptyUtils.isNotEmpty(hint) && hint.contains(name)) {
                    toMemberId = "";
                    commentContentEt.setHint("");
                } else {
                    //初次点击
                    toMemberId = commentDataList.get(position).getMember_id();
                    commentContentEt.setHint("回复：" + name);
                }
            }
        });

    }

    /**
     * 获取页面详情数据
     */
    private void getData() {
        //加载评论列表信息
        if (EmptyUtils.isNotEmpty(momentId)) {
            presenter.getCommentList(momentId);
        }
    }

    /**
     * 单独获取评论内容
     */
    private void getCommentData() {
        //加载评论列表信息
        if (EmptyUtils.isNotEmpty(momentId)) {
            presenter.getSingleCommentList(p, momentId);
        }
    }

    /**
     * 加载页面基本信息
     */
    private void setBaseInfo(final MomentDetail.Moment item) {
        if (null != item && EmptyUtils.isNotEmpty(item.getId())) {

            //设置昵称
            userNameTv.setText(item.getReal_name());
            String vipType = AccountUtils.getUser(this).getVip_type();
            //0,普通用户 1，VIP
            boolean vip = vipType.equals("1") ? true : false;
            if (vip) {
                if (EmptyUtils.isNotEmpty(item.getColor())) {
                    //设置昵称字体颜色
                    userNameTv.setTextColor(Color.parseColor(item.getColor()));
                }
            }
            //显示头像
            XImage.loadAvatar(avatarIv, item.getPhoto_path());

            //用户等级
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
            //0没关注 1已关注
            final boolean isFocus = item.getIs_focus().equals("1") ? true : false;
            if (isFocus) {
                //灰色
                followStateTv.setTextColor(Color.parseColor("#ACACAC"));
                followStateTv.setText("已关注");
                followStateTv.setBackground(getResources().getDrawable(R.drawable.attention_nor_bg));
            } else {
                //黄色
                followStateTv.setTextColor(Color.parseColor("#FFD71B"));
                followStateTv.setText("关注");
                followStateTv.setBackground(getResources().getDrawable(R.drawable.attention_pre_bg));
            }

            //添加or取消关注
            followStateTv.setOnClickListener(new View.OnClickListener() {
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
            dateTv.setText(item.getTime_ago());

            //院系
            departmentTv.setText(item.getSchool_department_name());

            //发布内容
            tweetContentTv.setText(item.getContent());

            //发布视频封面图片


            //发布图片

            List<MomentDetail.Moment.MomentPhoto> list = item.getMoments_photo();

            if (null != list && list.size() > 0) {
                GridViewAdapter2 adapter = new GridViewAdapter2(this, list);
                tweetPicGrv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tweetVideoCoverLy.setVisibility(View.GONE);

            } else {
                if (EmptyUtils.isNotEmpty(item.getVideo_path())) {
                    tweetVideoCoverLy.setVisibility(View.VISIBLE);
                    //加载封面图片
                    XImage.loadImage(tweetVideoCoverIv, item.getVideo_cover_path());

                    tweetVideoCoverLy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ToastUtils.showShortToast("加载视频");
                            Intent intent = new Intent();
                            intent.putExtra("path", item.getVideo_path());
                            intent.putExtra("cover", item.getVideo_cover_path());
                            intent.setClass(MomentDetailActivity.this, VideoPlayActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            //转发
            forwardNumTv.setText(item.getForward_count());

            // 0没点赞 1已点赞
            boolean isLike = item.getIs_like().equals("1") ? true : false;

            likeNumTv.setText(item.getLike_count());

            if (isLike) {
                likeIv.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_s));
                likeNumTv.setTextColor(getResources().getColor(R.color.color_main));
            } else {
                likeIv.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_n));
                likeNumTv.setTextColor(Color.parseColor("#AEAEAE"));
            }

            likeLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //喜欢/取消喜欢
                    presenter.likeMoments(item.getId());
                }
            });

            //评论
            commentNumTv.setText(item.getComment_count());

            //处理隐藏内容
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals("0")) {
                levelNumTv.setVisibility(View.GONE);
                levelIconIv.setVisibility(View.GONE);
                departmentTv.setVisibility(View.GONE);
                //followStateTv.setVisibility(View.GONE);
            }

            String userId = AccountUtils.getUser(this).getId();
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals(userId)) {
                //隐藏关注按钮
                //followStateTv.setVisibility(View.GONE);
            }
        }
    }


    CircleImageView avatarIv1;
    TextView userNameTv1;
    ImageView levelIconIv1;
    TextView levelNumTv1;
    TextView dateTv1;
    TextView followStateTv1;
    TextView tweetContentTv1;
    AutoMeasureHeightGridView tweetPicGrv1;
    ImageView tweetVideoCoverIv1;
    ImageView tweetVideoPlayIv1;
    RelativeLayout tweetVideoCoverLy1;
    RelativeLayout userRl1;
    ImageView forwardIv1;
    TextView forwardNumTv1;
    ImageView likeIv1;
    TextView likeNumTv1;
    LinearLayout likeLy1;
    ImageView commentIv1;
    TextView commentNumTv1;

    ImageView forwardBgIv;
    TextView forwardContentTv;
    FrameLayout mediaLy;
    ImageView mediaPicIv;
    ImageView mediaAvatarIv;
    TextView mediaContentTv;
    TextView mediaTitleTv;
    LinearLayout forwardLy;


    /***添加头部内容**/
    public void addHeadView() {
        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = lif.inflate(R.layout.moment_detail_head, null);

        commentAdapter.removeAllHeaderView();
        commentAdapter.addHeaderView(headerView);
        avatarIv1 = headerView.findViewById(R.id.avatar_iv);
        userNameTv1 = headerView.findViewById(R.id.user_name_tv);
        levelIconIv1 = headerView.findViewById(R.id.level_icon_iv);
        levelNumTv1 = headerView.findViewById(R.id.level_num_tv);

        dateTv1 = headerView.findViewById(R.id.date_tv);
        followStateTv1 = headerView.findViewById(R.id.follow_state_tv);
        tweetContentTv1 = headerView.findViewById(R.id.tweet_content_tv);
        tweetPicGrv1 = headerView.findViewById(R.id.tweet_pic_grv);
        tweetVideoCoverIv1 = headerView.findViewById(R.id.tweet_video_cover_iv);

        tweetVideoPlayIv1 = headerView.findViewById(R.id.tweet_video_play_iv);
        tweetVideoCoverLy1 = headerView.findViewById(R.id.tweet_video_cover_ly);
        userRl1 = headerView.findViewById(R.id.user_rl);

        forwardIv1 = headerView.findViewById(R.id.forward_iv);
        forwardNumTv1 = headerView.findViewById(R.id.forward_num_tv);
        likeIv1 = headerView.findViewById(R.id.like_iv);
        likeNumTv1 = headerView.findViewById(R.id.like_num_tv);
        likeLy1 = headerView.findViewById(R.id.like_ly);
        commentIv1 = headerView.findViewById(R.id.comment_iv);
        commentNumTv1 = headerView.findViewById(R.id.comment_num_tv);

        forwardBgIv = headerView.findViewById(R.id.forward_bg_iv);
        forwardContentTv = headerView.findViewById(R.id.forward_content_tv);
        mediaLy = headerView.findViewById(R.id.media_fly);
        mediaPicIv = headerView.findViewById(R.id.media_pic_iv);
        mediaAvatarIv = headerView.findViewById(R.id.media_avatar_iv);
        mediaContentTv = headerView.findViewById(R.id.media_content_tv);
        mediaTitleTv = headerView.findViewById(R.id.media_title_tv);
        forwardLy = headerView.findViewById(R.id.forward_ly);


    }

    /**
     * 头布局控件赋值
     */
    private void setHeadData1(final MomentInfo item) {
        if (null != item && EmptyUtils.isNotEmpty(item.getId())) {

            //设置昵称
            userNameTv1.setText(item.getReal_name());
            String vipType = AccountUtils.getUser(this).getVip_type();
            //0,普通用户 1，VIP
            boolean vip = vipType.equals("1") ? true : false;
            if (vip) {
                if (EmptyUtils.isNotEmpty(item.getColor())) {
                    //设置昵称字体颜色
                    userNameTv1.setTextColor(Color.parseColor(item.getColor()));
                }
            }
            //显示头像
            XImage.loadAvatar(avatarIv1, item.getPhoto_path());

            //用户等级
            levelNumTv1.setText(item.getExperience());
            String type = item.getVip_type();
            switch (item.getSex()) {
                case "1":
                    //show boy icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv1.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
                case "2":
                    //show girl icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_vip));
                    } else {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.girl_level_color));
                    break;
                default:
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv1.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
            }

            //处理关注状态
            //0没关注 1已关注
            final boolean isFocus = item.getIs_focus().equals("1") ? true : false;
            if (isFocus) {
                //灰色
                followStateTv1.setTextColor(Color.parseColor("#ACACAC"));
                followStateTv1.setText("已关注");
                followStateTv1.setBackground(getResources().getDrawable(R.drawable.attention_nor_bg));
            } else {
                //黄色
                followStateTv1.setTextColor(Color.parseColor("#FFD71B"));
                followStateTv1.setText("关注");
                followStateTv1.setBackground(getResources().getDrawable(R.drawable.attention_pre_bg));

            }

            //添加or取消关注
            followStateTv1.setOnClickListener(new View.OnClickListener() {
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
            dateTv1.setText(item.getTime_ago());

            //院系

            //发布内容
            tweetContentTv1.setText(item.getContent());

            //发布视频封面图片


            //发布图片

            List<MomentInfo.MomentPhoto> list = item.getMoments_photo();

            if (null != list && list.size() > 0) {
                GridViewAdapter adapter = new GridViewAdapter(this, list);
                tweetPicGrv1.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tweetVideoCoverLy1.setVisibility(View.GONE);

            } else {
                if (EmptyUtils.isNotEmpty(item.getVideo_path())) {
                    tweetVideoCoverLy1.setVisibility(View.VISIBLE);
                    //加载封面图片
                    XImage.loadImage(tweetVideoCoverIv1, item.getVideo_cover_path());

                    tweetVideoCoverLy1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ToastUtils.showShortToast("加载视频");
                            Intent intent = new Intent();
                            intent.putExtra("path", item.getVideo_path());
                            intent.putExtra("cover", item.getVideo_cover_path());
                            intent.setClass(MomentDetailActivity.this, VideoPlayActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            //转发
            forwardNumTv1.setText(item.getForward_count());

            // 0没点赞 1已点赞
            boolean isLike = item.getIs_like().equals("1") ? true : false;

            likeNumTv1.setText(item.getLike_count());

            if (isLike) {
                likeIv1.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_s));
                likeNumTv1.setTextColor(getResources().getColor(R.color.color_main));
            } else {
                likeIv1.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_n));
                likeNumTv1.setTextColor(Color.parseColor("#AEAEAE"));
            }

            likeLy1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //喜欢/取消喜欢
                    presenter.likeMoments(item.getId());
                }
            });

            //评论
            commentNumTv1.setText(item.getComment_count());

            //处理隐藏内容
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals("0")) {
                levelNumTv1.setVisibility(View.GONE);
                levelIconIv1.setVisibility(View.GONE);
                //followStateTv1.setVisibility(View.GONE);
            }

            String userId = AccountUtils.getUser(this).getId();
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals(userId)) {
                //隐藏关注按钮
                //followStateTv1.setVisibility(View.GONE);
            }
        }

        //点击用户信息布局跳转到用户信息主页
        userRl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果memberId为"0"则是“学弟学妹提问”内容，不再继续跳转页面
                if (EmptyUtils.isNotEmpty(memberId) && !memberId.equals("0")) {
                    //用户资料页面跳转
                    Intent intent = new Intent();
                    intent.putExtra("userId", memberId);
                    intent.setClass(MomentDetailActivity.this, FriendProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void setHeadData(final MomentInfo item) {
        if (null != item && EmptyUtils.isNotEmpty(item.getId())) {

            //-----------------------------公共-------------------------------
            //设置昵称
            userNameTv1.setText(item.getReal_name());
            String vipType = AccountUtils.getUser(this).getVip_type();
            //0,普通用户 1，VIP
            boolean vip = vipType.equals("1") ? true : false;
            if (vip) {
                if (EmptyUtils.isNotEmpty(item.getColor())) {
                    //设置昵称字体颜色
                    userNameTv1.setTextColor(Color.parseColor(item.getColor()));
                }
            }

            //显示头像
            XImage.loadAvatar(avatarIv1, item.getPhoto_path());

            //用户等级

            levelNumTv1.setText(item.getExperience());
            String type = item.getVip_type();
            switch (item.getSex()) {
                case "1":
                    //show boy icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv1.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
                case "2":
                    //show girl icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_vip));
                    } else {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_n));
                    }
                    levelNumTv1.setTextColor(getResources().getColor(R.color.girl_level_color));
                    break;
                default:
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv1.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv1.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
            }

            //处理关注状态
            //0没关注 1已关注
            final boolean isFocus = item.getIs_focus().equals("1") ? true : false;

            if (isFocus) {
                //灰色
                followStateTv1.setTextColor(Color.parseColor("#ACACAC"));
                followStateTv1.setText("已关注");
                followStateTv1.setBackground(getResources().getDrawable(R.drawable.attention_nor_bg));
            } else {
                //黄色
                followStateTv1.setTextColor(Color.parseColor("#FFD71B"));
                followStateTv1.setText("关注");
                followStateTv1.setBackground(getResources().getDrawable(R.drawable.attention_pre_bg));
            }

            //添加or取消关注
            followStateTv1.setOnClickListener(new View.OnClickListener() {
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
            dateTv1.setText(item.getTime_ago());





            String mType = item.getType();
            // 0纯文字，1,图文；2，视频；3，文章


            //转发
            forwardNumTv1.setText(item.getForward_count());
            final String forwardId = item.getForward_id();

            forwardLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //-----------------------------传值数据处理-------------------------------
                    boolean isVideo = false;
                    String coverPath = "";
                    String forwardContent = "";
                    String fId;

                    if (!forwardId.equals("0")) {
                        //是转发
                        MomentInfo.ForwardMoment moment1 = item.getForward_moments();
                        List<MomentInfo.MomentPhoto> list1 = moment1.getMoments_photo();
                        fId = forwardId;
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
                        fId = item.getId();
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
                    intent.setClass(MomentDetailActivity.this, ForwardActivity.class);
                    startActivity(intent);
                }
            });

            // 0没点赞 1已点赞
            boolean isLike = item.getIs_like().equals("1") ? true : false;

            likeNumTv1.setText(item.getLike_count());

            if (isLike) {
                likeIv1.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_s));
                likeNumTv1.setTextColor(getResources().getColor(R.color.color_main));
            } else {
                likeIv1.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_n));
                likeNumTv1.setTextColor(Color.parseColor("#AEAEAE"));
            }

            likeLy1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //喜欢/取消喜欢
                    presenter.likeMoments(item.getId());
                }
            });

            //评论
            commentNumTv1.setText(item.getComment_count());

            String memberId = item.getMember_id();
            //处理隐藏内容
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals("0")) {
                levelNumTv1.setVisibility(View.INVISIBLE);
                levelIconIv1.setVisibility(View.INVISIBLE);
                //departmentTv1.setVisibility(View.INVISIBLE);
                //followStateTv1.setVisibility(View.INVISIBLE);
            } else {
                levelNumTv.setVisibility(View.VISIBLE);
                levelIconIv.setVisibility(View.VISIBLE);
                //departmentTv.setVisibility(View.VISIBLE);
                //followStateTv1.setVisibility(View.VISIBLE);
            }

            String userId = AccountUtils.getUser(this).getId();
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals(userId)) {
                //隐藏关注按钮
                //followStateTv1.setVisibility(View.INVISIBLE);
            } else {
                //followStateTv1.setVisibility(View.VISIBLE);
            }
            //0,普通朋友圈，1，二手市场，2，学弟学妹提问
            boolean anonymous = item.getTag().equals("2");
            if (anonymous) {
                //followStateTv1.setVisibility(View.GONE);
            }

            //-----------------------------公共-------------------------------


            //-------------------------------转发处理-----------------------------
            String forward_id = item.getForward_id();
            if (forward_id.equals("0")) {

                List<MomentInfo.MomentPhoto> list = item.getMoments_photo();

                //不是转发
                forwardBgIv.setVisibility(View.GONE);
                tweetContentTv1.setText(item.getContent());
                forwardContentTv.setVisibility(View.GONE);

                if (mType.equals("3")) {
                    tweetPicGrv1.setVisibility(View.GONE);
                    tweetVideoCoverIv1.setVisibility(View.GONE);
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
                    tweetPicGrv1.setVisibility(View.GONE);

                    if (null != list && list.size() > 0) {
                        GridViewAdapter adapter = new GridViewAdapter(this, list);
                        tweetPicGrv1.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        tweetPicGrv1.setVisibility(View.VISIBLE);
                        tweetVideoCoverLy1.setVisibility(View.GONE);

                    } else {
                        tweetPicGrv1.setVisibility(View.GONE);
                        if (EmptyUtils.isNotEmpty(item.getVideo_path())) {
                            tweetPicGrv1.setVisibility(View.GONE);
                            tweetVideoCoverLy1.setVisibility(View.VISIBLE);
                            tweetVideoCoverIv1.setVisibility(View.VISIBLE);
                            //加载封面图片
                            XImage.loadImage(tweetVideoCoverIv1, item.getVideo_cover_path());

                            tweetVideoCoverLy1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ToastUtils.showShortToast("加载视频");
                                    Intent intent = new Intent();
                                    intent.putExtra("path", item.getVideo_path());
                                    intent.putExtra("cover", item.getVideo_cover_path());
                                    intent.setClass(MomentDetailActivity.this, VideoPlayActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //给视频缩略图加载默认封面
                            //coverIv.setImageResource(R.mipmap.default_image);
                            //解决复用引起的bug
                            tweetVideoCoverIv1.setVisibility(View.GONE);
                            tweetPicGrv1.setVisibility(View.GONE);
                        }
                    }
                }

            } else {
                //是转发
                final MomentInfo.ForwardMoment item1 = item.getForward_moments();
                List<MomentInfo.MomentPhoto> list = item1.getMoments_photo();

                forwardBgIv.setVisibility(View.VISIBLE);
                //显示内容
                tweetContentTv1.setText(item.getContent());

                //显示原来的转发内容
                forwardContentTv.setVisibility(View.VISIBLE);
                forwardContentTv.setText(item1.getReal_name() + "：" +
                        item1.getContent());


                if (mType.equals("3")) {
                    tweetPicGrv1.setVisibility(View.GONE);
                    tweetVideoCoverLy1.setVisibility(View.GONE);
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
                    tweetPicGrv1.setVisibility(View.GONE);

                    if (null != list && list.size() > 0) {
                        GridViewAdapter adapter = new GridViewAdapter(this, list);
                        tweetPicGrv1.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        tweetPicGrv1.setVisibility(View.VISIBLE);
                        tweetVideoCoverLy1.setVisibility(View.GONE);

                    } else {
                        tweetPicGrv1.setVisibility(View.GONE);
                        if (EmptyUtils.isNotEmpty(item1.getVideo_path())) {
                            tweetPicGrv1.setVisibility(View.GONE);
                            tweetVideoCoverLy1.setVisibility(View.VISIBLE);
                            tweetVideoCoverIv1.setVisibility(View.VISIBLE);
                            //加载封面图片
                            XImage.loadImage(tweetVideoCoverIv1, item1.getVideo_cover_path());
                            tweetVideoCoverLy1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ToastUtils.showShortToast("加载视频");
                                    Intent intent = new Intent();
                                    intent.putExtra("path", item1.getVideo_path());
                                    intent.putExtra("cover", item1.getVideo_cover_path());
                                    intent.setClass(MomentDetailActivity.this, VideoPlayActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //给视频缩略图加载默认封面
                            //coverIv.setImageResource(R.mipmap.default_image);
                            //解决复用引起的bug
                            tweetVideoCoverIv1.setVisibility(View.GONE);
                            tweetPicGrv1.setVisibility(View.GONE);
                        }
                    }
                }
            }
            //-------------------------------转发处理-----------------------------
        }
    }

    /**
     * 头布局控件赋值
     */
    private void setHeadData2(final MomentDetail.Moment item) {
        if (null != item && EmptyUtils.isNotEmpty(item.getId())) {


            //只更新底部的转发、喜欢、评论的数量

            forwardNumTv1.setText(item.getForward_count());
            // 0没点赞 1已点赞
            boolean isLike = item.getIs_like().equals("1") ? true : false;

            likeNumTv1.setText(item.getLike_count());

            if (isLike) {
                likeIv1.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_s));
                likeNumTv1.setTextColor(getResources().getColor(R.color.color_main));
            } else {
                likeIv1.setBackground(getResources().getDrawable(R.mipmap.moment_icon_like_n));
                likeNumTv1.setTextColor(Color.parseColor("#AEAEAE"));
            }

            likeLy1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //喜欢/取消喜欢
                    presenter.likeMoments(item.getId());
                }
            });

            //评论
            commentNumTv1.setText(item.getComment_count());
        }
    }


    @OnClick({R.id.head_back_ly, R.id.follow_state_tv, R.id.forward_iv,
            R.id.like_ly, R.id.comment_sub_tv, R.id.user_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                closeKeyboard();
                break;
            case R.id.follow_state_tv:
                //关注
                break;
            case R.id.forward_iv:
                break;
            case R.id.like_ly:
                //喜欢
                break;
            case R.id.comment_sub_tv:
                //提交评论
                String content = commentContentEt.getText().toString();
                if (EmptyUtils.isNotEmpty(content)) {
                    if (EmptyUtils.isNotEmpty(momentId)) {
                        //提交数据
                        presenter.submitComment(momentId, content, toMemberId);
                        //清空回复某人的相关数据
                        toMemberId = "";
                        commentContentEt.setHint("");
                    } else {
                        ToastUtils.showShortToast("页面参数异常，无法提交评论数据");
                    }
                } else {
                    ToastUtils.showShortToast("评论内容不能为空");
                }
                closeKeyboard();
                break;
            case R.id.user_rl:
                //如果memberId为"0"则是“学弟学妹提问”内容，不再继续跳转页面
                if (EmptyUtils.isNotEmpty(memberId) && !memberId.equals("0")) {
                    //用户资料页面跳转
                    Intent intent = new Intent();
                    intent.putExtra("userId", memberId);
                    intent.setClass(MomentDetailActivity.this, FriendProfileActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(MomentDetail detail) {
        MomentDetail.Moment moment = detail.getMoments();
        //基本信息赋值
        setHeadData2(moment);
    }

    @Override
    public void liftData(String info) {
        //提交评论返回状态信息
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
        }
        //重新页面信息
        getData();
        getCommentData();

        //清空输入框内容
        commentContentEt.setText(null);
        commentContentEt.clearComposingText();
    }

    @Override
    public void liftData(List<CommentBean> beans) {
        if (this.commentDataList == null || beans == null || beans.size() == 0) {
            if (p == 1) {
                if (null != swipeLayout) {
                    swipeLayout.setRefreshing(false);
                }
            }
            if (null != commentAdapter) {
                commentAdapter.loadMoreEnd();
            }
            return;
        }
        if (p == 1) {
            if (null != swipeLayout) {
                swipeLayout.setRefreshing(false);
            }
            this.commentDataList.clear();
            this.commentDataList.addAll(beans);
            commentAdapter.setNewData(commentDataList);
            mCurrentCounter = commentAdapter.getData().size();
        } else {
            if (null != swipeLayout) {
                swipeLayout.setRefreshing(false);
            }
            this.commentDataList.addAll(beans);
            commentAdapter.setNewData(commentDataList);
            mCurrentCounter = commentAdapter.getData().size();
        }
    }

    @Override
    public void onEmpty() {
        if (null != swipeLayout) {
            swipeLayout.setRefreshing(false);
        }
        if (null != commentAdapter) {
            commentAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onNetError() {
        if (null != swipeLayout) {
            swipeLayout.setRefreshing(false);
        }
        if (null != commentAdapter) {
            commentAdapter.loadMoreEnd();
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

            ViewHolder holder = null;
            if (convertView == null) {
                //convertView = View.inflate(mContext, R.layout.grid_item_layout, null);
                convertView = inflater.inflate(R.layout.pic_grid_item, null);
                holder = new ViewHolder();
                holder.iv = convertView.findViewById(R.id.pic_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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


    /**
     * GridView图片适配器
     */
    private class GridViewAdapter2 extends BaseAdapter {

        private Context mContext;
        private List<MomentDetail.Moment.MomentPhoto> mdatas;
        private LayoutInflater inflater;

        public GridViewAdapter2(Context context, List<MomentDetail.Moment.MomentPhoto> data) {
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

            ViewHolder holder = null;
            if (convertView == null) {
                //convertView = View.inflate(mContext, R.layout.grid_item_layout, null);
                convertView = inflater.inflate(R.layout.pic_grid_item, null);
                holder = new ViewHolder();
                holder.iv = convertView.findViewById(R.id.pic_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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


    /**
     * 评论列表适配器
     */
    class CommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

        public CommentAdapter(@Nullable List<CommentBean> data) {
            super(R.layout.comment_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final CommentBean item) {

            //设置昵称
            helper.setText(R.id.user_name_tv, item.getReal_name());
            TextView nameTv = helper.getView(R.id.user_name_tv);
            if (EmptyUtils.isNotEmpty(item.getColor())) {
                //设置昵称字体颜色
                nameTv.setTextColor(Color.parseColor(item.getColor()));
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

            //院系
            /*TextView departmentTv = helper.getView(R.id.department_tv);
            String school_class = item.getSchool_class() + "届/";
            departmentTv.setText(school_class + item.getSchool_department_name());*/


            //评论内容
            TextView commentTv = helper.getView(R.id.comment_content_tv);

            //处理针对某人的评论
            String toRealName = item.getTo_real_name();
            if (EmptyUtils.isNotEmpty(toRealName)) {
                commentTv.setText("回复" + toRealName + "：" + item.getContent());
            } else {
                //普通评论
                commentTv.setText(item.getContent());
            }

        }
    }


    /**
     * 评论列表ListView适配器
     */
    private class ListViewAdapter extends BaseAdapter {

        private Context mContext;
        private List<MomentDetail.MomentComment> mdatas;
        private LayoutInflater inflater;

        public ListViewAdapter(Context context, List<MomentDetail.MomentComment> data) {
            mContext = context;
            mdatas = data;
            inflater = LayoutInflater.from(context);
        }

        public void addData(List<MomentDetail.MomentComment> data) {
            mdatas = data;
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

            ViewHolder holder = null;
            if (convertView == null) {
                //convertView = View.inflate(mContext, R.layout.grid_item_layout, null);
                convertView = inflater.inflate(R.layout.comment_list_item, null);
                holder = new ViewHolder();
                holder.user_name_tv = convertView.findViewById(R.id.user_name_tv);
                holder.avatarIv = convertView.findViewById(R.id.avatar_iv);

                holder.levelIconIv = convertView.findViewById(R.id.level_icon_iv);
                holder.levelNumTv = convertView.findViewById(R.id.level_num_tv);
                //holder.departmentTv = convertView.findViewById(R.id.department_tv);
                holder.commentTv = convertView.findViewById(R.id.comment_content_tv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //显示数据
            MomentDetail.MomentComment item = mdatas.get(position);

            //设置昵称
            holder.user_name_tv.setText(item.getReal_name());
            if (EmptyUtils.isNotEmpty(item.getColor())) {
                //设置昵称字体颜色
                holder.user_name_tv.setTextColor(Color.parseColor(item.getColor()));
            }

            //显示头像
            XImage.loadAvatar(holder.avatarIv, item.getPhoto_path());

            //用户等级
            ImageView levelIconIv = holder.levelIconIv;
            TextView levelNumTv = holder.levelNumTv;

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

            //院系
            /*TextView departmentTv = holder.departmentTv;
            departmentTv.setText(item.getSchool_department_name());*/

            //评论内容
            TextView commentTv = holder.commentTv;

            //处理针对某人的评论
            String toRealName = item.getTo_real_name();
            if (EmptyUtils.isNotEmpty(toRealName)) {
                commentTv.setText("回复" + toRealName + "：" + item.getContent());
            } else {
                //普通评论
                commentTv.setText(item.getContent());
            }

            return convertView;
        }

        public final class ViewHolder {
            public TextView user_name_tv;
            public ImageView avatarIv;
            public ImageView levelIconIv;
            public TextView levelNumTv;

            public TextView departmentTv;
            public TextView commentTv;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.UPDATE_MOMENT state) {
        switch (state) {
            case ON_CHANGE:
                //刷新数据
                getData();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 隐藏软键盘
     */
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootLy.getWindowToken(), 0);
    }


}
