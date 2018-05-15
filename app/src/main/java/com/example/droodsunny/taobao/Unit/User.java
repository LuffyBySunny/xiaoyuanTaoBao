package com.example.droodsunny.taobao.Unit;

//实现序列化的用户

import cn.bmob.v3.BmobObject;

public class User extends BmobObject {
    private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

}
