package com.magento.completion.contributors;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.xml.XmlToken;
import com.magento.completion.EntityXmlPipeCompletionProvider;

public class EntityXmlPipeCompletionContributor extends CompletionContributor
{
    public EntityXmlPipeCompletionContributor()
    {
        extend(CompletionType.BASIC,
                XmlPatterns.psiElement(XmlToken.class).withLanguage(XMLLanguage.INSTANCE),
                new EntityXmlPipeCompletionProvider());
    }

}
