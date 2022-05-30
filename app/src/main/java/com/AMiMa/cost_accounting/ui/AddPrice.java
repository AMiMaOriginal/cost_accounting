package com.AMiMa.cost_accounting.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.database.DBHelper;
import com.AMiMa.cost_accounting.data_classes.Element;

public class AddPrice extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, View.OnKeyListener {

    private EditText inputPrice;
    private String newPrice;
    private DBHelper dbHelper;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);

        initToolbar();

        inputPrice = findViewById(R.id.priceElement);
        inputPrice.setOnKeyListener(this);

        inputPrice.requestFocus();
        showOrHideKeyboard();

        dbHelper = new DBHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MenuSettings.DONE, 0, "Готово")
                .setOnMenuItemClickListener(this)
                .setIcon(R.mipmap.done)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        showOrHideKeyboard();
        addPrice();
        return false;
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            addPrice();
            return true;
        }
        return false;
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle("");
        titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText("Новый расход на " + getIntent().getStringExtra("name"));
        titleToolbar.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleToolbar.setSingleLine(true);
        titleToolbar.setMarqueeRepeatLimit(-1);
        titleToolbar.setSelected(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addPrice(){
        newPrice = inputPrice.getText().toString();
        if (!newPrice.trim().isEmpty()){
            String name = getIntent().getStringExtra("name");
            String price = getIntent().getStringExtra("oldPrice");

            try {
                double d = Float.parseFloat(price) + Float.parseFloat(newPrice);
                String result = Products.workWithData.toTwoNumbersAfterDecimalPoint(d);
                Products.workWithData.updateTable(name, result, Products.nameThisSection);
                Products.dataProduct.set(getIntent().getIntExtra("id", 0), new Element(name, result));
                Products.adapterProduct.notifyDataSetChanged();

                addInHistory(name, newPrice);

                DBHelper.temp_name = getIntent().getStringExtra("nameThisSection");
                finish();
            } catch (Exception e){
                Toast toast = Toast.makeText(this, "Введено неккоректное значение", Toast.LENGTH_SHORT);
                toast.show();
                inputPrice.setText("");
            }
        }
    }

    private void showOrHideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void addInHistory(String name, String newPrice){
        DBHelper.temp_name = "history";
        SQLiteDatabase sQLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.element, name);
        contentValues.put(DBHelper.price, newPrice);
        sQLiteDatabase.insert(DBHelper.temp_name, null, contentValues);
        sQLiteDatabase.close();
        DBHelper.temp_name = Products.nameThisSection;
    }
}