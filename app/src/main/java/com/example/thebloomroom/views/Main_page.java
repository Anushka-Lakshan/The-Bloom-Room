package com.example.thebloomroom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thebloomroom.Adaptors.GridAdaptor;
import com.example.thebloomroom.R;
import com.example.thebloomroom.models.Category;
import com.example.thebloomroom.models.CategoryDBModel;
import com.example.thebloomroom.models.DBoperations;
import com.example.thebloomroom.models.Product;

import java.util.ArrayList;

public class Main_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner CategorySpinner;
    GridView gridView;

    int UserID;
    String UserName;

    ArrayList<Category> AllCategories = new ArrayList<Category>();

    String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        UserID = getIntent().getIntExtra("UserID", 0);
        UserName = getIntent().getStringExtra("user");
        gridView = findViewById(R.id.UserProducts);

        TextView welcome = findViewById(R.id.username);
        welcome.setText(UserName);

        //populate spinner

        CategorySpinner = findViewById(R.id.FilterSpinner);

        CategoryDBModel categoryDBModel = new CategoryDBModel(this);
        AllCategories = categoryDBModel.getAll();

        String[] categories;

        if (AllCategories.size() == 0) {
            categories = new String[]{"No Categories to select"};
        } else {
            categories = new String[AllCategories.size() + 1];
            categories[0] = "All";

            for (int i = 0; i < AllCategories.size(); i++) {
                categories[i + 1] = AllCategories.get(i).getName();
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(arrayAdapter);

        CategorySpinner.setOnItemSelectedListener(this);

        showProducts();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.FilterSpinner) {
            String selectedCategoryName = adapterView.getItemAtPosition(i).toString();
            this.selectedCategory = selectedCategoryName;
            Toast.makeText(this, "Selected Category: " + selectedCategoryName, Toast.LENGTH_SHORT).show();

            showProducts();
        }
    }

    public void showProducts(){
        DBoperations dboperations = new DBoperations(this);

        try {
            ArrayList<Product> products = dboperations.getAllProducts();

            ArrayList<Product> filteredProducts = new ArrayList<>();

            if(selectedCategory.equals("All")){
                filteredProducts = products;
            }else {
                for (Product product : products) {
                    if (product.getCategoryName().equals(selectedCategory)) {
                        filteredProducts.add(product);
                    }
                }
            }

            if (products == null || products.isEmpty()) {
                Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
            } else {
                GridAdaptor gridAdapter = new GridAdaptor(this, filteredProducts);
                gridView.setAdapter(gridAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i < products.size()) {
                            Toast.makeText(getApplicationContext(), "Selected Product: " + products.get(i).getName(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), Product_details.class);
                            intent.putExtra("UserID", UserID);
                            intent.putExtra("product", products.get(i).getId());
                            startActivity(intent);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.d("MPerror", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}