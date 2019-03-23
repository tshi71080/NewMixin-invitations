package com.liuniukeji.mixin.ui.message;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 好友管理
 */
public class FriendEditDialog extends AppCompatActivity implements FriendEditContract.View {


    @BindView(R.id.top_title_ly)
    LinearLayout topTitleLy;
    @BindView(R.id.group_lv)
    ListView groupLv;
    @BindView(R.id.follow_sw)
    Switch followSw;
    @BindView(R.id.follow_option_ly)
    RelativeLayout followOptionLy;
    @BindView(R.id.sure_ly)
    LinearLayout sureLy;

    private ArrayList checkList1 = new ArrayList();
    private ListViewAdapter myAdapter1;

    private List<GroupInfo> dataList = new ArrayList<>();

    FriendEditContract.Presenter presenter;

    private String selectedId;
    private String memberId;
    private String groupId;
    boolean isQuietly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_edit_dialog);
        ButterKnife.bind(this);

        //--------------------------------设置窗口属性-------------------------------
        Window win = this.getWindow();
        win.getDecorView().setPadding(60, 60, 60, 60);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置对话框置顶显示
//        lp.gravity = Gravity.TOP;
        //lp.gravity = Gravity.BOTTOM;
        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);
        //--------------------------------设置窗口属性-------------------------------
        memberId = getIntent().getStringExtra("id");
        groupId = getIntent().getStringExtra("groupId");
        isQuietly = getIntent().getBooleanExtra("isQuietly", false);
         if(EmptyUtils.isNotEmpty(groupId)){
             //给默认选中的赋值
             selectedId=groupId;
         }

        if(isQuietly){
            followSw.setChecked(true);
        }

        presenter = new FriendEditPresenter(this);
        presenter.attachView(this);
        presenter.getGroupList();

        myAdapter1 = new ListViewAdapter(this, dataList);
        groupLv.setAdapter(myAdapter1);
        groupLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkPosition(position);
                //被选中的赋值
                selectedId = dataList.get(position).getId();
                //ToastUtils.showShortToast(selectedId);
            }
        });
    }

    @OnClick(R.id.sure_ly)
    public void onViewClicked() {
        if (EmptyUtils.isEmpty(memberId)) {
            ToastUtils.showShortToast("好友id为空");
            return;
        }
        if (EmptyUtils.isEmpty(selectedId)) {
            ToastUtils.showShortToast("分组id为空，请选择分组");
            return;
        }
        String quite = followSw.isChecked() ? "1" : "0";

        presenter.moveToGroup(memberId, selectedId, quite);
    }

    @Override
    public void liftGroupData(List<GroupInfo> infoList) {
        if (null != infoList && infoList.size() > 0) {
            dataList.clear();
            dataList.addAll(infoList);
            for (int i = 0; i < dataList.size(); i++) {
                // 均设置为未选
                if(dataList.get(i).getId().equals(groupId)){
                    checkList1.add(true);
                }
                checkList1.add(false);
            }
            myAdapter1.notifyDataSetChanged();
        }
    }

    @Override
    public void liftMoveInfo(String info) {
        ToastUtils.showShortToast(info);
        EventBus.getDefault().post(Constants.EDIT_GROUP.ON_CHANGE);
        finish();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }


    /**
     * 列表单选适配器
     */
    private class ListViewAdapter extends BaseAdapter {

        private Context mContext;
        private List<GroupInfo> mdatas;
        private LayoutInflater inflater;

        public ListViewAdapter(Context context, List<GroupInfo> data) {
            mContext = context;
            mdatas = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (null != mdatas) {
                return mdatas.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ListViewAdapter.ViewHolder holder = null;
            if (convertView == null) {
                //convertView = View.inflate(mContext, R.layout.grid_item_layout, null);
                convertView = inflater.inflate(R.layout.group_friend_item, null);
                holder = new ListViewAdapter.ViewHolder();
                holder.chb = convertView.findViewById(R.id.choose_item_chb);
                holder.tv1 = convertView.findViewById(R.id.tv_group_color);
                holder.tv2 = convertView.findViewById(R.id.tv_group_name);
                convertView.setTag(holder);
            } else {
                holder = (ListViewAdapter.ViewHolder) convertView.getTag();
            }

            final int mposition = position;
            final CheckBox mchb = holder.chb;
            final TextView tv1 = holder.tv1;
            final TextView tv2 = holder.tv2;

            //颜色填充
            tv1.setBackgroundColor(Color.parseColor(mdatas.get(position).getColor()));
            //分组名称
            tv2.setText(mdatas.get(position).getName());

            holder.chb.setChecked((Boolean) checkList1.get(position));

            //禁用自身点击选中属性
            mchb.setEnabled(false);

            return convertView;
        }

        public final class ViewHolder {
            public CheckBox chb;
            public TextView tv1;
            public TextView tv2;
        }
    }

    /***设置选中的位置，将其他位置设置为未选**/
    private void checkPosition(int position) {

        for (int i = 0; i < checkList1.size(); i++) {
            // 设置已选位置
            if (position == i) {
                checkList1.set(i, true);
            } else {
                checkList1.set(i, false);
            }
        }
        myAdapter1.notifyDataSetChanged();
    }



}
