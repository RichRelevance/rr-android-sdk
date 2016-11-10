package com.richrelevance.richrelevance;


import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class User extends SugarRecord implements Parcelable {


    private String userID;

    public User() {}

    public User(String userID) {
        this.userID = userID;
        save();
    }

    public String getUserID() {
        return userID;
    }

    public User(Parcel in){
        String[] data = new String[1];

        in.readStringArray(data);
        this.userID = data[0];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
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
