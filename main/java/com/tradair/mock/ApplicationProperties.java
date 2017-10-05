package com.tradair.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    @Value("${acceptor.file.path:/tradair/config/mock}")
    private String acceptorFilePath;

    public String getAcceptorFilePath() {
        return acceptorFilePath;
    }
}
