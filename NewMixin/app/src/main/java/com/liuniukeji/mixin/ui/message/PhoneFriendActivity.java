package com.liuniukeji.mixin.ui.message;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.home.FriendProfileActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.util.AccountUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 手机好友通讯录
 * 1.与后台数据比对
 * 2.前端加工
 */

public class PhoneFriendActivity extends AppCompatActivity implements PhoneFriendContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    PhoneFriendContract.Presenter presenter;
    private FriendAdapter mAdapter;
    private List<PhoneFriendInfo> dataList;

    private static final int PERMISSIONS_READ_CONTACTS = 1;

    /**
     * 手机联系人字符串
     */
    String numStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_friend);
        ButterKnife.bind(this);
        presenter = new PhoneFriendPresenter(this);
        presenter.attachView(this);

        headTitleTv.setText("手机通讯录");

        checkPermission();

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new FriendAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);

        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        //数据一次返回不需要加载更多
        mAdapter.setEnableLoadMore(false);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
            }
        });

        //点击跳转到主页详情页面
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("userId", dataList.get(position).getId());
                intent.setClass(PhoneFriendActivity.this, FriendProfileActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 获取后台数据
     */
    private void getList() {
        if (EmptyUtils.isNotEmpty(numStr)) {
            presenter.getFriendList(numStr);
            Log.e("PhoneFriend", numStr);
        } else {
            ToastUtils.showShortToast("无法获取数据");
        }
    }


    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //获取列表数据
                    getList();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 检查权限
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_READ_CONTACTS);
        } else {
            //获取联系人数据
            loadContacts();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSIONS_READ_CONTACTS) {
            if (null != grantResults && grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                Toast.makeText(this, "请开启应用读取联系人权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @SuppressWarnings("all")
    private void loadContacts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContentResolver resolver = getApplicationContext().getContentResolver();
                    Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key"},
                            null, null, "sort_key COLLATE LOCALIZED ASC");
                    if (phoneCursor == null || phoneCursor.getCount() == 0) {
                        Toast.makeText(getApplicationContext(),
                                "未获得读取联系人权限 或 未获得联系人数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");

                    StringBuilder sb = new StringBuilder();

                    if (phoneCursor.getCount() > 0) {
                        while (phoneCursor.moveToNext()) {

                            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                            if (TextUtils.isEmpty(phoneNumber))
                                continue;
                            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                            String sortKey = phoneCursor.getString(SORT_KEY_INDEX);

                            //去掉电话号码数字之间的空格
                            // phoneNumber.replaceAll("\\s*", "");
                            //179 8369 5639
                            //添加数据
                            sb.append(replaceBlank(phoneNumber));
                            sb.append(",");

                        }
                    }
                    //组合字符串赋值
                    numStr = sb.toString();
                    phoneCursor.close();

                    //通知执行获取数据操作
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void liftFriendData(List<PhoneFriendInfo> infoList) {
        swipeLayout.setRefreshing(false);
        if (null != infoList && infoList.size() > 0) {
            dataList.clear();
            dataList.addAll(infoList);
            mAdapter.setNewData(dataList);
        } else {
            mAdapter.setEmptyView(R.layout.empty_layout);
        }

    }

    @Override
    public void liftData(String info) {
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
            //刷新本页数据
            getList();
        }
    }

    @Override
    public void onEmpty() {
        swipeLayout.setRefreshing(false);
        mAdapter.setEmptyView(R.layout.empty_layout);
    }

    @Override
    public void onNetError() {
        swipeLayout.setRefreshing(false);
        mAdapter.setEmptyView(R.layout.empty_layout);
    }


    class FriendAdapter extends BaseQuickAdapter<PhoneFriendInfo, BaseViewHolder> {

        public FriendAdapter(@Nullable List<PhoneFriendInfo> data) {
            super(R.layout.phone_friend_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final PhoneFriendInfo item) {

            //设置昵称
            helper.setText(R.id.user_name_tv, item.getReal_name());
            TextView nameTv = helper.getView(R.id.user_name_tv);
//            if (EmptyUtils.isNotEmpty(item.getColor())) {
//                //设置昵称字体颜色
//                nameTv.setTextColor(Color.parseColor(item.getColor()));
//            }

            //显示头像
            ImageView avatarIv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(avatarIv, item.getPhoto_path());

            //用户等级
            ImageView levelIconIv = helper.getView(R.id.level_icon_iv);
            TextView levelNumTv = helper.getView(R.id.level_num_tv);

            //签名
            TextView signatureTv = helper.getView(R.id.signature_tv);
            signatureTv.setText(item.getSignature());

            levelNumTv.setText(item.getExperience());
            String type = item.getVip_type();
            switch (item.getSex()) {
                case "1":
                    //show boy icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
                case "2":
                    //show girl icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.girl_level_color));
                    break;
                default:
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
            }

            //处理关注状态
            TextView followTv = helper.getView(R.id.follow_state_tv);
            //0没关注 1已关注
            final boolean isFocus = item.getIs_focus().equals("1") ? true : false;
            if (isFocus) {
                //灰色
                followTv.setTextColor(Color.parseColor("#ACACAC"));
                followTv.setText("已关注");
                followTv.setBackground(getResources().getDrawable(R.drawable.attention_nor_bg));
            } else {
                //黄色
                followTv.setTextColor(Color.parseColor("#FFD71B"));
                followTv.setText("关注");
                followTv.setBackground(getResources().getDrawable(R.drawable.attention_pre_bg));

            }

            //添加or取消关注
            followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFocus) {
                        //取消关注操作
                        presenter.addOrCancelFocus(item.getId(), "2");
                    } else {
                        //添加关注操作
                        presenter.addOrCancelFocus(item.getId(), "1");
                    }
                }
            });

            //处理是自己的情况
            String userId = AccountUtils.getUser(PhoneFriendActivity.this).getId();
            String memberId = item.getId();
            if (EmptyUtils.isNotEmpty(memberId) && memberId.equals(userId)) {
                //隐藏关注按钮
                followTv.setVisibility(View.INVISIBLE);
            } else {
                followTv.setVisibility(View.VISIBLE);
            }
        }
    }


}
