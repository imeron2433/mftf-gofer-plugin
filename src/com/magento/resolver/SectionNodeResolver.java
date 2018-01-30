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
import com.magento.dom.description.SectionDomDescription;
import com.magento.dom.model.SectionXmlRoot;
import com.magento.dom.model.section.Element;
import com.magento.dom.model.section.Section;

import java.util.*;
import java.util.stream.Collectors;

public class SectionNodeResolver
{
    public List<String> getSectionNames()
    {
        return getSectionObjects().stream().map(section -> section.getSectionName().toString()).collect(Collectors.toList());
    }

    public List<String> getDistinctSectionNames()
    {
        return getSectionNames().stream().distinct().collect(Collectors.toList());
    }

    private List<Section> getSectionObjects()
    {
        List<Section> sections = new ArrayList<>();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];
        ContentIterator ci = (ContentIterator) virtualFile -> {
            String ext = virtualFile.getExtension();
            String dirname = virtualFile.getParent().getName();
            if ("xml".equals(ext) && "Section".equals(dirname)) {
                XmlFile myFile = (XmlFile) PsiManager.getInstance(p).findFile(virtualFile);
                XmlTag rootTag = myFile.getDocument().getRootTag();
                XmlTag[] xmlTags = rootTag.findSubTags("section");

                if (xmlTags.length > 0 && Objects.equals(rootTag.getName(), SectionDomDescription.ROOT_TAG)) {
                    DomManager domJohn = DomManager.getDomManager(p);
                    DomFileElement<SectionXmlRoot> element = domJohn.getFileElement(myFile, SectionXmlRoot.class);
                    sections.addAll(element.getRootElement().getSections());
                }
            }

            return true;
        };

        ProjectRootManager.getInstance(p).getFileIndex().iterateContent(ci);

        return sections;
    }


    public List<String> getSectionElementNames(String sectionName)
    {
        Optional<Section> optionalSection = getSectionObjects().stream().filter(section ->
                sectionName.equals(section.getSectionName().toString())).findFirst();

        if (!optionalSection.isPresent()) {
            return Collections.emptyList();
        }

        List<Element> elements = optionalSection.get().getElements();
        return elements.stream().map(element -> sectionName + "." + element.getName().toString()).collect(Collectors.toList());
    }
}
