package com.magento.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import com.magento.resolver.EntityNodeResolver;
import org.jetbrains.annotations.NotNull;

public class EntityXmlPipeCompletionProvider extends BaseXMLPipeCompletionProvider
{
    @Override
    protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet)
    {
        EntityNodeResolver ENTITY_NODE_RESOLVER = EntityNodeResolver.getInstance();

        String attributeName = getAttributeName(completionParameters);
        if (!"userInput".equals(attributeName)) {
            return;
        }

        String attributeValue = getAttributeValue(completionParameters);
        if (attributeValue == null) {
            return;
        }

        if (attributeValue.contains(".")) {
            String truncEntityName = attributeValue.substring(0, attributeValue.indexOf("."));
            addListToCompletionResultSet(ENTITY_NODE_RESOLVER.getDataEntityFields(truncEntityName), completionResultSet);
        } else {
            addListToCompletionResultSet(ENTITY_NODE_RESOLVER.getDistinctEntityNames(), completionResultSet);
        }
    }
}
