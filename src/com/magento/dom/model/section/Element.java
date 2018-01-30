package com.magento.dom.model.section;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface Element extends DomElement
{
    @Attribute("name")
    GenericAttributeValue<String> getName();
}
