package com.tradair.mock.fix;

import com.tradair.mock.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quickfix.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FixInitializerService {

    private final static Logger logger = LoggerFactory.getLogger(FixInitializerService.class);

    @Autowired
    private Application acceptor;
    @Autowired
    private ApplicationProperties applicationProperties;

    @PostConstruct
    public void start() {
        List<InputStream> configFiles = getAcceptorFiles();
        for (InputStream inputStream : configFiles) {
            SessionSettings sessionSettings = parseSessionSettingsFromFile(inputStream);
            quickfix.Acceptor acceptorSocket = initiateSocket(sessionSettings);
            startAcceptor(acceptorSocket, sessionSettings.getDefaultProperties().getProperty("BeginString"));
        }
    }

    private void startAcceptor(quickfix.Acceptor socket, String beginString) {
        try {
            socket.start();
            logger.info("Start Acceptor " + beginString + " is listening...");
        } catch (ConfigError | RuntimeError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initiates a fix socket from the input session settings
     * @param sessionSettings the settings for the fix server
     * @return
     */
    private quickfix.Acceptor initiateSocket(SessionSettings sessionSettings) {
        ThreadedSocketAcceptor socketAcceptor = null;
        try {
            MessageFactory messageFactory = new DefaultMessageFactory();
            FileLogFactory fileLogFactory = new FileLogFactory(sessionSettings);
            socketAcceptor = new ThreadedSocketAcceptor(acceptor, new MemoryStoreFactory(), sessionSettings, fileLogFactory, messageFactory);
        }
        catch (ConfigError e) {
            throw new RuntimeException(e);
        }
        return socketAcceptor;
    }

    /**
     * Gets the fix server config input stream
     * @return input stream of the fix server configurations
     */
    private List<InputStream> getAcceptorFiles() {
        List<InputStream> result = new ArrayList<>();
        File acceptorDir = new File(applicationProperties.getAcceptorFilePath());
        if (acceptorDir.isDirectory()) {
            try {
                File[] subDirectories = acceptorDir.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".cfg");
                    }
                });
                for (File file : subDirectories) {
                    logger.info("parsing cfg file " + file.getName());
                    FileInputStream inputStream = new FileInputStream(file);
                    result.add(inputStream);
                }

            }
            catch (FileNotFoundException e) {
                throw new RuntimeException("couldnt parse .cfg file");
            }

        }
        return result;
    }

    /**
     * parses the input stream into a configuration object of the fix server
     * @param inputStream the input stream representing a configuration file
     * @return configuration object of the fix server
     */
    private SessionSettings parseSessionSettingsFromFile(InputStream inputStream) {
        try {
            SessionSettings sessionSettings = new SessionSettings(inputStream);
            return sessionSettings;
        }
        catch (ConfigError e) {
            throw new RuntimeException(e);
        }
    }
}
