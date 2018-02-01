package com.magento.dom.model.page;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

import javax.annotation.Nullable;

public interface Page extends DomElement
{
    @Nullable
    @Attribute("name")
    GenericAttributeValue<String> getName();
}
