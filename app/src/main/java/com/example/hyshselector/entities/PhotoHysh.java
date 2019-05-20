package com.example.hyshselector.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoHysh implements Parcelable {

    private int id;
    private String name;
    private boolean selected = false;

    public PhotoHysh() {
    }


    protected PhotoHysh(Parcel in) {
        id = in.readInt();
        name = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<PhotoHysh> CREATOR = new Creator<PhotoHysh>() {
        @Override
        public PhotoHysh createFromParcel(Parcel in) {
            return new PhotoHysh(in);
        }

        @Override
        public PhotoHysh[] newArray(int size) {
            return new PhotoHysh[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
