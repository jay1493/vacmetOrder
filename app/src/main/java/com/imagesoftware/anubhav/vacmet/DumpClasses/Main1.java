package com.imagesoftware.anubhav.vacmet.DumpClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.imagesoftware.anubhav.vacmet.LoginActivity;

/**
 * Created by anubhav on 3/1/17.
 */

public class Main1 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
    }
}
