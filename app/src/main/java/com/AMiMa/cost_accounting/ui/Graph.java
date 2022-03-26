/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.os.Bundle
 *  android.view.View
 *  androidx.appcompat.app.AppCompatActivity
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.AMiMa.cost_accounting.ui;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.database.DBHelper;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph extends AppCompatActivity {

    private List<Entry> prices = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private Chart chart;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_graph);

        chart = findViewById(R.id.charta);

        initData();

        initChart();
    }

    private void initChart(){
        PieDataSet pieDataSet = new PieDataSet(prices, null);
        pieDataSet.setColors(getListColors());
        chart.setData(new PieData(labels, pieDataSet));
        chart.getLegend().setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        chart.setDescription("");
        chart.invalidate();
    }

    private void initData(){
        Cursor cursor = new DBHelper(this).getReadableDatabase().query(DBHelper.temp_name, new String[]{DBHelper.element, DBHelper.price}, null, null, null, null, null);
        int name = cursor.getColumnIndex(DBHelper.element);
        int price = cursor.getColumnIndex(DBHelper.price);
        if (cursor.moveToFirst()) {
            do {
                if (Float.parseFloat(cursor.getString(price)) != 0){
                    prices.add(new Entry(Float.parseFloat(cursor.getString(price)), 1));
                    labels.add(cursor.getString(name));
                }
            } while (cursor.moveToNext());
        }
    }

    private List<Integer> getListColors(){
        List<Integer> colors = new ArrayList<>();
        for (int i : ColorTemplate.COLORFUL_COLORS){
            colors.add(i);
        }
        for (int i : ColorTemplate.JOYFUL_COLORS){
            colors.add(i);
        }
        for (int i : ColorTemplate.LIBERTY_COLORS){
            colors.add(i);
        }
        for (int i : ColorTemplate.PASTEL_COLORS){
            colors.add(i);
        }
        for (int i : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(i);
        }
        return colors;
    }
}

