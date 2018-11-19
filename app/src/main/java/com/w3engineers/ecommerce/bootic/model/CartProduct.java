package com.w3engineers.ecommerce.bootic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartProduct implements Parcelable {

    private String id;
    private String name;
    private double price;
    private int quantity;
    private ProductColor productColorName;
    private ProductSize productSize;


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductColorName(ProductColor productColorName) {
        this.productColorName = productColorName;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductColor getProductColorName() {
        return productColorName;
    }

    public ProductSize getProductSize() {
        return productSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeInt(this.quantity);
        dest.writeParcelable(this.productColorName, flags);
        dest.writeParcelable(this.productSize, flags);
    }

    public CartProduct() {
    }

    protected CartProduct(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readDouble();
        this.quantity = in.readInt();
        this.productColorName = in.readParcelable(ProductColor.class.getClassLoader());
        this.productSize = in.readParcelable(ProductSize.class.getClassLoader());
    }

    public static final Parcelable.Creator<CartProduct> CREATOR = new Parcelable.Creator<CartProduct>() {
        @Override
        public CartProduct createFromParcel(Parcel source) {
            return new CartProduct(source);
        }

        @Override
        public CartProduct[] newArray(int size) {
            return new CartProduct[size];
        }
    };
}

