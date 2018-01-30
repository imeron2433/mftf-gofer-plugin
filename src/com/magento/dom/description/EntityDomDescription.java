package com.magento.dom.description;

import com.intellij.util.xml.DomFileDescription;
import com.magento.dom.model.EntityXmlRoot;

public class EntityDomDescription extends DomFileDescription<EntityXmlRoot> {

    public static final String ROOT_TAG = "entities";

    public EntityDomDescription() {
        super(EntityXmlRoot.class, ROOT_TAG);
    }
}
