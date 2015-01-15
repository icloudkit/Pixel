/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.quduo.pixel.interfaces.android.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.model.CountryRegionDataModel;

import java.util.List;

public class CountryRegionAdapter extends BaseAdapter implements SectionIndexer {

    private List<CountryRegionDataModel> mCountryRegionDataModelList = null;
    private Context mContext;

    public CountryRegionAdapter(Context mContext, List<CountryRegionDataModel> list) {
        this.mContext = mContext;
        this.mCountryRegionDataModelList = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<CountryRegionDataModel> list) {
        this.mCountryRegionDataModelList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.mCountryRegionDataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCountryRegionDataModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final CountryRegionDataModel mContent = mCountryRegionDataModelList.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_country_region, null);
            viewHolder.mDataTitle = (TextView) view.findViewById(R.id.data_title);
            viewHolder.mCatalogLetter = (TextView) view.findViewById(R.id.catalog_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.mCatalogLetter.setVisibility(View.VISIBLE);
            viewHolder.mCatalogLetter.setText(mContent.getCatalogLetter());
        } else {
            viewHolder.mCatalogLetter.setVisibility(View.GONE);
        }

        viewHolder.mDataTitle.setText(this.mCountryRegionDataModelList.get(position).getDataTitle());

        return view;
    }


    final static class ViewHolder {
        TextView mCatalogLetter;
        TextView mDataTitle;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return mCountryRegionDataModelList.get(position).getCatalogLetter().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mCountryRegionDataModelList.get(i).getCatalogLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
