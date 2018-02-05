package com.magento.resolver;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
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
    private static volatile EntityNodeResolver INSTANCE;
    private final List<Entity> ENTITY_LIST = new ArrayList<>();
    private static final Logger LOGGER = Logger.getInstance(EntityNodeResolver.class);

    private EntityNodeResolver() {

    }

    public synchronized static EntityNodeResolver getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new EntityNodeResolver();
        }

        return INSTANCE;
    }

    public List<String> getDistinctEntityNames()
    {
        updateEntityList();
        return ENTITY_LIST.stream().filter(obj -> obj.getEntityName() != null).map(entity -> entity.getEntityName().toString()).distinct().collect(Collectors.toList());
    }

    public List<String> getDataEntityFields(String entityName)
    {
        updateEntityList();
        Optional<Entity> optionalEntity = ENTITY_LIST.stream().filter(obj -> obj.getEntityName() != null).filter(entity ->
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
        updateEntityList();
        long occurances = ENTITY_LIST.stream().filter(obj -> obj.getEntityName() != null).filter(entity -> entityName.equals(entity.getEntityName().toString())).count();
        return occurances > 1;
    }

    private void updateEntityList()
    {
        ENTITY_LIST.clear();
        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        LOGGER.debug("Iterating through project: " + p.getName());

        Collection<VirtualFile> virtualFiles = getXmlVirtualFiles(p).stream().filter(Objects::nonNull).filter(file -> file.getParent().isDirectory() && "Data".equals(file.getParent().getName())).collect(Collectors.toList());;

        for (VirtualFile virtualFile : virtualFiles) {
            DomFileElement<EntityXmlRoot> optElement = retrieveRootElementFromDom(virtualFile, p, "entity", EntityDomDescription.ROOT_TAG, EntityXmlRoot.class);

            if (optElement != null && optElement.getRootElement().getEntities() != null) {
                ENTITY_LIST.addAll(optElement.getRootElement().getEntities());
            }
        }
    }
}
