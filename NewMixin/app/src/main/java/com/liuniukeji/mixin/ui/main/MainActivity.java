package com.liuniukeji.mixin.ui.main;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatui.Constant;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.db.InviteMessgeDao;
import com.hyphenate.chatui.db.UserDao;
import com.hyphenate.chatui.runtimepermissions.PermissionsManager;
import com.hyphenate.chatui.runtimepermissions.PermissionsResultAction;
import com.hyphenate.chatui.scanner.ScannerActivity;
import com.hyphenate.chatui.ui.ChatActivity;
import com.hyphenate.chatui.ui.ContactListFragment;
import com.hyphenate.chatui.ui.ConversationListFragment;
import com.hyphenate.chatui.ui.GroupsActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.XyqApplication;
import com.liuniukeji.mixin.base.BaseActivity;
import com.liuniukeji.mixin.ui.discover.GroupDetailActivity;
import com.liuniukeji.mixin.ui.home.FriendProfileActivity;
import com.liuniukeji.mixin.ui.loacation.LocationService;
import com.liuniukeji.mixin.ui.login.LoginActivity;
import com.liuniukeji.mixin.ui.message.MessageFragment;
import com.liuniukeji.mixin.ui.message.MyContactListFragment;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.LocationUtils;
import com.liuniukeji.mixin.util.common.DateHelper;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @author wanghaijun QQ:1819005139
 * @Description 程序主页面
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @date June.2018
 * @CreateBy Android Studio
 * @ModifiedBy // 修改作者, 联系方式, 修改日期 [无修改作者, 可为空]
 */
