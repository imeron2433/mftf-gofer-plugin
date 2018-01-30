package com.magento.dom.model.data;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface Data extends DomElement {
    String getValue();

    @Attribute("key")
    GenericAttributeValue<String> getFieldName();
}
