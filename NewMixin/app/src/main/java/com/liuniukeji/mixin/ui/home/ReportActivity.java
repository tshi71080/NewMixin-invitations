package com.liuniukeji.mixin.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 举报
 */

public class ReportActivity extends AppCompatActivity implements
        TakePhoto.TakeResultListener, InvokeListener, ReportContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.report_content_et)
    EditText reportContentEt;
    @BindView(R.id.report_add_pic_iv)
    ImageView reportAddPicIv;
    @BindView(R.id.report_pic_rv)
    RecyclerView reportPicRv;
    @BindView(R.id.report_submit_tv)
    TextView reportSubmitTv;

    ReportContract.Presenter presenter;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    File photo;
    List<File> photos;
    int current;
    ImageAdapter imageAdapter;
    String member_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        presenter = new ReportPresenter(this);
        presenter.attachView(this);

        headTitleTv.setText("举报");

        member_id=getIntent().getStringExtra("member_id");

        //初始化图片选择器
        getTakePhoto().onCreate(savedInstanceState);
        photos = new ArrayList<>();
        //图片列表
        imageAdapter = new ImageAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        reportPicRv.setLayoutManager(linearLayoutManager);
        reportPicRv.setAdapter(imageAdapter);
    }

    @OnClick({R.id.head_back_ly, R.id.report_add_pic_iv, R.id.report_submit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.report_add_pic_iv:
                //添加图片
                showHeaderEdit();
                break;
            case R.id.report_submit_tv:
                //提交内容
                String content = reportContentEt.getText().toString();
                if (EmptyUtils.isEmpty(content)) {
                    ToastUtils.showShortToast("请填写举报内容");
                    break;
                }

                if(EmptyUtils.isEmpty(member_id)){
                    ToastUtils.showShortToast("参数错误");
                    break;
                }

                if (null != photos && photos.size() > 0) {
                    presenter.submit(content,member_id, photos.get(0));
                } else {
                    presenter.submit(content,member_id, null);
                }

                break;
            default:
                break;
        }
    }

    private void showHeaderEdit() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        final Uri imageUri = Uri.fromFile(file);
        final TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        configCompress(takePhoto);
        builder.setCorrectImage(true);
        PhotoUploadDialog dialog = new PhotoUploadDialog(this);
        dialog.setPhotoUpListener(new PhotoUploadDialog.PhotoUpLisenter() {
            @Override
            public void tackPhoto() {
                takePhoto.onPickFromCapture(imageUri);
                takePhoto.setTakePhotoOptions(builder.create());
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

        int maxSize = 100 * 1024;
        int width = 800;
        int height = 800;
        boolean showProgressBar = true;
        CompressConfig config;
        LubanOptions option = new LubanOptions.Builder()
                .setMaxHeight(height)
                .setMaxWidth(width)
                .setMaxSize(maxSize)
                .create();
        config = CompressConfig.ofLuban(option);
        takePhoto.onEnableCompress(config, showProgressBar);
    }


    /***获取TakePhoto实例**/
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(
                    new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        try {
            FileInputStream fis = new FileInputStream(result.getImages().get(0).getCompressPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            final Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);
            photo = new File(result.getImages().get(0).getCompressPath());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (current == 1) {
                            ToastUtils.showShortToast("最多添加一张图片");
                        } else {
                            current++;
                            photos.add(photo);
                            imageAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //返回结果处理
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void liftData(String info) {
        if(EmptyUtils.isNotEmpty(info)){
            ToastUtils.showShortToast(info);
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
     * 图片列表适配器
     */

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
        Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ImageAdapter.MyViewHolder holder = new ImageAdapter.MyViewHolder(LayoutInflater.
                    from(mContext).inflate(R.layout.pub_img_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ImageAdapter.MyViewHolder holder, final int position) {
            if (null != photos && photos.size() > 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(photos.get(position).getPath());
                holder.ivCer.setBackground(new BitmapDrawable(bitmap));
            }

            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除图片
                    photos.remove(position);
                    imageAdapter.notifyDataSetChanged();
                    current--;
                }
            });
        }

        @Override
        public int getItemCount() {
            if (null != photos) {
                return photos.size();
            } else {
                return 0;
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivDel;
            ImageView ivCer;

            public MyViewHolder(View view) {
                super(view);
                ivDel = view.findViewById(R.id.iv_del);
                ivCer = view.findViewById(R.id.iv_img);
            }
        }
    }

}
