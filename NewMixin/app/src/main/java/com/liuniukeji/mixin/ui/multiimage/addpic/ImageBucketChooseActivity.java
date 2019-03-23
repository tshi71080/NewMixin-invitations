package com.liuniukeji.mixin.ui.multiimage.addpic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.liuniukeji.mixin.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llt on 2017/4/5.
 * 选择相册
 */

public class ImageBucketChooseActivity extends Activity {
    private ImageFetcher mHelper;
    private List<ImageBucket> mDataList = new ArrayList<ImageBucket>();
    private ListView mListView;
    private ImageBucketAdapter mAdapter;
    private int availableSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_bucket_choose);

        mHelper = ImageFetcher.getInstance(getApplicationContext());
        initData();
        initView();
    }

    private void initData() {
        mDataList = mHelper.getImagesBucketList(false);
        availableSize = getIntent().getIntExtra(
                CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                CustomConstants.MAX_IMAGE_SIZE);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new ImageBucketAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
        TextView titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText("本地相册");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                selectOne(position);

                Intent intent = new Intent(ImageBucketChooseActivity.this,
                        ImageChooseActivity.class);
                intent.putExtra(CustomConstants.EXTRA_IMAGE_LIST,
                        (Serializable) mDataList.get(position).imageList);
                intent.putExtra(CustomConstants.EXTRA_BUCKET_NAME,
                        mDataList.get(position).bucketName);
                intent.putExtra(CustomConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                        availableSize);
                startActivityForResult(intent, 0);
//                finish();
            }
        });

        ImageView cancelTv = (ImageView) findViewById(R.id.iv_left);
        cancelTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(ImageBucketChooseActivity.this,
//						ComplaintActivity.class);
//				startActivity(intent);
                finish();
            }
        });
    }

    private void selectOne(int position) {
        int size = mDataList.size();
        for (int i = 0; i != size; i++) {
            if (i == position) mDataList.get(i).selected = true;
            else {
                mDataList.get(i).selected = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
