package com.w3engineers.ecommerce.bootic.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.w3engineers.ecommerce.bootic.database.DBFavouriteProducts;

import java.util.Date;

public class Product implements Parcelable {

    private String id;
    private String title;
    private String category;
    private String description;
    private double price;
    private double previous_price;
    private Date date;
    private int sort;
    private String image_name;
    private String user_id;


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPrevious_price(double previous_price) {
        this.previous_price = previous_price;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getPrevious_price() {
        return previous_price;
    }

    public Date getDate() {
        return date;
    }

    public int getSort() {
        return sort;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public ContentValues toValues(){
        ContentValues values = new ContentValues(10);
        values.put(DBFavouriteProducts.COLUMN_ID, id);
        values.put(DBFavouriteProducts.COLUMN_TITLE, title);
        values.put(DBFavouriteProducts.COLUMN_CATEGORY, category);
        values.put(DBFavouriteProducts.COLUMN_DESCRIPTION, description);
        values.put(DBFavouriteProducts.COLUMN_PRICE, price);
        values.put(DBFavouriteProducts.COLUMN_PREV_PRICE, previous_price);
        values.put(DBFavouriteProducts.COLUMN_DATE, String.valueOf(date));
        values.put(DBFavouriteProducts.COLUMN_POSITION, sort);
        values.put(DBFavouriteProducts.COLUMN_IMAGE, image_name);
        values.put(DBFavouriteProducts.COLUMN_USER_ID, user_id);

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeDouble(this.price);
        dest.writeDouble(this.previous_price);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.sort);
        dest.writeString(this.image_name);
        dest.writeString(this.user_id);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.price = in.readDouble();
        this.previous_price = in.readDouble();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.sort = in.readInt();
        this.image_name = in.readString();
        this.user_id = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
