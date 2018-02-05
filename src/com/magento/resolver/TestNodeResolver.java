package com.magento.resolver;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomFileElement;
import com.magento.dom.description.TestDomDescription;
import com.magento.dom.model.TestXmlRoot;
import com.magento.dom.model.test.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TestNodeResolver extends BaseNodeResolver<TestXmlRoot>
{
    private static volatile TestNodeResolver INSTANCE;
    private final List<Test> TESTS_LIST = new ArrayList<>();;
    private static final Logger LOGGER = Logger.getInstance(TestNodeResolver.class);

    private TestNodeResolver() {

    }

    public synchronized static TestNodeResolver getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new TestNodeResolver();
        }

        return INSTANCE;
    }

    public List<String> getTestNames()
    {
        updateTestObjects();
        return TESTS_LIST.stream().map(test -> test.getName().toString()).collect(Collectors.toList());
    }

    private void updateTestObjects()
    {
        TESTS_LIST.clear();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p).stream().filter(Objects::nonNull).filter(file -> file.getParent().isDirectory() && "Test".equals(file.getParent().getName())).collect(Collectors.toList());;

        for (VirtualFile virtualFile : virtualFiles) {
            Optional<DomFileElement<TestXmlRoot>> optElement = Optional.ofNullable(retrieveRootElementFromDom(virtualFile, p, "test", TestDomDescription.ROOT_TAG, TestXmlRoot.class));

            optElement.ifPresent(
                    element -> TESTS_LIST.addAll(element.getRootElement().getTests())
            );
        }
    }
}
