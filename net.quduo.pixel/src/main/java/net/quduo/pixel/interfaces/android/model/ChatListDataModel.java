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

package net.quduo.pixel.interfaces.android.model;

import java.io.Serializable;

public class ChatListDataModel implements Serializable {

    private int mChatAvatar;
    private String mChatTitle;
    private String mDataTime;
    private String mChatSummary;

    public String getChatSummary() {
        return mChatSummary;
    }

    public void setChatSummary(String mChatSummary) {
        this.mChatSummary = mChatSummary;
    }

    public int getChatAvatar() {
        return mChatAvatar;
    }

    public void setChatAvatar(int mChatAvatar) {
        this.mChatAvatar = mChatAvatar;
    }

    public String getChatTitle() {
        return mChatTitle;
    }

    public void setChatTitle(String mChatTitle) {
        this.mChatTitle = mChatTitle;
    }

    public String getDataTime() {
        return mDataTime;
    }

    public void setDataTime(String mDataTime) {
        this.mDataTime = mDataTime;
    }


}
