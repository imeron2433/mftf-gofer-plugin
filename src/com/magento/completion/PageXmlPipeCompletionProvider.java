package com.magento.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import com.magento.resolver.PageNodeResolver;
import org.jetbrains.annotations.NotNull;

public class PageXmlPipeCompletionProvider extends BaseXMLPipeCompletionProvider
{
    private static PageNodeResolver PAGE_NODE_RESOLVER = new PageNodeResolver();

    @Override
    protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet)
    {
        String attributeName = getAttributeName(completionParameters);
        if (!"url".equals(attributeName)) {
            return;
        }

        String attributeValue = getAttributeValue(completionParameters);
        if (attributeValue == null) {
            return;
        }

        if (attributeValue.contains(".")) {
            String truncPageName = attributeValue.substring(0, attributeValue.indexOf("."));
            completionResultSet.addElement(LookupElementBuilder.create(truncPageName + ".url"));
        } else {
            addListToCompletionResultSet(PAGE_NODE_RESOLVER.getPageNames(), completionResultSet);
        }

    }
}
