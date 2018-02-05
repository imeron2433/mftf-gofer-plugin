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
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Page")
                && "page".equals(psiElement.getText())) {
            XmlAttribute attribute = ((XmlTagImpl)psiElement.getParent()).getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();
            PageNodeResolver PAGE_NODE_RESOLVER = PageNodeResolver.getInstance();

            if (Collections.frequency(PAGE_NODE_RESOLVER.getPageNames(), attributeValue) > 1) {
                annotationHolder.createWeakWarningAnnotation(psiElement.getParent(), "This Page 'name' attribute is in use elsewhere.");
            }
        }
    }
}
