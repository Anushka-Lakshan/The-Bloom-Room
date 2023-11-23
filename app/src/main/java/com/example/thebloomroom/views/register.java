package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thebloomroom.AlertMessage;
import com.example.thebloomroom.R;
import com.example.thebloomroom.models.User;
import com.example.thebloomroom.models.DBoperations;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {

    TextInputLayout name, email, password,password2 , address, tel;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        password2 = findViewById(R.id.register_password2);
        address = findViewById(R.id.register_address);
        tel = findViewById(R.id.register_tel);

    }

    public void gotoLogin(View view){
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }

    public void register(View view){

        String error = "";

        String nameText = name.getEditText().getText().toString();
        String emailText = email.getEditText().getText().toString();
        String passwordText = password.getEditText().getText().toString();
        String password2Text = password2.getEditText().getText().toString();
        String addressText = address.getEditText().getText().toString();
        String telText = tel.getEditText().getText().toString();

        if(nameText.isEmpty()){
            name.setError("Name is required");
            error = "Name is required.";
        }
        if(emailText.isEmpty()){
            email.setError("Email is required");
            error += "\nEmail is required.";
        }
        if(passwordText.isEmpty()){
            password.setError("Password is required");
            error += "\nPassword is required.";
        }
        if(password2Text.isEmpty()){
            password2.setError("Password is required");
            error += "\nConfirm Password is required.";
        }
        if(addressText.isEmpty()){
            address.setError("Address is required");
            error += "\nAddress is required.";
        }
        if(telText.isEmpty()){
            tel.setError("Tel is required");
            error += "\nPhone number is required.";
        }

        if(!passwordText.equals(password2Text)){
            error += "\nPassword doesn't match";
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher matcher = pattern.matcher(emailText);


        if(!matcher.matches()){
            error += "\nInvalid Email address.";
        }

        DBoperations DBoperations = new DBoperations(this);
        if(DBoperations.checkEmail(emailText)){
            error += "\nEmail already exists.";
        }

        if(error != ""){
            AlertMessage.show(this, "Attention!", error);
        }else{
            User user = new User();
            user.setName(nameText);
            user.setEmail(emailText);
            user.setPassword(passwordText);
            user.setAddress(addressText);
            user.setTel(telText);
            user.setRole("user");
            registerUser(user);
        }

    }

    public void registerUser(User user){
        try {

            DBoperations DBoperations = new DBoperations(this);
            long id = DBoperations.insert(user);
            if(id > 0){
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Signed up successfully, please login", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){

            AlertMessage.show(this, "Error!", e.getMessage());
        }
    }

    public void registerAdmin(View v){

        User user = new User();
        user.setName("Admin");
        user.setEmail("admin@admin");
        user.setPassword("123");
        user.setAddress("Booloom");
        user.setTel("123456789");
        user.setRole("admin");


        try {

            DBoperations DBoperations = new DBoperations(this);
            long id = DBoperations.insert(user);
            if(id > 0){
//                Intent intent = new Intent(this, Login.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "admin added successfully!!", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){

            AlertMessage.show(this, "Error!", e.getMessage());
        }
    }


}