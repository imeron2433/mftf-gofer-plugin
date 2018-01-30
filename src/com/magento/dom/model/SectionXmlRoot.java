package com.magento.dom.model;

import com.intellij.util.xml.DomElement;
import com.magento.dom.model.section.Section;

import java.util.List;

public interface SectionXmlRoot extends DomElement {
    List<Section> getSections();

}
