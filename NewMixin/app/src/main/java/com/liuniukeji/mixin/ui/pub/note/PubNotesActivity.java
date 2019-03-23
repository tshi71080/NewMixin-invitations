package com.liuniukeji.mixin.ui.pub.note;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jph.takephoto.model.TakePhotoOptions;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;
import com.liuniukeji.mixin.ui.multiimage.addpic.CustomConstants;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageBucketChooseActivity;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageItem;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImagePublishAdapter;
import com.liuniukeji.mixin.util.ImageLoader;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.dialog.PhotoUploadDialog;
import com.liuniukeji.mixin.ui.multiimage.addpic.CustomConstants;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageBucketChooseActivity;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImageItem;
import com.liuniukeji.mixin.ui.multiimage.addpic.ImagePublishAdapter;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ImagePreviewActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 发布图文信息
 */
public class PubNotesActivity extends AppCompatActivity implements PubNotesContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.pub_plate_rb1)
    RadioButton pubPlateRb1;
    @BindView(R.id.pub_plate_rb2)
    RadioButton pubPlateRb2;
    @BindView(R.id.pub_plate_rb3)
    RadioButton pubPlateRb3;
    @BindView(R.id.pub_plate_rg)
    RadioGroup pubPlateRg;
    @BindView(R.id.pub_content_et)
    EditText pubContentEt;
    @BindView(R.id.pub_add_pic_iv)
    ImageView pubAddPicIv;
    @BindView(R.id.public_sw)
    Switch publicSw;
    @BindView(R.id.sync_to_pyq_chb)
    CheckBox syncToPyqChb;
    @BindView(R.id.release_tv)
    TextView releaseTv;

    @BindView(R.id.custom_gridview)
    GridView gridView;


    private String path = "";
    private static final int TAKE_PICTURE = 0x000000;
    private static final int ALBUM_PIC = 0x000003;
    private static final int CATEGORY = 0x000004;
    public List<ImageItem> mDataList;
    ImagePublishAdapter mAdapter;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 61;

    private SharedPreferences sp;//保存到本地的数据

    PubNotesContract.Presenter presenter;
    @BindView(R.id.root_ly)
    LinearLayout rootLy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_notes);
        ButterKnife.bind(this);
        headTitleTv.setText("发布");

        presenter = new PublicNotesPresenter(this);
        presenter.attachView(this);

        //字体颜色跟随选中变化
        pubPlateRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.pub_plate_rb1:
                        pubPlateRb1.setTextColor(getResources().getColor(R.color.white));
                        pubPlateRb2.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb3.setTextColor(getResources().getColor(R.color.color_main));

                        break;
                    case R.id.pub_plate_rb2:
                        pubPlateRb1.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb2.setTextColor(getResources().getColor(R.color.white));
                        pubPlateRb3.setTextColor(getResources().getColor(R.color.color_main));
                        break;
                    case R.id.pub_plate_rb3:
                        pubPlateRb1.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb2.setTextColor(getResources().getColor(R.color.color_main));
                        pubPlateRb3.setTextColor(getResources().getColor(R.color.white));
                        break;
                    default:
                        break;
                }
            }
        });

        mDataList = new ArrayList<ImageItem>();
        CustomConstants.MAX_IMAGE_SIZE = 9;
        sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, "").commit();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == getDataSize()) {
//                    showDialog();
                    requestPermissions();
                } else {
                    NineGridView.setImageLoader(new NineGridView.ImageLoader() {
                        @Override
                        public void onDisplayImage(Context context, ImageView imageView, String url) {
                            ImageLoader.loadImageLocal(context, imageView, url);
                        }

                        @Override
                        public Bitmap getCacheImage(String url) {
                            return null;
                        }
                    });
                    List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
                    for (int i = 0; i < mDataList.size(); i++) {
                        ImageInfo info = new ImageInfo();
                        info.setThumbnailUrl(mDataList.get(i).getSourcePath());
                        info.setBigImageUrl(mDataList.get(i).getSourcePath());
                        imageInfos.add(info);
                    }
                    Intent intent = new Intent(PubNotesActivity.this, ImagePreviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) imageInfos);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        mAdapter = new ImagePublishAdapter(this, mDataList);
        gridView.setAdapter(mAdapter);

    }

    private int getDataSize() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 请求权限
     */
    public void requestPermissions() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
                ToastUtils.showShortToast(getResources().getString(R.string.permission_tip));
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            //存在权限
            showDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    //存在权限
                    showDialog();
                } else {
                    ToastUtils.showShortToast(getResources().getString(R.string.permission_tip));
                }
            }
        }
    }

    /**
     * 选择对话框
     */

    private void showDialog() {
        final TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setCorrectImage(true);
        PhotoUploadDialog dialog = new PhotoUploadDialog(this);
        dialog.setPhotoUpListener(new PhotoUploadDialog.PhotoUpLisenter() {
            @Override
            public void tackPhoto() {
                //拍照
                takePhoto();
                //ToastUtils.showShortToast("拍照");
            }

            @Override
            public void joinPhoto() {
                //选择相册
                choosePhone();
            }

            @Override
            public void cancel() {

            }
        });
        dialog.show();
    }


    @OnClick({R.id.head_back_ly, R.id.pub_add_pic_iv, R.id.release_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                closeKeyboard();
                break;
            case R.id.pub_add_pic_iv:
                //添加图片
                break;
            case R.id.release_tv:
                //发布
                String tag;
                switch (pubPlateRg.getCheckedRadioButtonId()) {
                    case R.id.pub_plate_rb1:
                        tag = "0";
                        break;
                    case R.id.pub_plate_rb2:
                        tag = "1";
                        break;
                    case R.id.pub_plate_rb3:
                        tag = "2";
                        break;
                    default:
                        tag = "0";
                        break;
                }

                String status = publicSw.isChecked() ? "0" : "1";
                String content = pubContentEt.getText().toString();

                if (EmptyUtils.isEmpty(content)) {
                    ToastUtils.showShortToast("发布内容不能为空");
                    break;
                }

                presenter.putPhoto(status, tag, content, mDataList);

                //Log.e("PUB","status:"+status+"//"+"tag"+tag);

                releaseTv.setEnabled(false);

                break;
            default:
                break;
        }
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File vFile = new File(Environment.getExternalStorageDirectory()
                + "/", String.valueOf(System.currentTimeMillis())
                + ".png");
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        path = vFile.getPath();
//        Uri cameraUri = Uri.fromFile(vFile);
        Uri cameraUri = getUriForFile(this,vFile);

        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        Log.e("EEEEE",cameraUri.toString());
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }


    public static Uri getUriForFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }


    /**
     * 从手机选择
     */
    public void choosePhone() {
        Intent intent = new Intent(this, ImageBucketChooseActivity.class);
        intent.putExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE, getAvailableSize());
        startActivityForResult(intent, ALBUM_PIC);
    }

    /***允许多少张图片**/
    private int getAvailableSize() {
        int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0) {
            return availSize;
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (getDataSize() != 0) {
                    //txtHint.setVisibility(View.GONE);
                }
                if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE && resultCode == RESULT_OK) {
                    ImageItem item = new ImageItem();
                    item.sourcePath = path;
                    mDataList.add(item);
                    String prefStr = JSON.toJSONString(mDataList);
                    sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();
                    mAdapter.bindList(mDataList);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case ALBUM_PIC:
                if (getDataSize() != 0) {
                    //txtHint.setVisibility(View.GONE);
                }
                String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
                if (!TextUtils.isEmpty(prefStr)) {
                    List<ImageItem> tempImages = JSON.parseArray(prefStr,
                            ImageItem.class);
                    mDataList = tempImages;
                }
                mAdapter.bindList(mDataList);
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void refreshData() {
        ToastUtils.showShortToast("发布成功");
        //通知发布的页面关闭
        setResult(0x112);
        finish();
        releaseTv.setEnabled(true);

    }

    @Override
    public void onEmpty() {
        releaseTv.setEnabled(true);

    }

    @Override
    public void onNetError() {
        releaseTv.setEnabled(true);
    }

    /**
     * 隐藏软键盘
     */
    private void closeKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootLy.getWindowToken(), 0);
    }

}
