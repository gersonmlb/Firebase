package com.tutorial.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class ChargerActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger);

        preferences = getSharedPreferences("mis_preferencias", Context.MODE_PRIVATE);
        editor = preferences.edit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3600);
    }

}
