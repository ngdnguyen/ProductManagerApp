package com.example.productmanagerapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "ProductDB.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tblCategories(" +
                "PK_iCategoryID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sName TEXT)");
        db.execSQL("CREATE TABLE tblProducts(" +
                "PK_iProductID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sName TEXT, Price REAL, " +
                "FK_iCategoryID INTEGER, " +
                "FOREIGN KEY(FK_iCategoryID) REFERENCES tblCategories(PK_iCategoryID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tblProducts");
        db.execSQL("DROP TABLE IF EXISTS tblCategories");
        onCreate(db);
    }
}