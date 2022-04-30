package com.example.ex7demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick1(View view) {
        startActivity(new Intent(this,GravityActivity.class));
    }

    public void onClick2(View view) {
        startActivity(new Intent(this,LinearAccel.class));
    }

    public void onClick3(View view) {
        startActivity(new Intent(this,AmbientActivity.class));

    }
}