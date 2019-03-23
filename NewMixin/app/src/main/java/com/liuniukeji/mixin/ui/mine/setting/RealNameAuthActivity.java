package com.liuniukeji.mixin.ui.mine.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.liuniukeji.mixin.ui.login.DepartmentActivity;
import com.liuniukeji.mixin.ui.login.DepartmentBean;
import com.liuniukeji.mixin.ui.login.GradeActivity;
import com.liuniukeji.mixin.ui.login.GradeBean;
import com.liuniukeji.mixin.ui.login.SchoolActivity;
import com.liuniukeji.mixin.ui.login.SchoolBean;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.PicassoImageLoader;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;
import com.liuniukeji.mixin.ui.login.DepartmentActivity;
import com.liuniukeji.mixin.ui.login.DepartmentBean;
import com.liuniukeji.mixin.ui.login.GradeActivity;
import com.liuniukeji.mixin.ui.login.GradeBean;
import com.liuniukeji.mixin.ui.login.SchoolActivity;
import com.liuniukeji.mixin.ui.login.SchoolBean;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实名认证
 */

public class RealNameAuthActivity extends AppCompatActivity implements AuthContract.View,
        TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.auth_name_et)
    EditText authNameEt;
    @BindView(R.id.auth_school_tv)
    TextView authSchoolTv;
    @BindView(R.id.auth_school_rl)
    RelativeLayout authSchoolRl;
    @BindView(R.id.auth_department_tv)
    TextView authDepartmentTv;
    @BindView(R.id.auth_department_rl)
    RelativeLayout authDepartmentRl;
    @BindView(R.id.auth_grade_tv)
    TextView authGradeTv;
    @BindView(R.id.auth_grade_rl)
    RelativeLayout authGradeRl;
    @BindView(R.id.auth_cer_front_iv)
    ImageView authCerFrontIv;
    @BindView(R.id.auth_cer_front_choose_iv)
    ImageView authCerFrontChooseIv;
    @BindView(R.id.auth_cer_back_iv)
    ImageView authCerBackIv;
    @BindView(R.id.auth_cer_back_choose_iv)
    ImageView authCerBackChooseIv;
    @BindView(R.id.auth_submit_tv)
    TextView authSubmitTv;
    @BindView(R.id.auth_tip_tv)
    TextView authTipTv;

    AuthContract.Presenter presenter;
    AuthInfo authInfo;

    File photo1;
    File photo2;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    int type = 0;
    SchoolBean schoolBean = new SchoolBean();
    DepartmentBean departmentBean = new DepartmentBean();
    GradeBean gradeBean = new GradeBean();

    private int IMAGE_PICKER = 0x00002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_auth);
        ButterKnife.bind(this);
        headTitleTv.setText("实名认证");
        presenter = new AuthPresenter(this);
        presenter.attachView(this);
        setData();
    }

    /**
     * 页面控件赋值
     */
    private void setData() {
        authInfo = (AuthInfo) getIntent().getSerializableExtra("AuthInfo");
        //是否可以编辑状态
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);

        if (null != authInfo) {
            //0未提交，1通过,2提交审核,3审核驳回
            String type = authInfo.getIs_audit();
            switch (type) {
                case "0":
                    //未认证
                    break;
                case "2":
                    //审核中
                    break;
                case "1":
                    //已认证
                    break;
                case "3":
                    //未通过
                    String note = authInfo.getNote();
                    if (EmptyUtils.isNotEmpty(note)) {
                        authTipTv.setText(note);
                        authTipTv.setTextColor(Color.parseColor("#FF4F64"));
                    }
                    break;
                default:
                    //未认证
                    break;
            }

            //姓名
            if (!isEdit) {
                authNameEt.setEnabled(false);
                authNameEt.setTextColor(Color.parseColor("#C5C5C5"));
            }

            authNameEt.setText(authInfo.getReal_name());

            //学校
            authSchoolTv.setText(authInfo.getSchool_name());

            //院系
            authDepartmentTv.setText(authInfo.getDepartment_name());

            //年级
            authGradeTv.setText(authInfo.getSchool_class_name());

            //学生证正面
            if (EmptyUtils.isNotEmpty(authInfo.getPhoto_front())) {
                //隐藏添加按钮
//                authCerFrontChooseIv.setVisibility(View.GONE);
                //显示照片
                authCerFrontIv.setVisibility(View.VISIBLE);
                XImage.loadImage(authCerFrontIv, authInfo.getPhoto_front());
            }

            if (!isEdit) {
                //隐藏添加按钮
                authCerFrontChooseIv.setVisibility(View.GONE);
            } else {
                authCerFrontChooseIv.setVisibility(View.VISIBLE);
            }

            //学生证反面
            if (EmptyUtils.isNotEmpty(authInfo.getPhoto_back())) {
                //隐藏添加按钮
//                authCerBackChooseIv.setVisibility(View.GONE);
                //显示照片
                authCerBackIv.setVisibility(View.VISIBLE);
                XImage.loadImage(authCerBackIv, authInfo.getPhoto_back());
            }

            if (!isEdit) {
                //隐藏添加按钮
                authCerBackChooseIv.setVisibility(View.GONE);
            } else {
                authCerBackChooseIv.setVisibility(View.VISIBLE);
            }

            //提交按钮显示隐藏处理
            if (!isEdit) {
                authSubmitTv.setVisibility(View.GONE);
                authTipTv.setVisibility(View.INVISIBLE);
            } else {
                authSubmitTv.setVisibility(View.VISIBLE);
                authTipTv.setVisibility(View.VISIBLE);
            }

            //学校、院系、年级
            if (!isEdit) {
                authSchoolRl.setClickable(false);
                authDepartmentRl.setClickable(false);
                authGradeRl.setClickable(false);
            } else {
                authSchoolRl.setClickable(true);
                authDepartmentRl.setClickable(true);
                authGradeRl.setClickable(true);
            }

        }
    }

    @OnClick({R.id.head_back_ly, R.id.auth_school_rl, R.id.auth_department_rl, R.id.auth_grade_rl,
            R.id.auth_cer_front_choose_iv, R.id.auth_cer_back_choose_iv, R.id.auth_submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.auth_school_rl:
                //选择学校
                Intent intent1 = new Intent();
                intent1.setClass(this, SchoolActivity.class);
                startActivityForResult(intent1, 1);

                break;
            case R.id.auth_department_rl:
                //选择院系
                Intent intent2 = new Intent();
                intent2.setClass(this, DepartmentActivity.class);
                intent2.putExtra("school_id", schoolBean.getId());
                startActivityForResult(intent2, 2);
                break;
            case R.id.auth_grade_rl:
                //选择年级
                Intent intent3 = new Intent();
                intent3.setClass(this, GradeActivity.class);
                startActivityForResult(intent3, 3);
                break;
            case R.id.auth_cer_front_choose_iv:
                //学生证正面照片选择
                type = 1;
                //showHeaderEdit();
                choosePic();
                break;
            case R.id.auth_cer_back_choose_iv:
                //学生证反面照片选择
                type = 2;
                //showHeaderEdit();
                choosePic();
                break;
            case R.id.auth_submit_tv:
                //信息提交
                AuthInfo info = new AuthInfo();
                //姓名
                String name = authNameEt.getText().toString();
                if (EmptyUtils.isNotEmpty(name)) {
                    info.setReal_name(name);
                } else {
                    ToastUtils.showShortToast("请输入真实姓名");
                    break;
                }
                //学校
                //重新选择的数据优先
                String schoolName = authSchoolTv.getText().toString();
                if (EmptyUtils.isNotEmpty(schoolName) && EmptyUtils.isNotEmpty(schoolBean.getId())) {
                    info.setSchool_id(schoolBean.getId());
                } else {
                    if (null != authInfo && EmptyUtils.isNotEmpty(authInfo.getSchool_id())) {
                        String schoolId = authInfo.getSchool_id();
                        info.setSchool_id(schoolId);
                    } else {
                        ToastUtils.showShortToast("请选择学校");
                        break;
                    }
                }

                //院系
                String department = authDepartmentTv.getText().toString();
                if (EmptyUtils.isNotEmpty(department) && EmptyUtils.isNotEmpty(departmentBean.getId())) {
                    info.setSchool_department_id(departmentBean.getId());
                } else {
                    if (null != authInfo && EmptyUtils.isNotEmpty(authInfo.getSchool_department_id())) {
                        String departmentId = authInfo.getSchool_department_id();
                        info.setSchool_department_id(departmentId);
                    } else {
                        ToastUtils.showShortToast("请选择院系");
                        break;
                    }
                }

                //年级
                String grade = authGradeTv.getText().toString();
                if (EmptyUtils.isNotEmpty(grade) && EmptyUtils.isNotEmpty(gradeBean.getKey())) {
                    info.setSchool_class(gradeBean.getKey());
                } else {
                    if (null != authInfo && EmptyUtils.isNotEmpty(authInfo.getSchool_class())) {
                        String schoolClass = authInfo.getSchool_class();
                        info.setSchool_class(schoolClass);
                    } else {
                        ToastUtils.showShortToast("请选择年级");
                        break;
                    }
                }

                //学生证正面照片文件
                if (null == photo1) {
                    if (null != authInfo && EmptyUtils.isNotEmpty(authInfo.getPhoto_front())) {
                        //处理被驳回的情况下的图片上传
                        if (authInfo.getIs_audit().equals("3")) {
                            ToastUtils.showShortToast("请重新上传学生证正面照片");
                            break;
                        }
                    } else {
                        ToastUtils.showShortToast("请上传学生证正面照片");
                        break;
                    }
                }
                //学生证反面照片文件
                if (null == photo2) {
                    if (null != authInfo && EmptyUtils.isNotEmpty(authInfo.getPhoto_back())) {
                        if (authInfo.getIs_audit().equals("3")) {
                            ToastUtils.showShortToast("请重新上传学生证反面照片");
                            break;
                        }
                    } else {
                        ToastUtils.showShortToast("请上传学生证反面照片");
                        break;
                    }
                }
                presenter.submit(info, photo1, photo2);

                break;
            default:
                break;
        }
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



      String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //弃用
       // getTakePhoto().onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case 1:
                    //学校信息
                    schoolBean = (SchoolBean) data.getSerializableExtra("SchoolBean");
                    authSchoolTv.setText(schoolBean.getName());
                    //清空院系信息
                    authDepartmentTv.setText(null);
                    break;
                case 2:
                    //院系信息
                    departmentBean = (DepartmentBean) data.getSerializableExtra("DepartmentBean");
                    authDepartmentTv.setText(departmentBean.getName());
                    break;
                case 3:
                    //年级信息
                    gradeBean = (GradeBean) data.getSerializableExtra("GradeBean");
                    authGradeTv.setText(gradeBean.getValue());
                    break;
                default:
                    break;
            }
        }

        //处理选择的证件照
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && null != data) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> lists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                path = lists.get(0).path;
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
             if(EmptyUtils.isNotEmpty(path)){
                 if (type == 1) {
                     //正面
                     photo1 = new File(path);
                     Glide.with(this).load(path).into(authCerFrontIv);
                 } else if (type == 2) {
                     //反面
                     photo2 = new File(path);
                     Glide.with(this).load(path).into(authCerBackIv);
                 }
             }
        }
    }

    @Override
    public void liftData(String info) {
        if (null != info) {
            ToastUtils.showShortToast(info);
            String name = authNameEt.getText().toString();
            //更新本地存储用户名
            if (EmptyUtils.isNotEmpty(name)) {
                UserBean user = AccountUtils.getUser(this);
                user.setReal_name(name);
                //重新保存数据
                AccountUtils.saveUserCache(this, user);
                //通知其他页面刷新相关数据
                EventBus.getDefault().post(Constants.USER_INFO.UPDATE);
            }
            finish();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
//------------------------------------图片选择------------------------------------------

    @Override
    public void takeSuccess(TResult result) {
        //Log.i(TAG, "takeSuccess：" + result.getImages().get(0).getOriginalPath());
        try {
            //    Bitmap bitmap = ImageUtils.getBitmap(result.getImages().get(0).getOriginalPath());
            FileInputStream fis = new FileInputStream(result.getImages().get(0).getCompressPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);

            if (type == 1) {
                photo1 = new File(result.getImages().get(0).getCompressPath());
            } else if (type == 2) {
                photo2 = new File(result.getImages().get(0).getCompressPath());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (type == 1) {
                            //authCerFrontIv.setImageDrawable(new BitmapDrawable(bitmap));
                            authCerFrontIv.setImageBitmap(bitmap);
                        } else if (type == 2) {
                            //authCerBackIv.setBackground(new BitmapDrawable(bitmap));
                            authCerBackIv.setImageBitmap(bitmap);
                        }
                    }
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
