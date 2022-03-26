/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  android.os.IBinder
 *  android.text.Editable
 *  android.text.TextWatcher
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnFocusChangeListener
 *  android.view.View$OnKeyListener
 *  android.view.ViewGroup
 *  android.view.Window
 *  android.view.inputmethod.InputMethodManager
 *  android.widget.ArrayAdapter
 *  android.widget.Button
 *  android.widget.EditText
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.AMiMa.cost_accounting.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.ui.AddPrice;
import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.ui.MenuSettings;
import com.AMiMa.cost_accounting.callbacks.MoveToAnotherActivity;
import com.AMiMa.cost_accounting.ui.Products;

import java.util.ArrayList;
import java.util.List;

public class AdapterForProduct extends RecyclerView.Adapter<AdapterForProduct.ViewHolder> implements CheckBox.OnCheckedChangeListener, View.OnFocusChangeListener,
        View.OnClickListener {

    private final LayoutInflater inflater;
    private final List<Element> data;
    private MenuSettings menuSettings;
    private List<LinearLayout> listItems = new ArrayList<>();
    private List<CheckBox> listCheckboxes = new ArrayList<>();
    private List<CompoundButton> listCheckedCheckboxes = new ArrayList<>();
    private MoveToAnotherActivity.fromProducts moveToAnotherActivity;
    private Context context;

    public AdapterForProduct(Context context, List<Element> data, MenuSettings menuSettings, MoveToAnotherActivity.fromProducts moveToAnotherActivity){
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.menuSettings = menuSettings;
        this.moveToAnotherActivity = moveToAnotherActivity;
    }

    @Override
    public AdapterForProduct.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_for_products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterForProduct.ViewHolder holder, int position) {
        Element element = data.get(position);
        holder.nameProduct.setText(element.getName());
        holder.priceProduct.setText(element.getPrice());

        holder.nameProduct.setId(position);
        holder.priceProduct.setId(position);
        holder.selectProduct.setId(position);
        holder.selectProduct.setOnCheckedChangeListener(this);
        holder.item.setId(position);
        holder.item.setOnClickListener(this);
        if (listItems.size() != data.size()){
            listItems.add(holder.item);
            listCheckboxes.add(holder.selectProduct);
            if (listItems.size() == data.size()){
                menuSettings.setItemsList(listItems);
                listItems.clear();
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!view.hasFocus()){
            EditText editText = view.findViewById(view.getId());
            editText.setText("");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            listCheckedCheckboxes.add(compoundButton);
        } else{
            listCheckedCheckboxes.remove(compoundButton);
        }
        menuSettings.onCheckedChanged(compoundButton, b);
    }

    @Override
    public void onClick(View view) {
        if (listCheckedCheckboxes.size() == 0){
            Intent intent = new Intent(context, AddPrice.class);
            intent.putExtra("name", data.get(view.getId()).getName());
            intent.putExtra("id", view.getId());
            intent.putExtra("oldPrice", data.get(view.getId()).getPrice());
            intent.putExtra("nameThisSection", Products.nameThisSection);
            moveToAnotherActivity.moveTo(intent);
            return;
        }
        CheckBox checkBox = listCheckboxes.get(view.getId());;
        if (checkBox.isChecked()){
            checkBox.setChecked(false);
        }else {
            checkBox.setChecked(true);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox selectProduct;
        private final TextView nameProduct, priceProduct;
        private final LinearLayout item;

        ViewHolder(View view) {
            super(view);
            selectProduct = view.findViewById(R.id.selectProduct);
            nameProduct = (TextView) view.findViewById(R.id.nameProduct);
            priceProduct = (TextView) view.findViewById(R.id.priceProduct);
            item = view.findViewById(R.id.addPrice);
        }
    }
}


