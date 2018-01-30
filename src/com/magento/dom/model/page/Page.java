package com.magento.dom.model.page;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface Page extends DomElement
{
    @Attribute("name")
    GenericAttributeValue<String> getName();
}
