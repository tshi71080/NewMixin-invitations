package com.liuniukeji.mixin.ui.discover;

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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.home.MomentDetailActivity;
import com.liuniukeji.mixin.ui.home.MomentInfo;
import com.liuniukeji.mixin.ui.pub.video.VideoPlayActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.ImageLoader;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 学弟/学妹提问列表
 */
public class BroSisActivity extends AppCompatActivity implements BroSisContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    BroSisContract.Presenter presenter;

    int p = 1;
    int mCurrentCounter = 0;

    private MomentAdapter mAdapter;
    private List<MomentInfo> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brosis);
        ButterKnife.bind(this);
        headTitleTv.setText("学弟/学妹提问");

        presenter = new BroSisPresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
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
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("MomentInfo", dataList.get(position));
                intent.putExtras(bundle);
                intent.setClass(BroSisActivity.this, MomentDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取页面列表数据
     */
    private void getData() {
        presenter.getMomentList(p);
    }

    @Override
    public void liftData(List<MomentInfo> infoList) {
        if (this.dataList == null || infoList == null || infoList.size() == 0) {
            if (p == 1) {
                mAdapter.setEmptyView(R.layout.empty_layout);
                if (null != swipeLayout) {
                    swipeLayout.setRefreshing(false);
                }
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
        }
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void liftData(String info) {
        //返回结果，刷新数据
        getData();
    }

    @Override
    public void onEmpty() {
        if (this.dataList != null && p == 1) {
            this.dataList.clear();
            mAdapter.setNewData(this.dataList);
            mAdapter.setEmptyView(R.layout.empty_layout);
        } else if (this.dataList != null && mAdapter != null && p > 1) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onNetError() {
        if (this.dataList != null && p == 1) {
            this.dataList.clear();
            mAdapter.setNewData(this.dataList);
            mAdapter.setEmptyView(R.layout.empty_layout);
        } else if (this.dataList != null && mAdapter != null && p > 1) {
            mAdapter.loadMoreEnd();
        }
    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }


    class MomentAdapter extends BaseQuickAdapter<MomentInfo, BaseViewHolder> {

        public MomentAdapter(@Nullable List<MomentInfo> data) {
            super(R.layout.moment_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final MomentInfo item) {

            //设置昵称
            helper.setText(R.id.user_name_tv, item.getReal_name());
            TextView nameTv = helper.getView(R.id.user_name_tv);
            String vipType= AccountUtils.getUser(BroSisActivity.this).getVip_type();
            //0,普通用户 1，VIP
            boolean vip=vipType.equals("1")?true:false;
            if(vip){
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

            //隐藏等级&会员信息
            levelIconIv.setVisibility(View.GONE);
            levelNumTv.setVisibility(View.GONE);

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
            followTv.setVisibility(View.GONE);
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
            TextView departmentTv = helper.getView(R.id.department_tv);
            departmentTv.setText(item.getSchool_department_name());
            departmentTv.setVisibility(View.GONE);

            //发布内容
            TextView tweetContentTv = helper.getView(R.id.tweet_content_tv);
            tweetContentTv.setText(item.getContent());

            //发布视频封面图片
            ImageView coverIv = helper.getView(R.id.tweet_video_cover_iv);
            RelativeLayout coverLy = helper.getView(R.id.tweet_video_cover_ly);


            //发布图片
            GridView grv = helper.getView(R.id.tweet_pic_grv);
            List<MomentInfo.MomentPhoto> list = item.getMoments_photo();

            if (null != list && list.size() > 0) {
                GridViewAdapter adapter = new GridViewAdapter(BroSisActivity.this, list);
                grv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                coverLy.setVisibility(View.GONE);

            } else {
                if (EmptyUtils.isNotEmpty(item.getVideo_path())) {
                    coverLy.setVisibility(View.VISIBLE);
                    //加载封面图片
                    XImage.loadImage(coverIv, item.getVideo_cover_path());

                    coverLy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShortToast("加载视频");
                            Intent intent = new Intent();
                            intent.putExtra("path", item.getVideo_path());
                            intent.putExtra("cover", item.getVideo_cover_path());
                            intent.setClass(mContext, VideoPlayActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

            //转发
            ImageView forwardIv = helper.getView(R.id.forward_iv);
            TextView forwardTv = helper.getView(R.id.forward_num_tv);
            forwardTv.setText(item.getForward_count());

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
