package com.liuniukeji.mixin.ui.mine;

import android.content.Context;
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
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会员中心
 */
public class VipCenterActivity extends AppCompatActivity implements VipCenterContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.expire_time_tv)
    TextView expireTimeTv;
    @BindView(R.id.current_score_tv)
    TextView currentScoreTv;
    @BindView(R.id.vip_months_grv)
    GridView vipMonthsGrv;
    @BindView(R.id.gold_coin_price_tv)
    TextView goldCoinPriceTv;

    private ArrayList checkList1 = new ArrayList();
    private GridViewAdapter myAdapter1;

    VipCenterContract.Presenter presenter;

    List<VipMonth> list = new ArrayList<>();
    String monthId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_center);
        ButterKnife.bind(this);
        headTitleTv.setText("会员中心");

        presenter = new VipCenterPresenter(this);
        presenter.attachView(this);
        presenter.getVipMonth();
        setData();
    }

    /**
     * 赋值
     */
    private void setData() {
        UserBean user = AccountUtils.getUser(this);
        expireTimeTv.setText(user.getVip_end_time());
        currentScoreTv.setText(user.getPoints());
    }

    @OnClick({R.id.head_back_ly, R.id.buy_vip_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.buy_vip_tv:
                if (EmptyUtils.isEmpty(monthId)) {
                    ToastUtils.showShortToast("请选择开通时长");
                } else {
                    presenter.buyVip(monthId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(List<VipMonth> mList) {
        if (null != mList) {
            //刷新数据
            this.list = mList;
            //会员月数列表
            myAdapter1 = new GridViewAdapter(this, list);
            vipMonthsGrv.setAdapter(myAdapter1);

            myAdapter1.notifyDataSetChanged();
            for (int i = 0; i < mList.size(); i++) {
                // 均设置为未选
                checkList1.add(false);
            }
        }
    }

    @Override
    public void liftData(String info) {
        ToastUtils.showShortToast(info);
        //获取用户信息数据
        presenter.getUserInfo();
    }

    @Override
    public void liftData(UserInfo info) {
        if (null != info) {
            //更新本地存储数据
            UserBean user = AccountUtils.getUser(this);
            user.updateUserInfo(info);
            //重新保存数据
            AccountUtils.saveUserCache(this, user);
            //通知其他页面刷新相关数据
            EventBus.getDefault().post(Constants.USER_INFO.UPDATE);
            finish();

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
        private List<VipMonth> mdatas;
        private LayoutInflater inflater;

        public GridViewAdapter(Context context, List<VipMonth> data) {
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

            ViewHolder holder = null;
            if (convertView == null) {
                //convertView = View.inflate(mContext, R.layout.grid_item_layout, null);
                convertView = inflater.inflate(R.layout.grid_item_layout, null);
                holder = new ViewHolder();
                holder.chb = convertView.findViewById(R.id.grid_item_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.chb.setText(mdatas.get(position).getName());
            final int mposition = position;
            final CheckBox mchb = holder.chb;

            holder.chb.setChecked((Boolean) checkList1.get(position));

            holder.chb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                //单击checkbox实现单选，根据状态变换进行单选设置

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        mchb.setTextColor(mContext.getResources().
                                getColor(R.color.white));
                        checkPosition(mposition);
                    } else {
                        mchb.setTextColor(mContext.getResources().
                                getColor(R.color.black));
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
                    monthId = mdatas.get(mposition).getId();
                    //金币价格赋值
                    goldCoinPriceTv.setText(mdatas.get(mposition).getPoints());
                }
            });
            return convertView;
        }

        public final class ViewHolder {
            public CheckBox chb;
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
