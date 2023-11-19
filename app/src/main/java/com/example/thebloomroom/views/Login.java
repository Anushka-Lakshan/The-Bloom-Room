package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.thebloomroom.AlertMessage;
import com.example.thebloomroom.R;
import com.example.thebloomroom.models.User;
import com.example.thebloomroom.models.UserDBModel;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    TextInputLayout email, password;
    UserDBModel userDBModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_pass);

        userDBModel = new UserDBModel(this);
    }

    public void gotoRegister(View v){
        Intent reg = new Intent(this, register.class);
        startActivity(reg);
    }

    public void login(View view){

        String email = this.email.getEditText().getText().toString();
        String password = this.password.getEditText().getText().toString();

        String error = "";

        if(email.isEmpty() || password.isEmpty()){
            error = "Email and Password are required";
        }

        if(error.isEmpty()){
            User user = userDBModel.login(email, password);
            if(user == null){
                error = "Invalid email or password";
            }
            else{

                if(user.getRole().equals("admin")){
                    Intent intent = new Intent(this, Admin_dashboard.class);
                    intent.putExtra("user", user.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(this, "Welcome Admin " + user.getName(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(this, Main_page.class);
                    intent.putExtra("user", user.getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        }

        if(!error.isEmpty()){
            this.email.setError(error);
            this.password.setError(error);

            AlertMessage.show(this, "Warning!", error);
        }


    }


}