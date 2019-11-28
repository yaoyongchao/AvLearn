package com.yyc.avlearn.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.yyc.avlearn.R;
import com.yyc.avlearn.primary.ImageOneActivity;

public class MyDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_demo);
        startActivity(new Intent(this, ImageOneActivity.class));
        doNext(ImageOneActivity.class);
    }


    private void doNext(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
}
