/*
 * Decompiled with CFR 0.0.
 *
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.ArrayAdapter
 *  android.widget.Button
 *  android.widget.TextView
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.AMiMa.cost_accounting.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.callbacks.MoveToAnotherActivity;
import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.database.DBHelper;
import com.AMiMa.cost_accounting.ui.Products;

import com.AMiMa.cost_accounting.ui.MenuSettings;

import java.util.ArrayList;
import java.util.List;

public class AdapterForMain extends RecyclerView.Adapter<AdapterForMain.ViewHolder> implements CheckBox.OnCheckedChangeListener, View.OnClickListener {

    public AdapterForMain(Context context, List<Element> data, MenuSettings menuSettings, MoveToAnotherActivity.fromSections moveToAnotherActivity) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.menuSettings = menuSettings;
        this.moveToAnotherActivity = moveToAnotherActivity;
    }

    private final LayoutInflater inflater;
    private final List<Element> data;
    private final MoveToAnotherActivity.fromSections moveToAnotherActivity;
    private final MenuSettings menuSettings;
    private final List<LinearLayout> listItems = new ArrayList<>();
    private final List<CheckBox> listCheckboxes = new ArrayList<>();
    private final List<CompoundButton> listCheckedCheckboxes = new ArrayList<>();

    @NonNull
    @Override
    public AdapterForMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_for_sections, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterForMain.ViewHolder holder, int position) {
        Element element = data.get(position);
        holder.nameSection.setText(element.getName());
        holder.spentAll.setText(element.getPrice());
        holder.nameSection.setId(position);
        holder.selectSection.setId(position);
        holder.selectSection.setOnCheckedChangeListener(this);
        holder.item.setId(position);
        holder.item.setOnClickListener(this);
        if (listItems.size() != data.size()){
            listItems.add(holder.item);
            listCheckboxes.add(holder.selectSection);
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
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            listCheckedCheckboxes.add(compoundButton);
        } else {
            listCheckedCheckboxes.remove(compoundButton);
        }
        menuSettings.onCheckedChanged(compoundButton, checked);
    }

    @Override
    public void onClick(View view) {
        if (listCheckedCheckboxes.size() == 0) {
            DBHelper.temp_name = data.get(view.getId()).getName();
            moveToAnotherActivity.moveTo(Products.class);
            return;
        }
        CheckBox checkBox = listCheckboxes.get(view.getId());
        checkBox.setChecked(!checkBox.isChecked());
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView spentAll, nameSection;
        private final CheckBox selectSection;
        private final LinearLayout item;
        ViewHolder(View view){
            super(view);
            nameSection = (TextView) view.findViewById(R.id.nameSection);
            spentAll = (TextView) view.findViewById(R.id.spentAll);
            selectSection = (CheckBox) view.findViewById(R.id.selectSection);
            item = view.findViewById(R.id.itemSection);
        }
    }
}

