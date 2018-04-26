package com.magento.resolver;


import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomFileElement;
import com.magento.dom.description.SectionDomDescription;
import com.magento.dom.model.SectionXmlRoot;
import com.magento.dom.model.section.Element;
import com.magento.dom.model.section.Section;

import java.util.*;
import java.util.stream.Collectors;

public class SectionNodeResolver extends BaseNodeResolver<SectionXmlRoot>
{
    private static volatile SectionNodeResolver INSTANCE;
    private final List<Section> SECTION_LIST = new ArrayList<>();
    private static final Logger LOGGER = Logger.getInstance(SectionNodeResolver.class);

    private SectionNodeResolver() {

    }

    public synchronized static SectionNodeResolver getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new SectionNodeResolver();
        }

        return INSTANCE;
    }

    public List<String> getSectionNames()
    {
        updateSectionObjects();
        return SECTION_LIST.stream().filter(obj -> obj.getSectionName() != null).map(section -> section.getSectionName().toString()).collect(Collectors.toList());
    }

    public List<String> getDistinctSectionNames()
    {
        return getSectionNames().stream().distinct().collect(Collectors.toList());
    }

    private void updateSectionObjects()
    {
        SECTION_LIST.clear();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p).stream().filter(Objects::nonNull).filter(file -> file.getParent().isDirectory() && "Section".equals(file.getParent().getName())).collect(Collectors.toList());

        for (VirtualFile virtualFile : virtualFiles) {
            Optional<DomFileElement<SectionXmlRoot>> optElement = Optional.ofNullable(retrieveRootElementFromDom(virtualFile, p, "section", SectionDomDescription.ROOT_TAG, SectionXmlRoot.class));
            optElement.ifPresent(
                    element -> SECTION_LIST.addAll(element.getRootElement().getSections())
            );
        }
    }


    public List<String> getSectionElementNames(String sectionName)
    {
        updateSectionObjects();
        Optional<Section> optionalSection = SECTION_LIST.stream().filter(obj -> obj.getSectionName() != null).filter(section ->
                sectionName.equals(section.getSectionName().toString())).findFirst();

        if (!optionalSection.isPresent()) {
            return Collections.emptyList();
        }

        List<Element> elements = optionalSection.get().getElements();
        return elements.stream().map(element -> sectionName + "." + element.getName().toString()).collect(Collectors.toList());
    }
}
