package com.example.thebloomroom.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thebloomroom.AlertMessage;
import com.example.thebloomroom.R;
import com.example.thebloomroom.models.Category;
import com.example.thebloomroom.models.CategoryDBModel;
import com.example.thebloomroom.models.DBoperations;

import java.util.ArrayList;

public class Manage_Category extends AppCompatActivity {

    EditText category_name;
    ListView category_list;

    DBoperations categoryDBModel;

    int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        category_name = findViewById(R.id.MC_name);
        category_list = findViewById(R.id.Category_List);

        categoryDBModel = new DBoperations(this);

        this.displayList();

        selectedIndex = 0;
    }

    public void insert(View view) {
        String name = category_name.getText().toString();

        if(name.isEmpty()){
            AlertMessage.show(this, "Error", "Category name cannot be empty");
            return;
        }



        Category category = new Category();
        category.setName(name);

        long result = categoryDBModel.insert(category,this);

        if(result > 0){
            AlertMessage.show(this, "Success", "Category inserted successfully");
            category_name.setText("");
            this.displayList();

        }else {
            AlertMessage.show(this, "Error", "Failed to insert category");
        }

    }

    public void update(View view) {

        if(selectedIndex == 0){
            AlertMessage.show(this, "Attention!", "Please select a category");
            return;
        }

        String name = category_name.getText().toString();

        if(name.isEmpty()){
            AlertMessage.show(this, "Error", "Category name cannot be empty");
            return;
        }

        Category category = new Category();
        category.setId(selectedIndex);
        category.setName(name);

        CategoryDBModel categoryModel = new CategoryDBModel(this);

        int result = categoryModel.updateCategory(category);

        if (result > 0) {
            AlertMessage.show(this, "Success", "Category updated successfully");
            category_name.setText("");
            selectedIndex = 0;
            this.displayList();
        } else {
            AlertMessage.show(this, "Error", "Failed to update category");
        }
    }

    public void displayList(){

        ArrayList<Category> categories = categoryDBModel.getAllCategories();


        if(categories != null){
            ArrayList<String> list = new ArrayList<>();
            String data = "";

            for(int i = 0; i < categories.size(); i++){
                data = "ID : " + categories.get(i).getId() + "\nName : " + categories.get(i).getName();
                list.add(data);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, list);

            category_list.setAdapter(arrayAdapter);

            category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Category user = categories.get(i);
                    Toast.makeText(getApplicationContext(), user.getName() + " selected", Toast.LENGTH_SHORT).show();
                    selectedIndex = user.getId();
                    category_name.setText(user.getName());
                }
            });
        }

    }

    public void Delete(View view) {
        if (selectedIndex == 0) {
            AlertMessage.show(this, "Attention!", "Please select a category");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm!")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform the operation if the user clicks "Yes"
                        DeleteCategory();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // If the user clicks "No", display a toast message
                        Toast.makeText(getApplicationContext(), "Operation canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    public void DeleteCategory(){
        CategoryDBModel categoryDBModel = new CategoryDBModel(this);

        int result = categoryDBModel.delete(selectedIndex);

        if(result > 0){
            AlertMessage.show(this, "Success", "Category deleted successfully");
            selectedIndex = 0;
            this.displayList();
            category_name.setText("");
        }else {
            AlertMessage.show(this, "Error", "Failed to delete category");
        }
    }


}