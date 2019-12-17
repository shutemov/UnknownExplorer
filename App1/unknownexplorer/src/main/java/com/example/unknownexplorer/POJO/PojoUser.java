package com.example.unknownexplorer.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PojoUser {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
