package com.liuniukeji.mixin.ui.message;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.StringUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.widget.AutoMeasureHeightGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新增/编辑分组
 */
public class EditGroupActivity extends AppCompatActivity implements EditGroupContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.delete_ly)
    LinearLayout deleteLy;
    @BindView(R.id.top_ly)
    RelativeLayout topLy;
    @BindView(R.id.group_name_tv)
    TextView groupNameTv;
    @BindView(R.id.group_color_grv)
    AutoMeasureHeightGridView groupColorGrv;
    @BindView(R.id.sure_ly)
    LinearLayout sureLy;
    @BindView(R.id.edit_group_ly)
    RelativeLayout editGroupLy;
    @BindView(R.id.group_name_rl)
    RelativeLayout groupNameRl;

    /**
     * 类型 1新增 2编辑
     */
    int type;

    EditGroupContract.Presenter presenter;

    private ArrayList checkList1 = new ArrayList();
    private GridViewAdapter myAdapter1;

    String chooseColor;
    String groupId;
    String groupName;
    String groupColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            //新增
            headTitleTv.setText("新建分组");
        } else if (type == 2) {
            //编辑修改
            headTitleTv.setText("编辑分组");
            deleteLy.setVisibility(View.VISIBLE);
            groupId = getIntent().getStringExtra("id");
            groupName = getIntent().getStringExtra("name");
            groupColor = getIntent().getStringExtra("color");

            if (EmptyUtils.isNotEmpty(groupColor)) {
                chooseColor = groupColor;
            }
            groupNameTv.setText(groupName);
        }

        presenter = new EditGroupPresenter(this);
        presenter.attachView(this);


        //颜色值
        String[] colorArray = getResources().getStringArray(R.array.group_color);
        List<String> colorList = Arrays.asList(colorArray);

        //加载颜色列表
        if (null != colorList) {
            myAdapter1 = new GridViewAdapter(this, colorList);
            groupColorGrv.setAdapter(myAdapter1);

            myAdapter1.notifyDataSetChanged();
            for (int i = 0; i < colorList.size(); i++) {
                // 均设置为未选
                checkList1.add(false);
            }
        }
    }

    @OnClick({R.id.head_back_ly, R.id.delete_ly, R.id.sure_ly, R.id.group_name_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                //返回
                goBack();
                break;
            case R.id.delete_ly:
                //删除分组
                AlertDialog dialog = new AlertDialog.Builder(this).
                        setTitle(R.string.tip).setMessage(R.string.delete_group_tip).
                        setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除操作
                                if (EmptyUtils.isNotEmpty(groupId)) {
                                    presenter.deleteGroup(groupId);
                                } else {
                                    ToastUtils.showShortToast("参数错误，无法完成操作");
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.cancel, null).show();
                break;
            case R.id.sure_ly:
                //确定
                //分组名称
                String groupName = groupNameTv.getText().toString();
                if (EmptyUtils.isEmpty(groupName)) {
                    ToastUtils.showShortToast("分组名称不能为空");
                    return;
                }
                if (EmptyUtils.isEmpty(chooseColor)) {
                    ToastUtils.showShortToast("请选择分组颜色");
                    return;
                }

                if (type == 1) {
                    //新增
                    presenter.buildGroup(1, groupName, chooseColor, "");

                } else if (type == 2) {
                    //编辑修改
                    if (EmptyUtils.isNotEmpty(groupId)) {
                        presenter.buildGroup(2, groupName, chooseColor, groupId);
                    }
                } else {
                    ToastUtils.showShortToast("参数错误，无法完成修改");
                    return;
                }
                break;
            case R.id.group_name_rl:
                //点击填写分组名称
                buildNewGroup();
                break;
            default:
                break;
        }
    }

    /**
     * 新建分组，填写分组名称
     */
    private void buildNewGroup() {

        final Dialog mDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.new_group, null);

        final EditText nameEt = root.findViewById(R.id.group_name_et);
        final TextView cancelTv = root.findViewById(R.id.cancel_tv);
        final TextView sureTv = root.findViewById(R.id.sure_tv);
        final TextView titleTv = root.findViewById(R.id.title_tv);
        if (type == 2) {
            titleTv.setText("编辑分组");
            //分组名称传值赋值
            nameEt.setText(groupName);
        }

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
                   if(StringUtils.isEmoji(name)){
                       ToastUtils.showShortToast("请不要包含emoji表情");
                   }else{
                       groupNameTv.setText(name);
                       mDialog.dismiss();
                   }
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


    private void goBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }


    @Override
    public void liftBuildGroupInfo(String info) {
        //新建分组返回结果
        ToastUtils.showShortToast(info);
        EventBus.getDefault().post(Constants.EDIT_GROUP.ON_CHANGE);
        finish();
    }

    @Override
    public void liftDelGroupInfo(String info) {
        //删除分组返回结果
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
     * GridView单选适配器
     */
    private class GridViewAdapter extends BaseAdapter {

        private Context mContext;
        private List<String> mdatas;
        private LayoutInflater inflater;

        public GridViewAdapter(Context context, List<String> data) {
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

            GridViewAdapter.ViewHolder holder = null;
            if (convertView == null) {
                //convertView = View.inflate(mContext, R.layout.grid_item_layout, null);
                convertView = inflater.inflate(R.layout.color_item, null);
                holder = new GridViewAdapter.ViewHolder();
                holder.chb = convertView.findViewById(R.id.grid_item_chb);
                holder.iv1 = convertView.findViewById(R.id.tag_iv);
                convertView.setTag(holder);
            } else {
                holder = (GridViewAdapter.ViewHolder) convertView.getTag();
            }

            final int mposition = position;
            final CheckBox mchb = holder.chb;
            final ImageView iv1 = holder.iv1;

            //颜色填充
            mchb.setBackgroundColor(Color.parseColor(mdatas.get(position)));


            holder.chb.setChecked((Boolean) checkList1.get(position));

            //颜色选中处理
            //if (mdatas.get(position).equals(groupColor)) {
            //  checkPosition(mposition);
            //}

            holder.chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                //单击checkbox实现单选，根据状态变换进行单选设置

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        iv1.setVisibility(View.VISIBLE);
                        checkPosition(mposition);
                    } else {
                        iv1.setVisibility(View.GONE);
                        //将已选择的位置设为选择
                        checkList1.set(mposition, false);
                    }
                }
            });

            //选中事件处理
            holder.chb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPosition(mposition);
                    //变量赋值,确定选定的颜色值
                    chooseColor = mdatas.get(mposition);
                }
            });
            return convertView;
        }

        public final class ViewHolder {
            public CheckBox chb;
            public ImageView iv1;
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

    /***清除选中的位置**/
    private void resetCheckPosition() {
        for (int i = 0; i < checkList1.size(); i++) {
            if ((Boolean) checkList1.get(i)) {
                checkList1.set(i, false);
            }
        }
        myAdapter1.notifyDataSetChanged();
    }


}
