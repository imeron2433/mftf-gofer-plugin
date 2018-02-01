package com.magento.resolver;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
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
    private static final Logger LOGGER = Logger.getInstance(SectionNodeResolver.class);

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

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p);

        for (VirtualFile virtualFile : virtualFiles) {
            Optional<DomFileElement<SectionXmlRoot>> optElement =
                    Optional.ofNullable(
                            retrieveRootElementFromDom(
                                    virtualFile,
                                    p,
                                    "Section",
                                    "section",
                                    SectionDomDescription.ROOT_TAG,
                                    SectionXmlRoot.class
                            )
                    );

            optElement.ifPresent(
                    element -> sections.addAll(element.getRootElement().getSections())
            );
        }


//        ProjectFileIndex pfi = ProjectFileIndex.SERVICE.getInstance(p);
//        pfi.iterateContent(new ContentIterator() {
//            @Override
//            public boolean processFile(VirtualFile virtualFile)
//            {
//                Optional<DomFileElement<SectionXmlRoot>> optElement =
//                        Optional.ofNullable(
//                                retrieveRootElementFromDom(
//                                        virtualFile,
//                                        p,
//                                        "Section",
//                                        "section",
//                                        SectionDomDescription.ROOT_TAG,
//                                        SectionXmlRoot.class
//                                )
//                        );
//
//                optElement.ifPresent(
//                        element -> sections.addAll(element.getRootElement().getSections())
//                );
//
//                return false;
//            }
//        });

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
