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

        String sql4 = "CREATE TABLE IF NOT EXISTS cart (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user INTEGER NOT NULL, productName VARCHAR(255) NOT NULL, price FLOAT NOT NULL, quantity INTEGER NOT NULL,productID INTEGER NOT NULL);";

        try {
            sqLiteDatabase.execSQL(sql1);
            sqLiteDatabase.execSQL(sql2);
            sqLiteDatabase.execSQL(sql3);
            sqLiteDatabase.execSQL(sql4);
        }
        catch (SQLException e){
            Log.e("SQL error", "Error creating 'categories' table: " + e.getMessage());
        }

        String sqlAddAdmin = "INSERT INTO users (name, email, password, address, tel, role) VALUES" +
                " ('admin', 'admin@admin', '123', 'nowhere', '123456', 'admin');";

        try {
            sqLiteDatabase.execSQL(sqlAddAdmin);
        }
        catch (SQLException e){
            Log.e("SQL error", "Error creating 'users' table: " + e.getMessage());
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

    public boolean checkEmail(String email){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM users WHERE email = " + "'" + email + "';";

        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
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



    public ArrayList<Product> getAllProducts(){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT products.*, categories.name AS category_name FROM products " +
                "INNER JOIN categories ON products.category = categories.id;";
        Cursor cursor = database.rawQuery(sql, null);

        ArrayList<Product> products = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getFloat(2));
                product.setCategoryId(cursor.getInt(3));
                product.setImage(cursor.getBlob(4));
                product.setCategoryName(cursor.getString(5));

                products.add(product);

            } while (cursor.moveToNext());
        }else {
            return null;
        }

        cursor.close();
        return products;

    }

    public Product getProductById(int id) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT products.*, categories.name AS category_name FROM products " +
                "INNER JOIN categories ON products.category = categories.id WHERE products.id = " + id + ";";
        Cursor cursor = database.rawQuery(sql, null);

        Product product = null;

        if (cursor != null && cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setPrice(cursor.getFloat(2));
            product.setCategoryId(cursor.getInt(3));
            product.setImage(cursor.getBlob(4));
            product.setCategoryName(cursor.getString(5));
        }

        if (cursor != null) {
            cursor.close();
        }

        return product;
    }

    //cart operations

    public boolean addToCart(CartItem cartItem){

        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("user", cartItem.getUserID());
        contentValues.put("productName", cartItem.getProductName());
        contentValues.put("price", cartItem.getPrice());
        contentValues.put("quantity", cartItem.getQuantity());
        contentValues.put("productID", cartItem.getProductID());

        long result = database.insert("cart", null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<CartItem> getCart(int userID){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM cart WHERE user = " + userID + ";";

        Cursor cursor = database.rawQuery(sql, null);
        ArrayList<CartItem> cartItems = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                CartItem cartItem = new CartItem();
                cartItem.setId(cursor.getInt(0));
                cartItem.setUserID(cursor.getInt(1));
                cartItem.setProductName(cursor.getString(2));
                cartItem.setPrice(cursor.getFloat(3));
                cartItem.setQuantity(cursor.getInt(4));
                cartItem.setProductID(cursor.getInt(5));
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }else {
            return null;
        }

        cursor.close();
        return cartItems;

    }

    public void deleteCartItem(int userID, int productID){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "DELETE FROM cart WHERE user = " + userID + " AND productID = " + productID + ";";

        try {
            database.execSQL(sql);

        }
        catch (SQLException e){
            Log.d("SQL error", e.getMessage());

        }
    }

    public void clearCart(int userID){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "DELETE FROM cart WHERE user = " + userID + ";";

        try {
            database.execSQL(sql);
        }
        catch (SQLException e){
            Log.d("SQL error", e.getMessage());
        }
    }

    public void updateQuantity(int userID, int productID, int quantity){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "UPDATE cart SET quantity = " + quantity + " WHERE user = " + userID + " AND productID = " + productID + ";";

        try {
            database.execSQL(sql);
        }
        catch (SQLException e){
            Log.d("SQL error", e.getMessage());
        }
    }


}
