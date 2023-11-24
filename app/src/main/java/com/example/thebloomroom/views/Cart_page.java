package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thebloomroom.Adaptors.CartAdaptor;
import com.example.thebloomroom.R;
import com.example.thebloomroom.models.CartItem;
import com.example.thebloomroom.models.DBoperations;

import java.util.ArrayList;

public class Cart_page extends AppCompatActivity {

    int UserID;
    String UserName;

    ListView listView;

    ArrayList<CartItem> cartItems;
    DBoperations dboperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        UserID = getIntent().getIntExtra("UserID", 0);

        UserName = getIntent().getStringExtra("user");

        listView = findViewById(R.id.cartList);

        dboperations = new DBoperations(this);

        if(UserID == 0){
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        updateCart();
    }

    private void updateCart(){

        cartItems = dboperations.getCart(UserID);

        if(cartItems == null){
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_LONG).show();
        }
        else {
            CartAdaptor cartAdaptor = new CartAdaptor(this, cartItems);
            listView.setAdapter(cartAdaptor);
        }
    }

    public void clearCart(View view){

        dboperations.clearCart(UserID);
        recreate();
    }

    public void removeItem(View view){
        int position = (int) view.getTag();

        CartItem cartItem = cartItems.get(position);

        dboperations.deleteCartItem(this.UserID, cartItem.getProductID());
        recreate();
        Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
    }

    public void Increase(View view){
        int position = (int) view.getTag();

        CartItem cartItem = cartItems.get(position);
        int quantity = cartItem.getQuantity();
        quantity++;

        dboperations.updateQuantity(this.UserID, cartItem.getProductID(), quantity);
        recreate();
        Toast.makeText(this, "Quantity increased", Toast.LENGTH_SHORT).show();
    }

    public void Decrease(View view){
        int position = (int) view.getTag();

        CartItem cartItem = cartItems.get(position);
        int quantity = cartItem.getQuantity();
        quantity--;

        if(quantity <= 0){
            dboperations.deleteCartItem(this.UserID, cartItem.getProductID());
            recreate();
            Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
        }
        else{
            dboperations.updateQuantity(this.UserID, cartItem.getProductID(), quantity);
            recreate();
            Toast.makeText(this, "Quantity decreased", Toast.LENGTH_SHORT).show();
        }


    }



    public void gotoMain(View view){
        Intent intent = new Intent(this, Main_page.class);
        intent.putExtra("user_id", UserID);
        startActivity(intent);
    }

    public void gotoProfile(View view){
        Intent intent = new Intent(this, Settigns_page.class);
        intent.putExtra("UserID", UserID);
        intent.putExtra("user", UserName);
        startActivity(intent);
    }





}