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
    private static EntityNodeResolver ENTITY_NODE_RESOLVER = new EntityNodeResolver();

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Data")
                && psiElement.getText().equals("entity")) {
            XmlAttribute attribute = ((XmlTagImpl)psiElement.getParent()).getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();

            if (ENTITY_NODE_RESOLVER.entityNameIsDuplicate(attributeValue)) {
                annotationHolder.createErrorAnnotation(psiElement.getParent(), "The Entity 'name' must be unique.");
            }
        }
    }
}
