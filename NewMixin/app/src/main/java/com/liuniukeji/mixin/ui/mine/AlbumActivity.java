package com.liuniukeji.mixin.ui.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的相册
 */

public class AlbumActivity extends AppCompatActivity {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.album_container_ly)
    FrameLayout albumContainerLy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        headTitleTv.setText("我的相册");

        UserBean userBean= AccountUtils.getUser(this);

        String userId="";
        if(null!=userBean){
             userId=userBean.getId();
        }
        //加载相册页面
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction tr = fragmentManager.beginTransaction();
        tr.add(R.id.album_container_ly,AlbumFragment.newInstance(userId),"Album");
        tr.commit();
    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }
}
