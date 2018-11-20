package com.app.adssan.ayucraze.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderedProduct implements Parcelable {

    private int order_id;
    private int product_id;
    private String product_title;
    private String product_color;
    private String product_size;
    private double order_price;
    private double order_method;
    private double order_status;



    public int getOrdered_id() {
        return order_id;
    }

    public void setOrdered_id(int ordered_id) {
        this.order_id = ordered_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public double getOrder_method() {
        return order_method;
    }

    public void setOrder_method(double order_method) {
        this.order_method = order_method;
    }

    public double getOrder_status() {
        return order_status;
    }

    public void setOrder_status(double order_status) {
        this.order_status = order_status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.order_id);
        dest.writeInt(this.product_id);
        dest.writeString(this.product_title);
        dest.writeString(this.product_color);
        dest.writeString(this.product_size);
        dest.writeDouble(this.order_price);
        dest.writeDouble(this.order_method);
        dest.writeDouble(this.order_status);
    }

    public OrderedProduct() {
    }

    protected OrderedProduct(Parcel in) {
        this.order_id = in.readInt();
        this.product_id = in.readInt();
        this.product_title = in.readString();
        this.product_color = in.readString();
        this.product_size = in.readString();
        this.order_price = in.readDouble();
        this.order_method = in.readDouble();
        this.order_status = in.readDouble();
    }

    public static final Parcelable.Creator<OrderedProduct> CREATOR = new Parcelable.Creator<OrderedProduct>() {
        @Override
        public OrderedProduct createFromParcel(Parcel source) {
            return new OrderedProduct(source);
        }

        @Override
        public OrderedProduct[] newArray(int size) {
            return new OrderedProduct[size];
        }
    };
}

