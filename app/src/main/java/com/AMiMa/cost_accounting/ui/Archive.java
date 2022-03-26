/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  androidx.appcompat.app.AppCompatActivity
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.AMiMa.cost_accounting.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.database.DBHelper;
import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.logic.WorkWithData;
import com.AMiMa.cost_accounting.ui.adapters.AdapterForArchive;

import java.util.ArrayList;
import java.util.List;

public class Archive extends AppCompatActivity {

    private List<Element> data = new ArrayList<>();
    private DBHelper dbHelper;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_archive);

        Toolbar toolbar = findViewById(R.id.historyToolbar);
        toolbar.setTitle("Архив");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        WorkWithData workWithData = new WorkWithData(dbHelper);

        AdapterForArchive adapter = new AdapterForArchive(this, data);
        RecyclerView rv = findViewById(R.id.archive_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        rv.addItemDecoration(itemDecoration);
        rv.setAdapter(adapter);

        DBHelper.temp_name = "archive";
        SQLiteDatabase sQLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sQLiteDatabase.query(DBHelper.temp_name, new String[]{DBHelper.element, DBHelper.price}, null, null, null, null, null);
        int date = cursor.getColumnIndex(DBHelper.element);
        int spentMoney = cursor.getColumnIndex(DBHelper.price);
        if (cursor.moveToFirst()) {
            do {
                data.add(new Element(cursor.getString(date),
                        workWithData.toTwoNumbersAfterDecimalPoint(Float.parseFloat(cursor.getString(spentMoney))).toString())); //WTF?!
            } while (cursor.moveToNext());
        }
        adapter.notifyDataSetChanged();
        sQLiteDatabase.close();
        cursor.close();
    }

    public void viewDataInHistory(View view) {
        String s = (data.get(view.getId())).getName();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < s.length(); i ++){
            if (s.charAt(i) == '-')
            {
                name.append('_');
            }else{
                name.append(s.charAt(i));
            }
        }
        DBHelper.temp_name = "D" + name;
        startActivity(new Intent(this, Graph.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}

