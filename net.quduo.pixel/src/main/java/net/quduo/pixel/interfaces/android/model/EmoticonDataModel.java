package net.quduo.pixel.interfaces.android.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;

public class EmoticonDataModel implements Parcelable, Comparable<EmoticonDataModel> {

    private String name;
    // private Drawable icon;
    private String iconUrl;
    private String description;
    private int type;
    private int order;

    public EmoticonDataModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 把javanbean中的数据写到Parcel。先写id然后写name
        dest.writeString(this.name);
        dest.writeString(this.iconUrl);
        dest.writeString(this.description);
        dest.writeInt(this.type);
        dest.writeInt(this.order);
    }

    // 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
    public static final Parcelable.Creator<EmoticonDataModel> CREATOR = new Parcelable.Creator<EmoticonDataModel>() {

        @Override
        public EmoticonDataModel createFromParcel(Parcel source) {
            // 从Parcel中读取数据，返回person对象
            // readHashMap()
            EmoticonDataModel emoticonDataModel = new EmoticonDataModel();
            emoticonDataModel.setName(source.readString());
            emoticonDataModel.setIconUrl(source.readString());
            emoticonDataModel.setDescription(source.readString());
            emoticonDataModel.setType(source.readInt());
            emoticonDataModel.setOrder(source.readInt());
            return emoticonDataModel;
        }

        @Override
        public EmoticonDataModel[] newArray(int size) {
            return new EmoticonDataModel[size];
        }
    };

    @Override
    public int compareTo(EmoticonDataModel dataModel) {
        if (dataModel != null) {
            if (this.getOrder() > dataModel.getOrder()) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 得到排序的List
     *
     * @param list
     * @return
     */
    public static List<EmoticonDataModel> getOrderList(List<EmoticonDataModel> list) {
        Collections.sort(list);
        return list;
    }

}
