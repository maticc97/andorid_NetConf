package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class showDevices extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get extras from another activity
        Bundle extras = getIntent().getExtras();

        //get value by key
        String selectedCustomer = extras.getString("customer");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_devices);

        Toast t = Toast.makeText(getApplicationContext(),
                "Pressed Item " + selectedCustomer,
                Toast.LENGTH_LONG);
        t.show();
    }


}