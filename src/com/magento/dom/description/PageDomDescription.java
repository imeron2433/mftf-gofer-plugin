package com.magento.dom.description;

import com.intellij.util.xml.DomFileDescription;
import com.magento.dom.model.PageXmlRoot;

public class PageDomDescription extends DomFileDescription<PageXmlRoot> {

    public static final String ROOT_TAG = "pages";

    public PageDomDescription() {
        super(PageXmlRoot.class, ROOT_TAG);
    }
}
