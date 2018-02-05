package com.magento.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import com.magento.resolver.SectionNodeResolver;
import org.jetbrains.annotations.NotNull;

public class SectionXmlPipeCompletionProvider extends BaseXMLPipeCompletionProvider
{
//    private SectionNodeResolver SECTION_NODE_RESOLVER = new SectionNodeResolver();

    @Override
    protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet)
    {
        SectionNodeResolver SECTION_NODE_RESOLVER = SectionNodeResolver.getInstance();

        String attributeName = getAttributeName(completionParameters);
        if (!"selector".equals(attributeName)) {
            return;
        }

        String attributeValue = getAttributeValue(completionParameters);
        if (attributeValue == null) {
            return;
        }

        if (attributeValue.contains(".")) {
            String truncSectionName = attributeValue.substring(0, attributeValue.indexOf("."));
            addListToCompletionResultSet(SECTION_NODE_RESOLVER.getSectionElementNames(truncSectionName), completionResultSet);
        } else {
            addListToCompletionResultSet(SECTION_NODE_RESOLVER.getDistinctSectionNames(), completionResultSet);
        }
    }
}
