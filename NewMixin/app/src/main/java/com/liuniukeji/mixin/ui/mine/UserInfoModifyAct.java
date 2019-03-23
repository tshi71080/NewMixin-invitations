package com.liuniukeji.mixin.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.ui.mine.baseinfo.AddressActivity;
import com.liuniukeji.mixin.ui.mine.baseinfo.BirthdayActivity;
import com.liuniukeji.mixin.ui.mine.baseinfo.ChangeNicknameActivity;
import com.liuniukeji.mixin.ui.mine.baseinfo.HobbyActivity;
import com.liuniukeji.mixin.ui.mine.baseinfo.SignatureActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.ClipboardUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.PicassoImageLoader;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.ui.mine.baseinfo.AddressActivity;
import com.liuniukeji.mixin.ui.mine.baseinfo.BirthdayActivity;
import com.liuniukeji.mixin.ui.mine.baseinfo.HobbyActivity;
import com.liuniukeji.mixin.ui.mine.baseinfo.SignatureActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.widget.SexDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的资料(修改)
 */
public class UserInfoModifyAct extends AppCompatActivity implements UserInfoModifyContract.View,
        TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.modify_avatar_iv)
    CircleImageView modifyAvatarIv;
    @BindView(R.id.modify_avatar_rl)
    RelativeLayout modifyAvatarRl;
    @BindView(R.id.signature_content_tv)
    TextView signatureContentTv;
    @BindView(R.id.modify_signature_rl)
    RelativeLayout modifySignatureRl;
    @BindView(R.id.modify_birthday_tv)
    TextView modifyBirthdayTv;
    @BindView(R.id.modify_gender_tv)
    TextView modifyGenderTv;
    @BindView(R.id.modify_birthday_rl)
    RelativeLayout modifyBirthdayRl;
    @BindView(R.id.modify_address_tv)
    TextView modifyAddressTv;
    @BindView(R.id.modify_address_rl)
    RelativeLayout modifyAddressRl;
    @BindView(R.id.modify_hobby_tv)
    TextView modifyHobbyTv;
    @BindView(R.id.modify_hobby_rl)
    RelativeLayout modifyHobbyRl;
    @BindView(R.id.tv_mixin_id)
    TextView MinxinIdView;
    @BindView(R.id.tv_user_nickname)
    TextView tv_user_nickname;
    @BindView(R.id.rl_copy_code)
    RelativeLayout rl_copy_code;


    UserInfoModifyContract.Presenter presenter;
    File photo1;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private int IMAGE_PICKER = 0x00002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_modify);
        ButterKnife.bind(this);
        headTitleTv.setText("我的资料");
        setData();
        EventBus.getDefault().register(this);

        presenter = new UserInfoModifyPresenter(this);
        presenter.attachView(this);
        initImagePicker();
    }

    /**
     * 页面控件赋值
     */
    private void setData() {
        UserBean user = AccountUtils.getUser(this);
        if (null != user) {

            //头像
            XImage.loadAvatar(modifyAvatarIv, user.getPhoto_path());
            MinxinIdView.setText(user.getMember_code());
            tv_user_nickname.setText(user.getReal_name());
            //个性签名
            if (EmptyUtils.isNotEmpty(user.getSignature())) {
                signatureContentTv.setText(user.getSignature());
            } else {
                signatureContentTv.setText("未填");
            }
            //性别（一经注册不得修改，这里只展示）
            modifyGenderTv.setText(user.getGender());
            //生日
            modifyBirthdayTv.setText(user.getBirthday());
            //地址
            modifyAddressTv.setText(user.getAddress());
            //兴趣爱好
            if (EmptyUtils.isNotEmpty(user.getInterests())) {
                modifyHobbyTv.setText(user.getInterests());
            } else {
                modifyHobbyTv.setText("多个兴趣爱好用空格隔开");
            }
            rl_copy_code.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardUtils.copyText(MinxinIdView.getText().toString());
                    ToastUtils.showLongToast("复制成功");
                    return true;
                }
            });
        }
    }

    @OnClick({R.id.head_back_ly, R.id.modify_avatar_iv, R.id.modify_avatar_rl,
            R.id.modify_signature_rl, R.id.modify_birthday_rl,
            R.id.modify_address_rl, R.id.modify_hobby_rl,R.id.rl_nick_name,R.id.ll_sex})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.head_back_ly:
                //返回
                finish();
                break;
            case R.id.modify_avatar_iv:
            case R.id.modify_avatar_rl:
                //修改头像
