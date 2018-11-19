package com.w3engineers.ecommerce.bootic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String user_id;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String password;
    private String address;
    private String number;
    private String image;
    private String membership;



    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getImage() {
        return image;
    }

    public String getMembership() {
        return membership;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.address);
        dest.writeString(this.number);
        dest.writeString(this.image);
        dest.writeString(this.membership);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.user_id = in.readString();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.address = in.readString();
        this.number = in.readString();
        this.image = in.readString();
        this.membership = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
