package com.swapping.productservice.configuration.rabbitmq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomJackson2JsonMessageConverter extends Jackson2JsonMessageConverter {

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        MessageProperties messageProperties = message.getMessageProperties();
        if (Objects.nonNull(messageProperties) && StringUtils.isBlank(messageProperties.getContentType())) {
            messageProperties.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
        return super.fromMessage(message);
    }
}