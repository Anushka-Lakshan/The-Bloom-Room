package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thebloomroom.R;

public class Manage_Product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
    }

    public void gotoAddProduct(View view){
        Intent i = new Intent(this, Add_product.class);
        startActivity(i);
    }
}