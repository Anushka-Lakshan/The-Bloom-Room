package com.example.thebloomroom.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.thebloomroom.AlertMessage;
import com.example.thebloomroom.views.Manage_Category;

import java.util.ArrayList;

public class CategoryDBModel extends SQLiteOpenHelper {

    public CategoryDBModel(@Nullable Context context) {
        super(context, "Bloom", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS categories (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name VARCHAR(255) NOT NULL" +
                "    );";

        try {
            sqLiteDatabase.execSQL(sql);
            Log.d("CategoryDBModel", "Table 'categories' created successfully");
        } catch (Exception e) {
            Log.e("CategoryDBModel", "Error creating 'categories' table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS categories;";
        sqLiteDatabase.execSQL(sql);
    }

    public long insert(Category category){

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", category.getName());


        return database.insert("categories", null, contentValues);
    }

    public ArrayList<Category> getAll(){

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

    public int updateCategory(Category category){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name",category.getName());


        return database.update("categories",contentValues,
                "id="+ category.getId(),null);
    }

    public int delete(int id){
        SQLiteDatabase database = getWritableDatabase();
        return database.delete("categories","id = " + id,null);
    }


}
