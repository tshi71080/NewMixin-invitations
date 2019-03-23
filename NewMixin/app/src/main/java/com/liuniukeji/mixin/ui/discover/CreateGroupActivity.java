package com.liuniukeji.mixin.ui.discover;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.CommonResult;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 新建群组（兴趣组）
 */

public class CreateGroupActivity extends AppCompatActivity implements CreateGroupContract.View {
    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.group_avatar_iv)
    CircleImageView groupAvatarIv;
    @BindView(R.id.group_avatar_rl)
    RelativeLayout groupAvatarRl;
    @BindView(R.id.group_name_tv)
    TextView groupNameTv;
    @BindView(R.id.group_name_rl)
    RelativeLayout groupNameRl;
    @BindView(R.id.brief_title_ly)
    LinearLayout briefTitleLy;
    @BindView(R.id.group_brief_info_tv)
    TextView groupBriefInfoTv;
    @BindView(R.id.group_brief_info_rl)
    RelativeLayout groupBriefInfoRl;
    @BindView(R.id.group_psw_rl)
    RelativeLayout groupPswRl;
    @BindView(R.id.open_approve_sw)
    Switch openApproveSw;
    @BindView(R.id.create_group_tv)
    TextView createGroupTv;

    CreateGroupContract.Presenter presenter;

    String groupId;
    String groupName;
    String groupIntro;
    String groupPsw;
    String isPsw;
    String isApprove;

    private final static int IMAGE_PICKER = 1001;
    private final static int GROUP_NAME = 1002;
    private final static int GROUP_INTRO = 1003;
    private final static int GROUP_PSW = 1004;

    String path;
    File photo1;

    GroupBean bean = new GroupBean();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);

        headTitleTv.setText("新建群组");
        presenter = new CreateGroupPresenter(this);
        presenter.attachView(this);

    }

    Intent intent = new Intent();

    @OnClick({R.id.head_back_ly, R.id.group_avatar_rl, R.id.group_name_rl,
            R.id.group_brief_info_rl, R.id.group_psw_rl, R.id.create_group_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.group_avatar_rl:
                //选择头像
                ImagePicker imagePicker2 = ImagePicker.getInstance();
                imagePicker2.setMultiMode(false);
                Intent intent2 = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent2, IMAGE_PICKER);
                break;
            case R.id.group_name_rl:
                //填写群组名称
                intent.setClass(CreateGroupActivity.this, GroupNameActivity.class);
                intent.putExtra("nameValue",groupNameTv.getText().toString());
                startActivityForResult(intent, GROUP_NAME);
                break;
            case R.id.group_brief_info_rl:
                //填写群组简介
                intent.putExtra("introValue", groupBriefInfoTv.getText().toString());
                intent.setClass(CreateGroupActivity.this, GroupIntroActivity.class);
                startActivityForResult(intent, GROUP_INTRO);
                break;
            case R.id.group_psw_rl:
                //群密码设置
                intent.setClass(CreateGroupActivity.this, GroupPswActivity.class);
                startActivityForResult(intent, GROUP_PSW);
                break;
            case R.id.create_group_tv:
                //提交数据

                //群头像
                if (null != photo1) {
                    bean.setLogo(photo1);
                } else {
                    ToastUtils.showShortToast("请选择群头像");
                    break;
                }

                if (EmptyUtils.isEmpty(groupName)) {
                    ToastUtils.showShortToast("群名称不能为空");
                    break;
                }
                bean.setName(groupName);
                if (EmptyUtils.isEmpty(groupIntro)) {
                    ToastUtils.showShortToast("群简介不能为空");
                    break;
                }
                bean.setDescription(groupIntro);

                //设置密码的属性需要一起传值
                if (EmptyUtils.isNotEmpty(isPsw)) {
                    if (EmptyUtils.isNotEmpty(groupPsw)) {
                        bean.setIs_pwd(isPsw);
                        bean.setPassword(groupPsw);
                    }
                }

                //是否开启群审批 1 是 0 否
                isApprove = openApproveSw.isChecked() ? "1" : "0";
                bean.setMembersonly(isApprove);

                //--------------------环信建群操作----------------------

                createGroup();

                //--------------------环信建群操作----------------------

                break;
            default:
                break;
        }
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
                    break;
                case GROUP_INTRO:
                    groupIntro = data.getStringExtra("groupIntro");
                    //ToastUtils.showShortToast(groupIntro);
                    groupBriefInfoTv.setText(groupIntro);
                    break;
                case GROUP_PSW:
                    groupPsw = data.getStringExtra("groupPsw");
                    isPsw = data.getStringExtra("isPsw");
                    //ToastUtils.showShortToast(groupPsw + "//" + isPsw);
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
            }
        }
    }

    /**
     * 创建群组
     */
    @SuppressWarnings("all")
    private void createGroup() {
        String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(st1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String[] members = new String[0];
                try {
                    EMGroupOptions option = new EMGroupOptions();
                    option.maxUsers = 200;
                    option.inviteNeedConfirm = true;


                    String reason = CreateGroupActivity.this.getString(R.string.invite_join_group);
                    reason = EMClient.getInstance().getCurrentUser() + reason + groupName;

//                    if (publibCheckBox.isChecked()) {
//                        option.style = memberCheckbox.isChecked() ? EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval :
//                                EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
//                    } else {
//                        option.style = memberCheckbox.isChecked() ? EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite :
//                                EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
//                    }

                    //默认创建公开群
                    option.style = bean.getMembersonly().equals("1") ? EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval
                            : EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;

                    //环信新建群组之后
                    // EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);
                    EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, groupIntro, members, reason, option);

                    groupId = group.getGroupId();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != progressDialog) {
                                progressDialog.dismiss();
                            }
                        }
                    });

                    //通知APP服务器建群
                    handler.sendEmptyMessage(1);

                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != progressDialog) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(CreateGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    if (EmptyUtils.isNotEmpty(groupId)) {
                        bean.setIm_id(groupId);
                        bean.setLat("38.2");
                        bean.setLng("116.1234455");
                        presenter.createGroup(bean);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void liftData(CommonResult result, GroupBackBean backBean) {
        if (result.getStatus() != 1) {
            //建群失败
            //删掉环信建的群组
            doDestroyGroup();
        } else {
            if (EmptyUtils.isNotEmpty(result.getInfo()) && !result.getInfo().contains("成功")) {
                doDestroyGroup();
            }
        }
        if (EmptyUtils.isNotEmpty(result.getInfo())) {
            ToastUtils.showShortToast(result.getInfo());
        }

        //添加群拓展,添加群名称与头像信息
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("real_name", backBean.getName());
            jsonObject.put("photo_path", backBean.getLogo());
           final  String jsonString =jsonObject.toString();
            //Log.e("JJJJJ",jsonString);

            //需要网络操作，故需要开启线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().updateGroupExtension(groupId,jsonString);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //通知群组新建好了
        EventBus.getDefault().post(Constants.GROUP_INFO.CREATE);
        finish();
    }

    private void doDestroyGroup() {
        try {
            EMClient.getInstance().groupManager().destroyGroup(bean.getIm_id());
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEmpty() {
        doDestroyGroup();
    }

    @Override
    public void onNetError() {
        doDestroyGroup();
    }

}
