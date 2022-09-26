package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;





public class Login extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String isLogedIn = "isLogedIn";
    public static final String token = "token";

    public Boolean result;
    public EditText username_editext;
    public EditText password_editext;
    public String username;
    public String passwd;

    public String API_IP_ADDR = "http://192.168.1.143:5000/api/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        set_API_address();

        saveData();

        Button login_btn;
        login_btn = (Button)findViewById(R.id.login_btn);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        result = sharedPreferences.getBoolean(isLogedIn, false);


        if (result) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        } else{

        }
        postData(login_btn);


    }

    public void set_API_address(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("API_addr", API_IP_ADDR);
        editor.apply();
    }

    public String get_API_address(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString("API_addr", "http://192.168.1.143:5000/api/v1/");
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(isLogedIn, false);
        editor.commit();
    }

    public String[]  getUserData(){
         username_editext = (EditText)findViewById(R.id.login_email);
         password_editext = (EditText) findViewById(R.id.password);

         username = username_editext.getText().toString();
         passwd = password_editext.getText().toString();

         String[] values = new String[2];
         values[0] = username;
         values[1] = passwd;

        return values;
    }



    private void postData(Button login_btn) {

        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String[] userData = getUserData();
                String username = userData[0];
                String passwd = userData[1];

                // on below line we are creating a retrofit
                // builder and passing our base url
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                String API_URL =  sharedPreferences.getString("API_addr", "http://192.168.1.143:5000/api/v1/");
                String url = API_URL;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        // as we are sending data in json format so
                        // we have to add Gson converter factory
                        .addConverterFactory(GsonConverterFactory.create())
                        // at last we are building our retrofit builder.
                        .build();
                // below line is to create an instance for our retrofit api class.
                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

                // passing data from our text fields to our modal class.
                loginUser user = new loginUser();
                user.setUser(username,  passwd);

                // calling a method to create a post and passing our modal class.
                Call<token> call = retrofitAPI.createPost(user);
                Log.e("call", "a" + username);

                // on below line we are executing our method.
                call.enqueue(new Callback<token>() {
                    @Override
                    public void onResponse(@NonNull Call<token> call, @NonNull Response<token> response) {
                        // we are getting response from our body
                       // and passing it to our modal class.

                        String tkn = null;
                        token tookn =  response.body();

                        //if there is no response,
                        if(tookn!=null){
                            tkn = tookn.getToken();
                        }

                        // on below line we are getting our data from modal class and adding it to our string.
                        if(response.code() == 200 && tkn != null){
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putBoolean(isLogedIn, true);
                            editor.putString(token, tkn);
                            editor.commit();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            Log.e("aa", "aa" + response.body());
                            startActivity(intent);
                            Toast.makeText(Login.this, "Successfully LOGED IN, TOKEN STORED", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "AUTH FAILEDEDED", Toast.LENGTH_SHORT).show();
                        }
                        // string to our text view.
                    }

                    @Override
                    public void onFailure(Call<token> call, Throwable t) {
                        Log.e("aa", "aa");
                    }
                });
            };
        });

    }
}