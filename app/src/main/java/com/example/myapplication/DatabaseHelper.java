package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserData.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "EMAIL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); // Version 1 for initial DB creation
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table SQL query
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table if it exists and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert data into the database
    public boolean insertData(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase(); // Open DB for writing
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);

        long result = db.insert(TABLE_NAME, null, contentValues); // Insert data
        db.close(); // Close the DB after insertion

        return result != -1; // Return true if insertion was successful, false otherwise
    }

    // Method to get all data from the database
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase(); // Open DB for reading
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null); // Return cursor with all data
    }

    // Method to update data in the database
    public boolean updateData(String id, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase(); // Open DB for writing
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);

        int result = db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id}); // Update data
        db.close(); // Close the DB after updating

        return result > 0; // Return true if update was successful, false otherwise
    }

    // Method to delete data from the database
    public int deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase(); // Open DB for writing
        int rowsDeleted = db.delete(TABLE_NAME, COL_1 + " = ?", new String[]{id}); // Delete data
        db.close(); // Close the DB after deletion

        return rowsDeleted; // Return number of rows deleted
    }
}
