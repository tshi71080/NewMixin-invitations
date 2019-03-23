package com.liuniukeji.mixin.ui.multiimage.addpic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class ImagePublishAdapter extends BaseAdapter {
    private List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private Activity mContext;
    SharedPreferences sp;
    OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public ImagePublishAdapter(Activity context, List<ImageItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        sp = mContext.getSharedPreferences(
                CustomConstants.APPLICATION_NAME, Context.MODE_PRIVATE);
    }

    public void bindList(List<ImageItem> dataList) {
        this.mDataList = dataList;
    }

    public int getCount() {
        // 多返回一个用于展示添加图标
        if (mDataList == null) {
            return 1;
        } else if (mDataList.size() == CustomConstants.MAX_IMAGE_SIZE) {
            return CustomConstants.MAX_IMAGE_SIZE;
        } else {
            return mDataList.size() + 1;
        }
    }

    public Object getItem(int position) {
        if (mDataList != null
                && mDataList.size() == CustomConstants.MAX_IMAGE_SIZE) {
            return mDataList.get(position);
        } else if (mDataList == null || position - 1 < 0
                || position > mDataList.size()) {
            return null;
        } else {
            return mDataList.get(position - 1);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
        convertView = View.inflate(mContext, R.layout.item_publish, null);
        ImageView imageIv = (ImageView) convertView
                .findViewById(R.id.item_grid_image);
        ImageView imageDel = (ImageView) convertView
                .findViewById(R.id.item_grid_image_delete);
        if (isShowAddItem(position)) {
            imageIv.setImageResource(R.mipmap.social_release_btn_add);
            imageDel.setVisibility(View.GONE);
        } else {
            final ImageItem item = mDataList.get(position);
            if (null != item.getCommunity_image_url() && !"".equals(item.getCommunity_image_url())) {
//                Glide.with(mContext).load(UrlUtils.APIHTTP + item.getCommunity_image_url()).into(imageIv);
                ImageLoader.loadImage(mContext, imageIv, item.getCommunity_image_url());
            } else {
                ImageLoader.loadImageLocal(mContext, imageIv, item.sourcePath);
//                ImageDisplayer.getInstance(mContext).displayBmp(imageIv, item.thumbnailPath, item.sourcePath);
            }
        }
        imageDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialogShow(position);
            }
        });
        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = mDataList == null ? 0 : mDataList.size();
        return position == size;
    }

    public void deleteDialogShow(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle(R.string.tip).setMessage(R.string.tip_content1).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null == onDeleteListener) {
                    mDataList.remove(position);
                    sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES,
                            JSON.toJSONString(mDataList)).commit();
                    notifyDataSetChanged();
                } else {
                    onDeleteListener.doDelete(position, mDataList.get(position));
                }
            }
        }).setNegativeButton(R.string.cancel, null).show();
    }

    public List<ImageItem> getmDataList() {
        return mDataList;
    }

    public interface OnDeleteListener {
        void doDelete(int position, ImageItem imageItem);
    }
}
