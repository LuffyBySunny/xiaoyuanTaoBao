package com.example.droodsunny.taobao.Unit;

//实现序列化的用户

public class User  {
    private String email;
    private boolean ifremePass;
    private boolean ifauto;
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
    public boolean isIfremePass() {
        return ifremePass;
    }

    public void setIfremePass(boolean ifremePass) {
        this.ifremePass = ifremePass;
    }

    public boolean isIfauto() {
        return ifauto;
    }

    public void setIfauto(boolean ifauto) {
        this.ifauto = ifauto;
    }
}
