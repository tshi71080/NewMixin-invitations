package com.liuniukeji.mixin.ui.areacode;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;

import java.util.List;

/**
 * @Date ${Date}
 * @CreateBy Android Studio
 */
public class AreaCodeAdapter extends BaseQuickAdapter<AreaCodeBean,BaseViewHolder> {

    public AreaCodeAdapter(@Nullable List<AreaCodeBean> data) {
        super(R.layout.item_area_code, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AreaCodeBean item) {
        helper.setText(R.id.tv_country_name,item.getCountry_cn()+"("+item.getCountry()+")").setText(R.id.tv_area_code,"+"+item.getCode());
    }
}
