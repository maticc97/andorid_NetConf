package com.example.myapplication;

public class DataModal {
    // string variables for our name and job
    private String email;
    private String password;
    private String token;

    public DataModal(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void getToken(String token) {
        this.token = token;
    }
}

