package com.magento.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;

import java.util.List;

public abstract class BaseXMLPipeCompletionProvider extends CompletionProvider<CompletionParameters>
{

    private static final String XML_ATTRIBUTE_VALUE_TOKEN = "XmlToken:XML_ATTRIBUTE_VALUE_TOKEN";
    private static final String DEFAULT_FIELD_TEXT = "IntellijIdeaRulezzz ";

    protected String getAttributeValue(CompletionParameters completionParameters)
    {
        if (completionParameters.getPosition().toString().equals(XML_ATTRIBUTE_VALUE_TOKEN)) {
            String textValue = completionParameters.getPosition().getParent().getParent().getText();
            String attributeValue = textValue.substring(
                    textValue.indexOf("=") + 2,
                    textValue.length() - 1
            ).replaceAll(DEFAULT_FIELD_TEXT, "");

            if (attributeValue.contains("{{")) {
                return attributeValue.replaceAll("\\{", "").replaceAll("}", "");
            }
        }

        return null;
    }

    protected String getAttributeName(CompletionParameters completionParameters)
    {
        if (completionParameters.getPosition().toString().equals(XML_ATTRIBUTE_VALUE_TOKEN)) {
            String textValue = completionParameters.getPosition().getParent().getParent().getText();
            return textValue.substring(0, textValue.indexOf("="));
        }

        return null;
    }

    protected void addListToCompletionResultSet(List<String> elements, CompletionResultSet completionResultSet)
    {
        for (String field : elements) {
            completionResultSet.addElement(LookupElementBuilder.create(field));
        }
    }
}
