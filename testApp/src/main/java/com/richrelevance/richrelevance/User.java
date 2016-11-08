package com.richrelevance.richrelevance;


import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class User extends SugarRecord implements Parcelable {

    private String name;

    private String userID;

    public User() {}

    public User(String name, String userID) {
        this.name = name;
        this.userID = userID;
        save();
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public User(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.name = data[0];
        this.userID = data[1];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.name,
                this.userID});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
