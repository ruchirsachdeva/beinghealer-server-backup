package com.beinghealer.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by SBI on 4/2/2018.
 */
@Getter
@Setter
public class EmailTemplate {

    private String templateId;

    private String template;

    private Map<String, String> replacementParams;


    public EmailTemplate(String templateId) {
        this.templateId = templateId;
        try {
            this.template = loadTemplate(templateId);
        } catch (Exception e) {
            this.template = "";
        }
    }

    private String loadTemplate(String templateId) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("email-templates/" + templateId).getFile());
        String content = "";
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new Exception("Could not read template with ID = " + templateId);
        }
        return content;
    }

    public String getTemplate(Map<String, String> replacements) {
        String cTemplate = this.getTemplate();

        if (!ObjectUtils.isEmpty(cTemplate)) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        return cTemplate;
    }
    //getters and setters
}