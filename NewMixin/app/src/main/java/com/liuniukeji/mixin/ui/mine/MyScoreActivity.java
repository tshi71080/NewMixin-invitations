package com.liuniukeji.mixin.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.util.payali.AlipayHelper;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的学分
 */

public class MyScoreActivity extends AppCompatActivity implements MyScoreContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.current_score_tv)
    TextView currentScoreTv;
    @BindView(R.id.fill_score_grv)
    GridView fillScoreGrv;
    @BindView(R.id.pay_type_rb1)
    RadioButton payTypeRb1;
    @BindView(R.id.pay_type_rb2)
    RadioButton payTypeRb2;
    @BindView(R.id.pay_type_rg)
    RadioGroup payTypeRg;
    @BindView(R.id.pay_type_ly2)
    LinearLayout payTypeLy2;
    @BindView(R.id.pay_type_ly1)
    LinearLayout payTypeLy1;
    @BindView(R.id.fill_score_tv)
    TextView fillScoreTv;

    @BindView(R.id.head_right_ly)
    LinearLayout headRightLy;

    MyScoreContract.Presenter presenter;

    private ArrayList checkList1 = new ArrayList();
    List<ScoreInfo> list = new ArrayList<>();
    private GridViewAdapter myAdapter1;

    String scoreId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        ButterKnife.bind(this);
        headTitleTv.setText("我的学分");
        presenter = new MyScorePresenter(this);
        presenter.attachView(this);
        setData();
        presenter.getScoreInfo();

        EventBus.getDefault().register(this);

    }

    private void setData() {
        UserBean user = AccountUtils.getUser(this);
        String score = user.getPoints();
        //当前学分赋值
        currentScoreTv.setText(score);
    }


    @OnClick({R.id.head_back_ly, R.id.pay_type_ly2, R.id.pay_type_ly1,
            R.id.fill_score_tv, R.id.head_right_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.pay_type_ly2:
                payTypeRb2.setChecked(true);
                break;
            case R.id.pay_type_ly1:
                payTypeRb1.setChecked(true);
                break;
            case R.id.fill_score_tv:

                if (EmptyUtils.isNotEmpty(scoreId)) {
                    if (payTypeRb1.isChecked()) {
                        //支付宝支付
                        AlipayHelper helper=new AlipayHelper(this);
                        helper.buyScore(scoreId);

                    } else if (payTypeRb2.isChecked()) {
                        //微信支付

                    } else {
                        ToastUtils.showShortToast("请选择支付方式");
                    }
                } else {
                    ToastUtils.showShortToast("请选择充值的学分类型");
                }
                break;
            case R.id.head_right_ly:
                startActivity(new Intent().setClass(this, ScoreDetailActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(List<ScoreInfo> mList) {

        if (null != mList) {
            //刷新数据
            this.list = mList;
            //会员月数列表
            myAdapter1 = new GridViewAdapter(this, list);
            fillScoreGrv.setAdapter(myAdapter1);

            myAdapter1.notifyDataSetChanged();
            for (int i = 0; i < mList.size(); i++) {
                // 均设置为未选
                checkList1.add(false);
            }
        }
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
        private List<ScoreInfo> mdatas;
        private LayoutInflater inflater;

        public GridViewAdapter(Context context, List<ScoreInfo> data) {
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
                convertView = inflater.inflate(R.layout.grid_item_score_layout, null);
                holder = new GridViewAdapter.ViewHolder();
                holder.chb = convertView.findViewById(R.id.grid_item_chb);
                holder.tv1 = convertView.findViewById(R.id.grid_item_tv1);
                holder.tv2 = convertView.findViewById(R.id.grid_item_tv2);
                convertView.setTag(holder);
            } else {
                holder = (GridViewAdapter.ViewHolder) convertView.getTag();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("价格：");
            sb.append(mdatas.get(position).getMoney());
            sb.append("元");
            holder.tv1.setText(mdatas.get(position).getPoints());
            holder.tv2.setText(sb.toString());

            final int mposition = position;
            final CheckBox mchb = holder.chb;
            final TextView tv1 = holder.tv1;
            final TextView tv2 = holder.tv2;

            holder.chb.setChecked((Boolean) checkList1.get(position));

            holder.chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                //单击checkbox实现单选，根据状态变换进行单选设置

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        tv1.setTextColor(mContext.getResources().
                                getColor(R.color.white));
                        tv2.setTextColor(mContext.getResources().
                                getColor(R.color.white));
                        checkPosition(mposition);

                        //选中的学分类型id
                        scoreId = mdatas.get(mposition).getId();

                    } else {
                        tv1.setTextColor(mContext.getResources().
                                getColor(R.color.grid_item_text_color));
                        tv2.setTextColor(mContext.getResources().
                                getColor(R.color.grid_item_text_color));
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
                    //变量赋值
                }
            });
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

    /***清除选中的位置**/
    private void resetCheckPosition() {
        for (int i = 0; i < checkList1.size(); i++) {
            if ((Boolean) checkList1.get(i)) {
                checkList1.set(i, false);
            }
        }
        myAdapter1.notifyDataSetChanged();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.ALI_PAY_STATE state) {
        switch (state) {
            case ON_SUCCESS:
            case ON_ERROR:
            case OTHER:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
