package com.magento.annotations;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StepKeyAnnotator implements Annotator
{
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        // get the open file compare with where element is
        Project p = ProjectManager.getInstance().getOpenProjects()[0];
        FileEditorManager manager = FileEditorManager.getInstance(p);
        VirtualFile file = manager.getSelectedFiles()[0];
        if (!file.getName().equals(psiElement.getContainingFile().getName())) {
            return;
        }

        if (psiElement.toString().equals("XmlToken:XML_NAME") && "stepKey".equals(psiElement.getText()))
        {
            List<String> stepKeysInUse = Arrays.stream(psiElement.getParent().getParent().getParent().getChildren())
                    .filter(Objects::nonNull)
                    .filter(psiElement1 -> psiElement1.getText().contains("stepKey"))
                    .map(this::returnStepKeyOrNull)
                    .collect(Collectors.toList());

            String val = returnStepKeyOrNull(psiElement.getParent().getParent());

            if (Collections.frequency(stepKeysInUse, val) > 1) {
                annotationHolder.createErrorAnnotation(psiElement.getParent(), "The 'stepKey' must be unique");

            }
        }
    }

    private String returnStepKeyOrNull(PsiElement psiElement)
    {
        String attributeValue = null;
        if (!psiElement.getClass().getSimpleName().equals("XmlTagImpl")) {
            return null;
        }

        XmlAttribute attribute = ((XmlTagImpl)psiElement).getAttribute("stepKey");
        if (attribute != null) {
            attributeValue = attribute.getValue();
        }

        return attributeValue;
    }
}
