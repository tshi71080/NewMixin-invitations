package com.liuniukeji.mixin.ui.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.login.ConnectFriendBean;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.ui.mine.baseinfo.CommonInfo;
import com.liuniukeji.mixin.ui.mine.baseinfo.SignatureContract;
import com.liuniukeji.mixin.ui.mine.baseinfo.SignaturePresenter;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的昵称
 * (修改功能)
 */
public class SetFriendMarkActivity extends AppCompatActivity implements FriendProfileContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.signature_content_et)
    EditText signatureContentEt;
    @BindView(R.id.signature_save_tv)
    TextView signatureSaveTv;

    FriendProfileContract.Presenter presenter;
    private String friendUserId;
    private String remarkName;
    private String im_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setmark);
        ButterKnife.bind(this);
        headTitleTv.setText("设置备注");
        presenter = new FriendProfilePresenter(this);
        presenter.attachView(this);
        friendUserId = getIntent().getStringExtra("friendUserId");
        remarkName = getIntent().getStringExtra("remarkName");
        im_name = getIntent().getStringExtra("im_name");
        //赋值
        signatureContentEt.setText(remarkName);
    }

    @OnClick({R.id.head_back_ly, R.id.signature_save_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.signature_save_tv:
                //保存数据
                String content = signatureContentEt.getText().toString();
                if (EmptyUtils.isNotEmpty(content)) {
                    presenter.setFriedmark(friendUserId,content);
                } else {
                    ToastUtils.showShortToast("请填写备注名称");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }

    @Override
    public void liftData(FriendUserInfo userInfo) {

    }

    @Override
    public void liftData(String info) {

    }

    @Override
    public void liftAddBlack(String info) {

    }

    @Override
    public void setMarkSuccess() {
        presenter.getRemarkList();
        /**
         * 修改完备注本地保存的联系人昵称也要修改
         */
        Map<String,EaseUser> map = DemoHelper.getInstance().getContactList();
        EaseUser myFrienduser = map.get(im_name);
        if(myFrienduser!=null){
            myFrienduser.setNickname(signatureContentEt.getText().toString());
        }
        //修改后重新保存本地联系人信息
        List<EaseUser> easeUserList = new ArrayList<>();
        map.remove(im_name);
        map.put(im_name,myFrienduser);
        for(EaseUser easeUser:map.values()){
            easeUserList.add(easeUser);
        }
        //更新从接口获取过来的联系人
        List<ConnectFriendBean> connectFriendBeanList = AccountUtils.getUserConnectFriend();
        for(int i=0;i<connectFriendBeanList.size();i++){
            ConnectFriendBean connectFriendBean = connectFriendBeanList.get(i);
            if(im_name.equals(connectFriendBean.getIm_name())){
                connectFriendBean.setRemark(signatureContentEt.getText().toString());
                connectFriendBeanList.add(connectFriendBean);
                connectFriendBeanList.remove(i);
                AccountUtils.clearUserConnectFriend();
                AccountUtils.saveUserConnectfriend(connectFriendBeanList);
                break;
            }
        }
        DemoHelper.getInstance().updateContactList(easeUserList);
        setResult(0x0009);
        finish();
    }

    @Override
    public void getRemarklist(List<MyRemarkFriendBean> remarkFriendBeanList) {
        //保存用户备注好友的信息
        String remakString = MMKV.defaultMMKV().getString(Constants.USERREMARKINFO,"");
        if(!TextUtils.isEmpty(remakString)){
            MMKV.defaultMMKV().remove(Constants.USERREMARKINFO);
        }
        try {
            String userResult = JSON.toJSONString(remarkFriendBeanList);
            MMKV.defaultMMKV().putString(Constants.USERREMARKINFO,userResult);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

    @Override
    public void applySucess() {

    }

    @Override
    public void deleteSuccess() {

    }
}
