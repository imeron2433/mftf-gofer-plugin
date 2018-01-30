package com.magento.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.magento.resolver.PageNodeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class PageNameAnnotator implements Annotator
{
    private static PageNodeResolver PAGE_NODE_RESOLVER = new PageNodeResolver();


    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Page")
                && psiElement.getText().equals("page")) {
            XmlAttribute attribute = ((XmlTagImpl)psiElement.getParent()).getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();

            if (Collections.frequency(PAGE_NODE_RESOLVER.getPageNames(), attributeValue) > 1) {
                annotationHolder.createErrorAnnotation(psiElement.getParent(), "The Page 'name' attribute must be unique.");
            }
        }
    }
}
