package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class showDetail extends AppCompatActivity {

    Bundle extras;
    String selected_device;
    private String TAG = showDevices.class.getSimpleName();

    TextView hostname;
    TextView ip_addr;
    TextView type_text;
    TextView added_by;
    TextView config;
    ArrayList<HashMap<String, String>> devicesList;
    public static final String SHARED_PREFS = "sharedPrefs";
    private String tkn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        tkn =  sharedPreferences.getString("token", "");

        if (tkn == null){
            Intent intent = new Intent(showDetail.this, Login.class);

            //pass variable to another activity
            startActivity(intent);
        }

        hostname = findViewById(R.id.hostname);
        ip_addr = findViewById(R.id.ip_addr_text);
        type_text = findViewById(R.id.type_text);
        added_by = findViewById(R.id.added_by_text);
        config = findViewById(R.id.config_text);


        extras= getIntent().getExtras();
        selected_device= extras.getString("device_id");
        Toast.makeText(showDetail.this, selected_device, Toast.LENGTH_LONG).show();

        new getDetails().execute();
    }

    private class getDetails extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(showDetail.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            String API_URL =  sharedPreferences.getString("API_addr", "http://192.168.1.143:5000/api/v1/");

            String url = API_URL+"device/"+selected_device;
            String jsonStr = sh.makeServiceCall(url, tkn);

            if (jsonStr != null) {

                try {
                    JSONObject obj = new JSONObject(jsonStr);
                    hostname.setText(obj.getString("hostname"));
                    ip_addr.setText(obj.getString("ip_address"));
                    added_by.setText(obj.getString("added_by"));
                    config.setText(obj.getString("config"));
                    type_text.setText(obj.getString("type"));

                    Log.e(TAG, "Couldn't get json from server." + obj.getString("hostname"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {


        }

    }
}