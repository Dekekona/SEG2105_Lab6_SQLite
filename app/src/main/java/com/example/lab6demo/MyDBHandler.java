package com.example.lab6demo;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;


public class MyDBHandler extends SQLiteOpenHelper {

    // Defining the schema
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_SKU = "SKU";


    // This is the constructor
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Override onCreate() method to create the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_SKU + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    // Override onUpgrade() method to drop old tables and create new ones
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // Method to add to a database
    public void addProduct(Product product) {

        // Get the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where the column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_SKU, product.getSku());

        // Insert
        db.insert(TABLE_PRODUCTS, null, values);

        // Close
        db.close();

    }

    // Method to read from a database
    public Product findProduct(String productname) {

        // Getting reference to readable database
        SQLiteDatabase db = this.getReadableDatabase();

        // Run your query
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productname + "\"";
        Cursor cursor = db.rawQuery(query, null);

        // Create object and then to get results
        Product product = new Product();

        if (cursor.moveToFirst()) {
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setSku(Integer.parseInt(cursor.getString(2)));
        } else {
            product = null;
        }
        db.close();
        return product;
    }

    // Method to delete information from the database
    public boolean deleteProduct(String productname) {

        // Declare
        boolean result = false;

        // Getting reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Run your query and delete
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = \"" + productname + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String idStr = cursor.getString(0);
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = " + idStr, null);
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }




}
