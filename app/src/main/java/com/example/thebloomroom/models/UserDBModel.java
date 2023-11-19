package com.example.thebloomroom.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDBModel extends SQLiteOpenHelper {

    public UserDBModel(@Nullable Context context) {
        super(context, "Bloom", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE users (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name VARCHAR(255) NOT NULL," +
                "    email VARCHAR(255) NOT NULL," +
                "    password VARCHAR(255) NOT NULL," +
                "    address VARCHAR(255)," +
                "    tel VARCHAR(20),\n" +
                "    role VARCHAR(50) NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS users";
        sqLiteDatabase.execSQL(sql);
    }

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


}
