package com.hyphenate.chatui.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatui.Constant;
import com.hyphenate.easeui.EaseConstant;
import com.liuniukeji.mixin.R;
import com.hyphenate.chatui.runtimepermissions.PermissionsManager;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.ui.discover.GroupBackBean;
import com.liuniukeji.mixin.ui.discover.GroupInfo;
import com.liuniukeji.mixin.ui.discover.GroupMemberDetailContract;
import com.liuniukeji.mixin.ui.discover.GroupMemberDetailPresenter;
import com.liuniukeji.mixin.ui.main.MainActivity;
import com.liuniukeji.mixin.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.liuniukeji.mixin.util.Constants.MUTE_INFO.MUTE;
import static com.liuniukeji.mixin.util.Constants.MUTE_INFO.UN_MUTE;

/**
 * 聊天页面
 * chat activity，
 * EaseChatFragment was used {@link EaseChatFragment}
 */
public class ChatActivity extends BaseActivity implements GroupMemberDetailContract.View {
    public static ChatActivity activityInstance;
    private MyEaseChatFragment chatFragment;
    public String toChatUsername;
    private int chatType;
    private List<String> adminList = Collections.synchronizedList(new ArrayList<String>());
    private EMGroup group;
    private GroupMemberDetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        presenter = new GroupMemberDetailPresenter(this);
        presenter.attachView(this);

        activityInstance = this;
        EventBus.getDefault().register(this);
        //get user id or group id
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        activityInstance = null;
        presenter.detachView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (chatType == Constant.CHATTYPE_GROUP) {
            group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (null != group) {
                adminList.clear();
                adminList.addAll(group.getAdminList());
            }
            //获取禁言状态
            presenter.getMuteStatus(toChatUsername);
        }
    }

    /**
     * 是否禁言，禁言不显示输入框
     */
    private void isMute() {
        if (chatType == Constant.CHATTYPE_GROUP) {//群聊

            //获取群拓展信息
            if (null != group.getAnnouncement()) {
                String extensionInfo = group.getAnnouncement();
                if (!TextUtils.isEmpty(extensionInfo)) {
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(extensionInfo);
                    String status = jsonObject.getString(Constants.disableSendMsg);
                    if (!TextUtils.isEmpty(status)) {
                        //不是群主也不是管理员控制是否可以输入
                        if (!isCurrentOwner(group) && !isCurrentAdmin(group)) {
                            //0未禁言，1 已禁言
                            if ("0".equals(status)) {
                                chatFragment.inputMenu.setVisibility(View.VISIBLE);
                                chatFragment.muteHintView.setVisibility(View.GONE);
                            } else if ("1".equals(status)) {
                                chatFragment.inputMenu.setVisibility(View.GONE);
                                chatFragment.muteHintView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username)) {
            super.onNewIntent(intent);
        } else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    //是否是群主
    boolean isCurrentOwner(EMGroup group) {
        boolean result = false;
        if (null != group) {
            String owner = group.getOwner();
            if (owner == null || owner.isEmpty()) {
                return false;
            } else {
                result = owner.equals(EMClient.getInstance().getCurrentUser());
            }
        }
        return result;
    }

    //是否是管理员
    boolean isCurrentAdmin(EMGroup group) {
        synchronized (adminList) {
            String currentUser = EMClient.getInstance().getCurrentUser();
            for (String admin : adminList) {
                if (currentUser.equals(admin)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.MUTE_INFO info) {
        if (chatType == Constant.CHATTYPE_GROUP) {
            if (null != group) {
                adminList.clear();
                adminList.addAll(group.getAdminList());
            }
            if (!isCurrentOwner(group) && !isCurrentAdmin(group)) {
                if (info == MUTE) {
                    chatFragment.inputMenu.setVisibility(View.GONE);
                    chatFragment.muteHintView.setVisibility(View.VISIBLE);
                } else if (info == UN_MUTE) {
                    chatFragment.inputMenu.setVisibility(View.VISIBLE);
                    chatFragment.muteHintView.setVisibility(View.GONE);
                }
            } else {
                chatFragment.inputMenu.setVisibility(View.VISIBLE);
                chatFragment.muteHintView.setVisibility(View.GONE);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.GROUP_INFO info) {

        if (info == Constants.GROUP_INFO.DROP) {
            finish();
        }
    }

    @Override
    public void liftData(GroupInfo groupInfo) {

    }

    @Override
    public void liftData(CommonResult result) {

    }

    @Override
    public void liftUpdateData(GroupBackBean bean, CommonResult result) {

    }

    @Override
    public void muteSucess() {

    }

    @Override
    public void getStatusSucess(String status) {
        //不是群主也不是管理员控制是否可以输入
        if (!isCurrentOwner(group) && !isCurrentAdmin(group)) {
            //0未禁言，1 已禁言
            if ("0".equals(status)) {
                chatFragment.inputMenu.setVisibility(View.VISIBLE);
                chatFragment.muteHintView.setVisibility(View.GONE);
            } else if ("1".equals(status)) {
                chatFragment.inputMenu.setVisibility(View.GONE);
                chatFragment.muteHintView.setVisibility(View.VISIBLE);
            }
        } else {
            chatFragment.inputMenu.setVisibility(View.VISIBLE);
            chatFragment.muteHintView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
}
