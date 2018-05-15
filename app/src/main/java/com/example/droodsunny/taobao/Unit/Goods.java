package com.example.droodsunny.taobao.Unit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class Goods extends BmobObject implements Parcelable {
    public Goods(){}

    protected Goods(Parcel in) {
        Email = in.readString();
        price = in.readFloat();
        goodsName = in.readString();
        category = in.readString();
        description = in.readString();
        image=in.readArrayList(ArrayList.class.getClassLoader());
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel in) {
            return new Goods(in);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private String Email;
    private float price;
    private String goodsName;
    private String category;

    public List<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    private ArrayList image;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Email);
        dest.writeFloat(price);
        dest.writeString(goodsName);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeList(image);

    }
}
