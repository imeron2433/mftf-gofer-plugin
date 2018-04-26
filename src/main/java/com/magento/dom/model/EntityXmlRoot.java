package com.magento.dom.model;

import com.intellij.util.xml.DomElement;
import com.magento.dom.model.data.Entity;

import java.util.List;

public interface EntityXmlRoot extends DomElement {
    List<Entity> getEntities();
}
