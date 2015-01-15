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

package net.quduo.pixel.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import net.quduo.pixel.infrastructure.persistence.Column;
import net.quduo.pixel.infrastructure.persistence.Table;

/**
 * Serializable
 *
 * @author hongquanli <hongquanli@qq.com>
 * @version 1.0 2015-01-13 17:6:02
 */
@Table(name = "user")
public class User implements Parcelable {

    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 把javanbean中的数据写到Parcel。先写id然后写name
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    // 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            // 从Parcel中读取数据，返回person对象
            // readHashMap()
            return new User(source.readInt(), source.readString());
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
