package com.magento.template;


import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MagentoTemplateLoader implements StartupActivity
{
    private static final String TEMPLATES_RELATIVE_PATH = "/fileTemplates/xml";

    @Override
    public void runActivity(@NotNull Project project) {
        final FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(project);

        String fileTemplatesDir = this.getClass().getResource(TEMPLATES_RELATIVE_PATH).getPath();

        // loop through dir to see available templates and extract names
        File templateDir = new File(fileTemplatesDir); // TODO determine path to template dirs
        if (templateDir.listFiles() == null) {
            return;
        }

        // see if available templates already exist
        List<String> existingTemplateNames = Arrays.stream(fileTemplateManager.getAllCodeTemplates()).map(FileTemplate::getName).collect(Collectors.toList());

        // add only templates that do not
        Map<String, String> missingTemplateNamesToTemplatePath = Arrays.stream(templateDir.listFiles()).filter(File::isFile).filter(file -> !existingTemplateNames.contains(file.getName())).collect(Collectors.toMap(File::getName, File::getPath));

        // create missing templates
        for (Map.Entry<String, String> missingTemplateToPath : missingTemplateNamesToTemplatePath.entrySet()) {
            try {
                String templateName = missingTemplateToPath.getKey().replaceAll(".ft", "");
                FileTemplate fileTemplate = fileTemplateManager.addTemplate(templateName, "xml");
                byte[] encoded = Files.readAllBytes(Paths.get(missingTemplateToPath.getValue()));
                String text = new String(encoded, "UTF-8");
                fileTemplate.setText(text);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
