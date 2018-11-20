package com.app.adssan.ayucraze.model;

import android.graphics.Color;
import android.os.Parcel;
import android.util.Size;

public class AvailableProduct extends Product{

    private int quantity;
    private ProductColor[] colors;
    private ProductSize[] sizes;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setColors(ProductColor[] colors) {
        this.colors = colors;
    }

    public void setSizes(ProductSize[] sizes) {
        this.sizes = sizes;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductColor[] getColors() {
        return colors;
    }

    public ProductSize[] getSizes() {
        return sizes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.quantity);
        dest.writeTypedArray(this.colors, flags);
        dest.writeTypedArray(this.sizes, flags);
    }

    public AvailableProduct() {
    }

    protected AvailableProduct(Parcel in) {
        super(in);
        this.quantity = in.readInt();
        this.colors = in.createTypedArray(ProductColor.CREATOR);
        this.sizes = in.createTypedArray(ProductSize.CREATOR);
    }

    public static final Creator<AvailableProduct> CREATOR = new Creator<AvailableProduct>() {
        @Override
        public AvailableProduct createFromParcel(Parcel source) {
            return new AvailableProduct(source);
        }

        @Override
        public AvailableProduct[] newArray(int size) {
            return new AvailableProduct[size];
        }
    };

}