package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.home.FriendProfileActivity;
import com.liuniukeji.mixin.util.XImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 校友录
 */

public class AlumnActivity extends AppCompatActivity implements AlumnContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.right_ly)
    LinearLayout rightLy;
    @BindView(R.id.school_tv)
    TextView schoolTv;
    @BindView(R.id.grade_spn)
    Spinner gradeSpn;
    @BindView(R.id.department_elv)
    ExpandableListView departmentElv;

    AlumnContract.Presenter presenter;

    MyAdapter mAdapter;
    List<AlumnInfo.Grade> gradeList = new ArrayList<>();
    /**
     * 系别分组列表数据
     * （一级列表数据源）
     */
    List<AlumnInfo.Department> depList = new ArrayList<>();


    /**
     * 系别成员列表数据
     * （二级列表数据源）
     */
    List<AlumnMember> memList = new ArrayList<>();


    MyExpandableAdapter adapter = new MyExpandableAdapter();

    String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumn);
        ButterKnife.bind(this);
        headTitleTv.setText("校友录");

        presenter = new AlumnPresenter(this);
        presenter.attachView(this);

        presenter.getAlumnInfo();


        // 建立数据源
        //mItems = getResources().getStringArray(R.array.grade_name);

        // 建立Adapter并且绑定数据源
        mAdapter = new MyAdapter(this, gradeList);
        //绑定 Adapter到控件
        gradeSpn.setAdapter(mAdapter);


        //点击事件
        gradeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //ToastUtils.showShortToast("点击了："+ mItems[position]);

                //选择的年级值赋值
                grade = gradeList.get(position).getValue();

                //清除残存数据
                memList.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //系别折叠列表处理
        //去掉自带箭头，在一级列表中动态添加
        departmentElv.setGroupIndicator(null);

        adapter = new MyExpandableAdapter();
        departmentElv.setAdapter(adapter);

        departmentElv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //打开当前关闭其他的组
                int count = departmentElv.getExpandableListAdapter().getGroupCount();
                for(int j = 0; j < count; j++){
                    if(j != groupPosition){
                        departmentElv.collapseGroup(j);
                    }
                }
                //清除残存数据
                memList.clear();
                adapter.notifyDataSetChanged();

                presenter.getMember(grade, depList.get(groupPosition).getId());
            }
        });

        departmentElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //好友详情主页跳转
                Intent intent = new Intent();
                intent.putExtra("userId", memList.get(childPosition).getId());
                intent.setClass(AlumnActivity.this, FriendProfileActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @OnClick({R.id.head_back_ly, R.id.right_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.right_ly:
                //定位到我
                presenter.locationMe();
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(AlumnInfo info) {
        if (null != info) {
            //学校名称
            schoolTv.setText(info.getMy_school_name());

            //年级下拉列表
            List<AlumnInfo.Grade> list = info.getSchool_class();
            if (null != list) {
                gradeList.clear();
                gradeList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }

            //系别列表
            List<AlumnInfo.Department> dList = info.getSchool_department();
            if (null != dList) {
                depList.clear();
                depList.addAll(dList);

                adapter.notifyDataSetChanged();

            }

        }

    }

    @Override
    public void liftData(AlumnMe info) {
        if (null != info) {
            //“定位到我”设置年级选中
            if (null != mAdapter && null != gradeList) {
                int s = mAdapter.getCount();
                for (int i = 0; i < s; i++) {
                    if (gradeList.get(i).getKey().equals(info.getSchool_class())) {
                        gradeSpn.setSelection(i, true);
                        grade=gradeList.get(i).getValue();
                    }
                }
            }
            //“定位到我”设置院系选中展开
            if (null != adapter && null != depList) {
                int k = adapter.getGroupCount();
                for (int i = 0; i < k; i++) {
                    if (depList.get(i).getId().equals(info.getSchool_department_id())) {
                        departmentElv.expandGroup(i, true);
                    }
                }
            }
            //ToastUtils.showShortToast(info.getSchool_department_id() + "//" + info.getSchool_class());
        }
    }

    @Override
    public void liftData(List<AlumnMember> infoList) {
        if (null != infoList) {
            memList.clear();
            memList.addAll(infoList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }


    /**
     * 年级列表适配器类
     */
    public class MyAdapter extends BaseAdapter {
        private List<AlumnInfo.Grade> mList;
        private Context mContext;
        private LayoutInflater inflater;

        public MyAdapter(Context pContext, List<AlumnInfo.Grade> pList) {
            this.mContext = pContext;
            this.mList = pList;
            this.inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyAdapter.ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grade_item, null);
                holder = new MyAdapter.ViewHolder();
                convertView.setTag(holder);
                holder.tv = convertView.findViewById(R.id.item_name);
            } else {
                holder = (MyAdapter.ViewHolder) convertView.getTag();
            }

            //赋值
            holder.tv.setText(mList.get(position).getValue());

            return convertView;
        }

        public final class ViewHolder {
            public TextView tv;
        }

    }


    class MyExpandableAdapter extends BaseExpandableListAdapter {

        /***一级列表个数**/
        @Override
        public int getGroupCount() {
            if (null != depList) {
                return depList.size();
            } else {
                return 0;
            }
        }

        /***每个二级列表的个数**/
        @Override
        public int getChildrenCount(int groupPosition) {
            if (null != memList) {
                return memList.size();
            } else {
                return 0;
            }
        }

        /***一级列表中单个item**/
        @Override
        public Object getGroup(int groupPosition) {
            return depList.get(groupPosition);
        }

        /***二级列表中单个item**/
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (null != memList) {
                return memList.get(childPosition);
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
                convertView = getLayoutInflater().inflate(R.layout.dep_group_item, null);
            }
            TextView tv_group = (TextView) convertView.findViewById(R.id.tv_group);
            ImageView iv_group = (ImageView) convertView.findViewById(R.id.iv_group);

            if (null != depList && depList.size() > 0) {
                //分组名称
                tv_group.setText(depList.get(groupPosition).getName());

                //String colorStr = groupInfoList.get(groupPosition).getColor();
                //分组头部颜色设置
                //head.setBackgroundColor(Color.parseColor(colorStr));
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

            AlumnMember member = memList.get(childPosition);
            //加载头像
            XImage.loadAvatar(avatarIv, member.getPhoto_path());

            nameTv.setText(member.getReal_name());
            signature.setText(member.getSignature());


            return convertView;
        }

        /***二级列表中每个能否被选中，如果有点击事件一定要设为true**/
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


}
