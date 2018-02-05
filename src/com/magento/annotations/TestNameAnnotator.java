package com.magento.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.magento.resolver.TestNodeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class TestNameAnnotator implements Annotator
{
    private static final Logger LOGGER = Logger.getInstance(TestNameAnnotator.class);

    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        // get the open file compare with where element is
        Project p = psiElement.getProject();
        FileEditorManager manager = FileEditorManager.getInstance(p);
        VirtualFile[] virtualFiles = manager.getSelectedFiles();


        if (virtualFiles.length == 0 || !virtualFiles[0].getName().equals(psiElement.getContainingFile().getName())) {
            return;
        }

        if (!psiElement.getContainingFile().getContainingDirectory().toString().endsWith("Test") || !"XmlTagImpl".equals(psiElement.getParent().getClass().getSimpleName())) {
            return;
        }

        if ("test".equals(psiElement.getText())) {
            XmlTagImpl parentTag= (XmlTagImpl)psiElement.getParent();
            XmlAttribute attribute = parentTag.getAttribute("name");
            if (attribute == null) {
                return;
            }

            String attributeValue = attribute.getValue();
            TestNodeResolver TEST_NODE_RESOLVER = TestNodeResolver.getInstance();

            if (Collections.frequency(TEST_NODE_RESOLVER.getTestNames(), attributeValue) > 1) {

                annotationHolder.createWeakWarningAnnotation(attribute.getTextRange(), "The Test 'name' is in use elsewhere.");
            }
        }
    }
}
