package com.example.thebloomroom.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.thebloomroom.AlertMessage;
import com.example.thebloomroom.views.Manage_Category;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DBoperations extends SQLiteOpenHelper {

    public DBoperations(@Nullable Context context) {
        super(context, "Bloom", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql1 = "CREATE TABLE users (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name VARCHAR(255) NOT NULL," +
                "    email VARCHAR(255) NOT NULL," +
                "    password VARCHAR(255) NOT NULL," +
                "    address VARCHAR(255)," +
                "    tel VARCHAR(20)," +
                "    role VARCHAR(50) NOT NULL" +
                ");";

        String sql2 = "CREATE TABLE categories (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(255) NOT NULL);";

        String sql3 = "CREATE TABLE products ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(255) NOT NULL, price FLOAT NOT NULL, category INTEGER NOT NULL, image BLOB);";

        try {
            sqLiteDatabase.execSQL(sql1);
            sqLiteDatabase.execSQL(sql2);
            sqLiteDatabase.execSQL(sql3);
        }
        catch (SQLException e){
            Log.e("SQL error", "Error creating 'categories' table: " + e.getMessage());
        }




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS users";
        sqLiteDatabase.execSQL(sql);

        sql = "DROP TABLE IF EXISTS categories;";
        sqLiteDatabase.execSQL(sql);
    }

    //User operations

    public long insert(User user){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());

        String hashedPassword = hashPassword(user.getPassword());
        if (hashedPassword == null) {
            return -1;
        }

        contentValues.put("password", hashedPassword);
        contentValues.put("address", user.getAddress());
        contentValues.put("tel", user.getTel());
        contentValues.put("role", user.getRole());

        return database.insert("users", null, contentValues);
    }



    public User login(String email, String password){

        SQLiteDatabase database = getReadableDatabase();
        String hashedPassword = hashPassword(password);

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        User user = null;

        Cursor cursor = database.rawQuery(sql, new String[]{email, hashedPassword});

        if (cursor.moveToFirst()){
            user = new User();
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setAddress(cursor.getString(4));
            user.setTel(cursor.getString(5));
            user.setRole(cursor.getString(6));
        }

        cursor.close();
        return user;
    }







    public String hashPassword(String password) {
        try {
            // Creating MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes(StandardCharsets.UTF_8));

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // Convert byte array into signum representation
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return null; // Return null if hashing fails
    }

    //category operations

    public long insert(Category category, Manage_Category instance){

        SQLiteDatabase database = getWritableDatabase();

            Log.d("Name", category.getName());

            String sql = "INSERT INTO categories (name) VALUES ('" + category.getName() + "');";

            try {
                database.execSQL(sql);
                return 1;
            }
            catch (SQLException e){
                Log.d("Name", e.getMessage());
                AlertMessage.show(instance, "Error!", e.getMessage());
                return -1;
            }

    }

    public ArrayList<Category> getAllCategories(){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM categories;";
        Cursor cursor = database.rawQuery(sql, null);

        ArrayList<Category> categories = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Category category = new Category();

                category.setId(cursor.getInt(0));
                category.setName(cursor.getString(1));

                categories.add(category);

            } while (cursor.moveToNext());
        }else {
            return null;
        }

        cursor.close();
        return categories;

    }

    //product operations

    public long insertProduct(Product product) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", product.getName());
        contentValues.put("price", product.getPrice());
        contentValues.put("image", product.getImage());
        contentValues.put("category", product.getCategoryId());

        return database.insert("products", null, contentValues);
    }


}
