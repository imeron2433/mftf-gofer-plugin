package com.magento.dom.description;

import com.intellij.util.xml.DomFileDescription;
import com.magento.dom.model.SectionXmlRoot;

public class SectionDomDescription extends DomFileDescription<SectionXmlRoot> {

    public static final String ROOT_TAG = "sections";

    public SectionDomDescription() {
        super(SectionXmlRoot.class, ROOT_TAG);
    }
}
