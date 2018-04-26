package com.magento.resolver;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomFileElement;
import com.magento.dom.description.PageDomDescription;
import com.magento.dom.model.PageXmlRoot;
import com.magento.dom.model.page.Page;

import java.util.*;
import java.util.stream.Collectors;

public class PageNodeResolver extends BaseNodeResolver<PageXmlRoot>
{
    private static volatile PageNodeResolver INSTANCE;
    private final List<String> PAGES_LIST = new ArrayList<>();
    private static final Logger LOGGER = Logger.getInstance(PageNodeResolver.class);

    private PageNodeResolver() {

    }

    public synchronized static PageNodeResolver getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new PageNodeResolver();
        }

        return INSTANCE;
    }

    public List<String> getPageNames()
    {
        updatePageNames();
        return PAGES_LIST;
    }

    private void updatePageNames()
    {
        PAGES_LIST.clear();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p).stream().filter(Objects::nonNull).filter(file -> file.getParent().isDirectory() && "Page".equals(file.getParent().getName())).collect(Collectors.toList());

        for (VirtualFile virtualFile : virtualFiles) {
            Optional<DomFileElement<PageXmlRoot>> optElement = Optional.ofNullable(retrieveRootElementFromDom(virtualFile, p, "page", PageDomDescription.ROOT_TAG, PageXmlRoot.class));
            optElement.ifPresent(element -> {
                List<Page> myPages = element.getRootElement().getPages();
                PAGES_LIST.addAll(myPages.stream().filter(Objects::nonNull).filter(obj -> obj.getName() != null).map(page -> page.getName().toString()).collect(Collectors.toList()));
            });
        }
    }
}
