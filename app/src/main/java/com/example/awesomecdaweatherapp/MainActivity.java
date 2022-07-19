package com.example.awesomecdaweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected TextView mTextViewCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewCityName.setText(R.string.city_name);
        Toast.makeText(this, mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();
    }
}