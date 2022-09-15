package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> customerList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        //set on List items event listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //start new activity
                Intent intent = new Intent(MainActivity.this, showDevices.class);

                //pass variable to another activity
                intent.putExtra("customer", customerList.get(i).get("name"));
                startActivity(intent);
            }
        });


        new getCustomers().execute();



    }


    private class getCustomers extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://192.168.1.143:5000/api/v1/customers";
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {

                try {
                    // Getting JSON Array node
                    JSONArray customers = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < customers.length(); i++) {
                        JSONObject c = customers.getJSONObject(i);
                        String id = c.getString("_id");
                        String name = c.getString("name");
                        String contact_email = c.getString("contact_email");
                        String engineer_email = c.getString("engineer_email");
                        String added_by = c.getString("added_by");
                        String devices_count = c.getString("devices_count");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("_id", id);
                        contact.put("name", name);
                        contact.put("contact_email", contact_email);
                        contact.put("engineer_email", engineer_email);
                        contact.put("added_by", added_by);
                        contact.put("devices_count", devices_count);


                        // adding contact to contact list
                        customerList.add(contact);

                    }
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
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, customerList,
                    R.layout.list_customers, new String[]{"name", "contact_email", "engineer_email", "devices_count"},
                    new int[]{R.id.customer_id, R.id.email1_text, R.id.email2_text, R.id.device_count_text});
            lv.setAdapter(adapter);

        }

    }

}
