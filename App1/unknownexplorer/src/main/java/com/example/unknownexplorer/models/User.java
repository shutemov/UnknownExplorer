package com.example.unknownexplorer.models;

public class User {


   String login;
   String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
