package com.magento.dom.model;

import com.intellij.util.xml.DomElement;
import com.magento.dom.model.test.Test;

import java.util.List;

public interface TestXmlRoot extends DomElement {
    List<Test> getTests();
}
