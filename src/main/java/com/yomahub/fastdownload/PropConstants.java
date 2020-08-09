package com.yomahub.fastdownload;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "fastdownload", ignoreUnknownFields = true)
@Component
public class PropConstants {

    private String filePoolPath;

    public String getFilePoolPath() {
        return filePoolPath;
    }

    public void setFilePoolPath(String filePoolPath) {
        this.filePoolPath = filePoolPath;
    }
}
