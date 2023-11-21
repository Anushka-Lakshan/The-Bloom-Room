package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thebloomroom.R;

public class Admin_dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
    }

    public void gotoManageProducts(View view) {

        Intent i = new Intent(this, Manage_Product.class);
        startActivity(i);
    }

    public void gotoManageCategories(View view) {
        Intent i = new Intent(this, Manage_Category.class);
        startActivity(i);
    }
}