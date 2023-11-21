package com.example.thebloomroom.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductDBModel extends SQLiteOpenHelper {
    public ProductDBModel(@Nullable Context context) {
        super(context, "Bloom", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE products (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name VARCHAR(255) NOT NULL," +
                "    price FLOAT NOT NULL," +
                "    category INTEGER NOT NULL," +
                "    image BLOB);";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS products;";
        sqLiteDatabase.execSQL(sql);
    }

    public long insert(Product product) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", product.getName());
        contentValues.put("price", product.getPrice());
        contentValues.put("image", product.getImage());

        return database.insert("products", null, contentValues);
    }

    public ArrayList<Product> getAll(){

        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM products;";
        Cursor cursor = database.rawQuery(sql, null);

        ArrayList<Product> products = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getFloat(2));
                product.setImage(cursor.getBlob(3));

                products.add(product);

            } while (cursor.moveToNext());
        }else {
            return null;
        }

        cursor.close();
        return products;

    }
}
