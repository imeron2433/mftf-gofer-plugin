package com.magento.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.magento.resolver.TestNodeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class TestNameAnnotator implements Annotator
{
//    private TestNodeResolver TEST_NODE_RESOLVER = new TestNodeResolver();
    private static final Logger LOGGER = Logger.getInstance(TestNameAnnotator.class);

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {

        TestNodeResolver TEST_NODE_RESOLVER = new TestNodeResolver();

        if (!psiElement.getParent().getClass().getSimpleName().equals("XmlTagImpl")) {
            return;
        }

        if (psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Test") && psiElement.getText().equals("test")) {
            XmlTagImpl parentTag= (XmlTagImpl)psiElement.getParent();
            LOGGER.info("Element Type is: " + psiElement.getParent().getClass().getSimpleName());
            XmlAttribute attribute = parentTag.getAttribute("name");
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
