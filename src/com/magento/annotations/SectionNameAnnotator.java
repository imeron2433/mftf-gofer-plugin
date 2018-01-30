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
    private static SectionNodeResolver SECTION_NODE_RESOLVER = new SectionNodeResolver();

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Section")
                && psiElement.getText().equals("section")) {
            XmlAttribute attribute = ((XmlTagImpl)psiElement.getParent()).getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();

            if (Collections.frequency(SECTION_NODE_RESOLVER.getSectionNames(), attributeValue) > 1) {
                annotationHolder.createErrorAnnotation(psiElement.getParent(), "The Section 'name' attribute must be unique.");
            }
        }
    }
}
