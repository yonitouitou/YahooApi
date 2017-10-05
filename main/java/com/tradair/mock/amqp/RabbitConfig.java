package com.tradair.mock.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    protected static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    public static final String FIXGATEWAY_EXCHANGE_NAME = "YAHOO";

    public CachingConnectionFactory fixGatewayConnectionFactory() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setUsername("admin");
        ccf.setPassword("Panda230");
        ccf.setHost("sys-bus-uat-p.tradair.com");
        ccf.setPort(5672);
        return ccf;
    }

    public RabbitAdmin RabbitAdmin() {
        return new RabbitAdmin(fixGatewayConnectionFactory());
    }

    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}

