package com.example.myapplication;

import android.app.Application;

public class globalVariabiles extends Application {
    private String API_address;

    public void setAPI_address(){
        this.API_address = "http://1.1.1.1:5000/api/v1/";
    }
}
