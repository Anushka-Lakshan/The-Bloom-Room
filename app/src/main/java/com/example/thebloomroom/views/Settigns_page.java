package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thebloomroom.R;


public class Settigns_page extends AppCompatActivity {

    int UserID;
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settigns_page);

        UserID = getIntent().getIntExtra("UserID", 0);

        UserName = getIntent().getStringExtra("user");
    }

    public void Logout(View view){
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void gotoCart(View view){
        Intent intent = new Intent(this, Cart_page.class);
        intent.putExtra("UserID", this.UserID);
        intent.putExtra("user", UserName);
        startActivity(intent);
    }

    public void gotoMain(View view){
        Intent intent = new Intent(this, Main_page.class);
        intent.putExtra("user_id", UserID);
        intent.putExtra("user", UserName);
        startActivity(intent);
    }
}