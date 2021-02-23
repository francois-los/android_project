package com.example.passwordmanager.model;

public class UserModel {
    private String email;
    private String password;
    private String url;

    public UserModel() {
        //Constructor for Firebase
    }

    public UserModel(String email, String password, String url) {
        this.email = email;
        this.password = password;
        this.url = url;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getUrl(){ return url; }
}
