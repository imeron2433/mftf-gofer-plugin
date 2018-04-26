package com.magento.dom.model.test;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

public interface Test extends DomElement
{
    @Attribute("name")
    GenericAttributeValue<String> getName();
}
