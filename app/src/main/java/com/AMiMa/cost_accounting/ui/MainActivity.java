/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
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
 *  android.widget.TextView
 *  androidx.appcompat.app.AppCompatActivity
 *  java.io.FileInputStream
 *  java.io.FileNotFoundException
 *  java.io.FileOutputStream
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Calendar
 *  java.util.List
 */
package com.AMiMa.cost_accounting.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.callbacks.CallbackForWorkWithMenu;
import com.AMiMa.cost_accounting.database.DBHelper;

import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.logic.Presenter;

import com.AMiMa.cost_accounting.ui.adapters.AdapterForMain;
import com.AMiMa.cost_accounting.callbacks.MoveToAnotherActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.AMiMa.cost_accounting.callbacks.View, View.OnClickListener,
        CallbackForWorkWithMenu, SearchView.OnCloseListener, MoveToAnotherActivity.fromSections {

    private AdapterForMain adapterMain;
    private List<Element> dataMain = new ArrayList<>();
    private DBHelper dbHelper;
    private boolean firstLaunch;
    private boolean reset;
    private SharedPreferences sPref;
    private SearchView searchSection;
    private TextView spentMoney;
    private RecyclerView viewElement;
    private Button addSection;
    private Menu menu;
    private Toolbar toolbar;
    private CallbackForWorkWithMenu callback;
    private TextView titleToolbar;
    private MenuSettings menuSettings;

    public static Presenter workWithData;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sections);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText(R.string.app_name);

        callback = this;

        DBHelper.temp_name = DBHelper.mainTable;

        dbHelper = new DBHelper(this);
        workWithData = new Presenter(dbHelper, this);

        sPref = getPreferences(0);
        firstLaunch = sPref.getBoolean("firstLaunch", true);
        reset = sPref.getBoolean("reset", false);
        if (reset) {
            init();
            workWithData.reset(dataMain);
            resetOnCreate(false);
            wasReset();
        }
        checkNeedToReset();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addSection:
                moveTo(AddElement.class);
                break;
            case R.id.searchSection:
                titleToolbar.setText("");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        searchSection = (SearchView) menu.findItem(R.id.searchSection).getActionView();
        searchSection.setOnCloseListener(this);
        searchSection.setOnSearchClickListener(this);
        init();
        searchSection.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextChange(String newText) {
                dataMain.clear();
                DBHelper.temp_name = DBHelper.mainTable;
                getListElement();

                workWithData.searchElement(newText, dataMain);

                if (newText.trim().isEmpty()) {
                    getListElement();
                }
                adapterMain.notifyDataSetChanged();
                return false;
            }

            public boolean onQueryTextSubmit(String string2) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.history:
                moveTo(History.class);
                break;
            case R.id.graph:
                moveTo(Graph.class);
                break;
            case R.id.archive:
                moveTo(Archive.class);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onResume() {
        DBHelper.temp_name = DBHelper.mainTable;
        redrawingMenu();
        super.onResume();
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
    public boolean onClose() {
        titleToolbar.setText(R.string.app_name);
        return false;
    }

    @Override
    public void redrawingMenu() {
        invalidateOptionsMenu();
        titleToolbar.setText(R.string.app_name);
    }

    @Override
    public void removeBaseMenuElement() {
        menu.removeGroup(R.id.mainMenu);
    }

    @Override
    public void setData(List<Element> data){
        dataMain.clear();
        dataMain.addAll(data);
        adapterMain.notifyDataSetChanged();
    }

    @Override
    public void moveTo(Class to) {
        startActivity(new Intent(this, to));
    }

    @Override
    public void addElementInData(Element element) {
        dataMain.add(element);
        adapterMain.notifyDataSetChanged();
    }

    private void isFirstLaunchApp() {
        if (firstLaunch){
            SharedPreferences.Editor editor = sPref.edit();
            editor.putBoolean("firstLaunch", false);
            editor.commit();
        }
    }

    private void checkNeedToReset() {
        isFirstLaunchApp();
        String date = workWithData.getPreviousDate('-');
        Boolean wasReset = sPref.getBoolean(date, false);
        if (!wasReset && !firstLaunch){
            resetOnCreate(true);
            Notification.resetNotification(this);
        }else{
            wasReset();
        }
    }

    private void init(){
        menuSettings = new MenuSettings(menu, callback, titleToolbar, workWithData, dataMain, getLifecycle());
        adapterMain = new AdapterForMain(this, dataMain, menuSettings, this);
        viewElement = (RecyclerView) findViewById(R.id.listSections);
        viewElement.setLayoutManager(new LinearLayoutManager(this));
        viewElement.setAdapter(adapterMain);
        addSection = (Button) findViewById(R.id.addSection);
        addSection.setOnClickListener(this);
        spentMoney = (TextView) findViewById(R.id.spentAllMain);

        getListElement();
        getSpentAllMoney();
    }

    private void wasReset(){
        String date = workWithData.getPreviousDate('-');
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(date, true);
        editor.commit();
    }

    private void getListElement() {
        dataMain.clear();
        try {
            dataMain.addAll(workWithData.getListElement());
        } catch (InterruptedException e){
            System.out.println(e);
        }
        adapterMain.notifyDataSetChanged();
    }

    private void resetOnCreate(boolean needReset) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("reset", needReset);
        editor.commit();
    }

    private void getSpentAllMoney(){
        spentMoney.setText(workWithData.getSpentAllMoney(dataMain).toString());
    }
}

