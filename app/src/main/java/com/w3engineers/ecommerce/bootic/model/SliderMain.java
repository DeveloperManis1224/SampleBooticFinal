package com.w3engineers.ecommerce.bootic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SliderMain implements Parcelable {

    private String id;
    private String heading;
    private String preHeading;

    public SliderMain(String id, String heading, String preHeading) {
        this.id = id;
        this.heading = heading;
        this.preHeading = preHeading;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setPreHeading(String preHeading) {
        this.preHeading = preHeading;
    }

    public String getId() {
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public String getPreHeading() {
        return preHeading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.heading);
        dest.writeString(this.preHeading);
    }

    public SliderMain() {
    }

    protected SliderMain(Parcel in) {
        this.id = in.readString();
        this.heading = in.readString();
        this.preHeading = in.readString();
    }

    public static final Creator<SliderMain> CREATOR = new Creator<SliderMain>() {
        @Override
        public SliderMain createFromParcel(Parcel source) {
            return new SliderMain(source);
        }

        @Override
        public SliderMain[] newArray(int size) {
            return new SliderMain[size];
        }
    };
}