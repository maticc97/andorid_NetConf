package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class showDevices extends AppCompatActivity {
    //get extras from another activity
    Bundle extras;

    //get value by key
    String selectedCustomer;
    private String TAG = showDevices.class.getSimpleName();
    private ListView lv;
    public static final String SHARED_PREFS = "sharedPrefs";
    private String tkn;

    ArrayList<HashMap<String, String>> devicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_devices);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        tkn =  sharedPreferences.getString("token", "");

        if (tkn == null){
            Intent intent = new Intent(showDevices.this, Login.class);

            //pass variable to another activity
            startActivity(intent);
        }

        lv = (ListView) findViewById(R.id.list_devices);
        devicesList = new ArrayList<>();

        extras= getIntent().getExtras();
        selectedCustomer= extras.getString("customer");
        TextView cust_label = (TextView)findViewById(R.id.customer_label);
        cust_label.setText(selectedCustomer);

        Toast t = Toast.makeText(getApplicationContext(),
                "Pressed Item " + selectedCustomer,
                Toast.LENGTH_LONG);


        //set on List items event listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //start new activity
                Intent intent = new Intent(showDevices.this, showDetail.class);

                //pass variable to another activity
                intent.putExtra("device_id", devicesList.get(i).get("_id"));
                startActivity(intent);
            }
        });


        new getDevices().execute();

    }
    private class getDevices extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(showDevices.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://192.168.1.143:5000/api/v1/customers/"+selectedCustomer+"/devices/";
            String jsonStr = sh.makeServiceCall(url, tkn);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {

                try {
                    // Getting JSON Array node
                    JSONArray devices = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < devices.length(); i++) {
                        JSONObject c = devices.getJSONObject(i);
                        String id = c.getString("_id");
                        String hostname = c.getString("hostname");
                        String type = c.getString("type");
                        String ip_address = c.getString("ip_address");
                        String added_by = c.getString("added_by");

                        // tmp hash map for single contact
                        HashMap<String, String> device = new HashMap<>();

                        // adding each child node to HashMap key => value
                        device.put("_id", id);
                        device.put("hostname", hostname);
                        device.put("type", type);
                        device.put("ip_address", ip_address);
                        device.put("added_by", added_by);
                        Log.d("a", "a"+device);

                        // adding contact to contact list
                        devicesList.add(device);

                    }

                    Log.d("aa", "device list");
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(showDevices.this, devicesList,
                    R.layout.list_devices, new String[]{"hostname", "type", "ip_address", "added_by"},
                    new int[]{R.id.hostname, R.id.type_text, R.id.ip_addr_text, R.id.added_by_text});
            lv.setAdapter(adapter);

        }

    }



}