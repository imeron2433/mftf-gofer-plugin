package com.magento.resolver;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

abstract class BaseNodeResolver<T extends DomElement>
{
    @Nullable
    DomFileElement<T> retrieveRootElementFromDom(VirtualFile virtualFile, Project p, String subTag, String domRootTag, Class<T> objectClass)
    {
        if (virtualFile.getName().contains("SampleTests") || virtualFile.getName().contains("SampleTemplates")) {
            return null;
        }

        PsiFile psiFile = PsiManager.getInstance(p).findFile(virtualFile);

        if (psiFile != null && "XML".equals(psiFile.getFileType().getName())) {
            XmlFile myFile = (XmlFile) psiFile;
            Optional<XmlDocument> document = Optional.ofNullable(myFile.getDocument());
            Optional<XmlTag> rootTag = document.map(XmlDocument::getRootTag);
            Optional<XmlTag[]> xmlTags = rootTag.map(tag -> tag.findSubTags(subTag));

            if (!rootTag.isPresent() || !xmlTags.isPresent()) {
                return null;
            }

            if (xmlTags.get().length > 0 && Objects.equals(rootTag.get().getName(), domRootTag)) {
                DomManager domJohn = DomManager.getDomManager(p);
                return domJohn.getFileElement(myFile, objectClass);

            }
        }

        return null;
    }

    Collection<VirtualFile> getXmlVirtualFiles(Project p)
    {
        return FileBasedIndex.getInstance().getContainingFiles(
                FileTypeIndex.NAME,
                XmlFileType.INSTANCE,
                GlobalSearchScope.allScope(p)

        );
    }
}
