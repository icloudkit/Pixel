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
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.model.ContactListDataModel;

import java.util.List;

public class ContactListAdapter extends BaseAdapter implements SectionIndexer {

    private List<ContactListDataModel> mContactListDataModelList = null;
    private Context mContext;

    public ContactListAdapter(Context mContext, List<ContactListDataModel> list) {
        this.mContext = mContext;
        this.mContactListDataModelList = list;
    }

    @Override
    public int getCount() {
        return this.mContactListDataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mContactListDataModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        final ContactListDataModel contactListDataModel = mContactListDataModelList.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_contact, null);
            viewHolder.mContactAvatarImageView = (ImageView) view.findViewById(R.id.contact_avatar_image_view);
            viewHolder.mContactNameTextView = (TextView) view.findViewById(R.id.contact_name_text_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mContactAvatarImageView.setImageResource(contactListDataModel.getContactAvatar());
        viewHolder.mContactNameTextView.setText(contactListDataModel.getContactName());

        return view;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mContactListDataModelList.get(i).getCatalogLetter();

            // .toUpperCase()
            char firstChar = sortStr.charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mContactListDataModelList.get(position).getCatalogLetter().charAt(0);
    }

    final static class ViewHolder {
        ImageView mContactAvatarImageView;
        TextView mContactNameTextView;
    }
}
