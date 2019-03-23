package com.liuniukeji.mixin.ui.multiimage.addpic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.liuniukeji.mixin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by llt on 2017/4/5.
 * 图片选择
 */

public class ImageChooseActivity extends Activity
{
    private List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private String mBucketName;
    private int availableSize;
    private GridView mGridView;
    private TextView mBucketNameTv;
    private ImageView cancelTv;
    private ImageGridAdapter mAdapter;
    private Button mFinishBtn;
    private HashMap<String, ImageItem> selectedImgs = new HashMap<String, ImageItem>();//选中图片
    private SharedPreferences sp;//保存到本地的数据

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_choose);
        sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        mDataList = (List<ImageItem>) getIntent().getSerializableExtra(
                CustomConstants.EXTRA_IMAGE_LIST);
        if (mDataList == null) mDataList = new ArrayList<ImageItem>();
        mBucketName = getIntent().getStringExtra(
                CustomConstants.EXTRA_BUCKET_NAME);

        if (TextUtils.isEmpty(mBucketName))
        {
            mBucketName = "请选择";
        }
        availableSize = getIntent().getIntExtra(
                CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                CustomConstants.MAX_IMAGE_SIZE);

        initView();
        initListener();

    }

    private void initView()
    {
        mBucketNameTv = (TextView) findViewById(R.id.tv_title);
        mBucketNameTv.setText(mBucketName);

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mDataList);
        mGridView.setAdapter(mAdapter);
        mFinishBtn = (Button) findViewById(R.id.finish_btn);
        cancelTv = (ImageView) findViewById(R.id.iv_left);

        mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
                + availableSize + ")");
        mAdapter.notifyDataSetChanged();
    }

    private void initListener()
    {
        mFinishBtn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
                List<ImageItem> oldDataList = new ArrayList<ImageItem>();
                if (!TextUtils.isEmpty(prefStr))
                {
                    List<ImageItem> tempImages = JSON.parseArray(prefStr,
                            ImageItem.class);
                    oldDataList = tempImages;
                }
                if((new ArrayList<ImageItem>(selectedImgs.values()))!=null){
                    oldDataList.addAll(new ArrayList<ImageItem>(selectedImgs.values()));
                }
                prefStr = JSON.toJSONString(oldDataList);
                sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();
				/*Intent intent = new Intent(ImageChooseActivity.this,
						ComplaintActivity.class);
				intent.putExtra(
						CustomConstants.EXTRA_IMAGE_LIST,
						(Serializable) new ArrayList<ImageItem>(selectedImgs
								.values()));
				startActivity(intent);*/
				setResult(RESULT_OK);
                finish();
            }

        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

                ImageItem item = mDataList.get(position);
                if (item.isSelected)
                {
                    item.isSelected = false;
                    selectedImgs.remove(item.imageId);
                }
                else
                {
                    if (selectedImgs.size() >= availableSize)
                    {
                        Toast.makeText(ImageChooseActivity.this,
                                "最多选择" + availableSize + "张图片",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    item.isSelected = true;
                    selectedImgs.put(item.imageId, item);
                }

                mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
                        + availableSize + ")");
                mAdapter.notifyDataSetChanged();
            }

        });

        cancelTv.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent(ImageChooseActivity.this,
//                        ImageBucketChooseActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(ImageChooseActivity.this,
//                ImageBucketChooseActivity.class);
//        startActivity(intent);
//        finish();
    }
}