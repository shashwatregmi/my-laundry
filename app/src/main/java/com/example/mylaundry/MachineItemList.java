package com.example.mylaundry;

import android.os.Parcel;
import android.os.Parcelable;

import javax.crypto.Mac;

public class MachineItemList implements Parcelable {
    private int img;
    private String title;
    private String desc;

    public MachineItemList(int img, String title, String desc){
        this.img = img;
        this.title = title;
        this.desc = desc;
    }

    protected MachineItemList(Parcel in) {
        img = in.readInt();
        title = in.readString();
        desc = in.readString();
    }

    public static final Creator<MachineItemList> CREATOR = new Creator<MachineItemList>() {
        @Override
        public MachineItemList createFromParcel(Parcel in) {
            return new MachineItemList(in);
        }

        @Override
        public MachineItemList[] newArray(int size) {
            return new MachineItemList[size];
        }
    };

    public int getImg(){
        return img;
    }

    public String getTitle(){
        return title;
    }

    public String getDesc(){
        return desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(img);
        dest.writeString(title);
        dest.writeString(desc);
    }
}
