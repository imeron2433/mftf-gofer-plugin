package com.magento.resolver;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xml.DomFileElement;
import com.magento.dom.description.EntityDomDescription;
import com.magento.dom.model.EntityXmlRoot;
import com.magento.dom.model.data.Entity;
import com.magento.dom.model.data.Data;

import java.util.*;
import java.util.stream.Collectors;

public class EntityNodeResolver extends BaseNodeResolver<EntityXmlRoot>
{
    private static final Logger LOGGER = Logger.getInstance(EntityNodeResolver.class);

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

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p);

        for (VirtualFile virtualFile : virtualFiles) {
            DomFileElement<EntityXmlRoot> optElement =
                    retrieveRootElementFromDom(
                            virtualFile,
                            p,
                            "Data",
                            "entity",
                            EntityDomDescription.ROOT_TAG,
                            EntityXmlRoot.class
                    );

            if (optElement != null && optElement.getRootElement().getEntities() != null) {
                entities.addAll(optElement.getRootElement().getEntities());
            }
        }

//        ProjectFileIndex pfi = ProjectFileIndex.SERVICE.getInstance(p);
//        pfi.iterateContent(new ContentIterator() {
//            @Override
//            public boolean processFile(VirtualFile virtualFile)
//            {
//                Optional<DomFileElement<EntityXmlRoot>> optElement =
//                        Optional.ofNullable(
//                                retrieveRootElementFromDom(
//                                        virtualFile,
//                                        p,
//                                        "Data",
//                                        "entity",
//                                        EntityDomDescription.ROOT_TAG,
//                                        EntityXmlRoot.class
//                                )
//                        );
//
//                optElement.ifPresent(
//                        element -> entities.addAll(element.getRootElement().getEntities())
//                );
//
//                return false;
//            }
//        });

        return entities;
    }
}
