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
import com.magento.dom.description.EntityDomDescription;
import com.magento.dom.model.EntityXmlRoot;
import com.magento.dom.model.data.Entity;
import com.magento.dom.model.data.Data;

import java.util.*;
import java.util.stream.Collectors;

public class EntityNodeResolver
{
    public List<String> getDistinctEntityNames()
    {
        return getAllEntities().stream().map(entity -> entity.getEntityName().toString()).distinct().collect(Collectors.toList());
    }

    public List<String> getDataEntityFields(String entityName)
    {
        Optional<Entity> optionalEntity = getAllEntities().stream().filter(entity ->
                entityName.equals(entity.getEntityName().toString())
        ).findFirst();

        if (!optionalEntity.isPresent()) {
            return Collections.emptyList();
        }

        List<Data> entityFields = optionalEntity.get().getDatas();
        return entityFields.stream().map(field -> entityName + "." + field.getFieldName().toString()).collect(Collectors.toList());
    }

    public boolean entityNameIsDuplicate(String entityName)
    {
        long occurances = getAllEntities().stream().filter(entity -> entityName.equals(entity.getEntityName().toString())).count();
        return occurances > 1;
    }

    private List<Entity> getAllEntities()
    {
        List<Entity> entities = new ArrayList<>();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        ContentIterator ci = virtualFile -> {
            String ext = virtualFile.getExtension();
            String dirname = virtualFile.getParent().getName();

            if ("xml".equals(ext) && "Data".equals(dirname)) {
                XmlFile myFile = (XmlFile) PsiManager.getInstance(p).findFile(virtualFile);
                XmlTag rootTag = myFile.getDocument().getRootTag();
                XmlTag[] xmlTags = rootTag.findSubTags("entity");

                if (xmlTags.length > 0 && Objects.equals(rootTag.getName(), EntityDomDescription.ROOT_TAG)) {
                    DomManager domJohn = DomManager.getDomManager(p);
                    DomFileElement<EntityXmlRoot> element = domJohn.getFileElement(myFile, EntityXmlRoot.class);
                    entities.addAll(element.getRootElement().getEntities());

                }
            }

            return true;
        };

        ProjectRootManager.getInstance(p).getFileIndex().iterateContent(ci);

        return entities;
    }
}
