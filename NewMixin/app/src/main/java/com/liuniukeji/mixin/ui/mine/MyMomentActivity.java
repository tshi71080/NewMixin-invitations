package com.liuniukeji.mixin.ui.mine;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;
import com.liuniukeji.mixin.ui.home.FriendUserInfo;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.PicassoImageLoader;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.SizeUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的动态
 */
public class MyMomentActivity extends AppCompatActivity implements MyMomentContract.View,
        TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.back_ly)
    LinearLayout backLy;
    @BindView(R.id.more_ly)
    LinearLayout moreLy;
    @BindView(R.id.follow_tv)
    TextView followTv;
    @BindView(R.id.fans_tv)
    TextView fansTv;
    @BindView(R.id.moment_tv)
    TextView momentTv;
    @BindView(R.id.avatar_iv)
    CircleImageView avatarIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.level_icon_iv)
    ImageView levelIconIv;
    @BindView(R.id.level_num_tv)
    TextView levelNumTv;
    @BindView(R.id.department_tv)
    TextView departmentTv;
    @BindView(R.id.signature_tv)
    TextView signatureTv;
    @BindView(R.id.birthday_tv)
    TextView birthdayTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.hobby_rcv)
    RecyclerView hobbyRcv;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.module_tb)
    TabLayout moduleTb;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.vp)
    ViewPager vp;

    @BindView(R.id.cover_iv)
    ImageView coverIv;

    List<Fragment> fragmentList;

    MyAdapter pagedApter;

    MyMomentContract.Presenter presenter;

    File photo1;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private int IMAGE_PICKER = 0x00002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);
        ButterKnife.bind(this);

        presenter = new MyMomentPresenter(this);
        presenter.attachView(this);

        //本地存储数据
        UserBean user = AccountUtils.getUser(this);
        //setData();

        //不再使用本地数据，直接获取网络数据
        getData();

        if (null != user) {
            String userId = user.getId();
            if (EmptyUtils.isNotEmpty(userId)) {
                //-------------------------加载模块切换--------------------------
                fragmentList = new ArrayList<>();
                fragmentList.add(MomentFragment.newInstance(userId));
                fragmentList.add(AlbumFragment.newInstance(userId));
                String[] title = {"动态", "相册"};
                pagedApter = new MyAdapter(getSupportFragmentManager(), fragmentList, Arrays.asList(title));
                vp.setAdapter(pagedApter);
                moduleTb.setupWithViewPager(vp);
                setUpIndicatorWidth(moduleTb);
                //-------------------------加载模块切换--------------------------
            }
        }
    }

    private void getData() {
        UserBean user = AccountUtils.getUser(this);
        if (null != user) {
            String userId = user.getId();
            if (EmptyUtils.isNotEmpty(userId)) {
                presenter.getUserInfo(user.getId());
            }
        }
    }

    /**
     * 顶部基本信息赋值
     */
    private void setData() {
        UserBean user = AccountUtils.getUser(this);
        if (null != user) {
            nameTv.setText(user.getReal_name());
            //关注
            followTv.setText(user.getFollows());
            fansTv.setText(user.getFans());
            momentTv.setText(user.getMoments());
            //mineScoreTv.setText(user.getPoints());

//            departmentTv.setText();//缺字段


            //个性签名
            signatureTv.setText(user.getSignature());

            //处理性别显示图标&会员身份标识
            levelNumTv.setText(user.getExperience());
            String type = user.getVip_type();
            switch (user.getSex()) {
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

            //用户头像展示
            String logUrl = user.getPhoto_path();
            if (EmptyUtils.isNotEmpty(logUrl)) {
                XImage.loadAvatar(avatarIv, logUrl);
            }

            //生日
            birthdayTv.setText(user.getBirthday());
            //地址
            addressTv.setText(user.getAddress());
            //兴趣爱好

        }

    }


    /**
     * 顶部基本信息赋值
     */
    private void setData(FriendUserInfo user) {
        if (null != user) {
            nameTv.setText(user.getReal_name());
            //关注
            followTv.setText(user.getFollows());
            fansTv.setText(user.getFans());
            momentTv.setText(user.getMoments());
            //mineScoreTv.setText(user.getPoints());

            //年级/系别
            String school_class=user.getSchool_class();
            String depart_name=user.getSchool_department_name();
            departmentTv.setText(school_class+"届/"+depart_name);

            //封面加载
            XImage.loadCover(coverIv, user.getCover_path());

            //个性签名
            signatureTv.setText(user.getSignature());

            //处理性别显示图标&会员身份标识
            levelNumTv.setText(user.getExperience());
            String type = user.getVip_type();
            switch (user.getSex()) {
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

            //用户头像展示
            String logUrl = user.getPhoto_path();
            if (EmptyUtils.isNotEmpty(logUrl)) {
                XImage.loadAvatar(avatarIv, logUrl);
            }

            //生日
            birthdayTv.setText(user.getBirthday());
            //地址
            addressTv.setText(user.getAddress());
            //兴趣爱好
            //--------------------------------兴趣爱好----------------------------------
            LinearLayoutManager ms = new LinearLayoutManager(this);
            //  布局方式为横向布局
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);

            // 添加设置好的布局样式
            hobbyRcv.setLayoutManager(ms);

            List<String> list = user.getInterests();
            if (null != list && list.size() > 0) {
                HobbyAdapter adapter = new HobbyAdapter(list);
                hobbyRcv.setAdapter(adapter);
            }
            //--------------------------------兴趣爱好----------------------------------

        }
    }

    public class HobbyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public HobbyAdapter(List<String> data) {
            super(R.layout.hobby_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            helper.setText(R.id.tv_content, item);
        }
    }

    /**
     * 设置TAB中间的距离
     *
     * @param tabLayout
     */
    private void setUpIndicatorWidth(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i == 0) {
                View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)
                        tab.getLayoutParams();
                p.setMargins(0, 0, SizeUtils.dp2px(this.getBaseContext(), 120.0f), 0);
                tab.requestLayout();
            }
        }
    }

    @OnClick({R.id.back_ly, R.id.more_ly, R.id.cover_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_ly:
                finish();
                break;
            case R.id.more_ly:
                //
                break;
            case R.id.cover_iv:
                //修改封面
                //showHeaderEdit();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setCancelable(true);
//                builder.setNegativeButton("更换主页封面图片", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        choosePic();
//                    }
//                });
//                builder.show();


                //使用自定义对话框确认操作
                final Dialog mDialog = new Dialog(this, R.style.my_dialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.change_cover_dialog, null);

                final TextView sureTv = root.findViewById(R.id.change_cover_tv);

                sureTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choosePic();
                        mDialog.dismiss();
                    }
                });
                //对话框通用属性设置
                setDialog(mDialog, root, 1);

                break;
            default:
                break;
        }
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
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }


    /**
     * 选择图片
     */

    private void choosePic() {
        ImagePicker imagePicker = ImagePicker.getInstance();

        imagePicker.setMultiMode(false);

        //设置图片加载器
        imagePicker.setImageLoader(new PicassoImageLoader());
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(true);
        //是否按矩形区域保存
        imagePicker.setSaveRectangle(false);
        //选中数量限制
        imagePicker.setSelectLimit(1);
        //裁剪框的形状
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusWidth(960);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(600);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(1280);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(800);

        Intent intent2 = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent2, IMAGE_PICKER);

    }

    @Override
    public void liftData(FriendUserInfo userInfo) {
        setData(userInfo);
    }

    @Override
    public void liftData(String info) {
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
            getData();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }

    public class MyAdapter extends FragmentPagerAdapter {
        List<Fragment> frgList;
        List<String> stringList;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments, List<String> strings) {
            super(fm);
            frgList = fragments;
            stringList = strings;
        }

        public void setCommentCount(String string) {
            this.stringList.set(1, string);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return frgList.get(position);
        }

        @Override
        public int getCount() {
            return frgList == null ? 0 : frgList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return stringList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> lists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                path = lists.get(0).path;
                Glide.with(this).load(path).into(coverIv);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }

            //photo1 = new File(path);
            //提交数据
            if (EmptyUtils.isNotEmpty(path)) {
                presenter.changeCover(path);
            }
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        getTakePhoto().onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && null != data) {
//
//        }
//    }


    //------------------------------------图片选择------------------------------------------

    @Override
    public void takeSuccess(TResult result) {
        //Log.i(TAG, "takeSuccess：" + result.getImages().get(0).getOriginalPath());
        try {
            //    Bitmap bitmap = ImageUtils.getBitmap(result.getImages().get(0).getOriginalPath());
            FileInputStream fis = new FileInputStream(result.getImages().get(0).getCompressPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);

            photo1 = new File(result.getImages().get(0).getCompressPath());

            final String path = result.getImages().get(0).getCompressPath();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        coverIv.setBackground(new BitmapDrawable(bitmap));
                    }
                    presenter.changeCover(path);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }


    @Override
    public void takeCancel() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.
                onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType cate_id = PermissionManager.
                checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(cate_id)) {
            this.invokeParam = invokeParam;
        }
        return cate_id;
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).
                    bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }


    private void showHeaderEdit() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        final Uri imageUri = Uri.fromFile(file);
        final TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        configCompress(takePhoto);
        PhotoUploadDialog dialog = new PhotoUploadDialog(this);
        dialog.setPhotoUpListener(new PhotoUploadDialog.PhotoUpLisenter() {
            @Override
            public void tackPhoto() {
                takePhoto.onPickFromCapture(imageUri);
            }

            @Override
            public void joinPhoto() {
                takePhoto.onPickMultiple(1);
                takePhoto.setTakePhotoOptions(builder.create());
            }

            @Override
            public void cancel() {

            }
        });
        dialog.show();
    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = (1366 * 768) / 10;
        int width = 1366;
        int height = 768;
        boolean showProgressBar = false;
        CompressConfig config;
        LubanOptions option = new LubanOptions.Builder()
                .setMaxHeight(height)
                .setMaxWidth(width)
                .setMaxSize(maxSize)
                .create();
        config = CompressConfig.ofLuban(option);
        takePhoto.onEnableCompress(config, showProgressBar);
    }
//------------------------------------图片选择------------------------------------------


}
