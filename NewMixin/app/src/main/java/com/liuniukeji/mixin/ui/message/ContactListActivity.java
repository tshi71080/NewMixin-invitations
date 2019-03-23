package com.liuniukeji.mixin.ui.message;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chatui.Constant;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.ui.AddContactActivity;
import com.hyphenate.chatui.ui.ChatActivity;
import com.hyphenate.chatui.ui.ContactListFragment;
import com.hyphenate.easeui.domain.EaseUser;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.UrlUtils;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通讯录
 * (联系人列表)
 * 根据设计图自定义
 *
 * @see ContactListFragment
 */
public class ContactListActivity extends AppCompatActivity implements ContactListContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.head_add_ly)
    LinearLayout headAddLy;
    @BindView(R.id.head_phone_ly)
    LinearLayout headPhoneLy;
    @BindView(R.id.group_elv)
    ExpandableListView groupElv;
    @BindView(R.id.build_group_tv)
    TextView buildGroupTv;
    @BindView(R.id.edit_group_tv)
    TextView editGroupTv;

    private TextView dotView;
    //一级列表数据源
    //private String[] groups = {"软件设计", "数据库技术", "操作系统"};
    //二级列表数据源
    //private String[][] childs = {{"架构设计", "面向对象", "设计模式", "领域驱动设计"},
    // {"SQL Server", "Oracle", "MySql", "Dameng "}, {"Linux", "Windows", "嵌入式"}};

    ContactListContract.Presenter presenter;

    /**
     * 分组列表数据
     * （一级列表数据源）
     */
    List<GroupFriendInfo> groupInfoList = new ArrayList<>();

    /**
     * 分组下好友列表
     * （二级列表数据源）
     */
    List<GroupFriendInfo.Follow> followList = new ArrayList<>();
    MyExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ButterKnife.bind(this);
        View headView = LayoutInflater.from(this).inflate(R.layout.new_friend_top_view,null);
        dotView = headView.findViewById(R.id.nav_tv_dot);
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this,NewFriedsApplyActivity.class);
                startActivity(intent);
            }
        });
        if(MMKV.defaultMMKV().getInt("add_friend",0)==1){
            dotView.setVisibility(View.VISIBLE);
        }else{
            dotView.setVisibility(View.GONE);
        }
        presenter = new ContactListPresenter(this);
        presenter.attachView(this);
        headTitleTv.setText("通讯录");

        presenter.friendGroupMember();
        groupElv.addHeaderView(headView);
        //去掉自带箭头，在一级列表中动态添加
        groupElv.setGroupIndicator(null);
        adapter = new MyExpandableAdapter();
        groupElv.setAdapter(adapter);


        groupElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //聊天页面跳转
