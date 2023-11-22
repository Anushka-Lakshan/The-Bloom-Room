package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.thebloomroom.Adaptors.GridAdaptor;
import com.example.thebloomroom.R;
import com.example.thebloomroom.databinding.ActivityMainBinding;
import com.example.thebloomroom.models.DBoperations;
import com.example.thebloomroom.models.Product;

import java.util.ArrayList;

public class Manage_Product extends AppCompatActivity {

    ActivityMainBinding binding;

    GridView MP_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_manage_product);

        MP_grid = findViewById(R.id.MP_grid);
        if (MP_grid == null) {
            Toast.makeText(this, "Grid not found", Toast.LENGTH_SHORT).show();
            return;
        }

        DBoperations dboperations = new DBoperations(this);

        try {
            ArrayList<Product> products = dboperations.getAllProducts();

            if (products == null || products.isEmpty()) {
                Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
            } else {
                GridAdaptor gridAdapter = new GridAdaptor(this, products);
                MP_grid.setAdapter(gridAdapter);

                MP_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i < products.size()) {
                            Toast.makeText(Manage_Product.this, "Product ID: " + products.get(i).getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.d("MPerror", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void gotoAddProduct(View view){
        Intent i = new Intent(this, Add_product.class);
        startActivity(i);
    }
}