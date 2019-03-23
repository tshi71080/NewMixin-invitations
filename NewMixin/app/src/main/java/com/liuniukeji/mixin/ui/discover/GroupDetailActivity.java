package com.liuniukeji.mixin.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chatui.Constant;
import com.hyphenate.chatui.ui.ChatActivity;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.PicassoImageLoader;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 群组资料详情
 */
public class GroupDetailActivity extends AppCompatActivity implements GroupDetailContract.View {

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
    @BindView(R.id.group_join_in_tv)
    TextView groupJoinInTv;

    GroupDetailContract.Presenter presenter;
    String groupImId;

    String groupId;
    boolean isPsw;
    @BindView(R.id.group_psw_et)
    EditText groupPswEt;
    @BindView(R.id.group_psw_ly)
    LinearLayout groupPswLy;

    boolean isMember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);
        headTitleTv.setText("群资料");

        presenter = new GroupDetailPresenter(this);
        presenter.attachView(this);

        groupImId = getIntent().getStringExtra("groupImId");
        groupId = getIntent().getStringExtra("groupId");

        if (EmptyUtils.isNotEmpty(groupImId)) {
            //获取详情数据
            presenter.getGroupInfo(groupImId);
        }
        initImagePicker();
    }

    /**
     * 列表传值赋值
     *
     * @param info 群组信息
     */
    private void setData(GroupNearbyInfo info) {
        if (null != info) {
            XImage.loadAvatar(groupAvatarIv, info.getLogo());
            groupNameTv.setText(info.getName());
            groupBriefInfoTv.setText(info.getDescription());

            groupIdTv.setText(info.getId());
        }
    }

    /**
     * 网络请求数据赋值
     *
     * @param info 群组详细信息
     */
    private void setData(GroupInfo info) {
        if (null != info) {
            XImage.loadAvatar(groupAvatarIv, info.getLogo());
            groupNameTv.setText(info.getName());
            groupBriefInfoTv.setText(info.getDescription());

            groupIdTv.setText(info.getId());

            //群主信息
            groupMasterNameTv.setText(info.getReal_name());
            XImage.loadAvatar(groupMasterAvatarIv, info.getPhoto_path());

            //群密码
            isPsw = info.getIs_pwd().equals("1") ? true : false;
            if (isPsw) {
                groupPswLy.setVisibility(View.VISIBLE);
            } else {
                groupPswLy.setVisibility(View.GONE);
            }

            //判断当前用户是否为本群聊的成员
            //群角色: -1 不是群成员 0群成员 1群主
            String type = info.getFamily();
            if (EmptyUtils.isNotEmpty(type)) {
                switch (type) {
                    case "-1":
                        isMember = false;
                        break;
                    case "1":
                        isMember = true;
                        break;
                    case "0":
                        isMember = true;
                        break;
                    default:
                        break;
                }
            }

            if(isMember){
                groupJoinInTv.setText("进入群聊");
            }else{
                groupJoinInTv.setText("申请加入");
            }
        }
    }

    @OnClick({R.id.head_back_ly, R.id.group_join_in_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                //返回
                finish();
                break;
            case R.id.group_join_in_tv:
               if(isMember){
                   //直接进入群聊
                   //跳转到群聊页面
                   if (EmptyUtils.isNotEmpty(groupImId)) {
                       Intent intent = new Intent(GroupDetailActivity.this, ChatActivity.class);
                       intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                       intent.putExtra(Constant.EXTRA_USER_ID, groupImId);
                       startActivity(intent);
                   } else {
                       ToastUtils.showShortToast("群组参数错误，无法跳转");
                   }
               }else{
                   //申请加入
                   String reason = "我是" + AccountUtils.getUser(this).getReal_name();

                   String mGroupId = groupIdTv.getText().toString();

                   if (EmptyUtils.isNotEmpty(mGroupId)) {
                       if (isPsw) {
                           String psw = groupPswEt.getText().toString();
                           if (EmptyUtils.isNotEmpty(psw)) {
                               presenter.joinInGroup(mGroupId, psw, reason);
                           } else {
                               ToastUtils.showShortToast("请输入加群密码");
                           }
                       } else {
                           presenter.joinInGroup(mGroupId, "", reason);
                       }

                   } else {
                       ToastUtils.showShortToast("参数错误，无法加入该群组");
                   }
               }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(GroupInfo groupInfo) {
        if (null != groupInfo) {
            setData(groupInfo);
        }
    }

    @Override
    public void liftData(String info) {
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }


    /**
     * 初始化图片加载器
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        //设置图片加载器
        imagePicker.setImageLoader(new PicassoImageLoader());
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(true);
        //是否按矩形区域保存
        imagePicker.setSaveRectangle(false);
        //选中数量限制
        imagePicker.setSelectLimit(1);
        //裁剪框的形状
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusWidth(800);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(1000);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(1000);
    }

}
