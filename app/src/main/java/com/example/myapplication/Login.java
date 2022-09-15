package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String isLogedIn = "isLogedIn";
    public static final String SWITCH1 = "switch";

    public Boolean result;
    public EditText username;
    public EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        saveData();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        result = sharedPreferences.getBoolean(isLogedIn, false);

        if (result) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        } else {

        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(myIntent);
            }
        });

    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(isLogedIn, false);
        editor.commit();
    }

    public void  loginUser(){
         username = (EditText)findViewById(R.id.login_email);
         password = (EditText) findViewById(R.id.password);

         String usrnm_str = username.toString();
         String pass_str = password.toString();

        HttpClient httpclient = new DefaultHttpClient();


    }
}