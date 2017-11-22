package com.wen.hugo.bean;

/**
 * Created by hugo on 11/21/17.
 */

public class User {
    public String getUserename() {
        return userename;
    }

    public void setUserename(String userename) {
        this.userename = userename;
    }

    private String userename;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public User(String username,String password){
        this.userename = username;
        this.password = password;
    }

}
