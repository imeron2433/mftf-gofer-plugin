package com.magento.dom.model.data;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

import javax.annotation.Nullable;
import java.util.List;

public interface Entity extends DomElement {
    List<Data> getDatas();

    @Nullable
    @Attribute("name")
    GenericAttributeValue<String> getEntityName();
}
