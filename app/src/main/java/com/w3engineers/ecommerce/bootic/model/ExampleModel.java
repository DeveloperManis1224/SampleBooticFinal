package com.w3engineers.ecommerce.bootic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ExampleModel implements Parcelable {

    private String itemName;
    private String category;
    private String description;
    private int sort;
    private double price;
    private String image;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getSort() {
        return sort;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeInt(this.sort);
        dest.writeDouble(this.price);
        dest.writeString(this.image);
    }

    public ExampleModel() {
    }

    protected ExampleModel(Parcel in) {
        this.itemName = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.sort = in.readInt();
        this.price = in.readDouble();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<ExampleModel> CREATOR = new Parcelable.Creator<ExampleModel>() {
        @Override
        public ExampleModel createFromParcel(Parcel source) {
            return new ExampleModel(source);
        }

        @Override
        public ExampleModel[] newArray(int size) {
            return new ExampleModel[size];
        }
    };
}
