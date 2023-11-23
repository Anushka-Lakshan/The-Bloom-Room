package com.example.thebloomroom.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thebloomroom.AlertMessage;
import com.example.thebloomroom.R;
import com.example.thebloomroom.models.Category;
import com.example.thebloomroom.models.CategoryDBModel;
import com.example.thebloomroom.models.DBoperations;
import com.example.thebloomroom.models.Product;
import com.example.thebloomroom.models.ProductDBModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Add_product extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner CategorySpinner;
    ImageView ProductImage;
    EditText ProductName, ProductPrice;

    ArrayList<Category> AllCategories = new ArrayList<Category>();
    byte[] imagebytes;

    int selectedCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ProductImage = findViewById(R.id.AP_image);
        ProductName = findViewById(R.id.AP_name);
        ProductPrice = findViewById(R.id.AP_price);

        //populate spinner
        CategorySpinner = findViewById(R.id.CategorySpinner);

        CategoryDBModel categoryDBModel = new CategoryDBModel(this);
        AllCategories = categoryDBModel.getAll();

        String[] categories = new String[AllCategories.size()];

        if(AllCategories.size() == 0){
            categories[0] = "No Categories to select";
        }

        for(int i = 0; i < AllCategories.size(); i++){
            categories[i] =  AllCategories.get(i).getId()+" : "+AllCategories.get(i).getName();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(arrayAdapter);

        CategorySpinner.setOnItemSelectedListener(this);
    }

    public void selectImage(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        i.putExtra("crop", true);
        i.putExtra("outputX", 150);
        i.putExtra("outputY", 150);
        i.putExtra("aspectX", 1);
        i.putExtra("aspectY", 1);
        i.putExtra("scale", true);
        i.putExtra("scaleUpIfNeeded", true);
        i.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(i, "Select Product Image"), 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 111 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            byte[] CompressedImagebytes = compressAndResizeImage(uri);
            ProductImage.setImageBitmap(BitmapFactory.decodeByteArray(CompressedImagebytes, 0, CompressedImagebytes.length));

            this.imagebytes = CompressedImagebytes;

//            try {
//
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 20, arrayOutputStream);
//                imagebytes = arrayOutputStream.toByteArray();
//                ProductImage.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                Log.e("Error", e.getMessage());
//            }
        }
    }

    public void addProduct(View view) {
        String error = "";

        String nameText = ProductName.getText().toString();
        String priceString = ProductPrice.getText().toString();

        float priceText = 0;

        if (nameText.isEmpty()) {
            error = "Name is required.";
        }

        if (selectedCategoryId == 0) {
            error += "\nCategory is required.";
        }

        if (imagebytes == null) {
            error += "\nImage is required.";
        }

        try {
            priceText = Float.parseFloat(priceString);
            if (priceText <= 0) {
                error += "\nPrice should be greater than zero.";
            }
        } catch (NumberFormatException e) {
            error += "\nPrice is required and should be a valid number.";
        }

        if (error.length() > 0) {
            AlertMessage.show(this, "Warning", error);
            return;
        }
        else {
            Product product = new Product();
            product.setName(nameText);
            product.setPrice(priceText);
            product.setImage(imagebytes);
            product.setCategoryId(selectedCategoryId);


            DBoperations dboperations = new DBoperations(this);
            long result = dboperations.insertProduct(product);

            if(result > 0){
                AlertMessage.show(this, "Success", "Product inserted successfully");

            }else {
                AlertMessage.show(this, "Error", "Failed to insert product");
            }
        }


    }







    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.CategorySpinner){

            selectedCategoryId = Integer.parseInt(adapterView.getItemAtPosition(i).toString().split(" : ")[0]);
            Toast.makeText(this, "Selected Category ID : " + selectedCategoryId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //image compression methods

    public byte[] compressAndResizeImage(Uri uri) {
        Bitmap bitmap = null;
        try {
            // Get the original bitmap from the URI
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // Just decode the bounds, not the whole image
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

            // Calculate the inSampleSize to resize the image to 400x400
            options.inSampleSize = calculateInSampleSize(options, 400, 400);

            // Decode the image with the calculated sample size
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

            // Compress the bitmap to reduce its size
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Adjust quality as needed

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bitmap != null) {
                bitmap.recycle(); // Recycle the bitmap to free up memory
            }
        }
        return null;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}