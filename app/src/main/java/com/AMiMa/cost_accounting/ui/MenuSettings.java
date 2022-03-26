package com.AMiMa.cost_accounting.ui;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.AMiMa.cost_accounting.R;
import com.AMiMa.cost_accounting.callbacks.CallbackForWorkWithMenu;
import com.AMiMa.cost_accounting.data_classes.Element;
import com.AMiMa.cost_accounting.logic.Presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuSettings implements MenuItem.OnMenuItemClickListener, LifecycleObserver {

    private final Menu menu;
    private final CallbackForWorkWithMenu callback;
    private final TextView titleToolbar;
    private final Presenter workWithData;
    private Map<Element, CompoundButton> listChecked = new HashMap<>();
    private List<Element> data;
    private List<LinearLayout> itemsList = new ArrayList<>();

    public static final int GROUP_ID = 1;
    public static final int DELETE = 1;
    public static final int CLEAR = 2;
    public static final int SELECTED = 3;
    public static final int DONE = 4;

    public MenuSettings(Menu menu, CallbackForWorkWithMenu callback, TextView titleToolbar, Presenter workWithData, List<Element> data, Lifecycle lifecycle){
        this.menu = menu;
        this.callback = callback;
        this.titleToolbar = titleToolbar;
        this.workWithData = workWithData;
        this.data = data;
        lifecycle.addObserver(this);
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        int id = compoundButton.getId();

        if (checked) {
            listChecked.put(data.get(id), compoundButton);
        } else {
            listChecked.remove(data.get(id), compoundButton);
        }

        if (listChecked.size() == 0) {
            callback.redrawingMenu();
        } else {
            updateMenuInMainActivity();
        }
    }

    private void updateMenuInMainActivity(){
        callback.removeBaseMenuElement();
        menu.removeGroup(GROUP_ID);
        menu.add(GROUP_ID, SELECTED, 0, "Выбрано: " + listChecked.size())
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(GROUP_ID, CLEAR, 0, "Очистить")
                .setIcon(R.mipmap.clear)
                .setOnMenuItemClickListener(this)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(GROUP_ID, DELETE, 0, "Удалить")
                .setIcon(R.mipmap.delete)
                .setOnMenuItemClickListener(this)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        titleToolbar.setText("");
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case DELETE:
                listChecked.forEach((element, compoundButton) -> workWithData.removeElement(element.getName()));
                clearChecked();
                break;
            case CLEAR:
                clearChecked();
                break;
        }
        return true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void clearChecked(){
        if (listChecked.size() != 0){
            listChecked.clear();
            callback.redrawingMenu();
        }
    }

    public int getCountCheckedItems(){
        return listChecked.size();
    }

    public void setItemsList(List<LinearLayout> data){
        itemsList.addAll(data);
    }
}
