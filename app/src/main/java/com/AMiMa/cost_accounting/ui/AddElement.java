package com.AMiMa.cost_accounting.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AMiMa.cost_accounting.R;

import java.util.Locale;

public class AddElement extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, View.OnKeyListener {

    private EditText nameElement;
    private Boolean isSection;
    private TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_element);

        initToolbar();

        nameElement = findViewById(R.id.nameElement);
        nameElement.setOnKeyListener(this);

        isSection = getIntent().getBooleanExtra("isSection", true);

        nameElement.requestFocus();
        showOrHideKeyboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MenuSettings.DONE, 0, "Готово")
                .setOnMenuItemClickListener(this)
                .setIcon(R.mipmap.done)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        titleToolbar = findViewById(R.id.titleToolbar);
        toolbar.setTitle("");
        titleToolbar.setText("Добавить");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addElement(){
        String n = nameElement.getText().toString();
        if (isSection) {
            MainActivity.workWithData.addElement(n.toUpperCase(Locale.ROOT), this);
        } else {
            Products.workWithData.addElement(n.toUpperCase(Locale.ROOT), this);
        }
        finish();
    }

    private void showOrHideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == keyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
            addElement();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        showOrHideKeyboard();
        addElement();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}