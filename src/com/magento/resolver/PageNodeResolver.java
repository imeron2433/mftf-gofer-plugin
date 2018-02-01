package com.magento.resolver;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomFileElement;
import com.magento.dom.description.PageDomDescription;
import com.magento.dom.model.PageXmlRoot;
import com.magento.dom.model.page.Page;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PageNodeResolver extends BaseNodeResolver<PageXmlRoot>
{
    private static final Logger LOGGER = Logger.getInstance(PageNodeResolver.class);

    public List<String> getPageNames()
    {
        List<String> pages = new ArrayList<>();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p);

        for (VirtualFile virtualFile : virtualFiles) {
            Optional<DomFileElement<PageXmlRoot>> optElement =
                    Optional.ofNullable(
                            retrieveRootElementFromDom(
                                    virtualFile,
                                    p,
                                    "Page",
                                    "page",
                                    PageDomDescription.ROOT_TAG,
                                    PageXmlRoot.class
                            )
                    );

            optElement.ifPresent(
                    element -> {
                        List<Page> myPages = element.getRootElement().getPages();
                        pages.addAll(myPages.stream().map(page -> page.getName().toString()).collect(Collectors.toList()));
                    });
        }

//        ProjectFileIndex pfi = ProjectFileIndex.SERVICE.getInstance(p);
//        pfi.iterateContent(new ContentIterator() {
//
//            @Override
//            public boolean processFile(VirtualFile virtualFile)
//            {
//                Optional<DomFileElement<PageXmlRoot>> optElement =
//                        Optional.ofNullable(
//                                retrieveRootElementFromDom(
//                                        virtualFile,
//                                        p,
//                                        "Page",
//                                        "page",
//                                        PageDomDescription.ROOT_TAG,
//                                        PageXmlRoot.class
//                                )
//                        );
//
//                optElement.ifPresent(
//                        element -> {
//                            List<Page> myPages = element.getRootElement().getPages();
//                            pages.addAll(myPages.stream().map(page -> page.getName().toString()).collect(Collectors.toList()));
//                        });
//                return false;
//            }
//        });

        return pages;
    }
}
