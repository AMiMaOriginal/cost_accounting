/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.KeyEvent
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnKeyListener
 *  android.view.Window
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.SearchView
 *  android.widget.SearchView$OnQueryTextListener
 *  androidx.appcompat.app.AppCompatActivity
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.AMiMa.cost_accounting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.database.DBHelper;
import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.logic.Presenter;
import com.AMiMa.cost_accounting.callbacks.CallbackForWorkWithMenu;
import com.AMiMa.cost_accounting.ui.adapters.AdapterForProduct;
import com.AMiMa.cost_accounting.callbacks.MoveToAnotherActivity;

import java.util.ArrayList;
import java.util.List;

public class Products extends AppCompatActivity implements View.OnClickListener, com.AMiMa.cost_accounting.callbacks.View,
        CallbackForWorkWithMenu, MoveToAnotherActivity.fromProducts, SearchView.OnCloseListener {

    private DBHelper dbHelper;
    private RecyclerView listData;
    private SearchView searchProduct;
    private Menu menu;
    private Toolbar toolbar;
    private TextView titleToolbar;
    private Button addProduct;
    private MenuSettings menuSettings;

    public static AdapterForProduct adapterProduct;
    public static List<Element> dataProduct = new ArrayList<>();
    public static String nameThisSection;
    public static Presenter workWithData;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_products);

        nameThisSection = DBHelper.temp_name;
        dbHelper = new DBHelper(this);
        workWithData = new Presenter(dbHelper, this);

        initToolbar();

        addProduct = findViewById(R.id.addProduct);
        addProduct.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product, menu);
        this.menu = menu;
        initAdapter();
        searchProduct = (SearchView) menu.findItem(R.id.searchProduct).getActionView();
        searchProduct.setOnCloseListener(this);
        searchProduct.setOnSearchClickListener(this);
        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextChange(String newText) {
                getListElement();

                workWithData.searchElement(newText, dataProduct);

                if (newText.trim().isEmpty()) {
                    getListElement();
                }
                adapterProduct.notifyDataSetChanged();
                return false;
            }

            public boolean onQueryTextSubmit(String text) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onClose() {
        titleToolbar.setText("Раздел: " + nameThisSection);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        workWithData.updateTable(nameThisSection, workWithData.getSpentAllMoney(dataProduct).toString(), DBHelper.mainTable);
        super.onPause();
    }

    @Override
    public void onResume() {
        redrawingMenu();
        super.onResume();
    }

    @Override
    public void setData(List<Element> data){
        dataProduct.clear();
        dataProduct.addAll(data);
        adapterProduct.notifyDataSetChanged();
    }

    @Override
    public void addElementInData(Element element) {
        dataProduct.add(element);
        adapterProduct.notifyDataSetChanged();
    }

    @Override
    public void removeBaseMenuElement(){
        menu.removeItem(R.id.searchProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void redrawingMenu() {
        invalidateOptionsMenu();
        titleToolbar.setText("Раздел: " + nameThisSection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (menuSettings.getCountCheckedItems() == 0){
            super.onBackPressed();
        } else {
            menuSettings.clearChecked();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addProduct:
                Intent intent = new Intent(this, AddElement.class);
                intent.putExtra("isSection", false);
                moveTo(intent);
            case R.id.searchProduct:
                titleToolbar.setText("");
        }
    }

    @Override
    public void moveTo(Intent intent) {
        startActivity(intent);
    }

    private void getListElement() {
        dataProduct.clear();
        try {
            dataProduct.addAll(workWithData.getListElement());
        } catch (InterruptedException e){
            System.out.println(e);
        }
        adapterProduct.notifyDataSetChanged();
    }

    private void initAdapter(){
        menuSettings = new MenuSettings(menu, this, titleToolbar, workWithData, dataProduct, getLifecycle());
        adapterProduct = new AdapterForProduct(this, dataProduct, menuSettings, this);
        listData = (RecyclerView) findViewById(R.id.listProducts);
        listData.setLayoutManager(new LinearLayoutManager(this));
        listData.setAdapter(adapterProduct);

        getListElement();
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar_actionbar);
        titleToolbar = findViewById(R.id.titleToolbar);
        toolbar.setTitle("");
        titleToolbar.setText("Раздел: " + nameThisSection);
        titleToolbar.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleToolbar.setSingleLine(true);
        titleToolbar.setMarqueeRepeatLimit(-1);
        titleToolbar.setSelected(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addPrice(View view){
        Intent intent = new Intent(this, AddPrice.class);
        intent.putExtra("name", dataProduct.get(view.getId()).getName());
        intent.putExtra("id", view.getId());
        intent.putExtra("oldPrice", dataProduct.get(view.getId()).getPrice());
        intent.putExtra("nameThisSection", nameThisSection);
        startActivity(intent);
    }
}

