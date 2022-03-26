package com.AMiMa.cost_accounting.callbacks;

import com.AMiMa.cost_accounting.data_classes.Element;

import java.util.List;

public interface View {
    public void setData(List<Element> data);

    public void addElementInData(Element element);

}
