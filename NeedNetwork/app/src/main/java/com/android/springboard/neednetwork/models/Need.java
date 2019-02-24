package com.android.springboard.neednetwork.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Shouib on 7/9/2017.
 */

public class Need implements Parcelable {

    public Need() {
        super();
    }

    private String id;
    private String description;
    private String targetDate; // days
    private String location;
    private String createdBy;
    private List<String> users;
    private List<String> otherUsers;
    private String needType;
    private String targetAmount;

    public String getId() {
        return id;
    }

    public String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getOtherUsers() {
        return otherUsers;
    }

    public void setOtherUsers(List<String> otherUsers) {
        this.otherUsers = otherUsers;
    }

    public String getNeedType() {
        return needType;
    }

    public void setNeedType(String needType) {
        this.needType = needType;
    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(String targetAmount) {
        this.targetAmount = targetAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.targetDate);
        dest.writeString(this.location);
        dest.writeString(this.createdBy);
        dest.writeStringList(this.users);
        dest.writeStringList(this.otherUsers);
        dest.writeString(this.title);
        dest.writeString(this.targetAmount);
        dest.writeString(this.needType);
    }

    protected Need(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.targetDate = in.readString();
        this.location = in.readString();
        this.createdBy = in.readString();
        this.users = in.createStringArrayList();
        this.otherUsers = in.createStringArrayList();
        this.title = in.readString();
        this.targetAmount = in.readString();
        this.needType = in.readString();
    }

    public static final Parcelable.Creator<Need> CREATOR = new Parcelable.Creator<Need>() {
        @Override
        public Need createFromParcel(Parcel source) {
            return new Need(source);
        }

        @Override
        public Need[] newArray(int size) {
            return new Need[size];
        }
    };
}

