package com.magento.dom.model;

import com.intellij.util.xml.DomElement;
import com.magento.dom.model.page.Page;

import java.util.List;

public interface PageXmlRoot extends DomElement {
    List<Page> getPages();
}
