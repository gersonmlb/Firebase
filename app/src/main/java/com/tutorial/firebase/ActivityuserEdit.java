package com.tutorial.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ActivityuserEdit extends AppCompatActivity {

    public static final String user="names";

    TextView txtuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        txtuser= (TextView) findViewById(R.id.txtUser);

        //Capturamos el user y la enlazamos
        String user= getIntent().getStringExtra("names");
        txtuser.setText(user);

    }
}
