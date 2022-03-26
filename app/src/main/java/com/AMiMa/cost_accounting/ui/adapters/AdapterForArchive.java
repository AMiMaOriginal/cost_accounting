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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.data_classes.Element;

import java.util.List;

public class AdapterForArchive extends RecyclerView.Adapter<AdapterForArchive.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Element> data;

    public AdapterForArchive(Context context, List<Element> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public AdapterForArchive.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_for_archive, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterForArchive.ViewHolder holder, int position) {
        Element element = data.get(position);
        holder.dateInArchive.setText(element.getName());
        holder.priceInArchive.setText(element.getPrice());

        holder.dateInArchive.setId(position);
        holder.item.setId(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView priceInArchive, dateInArchive;
        private final RelativeLayout item;
        ViewHolder(View view){
            super(view);
            dateInArchive = (TextView) view.findViewById(R.id.dateInArchive);
            priceInArchive = (TextView) view.findViewById(R.id.priceInArchive);
            item = (RelativeLayout) view.findViewById(R.id.itemArchive);
        }
    }
}

