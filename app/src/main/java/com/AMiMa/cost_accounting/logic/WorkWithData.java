/*
 * Decompiled with CFR 0.0.
 *
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.os.IBinder
 *  android.util.Log
 *  android.view.View
 *  android.view.Window
 *  android.view.inputmethod.InputMethodManager
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.Calendar
 *  java.util.List
 *  java.util.function.Consumer
 */
package com.AMiMa.cost_accounting.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.database.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/** Этот класс отвечает за всю работу с базой данных и с уже имеющимися данными в приложении. */
public class WorkWithData {

    private final DBHelper dbHelper;

    public WorkWithData(DBHelper dBHelper) {
        this.dbHelper = dBHelper;
    }

    /** Проходится по всем таблицам базы данных, где хранятся записи о потраченных деньгах
     * на категории товаров и конкретные товары, обнуляя их. Перед началом работы заносит переданный список в архив.
     * @param list список разделов. Именно он нужен для полного сброса*/
    public void resetPreparation(List<Element> list) {
        addInArchive(list);
        list.forEach(element -> {
            DBHelper.temp_name = element.getName();
            reset();
        });
        DBHelper.temp_name = DBHelper.mainTable;
        reset();
    }

    private void reset() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.price, "0.0");
        SQLiteDatabase sQLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.query(DBHelper.temp_name, new String[]{DBHelper.price}, null, null, null, null, null);
        int price = cursor.getColumnIndex(DBHelper.price);
        if (cursor.moveToFirst()) {
            do {
                sQLiteDatabase.update(DBHelper.temp_name, contentValues, DBHelper.price + " = ?", new String[]{cursor.getString(price)});
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
    }

    private void addInArchive(List<Element> list) {
        String date = getPreviousDate('-');
        String nameTable = "D" + getPreviousDate('_');
        float spentAll = getSpentAllMoney(list);
        if (spentAll <= 0){
            return;
        }
        DBHelper.temp_name = "archive";
        SQLiteDatabase sQLiteDatabase = dbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.price, spentAll);
        contentValues.put(DBHelper.element, date);
        sQLiteDatabase.insert(DBHelper.temp_name, null, contentValues);
        sQLiteDatabase.close();

        DBHelper.temp_name = nameTable;
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        for (int i = 0; i < list.size(); i++){
            if (Float.parseFloat(list.get(i).getPrice()) == 0){
                return;
            }
            contentValues1.put(DBHelper.element, list.get(i).getName());
            contentValues1.put(DBHelper.price, list.get(i).getPrice());
            sql.insert(DBHelper.temp_name, null, contentValues1);
        }
        sql.close();
    }

    /** Возвращает год и предыдущий месяц
     * @param divider нужна чтобы разделить год и месяц. */
    public String getPreviousDate(Character divider){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        month--;
        if (month == 0){
            year--;
            month = 12;
        }
        return year + divider.toString() + month;
    }

    /** Создаёт таблицу с переданным названием и возвращает элемент с этим же названием, в случае неудачи возвращает null. */
    public Element addElement(String name, Context context) {
        SQLiteDatabase sQLiteDatabase = dbHelper.getReadableDatabase();
        try {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + name + " (_id integer primary key, elementList text, priceElement text)");
        } catch (Exception e){
            Toast toast = Toast.makeText(context, "Неккоректное название. Не используйте пробелы, цифры и спец. символы", Toast.LENGTH_LONG);
            toast.show();
            return null;
        }

        if (isNotRepeat(DBHelper.temp_name, name) && isNotRepeat(DBHelper.mainTable, name)) {
            return finishAddElement(name);
        } else {
            Toast toast = Toast.makeText(context, "Такое название уже существует", Toast.LENGTH_LONG);
            toast.show();
        }

        return null;
    }

    private Boolean isNotRepeat(String tableName, String nameElement){
        boolean isNotRepeat = true;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{DBHelper.element}, null, null, null, null, null);
        int names = cursor.getColumnIndex(DBHelper.element);
        if (cursor.moveToFirst()) {
            do {
                if (nameElement.equals(cursor.getString(names))) {
                    isNotRepeat = false;
                    break;
                }
            } while (cursor.moveToNext());
        }
        return isNotRepeat;
    }

    private Element finishAddElement(String name) {
        SQLiteDatabase sQLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.element, name);
        contentValues.put(DBHelper.price, "0.0");
        sQLiteDatabase.insert(DBHelper.temp_name, null, contentValues);
        sQLiteDatabase.close();
        return new Element(name, "0.0");
    }

    /** Удаляет таблицу с её подкатегорией. */
    public void removeElement(String name) {
        SQLiteDatabase sQLiteDatabase = dbHelper.getWritableDatabase();
        sQLiteDatabase.delete(DBHelper.temp_name, DBHelper.element + " = ?", new String[]{name});
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + name);
        sQLiteDatabase.close();
    }

    /** @param data массив в котором нужно искать
     *  @param search что искать*/
    public List<Element> searchElement(String search, List<Element> data) {
        List<Element> result = new ArrayList<>();
        for (Element i : data){
            if (i.getName().contains(search.toUpperCase(Locale.ROOT))){
                result.add(i);
            }
        }
        return result;
    }

    public String toTwoNumbersAfterDecimalPoint(double number) {
        String num = String.format("%.2f", number);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < num.length(); ++i)
        {
            if (num.charAt(i) == ',')
            {
                result.append(".");
            }
            else
            {
                result.append(num.charAt(i));
            }

        }
        return result.toString();
    }

    /** Ищет в указанной таблице переданное название в столбце DBHelper.element, после обновляет в этой строке DBHelper.price
     * на переданное значение. */
    public void updateTable(String nameUpdate, String priceUpdate, String nameTable) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.price, priceUpdate);
        sqLiteDatabase.update(nameTable, contentValues, DBHelper.element + " = ?", new String[]{nameUpdate});
        sqLiteDatabase.close();
    }

    /** Возвращает список элементов в таблице, название которой в данный момент присвоено DBHelper.temp_name */
    public List<Element> getListElement(){
        List<Element> result = new ArrayList<>();
        SQLiteDatabase sQLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sQLiteDatabase.query(DBHelper.temp_name, new String[]{DBHelper.element, DBHelper.price}, null, null, null, null, null);
        int name = cursor.getColumnIndex(DBHelper.element);
        int price = cursor.getColumnIndex(DBHelper.price);
        if (cursor.moveToFirst()) {
            do {
                result.add(new Element(cursor.getString(name), toTwoNumbersAfterDecimalPoint(cursor.getFloat(price))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sQLiteDatabase.close();
        return result;
    }

    public Float getSpentAllMoney(List<Element> data){
        float price = 0.0f;
        for (Element i : data)
        {
            price += Float.parseFloat(i.getPrice());
        }
        return Float.parseFloat(toTwoNumbersAfterDecimalPoint(price));
    }
}

