package com.liuniukeji.mixin.ui.mine.baseinfo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
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

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的生日
 * 修改功能
 */
public class BirthdayActivity extends AppCompatActivity implements BirthdayContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.choose_birthday_tv)
    TextView chooseBirthdayTv;
    @BindView(R.id.choose_birthday_rl)
    RelativeLayout chooseBirthdayRl;
    @BindView(R.id.birthday_sw)
    Switch birthdaySw;
    @BindView(R.id.birthday_save_tv)
    TextView birthdaySaveTv;

    BirthdayContract.Presenter presenter;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        ButterKnife.bind(this);
        presenter = new BirthdayPresenter(this);
        presenter.attachView(this);
        headTitleTv.setText("我的生日");
        setData();
    }

    /**
     * 页面控件赋值
     */
    private void setData() {
        UserBean user = AccountUtils.getUser(this);
        chooseBirthdayTv.setText(user.getBirthday());
        String isShow = user.getIs_show_birthday();
        if (EmptyUtils.isNotEmpty(isShow)&&isShow.equals("1")) {
            birthdaySw.setChecked(true);
        } else {
            birthdaySw.setChecked(false);
        }
    }

    @OnClick({R.id.head_back_ly, R.id.birthday_save_tv, R.id.choose_birthday_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.birthday_save_tv:
                //保存数据
                //是否展示生日0不展示1展示
                String isShow = birthdaySw.isChecked() ? "1" : "0";
                //请选择生日
                String birthday = chooseBirthdayTv.getText().toString();
                if (birthday.equals("请选择生日") || EmptyUtils.isEmpty(birthday)) {
                    ToastUtils.showShortToast("请选择生日");
                } else {
                    //提交数据
                    presenter.submit(birthday, isShow);
                }
                break;
            case R.id.choose_birthday_rl:
                //选择生日
                showDateDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 日期选择对话框
     */
    private void showDateDialog() {
        getDate();
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                chooseBirthdayTv.setText(year + "-" + (++month) + "-" + day);
            }
        };
        //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        DatePickerDialog dialog = new DatePickerDialog(BirthdayActivity.this, 0,
                listener, year, month, day);
        dialog.show();
    }

    /**
     * 获取当前日期
     */
    private void getDate() {
        Calendar cal = Calendar.getInstance();
        //获取年月日时分秒
        year = cal.get(Calendar.YEAR);
        //获取到的月份是从0开始计数
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void liftData(CommonInfo info) {
        if (null != info) {
            ToastUtils.showShortToast("修改生日信息成功");
            //更新本地存储数据
            UserBean user = AccountUtils.getUser(this);
            user.updateBirthday(info);
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
}
