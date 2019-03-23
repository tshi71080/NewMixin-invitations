package com.liuniukeji.mixin.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.base.BaseFragment;
import com.liuniukeji.mixin.ui.pub.video.VideoPlayActivity;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.base.BaseFragment;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 我的动态--相册模块
 *
 * @see MyMomentActivity
 */
public class AlbumFragment extends BaseFragment implements AlbumContract.View {

    private static final String ARG_PARAM1 = "param1";

    Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    private String memberId;

    int p = 1;
    int mCurrentCounter = 0;

    private AlbumAdapter mAdapter;
    private List<AlbumInfo> dataList;

    AlbumContract.Presenter presenter;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AlbumFragment.
     */
    public static AlbumFragment newInstance(String param1) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memberId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View getLayout(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_album, null, false);

        return view;
    }

    /**
     * 获取页面列表数据
     */
    private void getData() {
        if (EmptyUtils.isNotEmpty(memberId)) {
            presenter.getSomeoneAlbumList(p, memberId);
        } else {
            ToastUtils.showShortToast("页面参数错误，无法获取相册列表数据");
        }
    }

    @Override
    protected void processLogic() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        presenter = new AlbumPresenter(getContext());
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        rvList.setLayoutManager(manager);
        //rvList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        dataList = new ArrayList<>();
        mAdapter = new AlbumAdapter(dataList);
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
//                if (mCurrentCounter < Constants.COUNT_SIZE * p) {
                if (mCurrentCounter < 20 * p) {
                    mAdapter.loadMoreEnd();
                    return;
                }
                p++;
                getData();
            }
        }, rvList);

        //获取页面数据
        getData();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void liftData(List<AlbumInfo> infoList) {
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
    public void onEmpty() {
        if(null!=swipeLayout){
            swipeLayout.setRefreshing(false);
        }
        mAdapter.setEmptyView(R.layout.empty_layout);
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onNetError() {
        if(null!=swipeLayout){
            swipeLayout.setRefreshing(false);
        }
        mAdapter.setEmptyView(R.layout.empty_layout);
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    /**
     * 相册列表适配器
     */
    class AlbumAdapter extends BaseQuickAdapter<AlbumInfo, BaseViewHolder> {

        public AlbumAdapter(@Nullable List<AlbumInfo> data) {
            super(R.layout.album_grid_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final AlbumInfo item) {

            //显示普通图片
            ImageView picIv = helper.getView(R.id.pic_iv);
            ImageView playIv = helper.getView(R.id.video_play_iv);

            if (EmptyUtils.isNotEmpty(item.getVideo_path_thumb())) {
                //显示视频缩略图
                XImage.loadImage(picIv, item.getVideo_path_thumb());
                //Log.e("TTT2",item.getVideo_path_thumb());
                playIv.setVisibility(View.VISIBLE);

                //播放视频按钮
                playIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("path", item.getVideo_path());
                        intent.putExtra("cover", item.getPhoto_path_thumb());
                        intent.setClass(mContext, VideoPlayActivity.class);
                        startActivity(intent);
                    }
                });

            } else {
                //显示普通图片
                XImage.loadImage(picIv, item.getPhoto_path_thumb());
                //Log.e("TTT1",item.getPhoto_path());
                //Log.e("TTT3",item.getPhoto_path_thumb());
                playIv.setVisibility(View.GONE);
                //--------------------------------点击查看大图片---------------------------------
                List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl (item.getPhoto_path_thumb());
                info.setBigImageUrl(item.getPhoto_path());
                imageInfos.add(info);

                final List<ImageInfo> mimageInfos = imageInfos;
                picIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) mimageInfos);
                        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                //--------------------------------点击查看大图片---------------------------------
            }

        }
    }

}