//                showHeaderEdit();

                ImagePicker imagePicker2 = ImagePicker.getInstance();
                imagePicker2.setMultiMode(false);
                Intent intent2 = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent2, IMAGE_PICKER);

                break;
            case R.id.modify_signature_rl:
                //修改签名
                intent.setClass(this, SignatureActivity.class);
                startActivity(intent);
                break;
                //修改昵称
            case R.id.rl_nick_name:
                intent.setClass(this, ChangeNicknameActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_birthday_rl:
                //修改生日
                intent.setClass(this, BirthdayActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_address_rl:
                //修改地址
                intent.setClass(this, AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_hobby_rl:
                //修改爱好
                intent.setClass(this, HobbyActivity.class);
                startActivity(intent);
                break;
                //修改性别 0保密 1男 2女
            case R.id.ll_sex:
                new SexDialog(this,AccountUtils.getUser(this).getSex()){
                    @Override
                    public void onRight(String msex) {
                        super.onRight(msex);
                        presenter.updateUserSex(msex);
                    }
                }.show();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.USER_INFO state) {
        switch (state) {
            case UPDATE:
                //用户数据更新
                setData();
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

    @Override
    public void liftData(String info) {
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
            //网络更新用户信息
            presenter.getUserInfo();
        }
    }

    @Override
    public void liftData(UserInfo info) {
        if (null != info) {
            //更新本地存储的用户信息
            UserBean user = AccountUtils.getUser(this);
            user.setPhoto_path(info.getPhoto_path());
            //重新保存数据
            AccountUtils.saveUserCache(this, user);
            //通知其他页面刷新相关数据
            EventBus.getDefault().post(Constants.USER_INFO.UPDATE);
        }
    }

    @Override
    public void updateSucess(UserInfo info) {
        UserBean user = AccountUtils.getUser(this);
        user.setSex(info.getSex());
        AccountUtils.saveUserCache(this,user);
        switch (info.getSex()){
            case "0":
                modifyGenderTv.setText("保密");
                break;
            case "1":
                modifyGenderTv.setText("男");
                break;
            case "2":
                modifyGenderTv.setText("女");
                break;
        }

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }

    String path;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> lists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                path = lists.get(0).path;
                Glide.with(this).load(path).into(modifyAvatarIv);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
            //Log.e("AAAA", path);

            photo1 = new File(path);
            //提交数据
            if (EmptyUtils.isNotEmpty(path) && null != photo1) {
                presenter.updateAvatar(photo1);
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


            String filePath;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                filePath = result.getImages().get(0).getCompressPath();
            } else {
                filePath = this.getPackageName() + ".fileProvider" + result.getImages().get(0).getCompressPath();
            }

            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);

            photo1 = new File(filePath);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        //设置更新过的头像
                        modifyAvatarIv.setImageDrawable(new BitmapDrawable(bitmap));
                    }
                    presenter.updateAvatar(photo1);
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
        Log.e("UUUU", imageUri.toString());


        final Uri photoOutputUri = FileProvider.getUriForFile(
                UserInfoModifyAct.this,
                this.getPackageName() + ".fileProvider",
                file);

        final TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        configCompress(takePhoto);
        PhotoUploadDialog dialog = new PhotoUploadDialog(this);
        dialog.setPhotoUpListener(new PhotoUploadDialog.PhotoUpLisenter() {
            @Override
            public void tackPhoto() {
                //出现异常
                //takePhoto.onPickFromCapture(imageUri);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    takePhoto.onPickFromCapture(imageUri);
                } else {
                    takePhoto.onPickFromCapture(photoOutputUri);
                }
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


    /**
     * 初始化图片加载器
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
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
//        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusWidth(800);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(1000);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(1000);
    }



}
