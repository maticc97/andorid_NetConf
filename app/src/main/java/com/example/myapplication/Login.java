package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
                DataModal modal = new DataModal(username, passwd);

                // calling a method to create a post and passing our modal class.
                Call<ResponseBody> call = retrofitAPI.createPost(modal);
                Log.e("call", "a" + username);

                // on below line we are executing our method.
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // this method is called when we get response from our api.
                        if (response.code() == 200) {
                            try {
                                String s = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(Login.this, "Data added to API" + response.body(), Toast.LENGTH_SHORT).show();


                        // we are getting response from our body
                        // and passing it to our modal class.
                        ResponseBody responseFromAPI = response.body();

                        // on below line we are getting our data from modal class and adding it to our string.
                        if(response.code() == 200){
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putBoolean(isLogedIn, true);
                            editor.commit();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Login.this, "AUTH FAILED", Toast.LENGTH_SHORT).show();
                        }
                        // below line we are setting our
                        // string to our text view.
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("aa", "aa");
                    }
                });
            };
        });

    }
}