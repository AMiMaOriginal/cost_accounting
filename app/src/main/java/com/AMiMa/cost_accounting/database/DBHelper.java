/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteOpenHelper
 *  java.lang.Exception
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.AMiMa.cost_accounting.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String element = "elementList";
    public static final String id = "_id";
    public static final String mainTable = "sections";
    public static final String nameDB = "MyDB";
    public static final String price = "priceElement";
    public static String temp_name;

    public DBHelper(Context context) {
        super(context, nameDB, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE sections(_id integer primary key, elementList text, priceElement text)");
    }

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + temp_name + "(_id integer primary key, elementList text, priceElement text)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS sections");
        this.onCreate(sQLiteDatabase);
    }
}

