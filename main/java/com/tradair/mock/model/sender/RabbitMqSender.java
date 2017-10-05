package com.tradair.mock.model.sender;

import com.tradair.common.serialization.AbstractSerializer;
import com.tradair.common.serialization.kryo.KryoSerializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitMqSender extends Sender<RabbitTemplate> {

    protected AbstractSerializer serializer = new KryoSerializer();
    protected static final MessageProperties msgProperties = new MessageProperties();
    static {
        msgProperties.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
    }

    public RabbitMqSender(String externalId, RabbitTemplate payload) {
        super(externalId, payload);
    }

    @Override
    public <U> boolean send(U msg) {
        Message messageToSend = MessageBuilder.withBody((byte[]) msg).andProperties(msgProperties).build();
        payload.send("YAHOO", "market", messageToSend);
        return true;
    }
}
