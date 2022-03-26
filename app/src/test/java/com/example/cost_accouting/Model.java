package com.example.cost_accouting;

/*
 * Decompiled with CFR 0.0.
 *
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.os.IBinder
 *  android.util.Log
 *  android.view.View
 *  android.view.Window
 *  android.view.inputmethod.InputMethodManager
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Calendar
 *  java.util.List
 *  java.util.function.Consumer
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.cost_accouting.database.DBHelper;
import com.example.cost_accouting.data_classes.Element;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Model {

    public String getPreviousDate(Character divider){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        month--;
        if (month == 0){
            year--;
            month = 12;
        }
        return year + divider.toString() + month;
    }

    public String toTwoNumbersAfterDecimalPoint(double number) {
        String num = String.format("%.2f", number);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num.length(); ++i)
        {
            if (num.charAt(i) == ',')
            {
                result.append(".");
            }
            else
            {
                result.append(num.charAt(i));
            }

        }
        return result.toString();
    }
}


