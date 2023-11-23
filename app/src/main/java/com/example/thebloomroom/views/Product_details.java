package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thebloomroom.AlertMessage;
import com.example.thebloomroom.R;
import com.example.thebloomroom.models.CartItem;
import com.example.thebloomroom.models.DBoperations;
import com.example.thebloomroom.models.Product;

public class Product_details extends AppCompatActivity {

    int productID, userID;

    TextView Name, price, category;
    EditText quantity;
    ImageView image;

    DBoperations dboperations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getIntExtra("product", 0);
        userID = getIntent().getIntExtra("UserID", 0);

        Name = findViewById(R.id.PD_name);
        price = findViewById(R.id.PD_price);
        category = findViewById(R.id.PD_cat);
        image = findViewById(R.id.PD_image);

        quantity = findViewById(R.id.PD_qunt);

        dboperations = new DBoperations(this);

        Product product = dboperations.getProductById(productID);

        if(product == null){
            AlertMessage.show(this, "Error", "Product not found");
        }
        else {
            Name.setText(product.getName());
            price.setText("Rs." + String.valueOf(product.getPrice()));
            category.setText("Category: " + product.getCategoryName());

            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            image.setImageBitmap(bitmap);
        }




    }

    public void goback(View view){
        finish();

    }

    public void addToCart(View view){
        int userID = this.userID;
        String productName = Name.getText().toString();
        float price = Float.parseFloat(this.price.getText().toString().split("Rs.")[1]);
        int quantity = Integer.parseInt(this.quantity.getText().toString());

        if(quantity <= 0){
            AlertMessage.show(this, "Error", "Quantity must be greater than zero");
            return;
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserID(userID);
        cartItem.setProductName(productName);
        cartItem.setPrice(price);
        cartItem.setQuantity(quantity);

        if(dboperations.addToCart(cartItem)){
            AlertMessage.show(this, "Success", "Product added to cart");

        }
        else{
            AlertMessage.show(this, "Error", "Failed to add product to cart");
        }
    }
}