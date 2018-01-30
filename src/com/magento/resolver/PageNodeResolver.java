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
import com.magento.dom.description.PageDomDescription;
import com.magento.dom.model.PageXmlRoot;
import com.magento.dom.model.page.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PageNodeResolver
{
    public List<String> getPageNames()
    {
        List<String> pages = new ArrayList<>();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        ContentIterator ci = (ContentIterator) virtualFile -> {
            String ext = virtualFile.getExtension();
            String dirname = virtualFile.getParent().getName();

            if ("xml".equals(ext) && "Page".equals(dirname)) {
                XmlFile myFile = (XmlFile) PsiManager.getInstance(p).findFile(virtualFile);
                XmlTag rootTag = myFile.getDocument().getRootTag();
                XmlTag[] xmlTags = rootTag.findSubTags("page");

                if (xmlTags.length > 0 && Objects.equals(rootTag.getName(), PageDomDescription.ROOT_TAG)) {
                    DomManager domJohn = DomManager.getDomManager(p);
                    DomFileElement<PageXmlRoot> element = domJohn.getFileElement(myFile, PageXmlRoot.class);
                    List<Page> myPages = element.getRootElement().getPages();
                    pages.addAll(myPages.stream().map(page -> page.getName().toString()).collect(Collectors.toList()));
                }
            }

            return true;
        };

        ProjectRootManager.getInstance(p).getFileIndex().iterateContent(ci);

        return pages;
    }
}
