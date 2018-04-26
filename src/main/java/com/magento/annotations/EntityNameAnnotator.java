package com.magento.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.magento.resolver.EntityNodeResolver;
import org.jetbrains.annotations.NotNull;

public class EntityNameAnnotator implements Annotator
{
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {

        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Data") && "entity".equals(psiElement.getText())) {
            XmlAttribute attribute = ((XmlTagImpl)psiElement.getParent()).getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();
            EntityNodeResolver ENTITY_NODE_RESOLVER = EntityNodeResolver.getInstance();

            if (ENTITY_NODE_RESOLVER.entityNameIsDuplicate(attributeValue)) {
                annotationHolder.createWeakWarningAnnotation(psiElement.getParent(), "This Entity 'name' is in use elsewhere.");
            }
        }
    }
}
