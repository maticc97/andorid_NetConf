package com.example.myapplication;

class loginUser {
    // string variables for our name and job
    private String email;
    private String password;

    public void setUser(String email, String password){
        this.email = email;
        this.password = password;
    }

}

class token{
    private String token;

    public String getToken(){
        return token;
    }
}