public class MainActivity extends BaseActivity implements
        NavFragment.OnNavigationReselectListener {


    private NavFragment mNavBar;
    @BindView(R.id.activity_main_ui)
    LinearLayout mMainUi;


    protected static final String TAG = "MainActivity";

    private Button[] mTabs;
    private ContactListFragment contactListFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;

    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    private final static int SCANNIN_GREQUEST_CODE2 = 2;

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    private LocationService locationService;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void processLogic() {
        FragmentManager manager = getSupportFragmentManager();
        mNavBar = ((NavFragment) manager.findFragmentById(R.id.fag_nav));
        mNavBar.setup(this, manager, R.id.main_container, this);


        // runtime permission for android 6.0, just require all permissions here for simple
        requestPermissions();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }

        //make sure activity will not in background if user is logged into another device or removed
        if (getIntent() != null &&
                (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        showExceptionDialogFromIntent(getIntent());
        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);

        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();

        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
        //debug purpose only
        registerInternalDebugReceiver();

        // 获取华为 HMS 推送 token
        //HMSPushHelper.getInstance().getHMSToken(this);


        //获取定位
        // getBestLocation();
        //getNetworkLocation();
//        getGPSLocation();

        //开启服务
//        Intent service_intent  = new Intent(this,GPSService.class);
//
//---------------------------------使用百度地图获取位置信息-----------------------------------
        locationService = ((XyqApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，
        // 可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        // 定位SDK
        locationService.start();
        //---------------------------------使用百度地图获取位置信息-----------------------------------
        //-----------------------设置极光推送别名---------------------------
        JPushInterface.setAlias(this, 1111, AccountUtils.getUser(this).getId());
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("1");
        JPushInterface.setTags(this, 11, hashSet);
        //-----------------------设置极光推送别名---------------------------
    }

    public void scanQr() {
        //扫一扫
        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        startActivityForResult(intent, SCANNIN_GREQUEST_CODE2);
    }

    @Override
    public void onReselect(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null
                && fragment instanceof OnTabReselectListener) {
            OnTabReselectListener listener = (OnTabReselectListener) fragment;
            listener.onTabReselect();
        }
    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            //让Toast的显示时间和等待时间相同
            ToastUtils.showShortToastSafe("再按一次退出");
            touchTime = currentTime;
        } else {
            //super.onBackPressed();//多嵌套Fragment返回有问题
            /*this.finish();
            System.exit(0);*/
            XyqApplication.getInstance().exit();
        }
    }

    //------------------------------------------开始-----------------------------------


    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };


    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().vibrateAndPlayTone(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    public void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (mNavBar.mNavNews.getFragment() != null) {
                        ((MessageFragment) mNavBar.mNavNews.getFragment()).refresh();
                    }

                }
            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                // refresh conversation list
                if (mNavBar.mNavNews.getFragment() != null) {
                    ((MessageFragment) mNavBar.mNavNews.getFragment()).refresh();
                }

                if (mNavBar.mNavTweet.getFragment() != null) {
                    ((MyContactListFragment) mNavBar.mNavTweet.getFragment()).refresh();
                }

                if (contactListFragment != null) {
                    contactListFragment.refresh();
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
//                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
//                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {

        }

        @Override
        public void onGroupEvent(int event, String target, final List<String> username) {
            switch (event) {
                case EMMultiDeviceListener.GROUP_LEAVE:
                    ChatActivity.activityInstance.finish();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                //logMsg(sb.toString());
                locationService.stop();
                //将位置信息保存到本地
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                if (EmptyUtils.isNotEmpty(lat) && EmptyUtils.isNotEmpty(lng)) {
                    //将位置保存本地
                    AccountUtils.saveLocationInfo(MainActivity.this, lat, lng);
                }
            }
        }

    };

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            mNavBar.showMsgDot();
            //unreadLabel.setText(String.valueOf(count));
            //unreadLabel.setVisibility(View.VISIBLE);
        } else {
            mNavBar.hideMsgDot();
            //unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    //unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    //unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        //unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null) {
                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                }
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    //------------------------------------------结束-----------------------------------


    /**
     * 采用最好的方式获取定位信息
     */
    private void getBestLocation() {
        Criteria c = new Criteria();
        c.setPowerRequirement(Criteria.POWER_LOW);
        //c.setAltitudeRequired(true);
        c.setBearingAccuracy(Criteria.ACCURACY_COARSE);
        c.setAccuracy(Criteria.ACCURACY_LOW);

        //不要求海拔信息
        c.setAltitudeRequired(false);
        //不要求方位信息
        c.setBearingRequired(false);
        //是否允许付费
        c.setCostAllowed(true);


        Location best = LocationUtils.getBestLocation(this, c);
        if (best == null) {
            Toast.makeText(this, " best location is null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "best location: lat==" + best.getLatitude() + " lng==" + best.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 通过网络等获取定位信息
     */
    private void getNetworkLocation() {
        Location net = LocationUtils.getNetWorkLocation(this);
        if (net == null) {
            Toast.makeText(this, "net location is null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "network location: lat==" + net.getLatitude() +
                    "  lng==" + net.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 通过GPS获取定位信息
     */
    public void getGPSLocation() {
        Location gps = LocationUtils.getGPSLocation(this);
        if (gps == null) {

            LocationUtils.addLocationListener(this, LocationManager.GPS_PROVIDER, new LocationUtils.ILocationListener() {
                @Override
                public void onSuccessLocation(Location location) {
                    if (location != null) {
                        Toast.makeText(MainActivity.this, "gps onSuccessLocation location:  lat==" + location.getLatitude() + "     lng==" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "gps location is null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "gps location: lat==" + gps.getLatitude() + "  lng==" + gps.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //group code
        //site=liuniu&key=2&value=100069&date=2019/01/21

        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE2:
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    String result = bundle.getString("barCode");
                    //Log.e("RRRR",result);
                    //if (result.contains("site") && result.contains("key") && result.contains("value")) {
                    if (result.contains("site") && result.contains("key") && result.contains("value") && result.contains("date")) {
                        try {
                            String[] scnnerInfo = result.split("&");
                            String[] siteInfo = scnnerInfo[0].split("=");
                            String siteValue = siteInfo[1];
                            if (siteValue.equals("liuniu")) {
                                //标识是我们的二维码
                                String[] keys = scnnerInfo[1].split("=");
                                String key = keys[1];

                                String[] values = scnnerInfo[2].split("=");
                                String returnId = values[1];

                                String[] dates = scnnerInfo[3].split("=");
                                String date=dates[1];

                                //2019/01/21
                                //String dd=date.replace("/","-");

                                //判断二维码是否过期
                                if(!DateHelper.qrCodeExpire(date)){
                                    if (key.equals("1")) {
                                        Intent intent = new Intent(MainActivity.this, FriendProfileActivity.class);
                                        //环信账号
                                        intent.putExtra("userId", returnId);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(this, GroupDetailActivity.class);
                                        intent.putExtra("groupImId", returnId);
                                        startActivity(intent);
                                    }
                                }else{
                                    ToastUtils.showShortToast("二维码过期啦");
                                }
                            } else {
                                ToastUtils.showShortToast(result);
                            }
                        } catch (Exception e) {
                            ToastUtils.showShortToast("二维码数据异常");
                        }
                    } else {
                        ToastUtils.showShortToast(result);
                    }
                }
                break;
            default:
                break;
        }
    }
}
