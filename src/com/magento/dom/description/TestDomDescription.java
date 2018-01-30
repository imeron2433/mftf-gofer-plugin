package com.magento.dom.description;

import com.intellij.util.xml.DomFileDescription;
import com.magento.dom.model.TestXmlRoot;

public class TestDomDescription extends DomFileDescription<TestXmlRoot> {

    public static final String ROOT_TAG = "tests";

    public TestDomDescription() {
        super(TestXmlRoot.class, ROOT_TAG);
    }
}
