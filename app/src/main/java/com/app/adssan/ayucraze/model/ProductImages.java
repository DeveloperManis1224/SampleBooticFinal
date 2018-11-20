package com.app.adssan.ayucraze.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductImages implements Parcelable {

    private String id;
    private String image_name;
    private String product_id;


    public void setId(String id) {
        this.id = id;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getId() {
        return id;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.image_name);
        dest.writeString(this.product_id);
    }

    public ProductImages() {
    }

    protected ProductImages(Parcel in) {
        this.id = in.readString();
        this.image_name = in.readString();
        this.product_id = in.readString();
    }

    public static final Creator<ProductImages> CREATOR = new Creator<ProductImages>() {
        @Override
        public ProductImages createFromParcel(Parcel source) {
            return new ProductImages(source);
        }

        @Override
        public ProductImages[] newArray(int size) {
            return new ProductImages[size];
        }
    };
}
