package com.app.adssan.ayucraze.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductColor implements Parcelable {

    int color_id;
    String colorName;
    String colorCode;

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.color_id);
        dest.writeString(this.colorName);
        dest.writeString(this.colorCode);
    }

    public ProductColor() {
    }

    protected ProductColor(Parcel in) {
        this.color_id = in.readInt();
        this.colorName = in.readString();
        this.colorCode = in.readString();
    }

    public static final Creator<ProductColor> CREATOR = new Creator<ProductColor>() {
        @Override
        public ProductColor createFromParcel(Parcel source) {
            return new ProductColor(source);
        }

        @Override
        public ProductColor[] newArray(int size) {
            return new ProductColor[size];
        }
    };
}
