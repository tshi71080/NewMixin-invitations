package com.liuniukeji.mixin.ui.multisend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择群发好友
 */

public class MultiChooseActivity extends AppCompatActivity implements MultiChooseContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.head_ly)
    LinearLayout headLy;
    @BindView(R.id.group_elv)
    ExpandableListView groupElv;
    @BindView(R.id.choose_all_chb)
    CheckBox chooseAllChb;
    @BindView(R.id.choose_num_tv)
    TextView chooseNumTv;
    @BindView(R.id.next_step_ly)
    LinearLayout nextStepLy;

    MultiChooseContract.Presenter presenter;

    /**
     * 分组列表数据
     * （一级列表数据源）
     */
    List<GroupFriendBean> groupInfoList = new ArrayList<>();
    MyExpandableAdapter adapter;
    int group_count = 0;
    ArrayList<String> realNameList = new ArrayList<>();
    ArrayList<String> imNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multi_choose);
        ButterKnife.bind(this);
        headTitleTv.setText("选择群发对象");

        presenter = new MultiChoosePresenter(this);
        presenter.attachView(this);

        presenter.friendGroupMember();
        //去掉自带箭头，在一级列表中动态添加
        groupElv.setGroupIndicator(null);
        adapter = new MyExpandableAdapter();
        groupElv.setAdapter(adapter);


        groupElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupInfoList.get(groupPosition).getFollow_list().get(childPosition).isChoose()) {

                    groupInfoList.get(groupPosition).getFollow_list().get(childPosition).setChoose(false);
                    //child有一个不选则group也不选
                    if (groupInfoList.get(groupPosition).isCheck()) {
                        //组别的选中取消
                        groupInfoList.get(groupPosition).setCheck(false);

                        //取消全选的状态
                        if (chooseAllChb.isChecked()) {
                            chooseAllChb.setChecked(false);
                        }

                        if (group_count > 0) {
                            group_count -= 1;
                        }

                        //底部全选取消
                        for (int i = 0; i < groupInfoList.get(groupPosition).getFollow_list().size(); i++) {
                            if (childPosition != i) {
                                //剔除掉当前取消的子选项，其他的还是选中状态
                                groupInfoList.get(groupPosition).getFollow_list().get(i).setChoose(true);
                            }
                        }
                    }
                } else {
                    int count = 0;
                    groupInfoList.get(groupPosition).getFollow_list().get(childPosition).setChoose(true);
                    for (int i = 0; i < groupInfoList.get(groupPosition).getFollow_list().size(); i++) {
                        if (groupInfoList.get(groupPosition).getFollow_list().get(i).isChoose()) {
                            count += 1;
                        }
                    }
                    //如果child都被选了，则group也选择
                    if (count == groupInfoList.get(groupPosition).getFollow_list().size()) {
                        groupInfoList.get(groupPosition).setCheck(true);

                        group_count += 1;
                        //处理底部全选的状态
                        if (group_count == groupInfoList.size()) {
                            chooseAllChb.setChecked(true);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                //显示选中了多少数据
                showData();

                return false;
            }
        });


        //底部全选处理
        chooseAllChb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = chooseAllChb.isChecked();
                if (isChecked) {
                    for (int i = 0; i < groupInfoList.size(); i++) {
                        groupInfoList.get(i).setCheck(true);
                        for (int j = 0; j < groupInfoList.get(i).getFollow_list().size(); j++) {
                            //子选项设置
                            groupInfoList.get(i).getFollow_list().get(j).setChoose(true);
                        }
                    }
                } else {
                    for (int i = 0; i < groupInfoList.size(); i++) {
                        groupInfoList.get(i).setCheck(false);
                        for (int j = 0; j < groupInfoList.get(i).getFollow_list().size(); j++) {
                            //子选项设置
                            groupInfoList.get(i).getFollow_list().get(j).setChoose(false);
                        }
                    }
                }
                //通知刷新页面
                adapter.notifyDataSetChanged();

                //显示选中了多少数据
                showData();
            }
        });

    }

    @OnClick({R.id.head_back_ly, R.id.next_step_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.next_step_ly:
                //下一步
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("realNameList", realNameList);
                bundle.putStringArrayList("imNameList", imNameList);
                intent.putExtras(bundle);
                intent.setClass(this, MultiSendActivity.class);

                if (realNameList != null && realNameList.size() > 0) {
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showShortToast("请选择群发对象");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftGroupFriendData(List<GroupFriendBean> infoList) {
        if (null != infoList && infoList.size() > 0) {
            groupInfoList.clear();
            groupInfoList.addAll(infoList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }


    private void showData() {
        imNameList.clear();
        realNameList.clear();

        if (null != groupInfoList) {
            for (int i = 0; i < groupInfoList.size(); i++) {
                List<GroupFriendBean.Follow> flist = groupInfoList.get(i).getFollow_list();
                if (null != flist) {
                    for (int j = 0; j < flist.size(); j++) {
                        if (flist.get(j).isChoose()) {
                            imNameList.add(flist.get(j).getIm_name());
                            realNameList.add(flist.get(j).getReal_name());
                        }
                    }
                }
            }
        }
        chooseNumTv.setText("已选择" + imNameList.size() + "人");
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
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.choose_group_item, null);
            }
            TextView tv_group = (TextView) convertView.findViewById(R.id.tv_group);
            TextView head = (TextView) convertView.findViewById(R.id.tv_group_head);
            ImageView iv_group = (ImageView) convertView.findViewById(R.id.iv_group);
            final CheckBox chb_group = (CheckBox) convertView.findViewById(R.id.chb_group);


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


            //checkbox 点击监听
            chb_group.setChecked(groupInfoList.get(groupPosition).isCheck());

            chb_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = chb_group.isChecked();
                    groupInfoList.get(groupPosition).setCheck(isChecked);
                    changChildStates(groupPosition, isChecked);

                    //如果有组别取消选中,底部全选取消
                    if (!isChecked) {
                        if (chooseAllChb.isChecked()) {
                            chooseAllChb.setChecked(false);
                        }
                    }

                    //处理底部全选的状态
                    if (isChecked) {
                        group_count += 1;
                    } else {
                        if (group_count > 0) {
                            group_count -= 1;
                        }
                    }
                    if (group_count == groupInfoList.size()) {
                        chooseAllChb.setChecked(true);
                    }

                    notifyDataSetChanged();

                    //显示选中了多少数据
                    showData();
                }
            });


            return convertView;
        }


        public void changChildStates(int position, boolean isChecked) {
            List<GroupFriendBean.Follow> flist = groupInfoList.get(position).getFollow_list();
            if (null != flist) {
                for (int i = 0; i < flist.size(); i++) {
                    flist.get(i).setChoose(isChecked);
                }
            }
        }


        /*** 填充二级列表**/
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.choose_child_item, null);
            }
            ImageView avatarIv = (ImageView) convertView.findViewById(R.id.avatar_iv);
            TextView nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            ImageView followIv = (ImageView) convertView.findViewById(R.id.follow_secret_iv);
            CheckBox childChb = (CheckBox) convertView.findViewById(R.id.chb_child);
            LinearLayout allLy = (LinearLayout) convertView.findViewById(R.id.all_ly);

            if (null != groupInfoList && groupInfoList.size() > 0 && groupInfoList.get(groupPosition).getFollow_list() != null) {
                final GroupFriendBean.Follow follow = groupInfoList.get(groupPosition).getFollow_list().get(childPosition);

                //头像
                XImage.loadAvatar(avatarIv, follow.getPhoto_path());
                //姓名
                nameTv.setText(follow.getReal_name());
                childChb.setChecked(follow.isChoose());
            }
            return convertView;
        }

        /***二级列表中每个能否被选中，如果有点击事件一定要设为true**/
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
