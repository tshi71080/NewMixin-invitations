package com.liuniukeji.mixin.ui.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.base.BaseActivity;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.widget.PtrLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.liuniukeji.mixin.util.Constants.EDIT_GROUP.IS_OPERATE;

public class NewFriedsApplyActivity extends BaseActivity implements NewFriendsContract.View{

    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.friends_recycle)
    RecyclerView friendsRecycle;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewFriendsContract.Presenter mpresenter;
    private NewFriendsAdapter friendsAdapter;
    private List<NewFriendsBean> newFriendsBeanList = new ArrayList<>();

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_new_frieds_apply);
        ButterKnife.bind(this);
    }

    @Override
    protected void processLogic() {
        headTitleTv.setText("新朋友");
        mpresenter = new NewFriendsPresenter(this);
        mpresenter.attachView(this);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        friendsRecycle.setLayoutManager(new LinearLayoutManager(this));
        friendsAdapter = new NewFriendsAdapter(newFriendsBeanList);
        friendsAdapter.bindToRecyclerView(friendsRecycle);
        friendsRecycle.setAdapter(friendsAdapter);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mpresenter.getNewFriendsList();
            }
        });
        mpresenter.getNewFriendsList();
        friendsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.btn_add_friend){
                    mpresenter.addFriend(newFriendsBeanList.get(position).getId());
                }
            }
        });

    }

    @Override
    public void showdata(List<NewFriendsBean> list) {
        //获取好友请求成功，通知消失红点标识
        EventBus.getDefault().post(IS_OPERATE);
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
        newFriendsBeanList.clear();
        newFriendsBeanList.addAll(list);
        if(newFriendsBeanList.size()>0){
            friendsAdapter.setNewData(newFriendsBeanList);
        }else{
            friendsAdapter.setEmptyView(R.layout.empty_layout);
        }


    }

    @Override
    public void addSuccess() {
        mpresenter.getNewFriendsList();
    }

    @Override
    public void onEmpty() {
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNetError() {
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpresenter.detachView();
    }

    @OnClick({R.id.head_back_ly})
    public void onViewClicked(){
        finish();
    }
}
