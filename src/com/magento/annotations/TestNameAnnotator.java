package com.magento.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.magento.resolver.TestNodeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class TestNameAnnotator implements Annotator
{
    public static TestNodeResolver TEST_NODE_RESOLVER = new TestNodeResolver();

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Test")
                && psiElement.getText().equals("test")) {
            XmlAttribute attribute = ((XmlTagImpl)psiElement.getParent()).getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();

            if (Collections.frequency(TEST_NODE_RESOLVER.getTestNames(), attributeValue) > 1) {
                annotationHolder.createErrorAnnotation(attribute.getTextRange(), "The Test 'name' must be unique.");
            }
        }
    }
}
