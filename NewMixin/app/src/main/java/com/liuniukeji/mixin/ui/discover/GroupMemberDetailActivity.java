package com.liuniukeji.mixin.ui.discover;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.widget.AutoMeasureHeightGridView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.liuniukeji.mixin.util.Constants.MUTE_INFO.MUTE;
import static com.liuniukeji.mixin.util.Constants.MUTE_INFO.UN_MUTE;

/**
 * 群资料详情
 * 群组聊天页面使用
 * （包括普通成员与群主）
 */

public class GroupMemberDetailActivity extends AppCompatActivity implements GroupMemberDetailContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.group_avatar_iv)
    CircleImageView groupAvatarIv;
    @BindView(R.id.group_name_tv)
    TextView groupNameTv;
    @BindView(R.id.group_brief_info_tv)
    TextView groupBriefInfoTv;
    @BindView(R.id.group_id_tv)
    TextView groupIdTv;
    @BindView(R.id.group_master_name_tv)
    TextView groupMasterNameTv;
    @BindView(R.id.group_master_avatar_iv)
    CircleImageView groupMasterAvatarIv;
    @BindView(R.id.group_member_part_grv)
    AutoMeasureHeightGridView groupMemberPartGrv;
    @BindView(R.id.look_all_member_rl)
    RelativeLayout lookAllMemberRl;
    @BindView(R.id.invite_friend_rl)
    RelativeLayout inviteFriendRl;
    @BindView(R.id.group_psw_rl)
    RelativeLayout groupPswRl;
    @BindView(R.id.block_msg_sw)
    Switch blockMsgSw;
    @BindView(R.id.clear_chat_history_rl)
    RelativeLayout clearChatHistoryRl;
    @BindView(R.id.open_approve_sw)
    Switch openApproveSw;
    @BindView(R.id.group_leave_tv)
    TextView groupLeaveTv;

    @BindView(R.id.group_member_part_rcv)
    RecyclerView groupMemberPartRcv;


    GroupMemberDetailContract.Presenter presenter;

    String groupImId;
    String groupId;

    List<GroupInfo.Member> memberList = new ArrayList<>();
    @BindView(R.id.group_psw_ly)
    LinearLayout groupPswLy;
    @BindView(R.id.open_approve_ly)
    LinearLayout openApproveLy;
    @BindView(R.id.group_avatar_rl)
    RelativeLayout groupAvatarRl;
    @BindView(R.id.group_name_rl)
    RelativeLayout groupNameRl;
    @BindView(R.id.group_brief_info_rl)
    RelativeLayout groupBriefInfoRl;
    @BindView(R.id.mute_all_user)
    Switch muteAllUser;
    @BindView(R.id.ll_mute_group)
    LinearLayout llMuteGroup;
    @BindView(R.id.group_transform_rl)
    RelativeLayout groupTransformRl;
    @BindView(R.id.group_transform_ly)
    LinearLayout groupTransformLy;

    private MemberAdapter mAdapter;

    private List<String> adminList = Collections.synchronizedList(new ArrayList<String>());
    private EMGroup group;
    private ProgressDialog progressDialog;
    String st = "";

    private static final int REQUEST_CODE_ADD_USER = 100;

    private boolean isDisplayBlock = true;
    private boolean isDisplayApprove = true;

    private final static int IMAGE_PICKER = 1001;
    private final static int GROUP_NAME = 1002;
    private final static int GROUP_INTRO = 1003;
    private final static int GROUP_PSW = 1004;

    String groupName;
    String groupIntro;
    String groupPsw;
    String isPsw;
    String isApprove;

    String path;
    File photo1;

    String origin_name;
    String origin_logo;
    private boolean mIsChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        headTitleTv.setText("群资料");

        presenter = new GroupMemberDetailPresenter(this);
        presenter.attachView(this);

        groupImId = getIntent().getStringExtra("groupImId");
        //初始化环信的群组
        group = EMClient.getInstance().groupManager().getGroup(groupImId);


        if(null!=group){
            adminList = group.getAdminList();
        }

        /*if(!TextUtils.isEmpty(groupNotice)){
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(groupNotice);
            String status = jsonObject.getString(Constants.disableSendMsg);
            if(!TextUtils.isEmpty(status)){
                //0未禁言，1 已禁言
                if("0".equals(status)){
                    muteAllUser.setChecked(false);
                }else if("1".equals(status)){
                    muteAllUser.setChecked(true);
                }
            }
        }*/
        //Log.e("GGGGGG",groupImId);


        //根据环信群资料设置屏蔽状态
        if (null!=group&&group.isMsgBlocked()) {
            blockMsgSw.setChecked(true);
        }
        //释放切换按钮监听
        isDisplayBlock = false;

        //-------------------------------群禁言------------------------------------
        muteAllUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChecked = isChecked;
                String checkStatus = "";
                if (isChecked) {
                    checkStatus = "1";
                } else {
                    checkStatus = "0";
                }
                //走接口禁言
                presenter.muteAll(groupImId, checkStatus);
            }
        });

        //-------------------------------屏蔽群消息------------------------------------
        blockMsgSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isDisplayBlock) {
                    //解除锁定之后响应监听操作
                    GroupBean bean1 = new GroupBean();
                    bean1.setIm_id(groupImId);
                    bean1.setChangeType(5);
                    if (isChecked) {
                        //执行屏蔽操作
                        blockMsg();
                        bean1.setReminder("2");
                    } else {
                        //解除屏蔽操作
                        unBlockMsg();
                        bean1.setReminder("1");
                    }
                    presenter.updateGroup(bean1);
                }
            }
        });
        //-------------------------------屏蔽群消息------------------------------------

        //------------------------------群主与普通成员的操作处理---------------------------------
        if (isCurrentOwner(group)) {
            //群主操作权限
            groupLeaveTv.setText("解散群组");
            groupTransformLy.setVisibility(View.VISIBLE);
        } else {
            //普通成员操作权限
            //隐藏转让群组功能
            groupTransformLy.setVisibility(View.GONE);
            groupLeaveTv.setText("退出群组");
            //隐藏“群密码”功能
            groupPswLy.setVisibility(View.GONE);
            //隐藏“开启群审批”功能
            openApproveLy.setVisibility(View.GONE);

            groupAvatarRl.setEnabled(false);
            groupNameRl.setEnabled(false);
            groupBriefInfoRl.setClickable(false);
        }

        //------------------------------群主与普通成员的操作处理---------------------------------


        //-------------------------------开启群审批------------------------------------
        openApproveSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isDisplayApprove) {
                    //解除锁定之后响应监听操作
                    GroupBean bean1 = new GroupBean();
                    bean1.setIm_id(groupImId);
                    bean1.setChangeType(6);
                    if (isChecked) {
                        //开启
                        bean1.setMembersonly("1");
                    } else {
                        //关闭
                        bean1.setMembersonly("0");
                    }
                    presenter.updateGroup(bean1);
                }
            }
        });

        //-------------------------------开启群审批------------------------------------

        //保证每次进详情看到的都是最新的group
        updateGroup();
    }


    @SuppressWarnings("all")
    private void updateGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupImId);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
                            //群主操作权限
                            groupLeaveTv.setText("解散群组");
                            groupTransformLy.setVisibility(View.VISIBLE);
                        } else {
                            //普通成员操作权限
                            //隐藏转让群组功能
                            groupTransformLy.setVisibility(View.GONE);
                            groupLeaveTv.setText("退出群组");
                            //隐藏“群密码”功能
                            groupPswLy.setVisibility(View.GONE);
                            //隐藏“开启群审批”功能
                            openApproveLy.setVisibility(View.GONE);

                            groupAvatarRl.setEnabled(false);
                            groupNameRl.setEnabled(false);
                            groupBriefInfoRl.setClickable(false);
                        }
                    }
                });
            }
        }).start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.GROUP_INFO info) {
        if (info == Constants.GROUP_INFO.ON_CHANGE) {
            updateGroup();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EmptyUtils.isNotEmpty(groupImId)) {
            //获取详情数据
            presenter.getGroupInfo(groupImId);
            if (isCurrentOwner(group) || isCurrentAdmin(group)) {
                //获取禁言状态
                presenter.getMuteStatus(groupImId);
            }
        } else {
            ToastUtils.showShortToast("页面参数错误");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 屏蔽群消息
     */
    private void blockMsg() {
        String st8 = getResources().getString(R.string.group_is_blocked);
        final String st9 = getResources().getString(R.string.group_of_shielding);

        //屏蔽群消息
        createProgressDialog();
        progressDialog.setMessage(st8);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().blockGroupMessage(groupImId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    /**
     * 解除屏蔽群消息
     */
    private void unBlockMsg() {
        createProgressDialog();
        progressDialog.setMessage(getString(R.string.Is_unblock));
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().unblockGroupMessage(groupImId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.remove_group_of, Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        }).start();
    }

    /**
     * 页面控件赋值
     *
     * @param info 群组资料
     */
    private void setData(GroupInfo info) {
        if (null != info) {

            origin_name = info.getName();
            origin_logo = info.getLogo();

            //根据群资料设置群审批状态
            boolean isApprove = info.getMembersonly().equals("1");
            if (isApprove) {
                openApproveSw.setChecked(true);
            }
            //释放切换按钮监听
            isDisplayApprove = false;

            XImage.loadAvatar(groupAvatarIv, origin_logo);
            groupNameTv.setText(origin_name);
            groupBriefInfoTv.setText(info.getDescription());

            groupIdTv.setText(info.getId());

            //群主信息
            groupMasterNameTv.setText(info.getReal_name());
            XImage.loadAvatar(groupMasterAvatarIv, info.getPhoto_path());

            //群成员展示，本页最多展示12个，其余的查看全部，跳转单独列表显示
            //只有群主跟管理员才显示群成员
            if (isCurrentOwner(group) || isCurrentAdmin(group)) {
                groupMemberPartRcv.setVisibility(View.VISIBLE);
                lookAllMemberRl.setVisibility(View.VISIBLE);
                llMuteGroup.setVisibility(View.VISIBLE);
                memberList = info.getMember_list();
                if (null != memberList) {
                    GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 6);
                    groupMemberPartRcv.setLayoutManager(manager);
                    //rvList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

                    mAdapter = new MemberAdapter(memberList);
                    mAdapter.bindToRecyclerView(groupMemberPartRcv);
                }
            } else {
                lookAllMemberRl.setVisibility(View.GONE);
                groupMemberPartRcv.setVisibility(View.GONE);
                llMuteGroup.setVisibility(View.GONE);
            }

            //处理是否屏蔽消息
//            boolean isBlock=info.getReminder().equals("2");
//            if(isBlock){
//                blockMsgSw.setChecked(true);
//            }
//            isDisplayBlock=false;


            //群组id
            groupId = info.getId();
        }

    }

    /**
     * 群成员列表适配器
     */
    class MemberAdapter extends BaseQuickAdapter<GroupInfo.Member, BaseViewHolder> {

        public MemberAdapter(@Nullable List<GroupInfo.Member> data) {
            super(R.layout.member_grid_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final GroupInfo.Member item) {

            //显示普通图片
            ImageView picIv = helper.getView(R.id.pic_iv);

            XImage.loadAvatar(picIv, item.getPhoto_path());

        }

    }

    Intent intent = new Intent();

    @OnClick({R.id.head_back_ly, R.id.look_all_member_rl, R.id.invite_friend_rl, R.id.group_psw_rl,
            R.id.clear_chat_history_rl, R.id.group_leave_tv, R.id.group_avatar_rl,
            R.id.group_name_rl, R.id.group_brief_info_rl, R.id.group_qr_rl, R.id.group_transform_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.look_all_member_rl:
                //查看所有群成员
                Intent intent1 = new Intent();
                intent1.putExtra("im_id", groupImId);
                intent1.setClass(this, GroupMembersActivity.class);
                startActivity(intent1);
                break;
            case R.id.invite_friend_rl:
                //邀请好友，跳转到好友列表单个邀请
                if (EmptyUtils.isNotEmpty(groupId)) {
                    Intent intent2 = new Intent();
                    intent2.putExtra("groupId", groupId);
                    intent2.setClass(this, InviteFriendGroupActivity.class);
                    startActivity(intent2);
                } else {
                    ToastUtils.showShortToast("参数错误，无法执行操作");
                }
                break;
            case R.id.group_psw_rl:
                //群密码设置
                intent.setClass(this, GroupPswActivity.class);
                startActivityForResult(intent, GROUP_PSW);
                break;
            case R.id.clear_chat_history_rl:
                //清空聊天记录
                showClearHistoryDialog();
                break;
            case R.id.group_leave_tv:
                //离开or解散群组
                if (isCurrentOwner(group)) {
                    //群主则是“解散群组”
                    showDropGroupDialog();
                } else {
                    //普通成员则是“离开群组”
                    showExitGroupDialog();
                }
                break;
            case R.id.group_avatar_rl:
                //修改群头像
                ImagePicker imagePicker2 = ImagePicker.getInstance();
                imagePicker2.setMultiMode(false);
                Intent intent3 = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent3, IMAGE_PICKER);
                break;
            case R.id.group_name_rl:
                //修改群名称
                intent.setClass(this, GroupNameActivity.class);
                intent.putExtra("nameValue", groupNameTv.getText().toString());
                startActivityForResult(intent, GROUP_NAME);
                break;
            case R.id.group_brief_info_rl:
                //修改群简介
                intent.setClass(this, GroupIntroActivity.class);
                intent.putExtra("introValue", groupBriefInfoTv.getText().toString());
                startActivityForResult(intent, GROUP_INTRO);
                break;
            case R.id.group_qr_rl:
                //群二维码
                intent.setClass(this, GroupCodeActivity.class);
                intent.putExtra("groupImId", groupImId);
                intent.putExtra("groupName", groupNameTv.getText().toString());
                startActivity(intent);
                break;

            case R.id.group_transform_rl:
                //群组转让
                intent.setClass(this, GroupTransformActivity.class);
                intent.putExtra("groupImId", groupImId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 解散群组对话框
     */
    private void showDropGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要解散本群组吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (EmptyUtils.isNotEmpty(groupImId)) {
                    presenter.dropGroup(groupImId);
                    EventBus.getDefault().post(Constants.GROUP_INFO.DROP);
                }
            }
        });
        builder.show();
    }

    /**
     * 退出群组对话框
     */
    private void showExitGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要退出本群组吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (EmptyUtils.isNotEmpty(groupImId)) {
                    presenter.exitGroup(groupImId);
                    EventBus.getDefault().post(Constants.GROUP_INFO.DROP);
                }
            }
        });
        builder.show();
    }

    /**
     * 清除聊天记录确定对话框
     */
    private void showClearHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要清空聊天记录吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearGroupHistory();
            }
        });
        builder.show();
    }

    /**
     * 增加群成员
     *
     * @param newmembers
     */
    @SuppressWarnings("all")
    private void addMembersToGroup(final String[] newmembers) {
        final String st6 = getResources().getString(R.string.Add_group_members_fail);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // 创建者调用add方法
                    if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
                        EMClient.getInstance().groupManager().addUsersToGroup(groupImId, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMClient.getInstance().groupManager().inviteUser(groupImId, newmembers, null);
                    }
                    //刷新群组页面
                    //updateGroup();
                    //刷新群成员列表适配器
                    //refreshMembersAdapter();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ((TextView) findViewById(R.id.group_name)).setText(
//                                    group.getGroupName() + "(" + group.getMemberCount() + st);
                            if (null != progressDialog) {
                                progressDialog.dismiss();
                            }
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != progressDialog) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), st6 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case GROUP_NAME:
                    groupName = data.getStringExtra("groupName");
                    //ToastUtils.showShortToast(groupName);
                    groupNameTv.setText(groupName);
                    if (EmptyUtils.isNotEmpty(groupName)) {
                        GroupBean bean1 = new GroupBean();
                        bean1.setIm_id(groupImId);
                        bean1.setName(groupName);
                        bean1.setChangeType(2);
                        presenter.updateGroup(bean1);
                    }
                    break;
                case GROUP_INTRO:
                    groupIntro = data.getStringExtra("groupIntro");
                    //ToastUtils.showShortToast(groupIntro);
                    groupBriefInfoTv.setText(groupIntro);
                    if (EmptyUtils.isNotEmpty(groupIntro)) {
                        GroupBean bean1 = new GroupBean();
                        bean1.setIm_id(groupImId);
                        bean1.setDescription(groupIntro);
                        bean1.setChangeType(3);
                        presenter.updateGroup(bean1);
                    }
                    break;
                case GROUP_PSW:
                    groupPsw = data.getStringExtra("groupPsw");
                    isPsw = data.getStringExtra("isPsw");
                    //ToastUtils.showShortToast(groupPsw + "//" + isPsw);
                    if (EmptyUtils.isNotEmpty(groupPsw) && EmptyUtils.isNotEmpty(isPsw)) {
                        GroupBean bean1 = new GroupBean();
                        bean1.setIm_id(groupImId);
                        bean1.setIs_pwd(isPsw);
                        bean1.setPassword(groupPsw);
                        bean1.setChangeType(4);
                        presenter.updateGroup(bean1);
                    }
                    break;
                default:
                    break;
            }
        }
        //返回头像文件处理
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null) {
            if (requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> lists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                path = lists.get(0).path;
                Glide.with(this).load(path).into(groupAvatarIv);
                photo1 = new File(path);
                if (null != photo1) {
                    GroupBean bean1 = new GroupBean();
                    bean1.setIm_id(groupImId);
                    bean1.setLogo(photo1);
                    bean1.setChangeType(1);
                    presenter.updateGroup(bean1);
                }
            }
        }
    }

    @Override
    public void liftData(GroupInfo groupInfo) {
        setData(groupInfo);
    }

    @Override
    public void liftData(CommonResult result) {
        if (EmptyUtils.isNotEmpty(result.getInfo())) {
            ToastUtils.showShortToast(result.getInfo());
        }
        if (result.getStatus() == 1) {
            //操作成功
            finish();
        }
    }

    @Override
    public void liftUpdateData(GroupBackBean backBean, CommonResult result) {
        //群资料更新返回数据
        if (EmptyUtils.isNotEmpty(result.getInfo())) {
            ToastUtils.showShortToast(result.getInfo());
        }
        if (result.getStatus() == 1) {
            //操作成功
            //finish();
        }

        //如果是群拓展信息更新
        if (backBean.isExtension()) {
            String groupName = backBean.getName();
            String groupLogo = backBean.getLogo();
            int change_type = backBean.getChange_type();
            if (change_type == 1) {
                //修改的群头像，群头像数据返回了，群名称保持原样
                updateExtension(origin_name, groupLogo);
            } else if (change_type == 2) {
                //修改的群头像
                String fullLogo = "";
                if (!TextUtils.isEmpty(origin_logo) && !origin_logo.contains("http")) {
                    fullLogo = UrlUtils.APIHTTP + origin_logo;
                } else {
                    fullLogo = origin_logo;
                }
                updateExtension(groupName, fullLogo);
            }
        }
    }

    @Override
    public void muteSucess() {
        muteAllUser.setChecked(mIsChecked);
        // 发送透传消息
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
        String action = "";
        if (mIsChecked) {
            action = "com.mixin.mute";//action可以自定义
        } else {
            action = "com.mixin.unmute";//action可以自定义
        }
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        cmdMsg.setTo(groupImId);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    @Override
    public void getStatusSucess(String status) {
        //0未禁言，1 已禁言
        if ("0".equals(status)) {
            mIsChecked = false;
        } else if ("1".equals(status)) {
            mIsChecked = true;
        }
        muteAllUser.setChecked(mIsChecked);
    }

    /**
     * 更新群拓展信息
     *
     * @param name 群名称
     * @param logo 群头像
     */
    String jsonString = "";

    private void updateExtension(String name, String logo) {
        if (EmptyUtils.isNotEmpty(name) && EmptyUtils.isNotEmpty(logo)) {

            //添加群拓展,添加群名称与头像信息
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("real_name", name);
                jsonObject.put("photo_path", logo);
                jsonString = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //需要网络操作，故需要开启线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (EmptyUtils.isNotEmpty(jsonString)) {
                            EMGroup group = EMClient.getInstance().groupManager().updateGroupExtension(groupImId, jsonString);
                            final String ex = group.getExtension();
//                               runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getApplicationContext(), "扩展修改成功"+ex, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(), "扩展修改失败", Toast.LENGTH_SHORT).show();
//                                }
//                            });
                    }
                }
            }).start();

        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }


    /**
     * 清空群聊天记录
     */
    private void clearGroupHistory() {

        EMConversation conversation = EMClient.getInstance().chatManager().
                getConversation(group.getGroupId(), EMConversation.EMConversationType.GroupChat);

        if (conversation != null) {
            conversation.clearAllMessages();
        }
        Toast.makeText(this, R.string.messages_are_empty, Toast.LENGTH_SHORT).show();
    }

    //是否是群主
    boolean isCurrentOwner(EMGroup group) {
        boolean result=false;
        if(null!=group){
            String owner = group.getOwner();
            if (owner == null || owner.isEmpty()) {
                return false;
            }else{
                result=owner.equals(EMClient.getInstance().getCurrentUser());
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

    boolean isAdmin(String id) {
        synchronized (adminList) {
            for (String admin : adminList) {
                if (id.equals(admin)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isCanAddMember(EMGroup group) {
        if (group.isMemberAllowToInvite() ||
                isAdmin(EMClient.getInstance().getCurrentUser()) ||
                isCurrentOwner(group)) {
            return true;
        }
        return false;
    }


    private ProgressDialog createProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(GroupMemberDetailActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        return progressDialog;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.MUTE_INFO info) {
        if (isCurrentOwner(group) || isCurrentAdmin(group)) {
            if (info == MUTE) {
                muteAllUser.setChecked(true);
            } else if (info == UN_MUTE) {
                muteAllUser.setChecked(false);
            }
        }
    }


}
