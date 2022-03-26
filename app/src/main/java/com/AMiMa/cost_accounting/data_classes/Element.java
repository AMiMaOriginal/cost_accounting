/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.AMiMa.cost_accounting.data_classes;

public class Element {
    private String name;
    private String price;

    public Element(String name, String price) {
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

}

