package com.magento.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.magento.resolver.SectionNodeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class SectionNameAnnotator implements Annotator
{
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        SectionNodeResolver SECTION_NODE_RESOLVER = SectionNodeResolver.getInstance();

        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Section")
                && psiElement.getText().equals("section")) {
            XmlAttribute attribute = ((XmlTagImpl)psiElement.getParent()).getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();

            if (Collections.frequency(SECTION_NODE_RESOLVER.getSectionNames(), attributeValue) > 1) {
                annotationHolder.createWeakWarningAnnotation(psiElement.getParent(), "This Section 'name' attribute is being used elsewhere.");
            }
        }
    }
}
