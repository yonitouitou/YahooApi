package com.tradair.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.Properties;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final static String APPLICATION_PROPERTIES_FILE = System.getenv().get("CONF_DIR") + File.separator + "yahooApi.properties";

    public static void main(String[] args) {

        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(Application.class)
                .sources(Application.class)
                .properties(getProperties());

        SpringApplication app = springApplicationBuilder.build();
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);

        logger.info("\n ================================================================================"
                + "\n ========================= YahooApi Started succesfully ========================="
                + "\n ================================================================================ \n");
    }

    static Properties getProperties() {
        Properties props = new Properties();
        props.put("spring.config.location", APPLICATION_PROPERTIES_FILE);
        return props;
    }
}
