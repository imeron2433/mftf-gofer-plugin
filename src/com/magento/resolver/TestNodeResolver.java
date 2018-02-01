package com.magento.resolver;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.xml.DomFileElement;
import com.magento.dom.description.TestDomDescription;
import com.magento.dom.model.TestXmlRoot;
import com.magento.dom.model.test.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestNodeResolver extends BaseNodeResolver<TestXmlRoot>
{
    private static final Logger LOGGER = Logger.getInstance(TestNodeResolver.class);

    public List<String> getTestNames()
    {
        return getTestObjects().stream().map(test -> test.getName().toString()).collect(Collectors.toList());
    }

    private List<Test> getTestObjects()
    {
        List<Test> tests = new ArrayList<>();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p);

        for (VirtualFile virtualFile : virtualFiles) {
            Optional<DomFileElement<TestXmlRoot>> optElement =
                    Optional.ofNullable(
                            retrieveRootElementFromDom(
                                    virtualFile,
                                    p,
                                    "Test",
                                    "test",
                                    TestDomDescription.ROOT_TAG,
                                    TestXmlRoot.class
                            )
                    );

            optElement.ifPresent(
                    element -> tests.addAll(element.getRootElement().getTests())
            );
        }

//        ProjectFileIndex pfi = ProjectFileIndex.SERVICE.getInstance(p);
//
//        pfi.iterateContent(new ContentIterator() {
//            @Override
//            public boolean processFile(VirtualFile virtualFile)
//            {
//                Optional<DomFileElement<TestXmlRoot>> optElement =
//                        Optional.ofNullable(
//                                retrieveRootElementFromDom(
//                                        virtualFile,
//                                        p,
//                                        "Test",
//                                        "test",
//                                        TestDomDescription.ROOT_TAG,
//                                        TestXmlRoot.class
//                                )
//                        );
//
//                optElement.ifPresent(
//                        element -> tests.addAll(element.getRootElement().getTests())
//                );
//
//                return false;
//            }
//        });

        return tests;
    }
}
