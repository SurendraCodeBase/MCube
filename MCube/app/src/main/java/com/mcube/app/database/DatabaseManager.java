package com.mcube.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseManager {

    private static SQLiteDatabase db;

    public static SQLiteDatabase getDb(Context context) {
        if(db == null || !db.isOpen()) {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            db = databaseHelper.getWritableDatabase();
        }
        return db;
    }
}
