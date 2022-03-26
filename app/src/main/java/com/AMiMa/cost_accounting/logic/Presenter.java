package com.AMiMa.cost_accounting.logic;

import android.content.Context;

import com.AMiMa.cost_accounting.database.DBHelper;
import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.callbacks.View;

import java.util.List;

public class Presenter {

    public Presenter(DBHelper dbHelper, View view) {
        model = new WorkWithData(dbHelper);
        this.view = view;
    }

    private WorkWithData model;
    private View view;

    public void reset(List<Element> data)
    {
        model.resetPreparation(data);
    }

    public void addElement(String name, Context context) {
        Element element = model.addElement(name, context);
        if (element != null) {
            view.addElementInData(element);
        }
    }

    public void removeElement(String name) {
        model.removeElement(name);
    }

    public void searchElement(String search, List<Element> data) {
        view.setData(model.searchElement(search, data));
    }

    public void updateTable(String nameUpdate, String priceUpdate, String nameTable) {
        model.updateTable(nameUpdate, priceUpdate, nameTable);
    }

    public List<Element> getListElement() throws InterruptedException {
        return model.getListElement();
    }

    public Float getSpentAllMoney(List<Element> data)
    {
        return model.getSpentAllMoney(data);
    }

    public String getPreviousDate(Character divider) { return model.getPreviousDate(divider); }

    public String toTwoNumbersAfterDecimalPoint(double number) { return model.toTwoNumbersAfterDecimalPoint(number); }

}
