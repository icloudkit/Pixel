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
import android.widget.TextView;

import net.quduo.pixel.R;
import net.quduo.pixel.interfaces.android.model.ChatListDataModel;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    private List<ChatListDataModel> mChatListDataModelList = null;
    private Context mContext;

    public ChatListAdapter(Context mContext, List<ChatListDataModel> list) {
        this.mContext = mContext;
        this.mChatListDataModelList = list;
    }

    @Override
    public int getCount() {
        return this.mChatListDataModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mChatListDataModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        final ChatListDataModel chatListDataModel = mChatListDataModelList.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_chat, null);
            viewHolder.mChatAvatarImageView = (ImageView) view.findViewById(R.id.chat_avatar_image_view);
            viewHolder.mChatTitleTextView = (TextView) view.findViewById(R.id.chat_title_text_view);
            viewHolder.mDataTimeTextView = (TextView) view.findViewById(R.id.date_time_text_view);
            viewHolder.mChatSummaryTextView = (TextView) view.findViewById(R.id.chat_summary_text_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mChatAvatarImageView.setImageResource(chatListDataModel.getChatAvatar());
        viewHolder.mChatTitleTextView.setText(chatListDataModel.getChatTitle());
        viewHolder.mChatSummaryTextView.setText(chatListDataModel.getChatSummary());

        return view;
    }

    final static class ViewHolder {
        ImageView mChatAvatarImageView;
        TextView mChatTitleTextView;
        TextView mDataTimeTextView;
        TextView mChatSummaryTextView;
    }
}