//                GroupFriendInfo.Follow follow = groupInfoList.get(groupPosition).getFollow_list().get(childPosition);
//
//                Intent intent = new Intent(ContactListActivity.this, ChatActivity.class);
//
//                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
//
//                // it's single chat
//                intent.putExtra(Constant.EXTRA_USER_ID, follow.getIm_name());
//                startActivity(intent);

                return true;
            }
        });

        EventBus.getDefault().register(this);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.EDIT_GROUP state) {
        switch (state) {
            case ON_CHANGE:
                //数据更新
                presenter.friendGroupMember();
                break;
            case ADD_FRIEND:
                MMKV.defaultMMKV().putInt("add_friend",1);
                dotView.setVisibility(View.VISIBLE);
                break;
            case IS_OPERATE:
                MMKV.defaultMMKV().putInt("add_friend",0);
                dotView.setVisibility(View.GONE);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.GROUP_INFO state) {
        switch (state) {
            case MEMBER:
                //数据更新
                presenter.friendGroupMember();
                break;
        }
    }


    @OnClick({R.id.head_back_ly, R.id.head_add_ly, R.id.head_phone_ly, R.id.build_group_tv, R.id.edit_group_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.head_add_ly:
                //添加联系人
                startActivity(new Intent(this, AddContactActivity.class));
                break;
            case R.id.head_phone_ly:
                //手机通讯录
                startActivity(new Intent().setClass(this, PhoneFriendActivity.class));
                break;
            case R.id.build_group_tv:
                //新建分组
                //buildNewGroup();
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.setClass(this, EditGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_group_tv:
                //编辑分组
                startActivity(new Intent().setClass(this, GroupListActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void liftGroupFriendData(List<GroupFriendInfo> infoList) {
        if (null != infoList && infoList.size() > 0) {
            groupInfoList.clear();
            groupInfoList.addAll(infoList);
            adapter.notifyDataSetChanged();
            //设置classifyExpandList默认展开
            int groupCount = groupElv.getCount()-1;
            if(groupCount>0){
                groupElv.expandGroup(0);
            }
        }
    }

    @Override
    public void liftMoveInfo(String info) {

    }

    @Override
    public void liftBuildGroupInfo(String info) {
        ToastUtils.showShortToast(info);
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }



    class MyExpandableAdapter extends BaseExpandableListAdapter {

        /***一级列表个数**/
        @Override
        public int getGroupCount() {
            if (null != groupInfoList) {
                return groupInfoList.size();
            } else {
                return 0;
            }
        }

        /***每个二级列表的个数**/
        @Override
        public int getChildrenCount(int groupPosition) {
            if (null != groupInfoList.get(groupPosition).getFollow_list()) {
                return groupInfoList.get(groupPosition).getFollow_list().size();
            } else {
                return 0;
            }
        }

        /***一级列表中单个item**/
        @Override
        public Object getGroup(int groupPosition) {
            return groupInfoList.get(groupPosition);
        }

        /***二级列表中单个item**/
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (null != groupInfoList && groupInfoList.size() > 0 && groupInfoList.get(groupPosition).getFollow_list() != null) {
                return groupInfoList.get(groupPosition).getFollow_list().get(childPosition);
            } else {
                return null;
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /***每个item的id是否固定，一般为true**/
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.contact_group_item, null);
            }
            TextView tv_group = (TextView) convertView.findViewById(R.id.tv_group);
            TextView head = (TextView) convertView.findViewById(R.id.tv_group_head);
            ImageView iv_group = (ImageView) convertView.findViewById(R.id.iv_group);

            if (null != groupInfoList && groupInfoList.size() > 0) {
                //分组名称
                tv_group.setText(groupInfoList.get(groupPosition).getName());

                String colorStr = groupInfoList.get(groupPosition).getColor();
                //分组头部颜色设置
                head.setBackgroundColor(Color.parseColor(colorStr));
            }

            //控制是否展开图标
            if (isExpanded) {
                iv_group.setImageResource(R.mipmap.common_icon_group_open);
            } else {
                iv_group.setImageResource(R.mipmap.common_icon_group_close);
            }
            return convertView;
        }

        /*** 填充二级列表**/
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.contact_child_item, null);
            }
            ImageView avatarIv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            TextView nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            TextView signature = (TextView) convertView.findViewById(R.id.signature_tv);
            ImageView followIv = (ImageView) convertView.findViewById(R.id.follow_secret_iv);
            LinearLayout allLy = (LinearLayout) convertView.findViewById(R.id.all_ly);
            followIv.setVisibility(View.VISIBLE);


            if (null != groupInfoList && groupInfoList.size() > 0 && groupInfoList.get(groupPosition).getFollow_list() != null) {
                final GroupFriendInfo.Follow follow = groupInfoList.get(groupPosition).getFollow_list().get(childPosition);

                //头像
                XImage.loadAvatar(avatarIv, follow.getPhoto_path());
                //姓名
                nameTv.setText(follow.getReal_name());
                //签名
                signature.setText(follow.getSignature());

                //-----------------------保存数据聊天用户数据---------------------------
                EaseUser easeUser = new EaseUser(follow.getIm_name());
                easeUser.setAvatar(follow.getPhoto_path());
                easeUser.setNickname(follow.getReal_name());

                DemoHelper.getInstance().saveContact(easeUser);
                //-----------------------保存数据聊天用户数据---------------------------


                //点击跳转到聊天界面
                allLy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ContactListActivity.this, ChatActivity.class);

                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);

                        // it's single chat
                        intent.putExtra(Constant.EXTRA_USER_ID, follow.getIm_name());
                        startActivity(intent);
                    }
                });


                //悄悄关注标识
                if (follow.getIs_quietly().equals("1")) {
                    followIv.setVisibility(View.VISIBLE);
                } else {
                    followIv.setVisibility(View.GONE);
                }

                final String userId = follow.getId();
                //0不是 1是
                final boolean isQuietly=follow.getIs_quietly().equals("1");
                final String groupId=groupInfoList.get(groupPosition).getId();

                allLy.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("id", userId);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("isQuietly",isQuietly);

                        intent.setClass(ContactListActivity.this, FriendEditDialog.class);
                        startActivity(intent);
                        return true;
                    }
                });

                //长按头像事件处理
                avatarIv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("id", userId);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("isQuietly",isQuietly);
                        intent.setClass(ContactListActivity.this, FriendEditDialog.class);
                        startActivity(intent);
                        return true;
                    }
                });
            }
            return convertView;
        }

        /***二级列表中每个能否被选中，如果有点击事件一定要设为true**/
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    /**
     * 新建分组
     */
    private void buildNewGroup() {

        final Dialog mDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.new_group, null);

        final EditText nameEt = root.findViewById(R.id.group_name_et);
        final TextView cancelTv = root.findViewById(R.id.cancel_tv);
        final TextView sureTv = root.findViewById(R.id.sure_tv);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                mDialog.dismiss();

            }
        });

        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定
                String name = nameEt.getText().toString();
                if (EmptyUtils.isNotEmpty(name)) {
                    presenter.buildGroup(name);
                    mDialog.dismiss();
                } else {
                    ToastUtils.showShortToast("分组名称不能为空");
                }
            }
        });

        //-------------------------------------------------------------------------
        //对话框通用属性设置
        setDialog(mDialog, root, 1);

    }


    /***弹窗通用设置**/
    private void setDialog(Dialog mDialog, ViewGroup root, int type) {
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        if (type == 1) {
            dialogWindow.setGravity(Gravity.CENTER);
        } else {
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
        // 添加动画
        //dialogWindow.setWindowAnimations(R.style.dialog_style);

        // 获取对话框当前的参数值
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // 新位置X坐标
        lp.x = 0;
        // 新位置Y坐标
        lp.y = -20;
        // 宽度
        lp.width = (int) getResources().getDisplayMetrics().widthPixels;
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度

        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        // 透明度
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
