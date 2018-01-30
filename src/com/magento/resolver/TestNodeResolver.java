package com.magento.resolver;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.magento.dom.description.TestDomDescription;
import com.magento.dom.model.TestXmlRoot;
import com.magento.dom.model.test.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestNodeResolver
{
    public List<String> getTestNames()
    {
        return getTestObjects().stream().map(test -> test.getName().toString()).collect(Collectors.toList());
    }

    private List<Test> getTestObjects()
    {
        List<Test> tests = new ArrayList<>();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        ContentIterator ci = (ContentIterator) virtualFile -> {
            String ext = virtualFile.getExtension();
            String dirname = virtualFile.getParent().getName();
            if ("xml".equals(ext) && "Test".equals(dirname)) {
                XmlFile myFile = (XmlFile) PsiManager.getInstance(p).findFile(virtualFile);
                XmlTag rootTag = myFile.getDocument().getRootTag();
                XmlTag[] xmlTags = rootTag.findSubTags("test");

                if (xmlTags.length > 0 && Objects.equals(rootTag.getName(), TestDomDescription.ROOT_TAG)) {
                    DomManager domJohn = DomManager.getDomManager(p);
                    DomFileElement<TestXmlRoot> element = domJohn.getFileElement(myFile, TestXmlRoot.class);
                    tests.addAll(element.getRootElement().getTests());
                }
            }

            return true;
        };

        ProjectRootManager.getInstance(p).getFileIndex().iterateContent(ci);

        return tests;
    }
}
