package com.example.droodsunny.taobao.Unit;

import android.os.Parcel;
import android.os.Parcelable;
//实现序列化的用户

public class User implements Parcelable {
    private String email;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.password);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            User user = new User();
            user.email = source.readString();
            user.password = source.readString();
            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[0];
        }

    };



}
