package com.magento.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.xml.XmlAttribute;
import org.jetbrains.annotations.NotNull;

class MagentoPsiReferenceContributor extends PsiReferenceContributor
{

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar psiReferenceRegistrar)
    {
        psiReferenceRegistrar.registerReferenceProvider(PlatformPatterns.psiElement(XmlAttribute.class), new MagentoPsiReferenceProvider());
    }
}
