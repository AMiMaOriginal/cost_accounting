/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.ArrayAdapter
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  androidx.appcompat.app.AppCompatActivity
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.List
 */
package com.AMiMa.cost_accounting.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.database.DBHelper;
import com.AMiMa.cost_accounting.logic.WorkWithData;
import com.AMiMa.cost_accounting.ui.adapters.AdapterForHistory;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private List<String> listProducts = new ArrayList<>();
    private List<String> listPrices = new ArrayList<>();

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_history);

        initToolbar();

        initData();

        initRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        AdapterForHistory adapter = new AdapterForHistory(listProducts, listPrices, this);
        RecyclerView recyclerView = findViewById(R.id.listDataInHistory);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initData(){
        DBHelper dBHelper = new DBHelper(this);
        DBHelper.temp_name = "history";

        WorkWithData workWithData = new WorkWithData(dBHelper);
        List<Element> data = workWithData.getListElement();
        for (int i = data.size() - 1; i >= 0; i--){
            listProducts.add(data.get(i).getName());
            listPrices.add(data.get(i).getPrice());
        }
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle("История");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

