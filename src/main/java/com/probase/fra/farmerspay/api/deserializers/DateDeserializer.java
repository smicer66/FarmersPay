package com.probase.fra.farmerspay.api.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateDeserializer extends LocalDateDeserializer {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public DateDeserializer() {
        super(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
//        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
//            long value = parser.getValueAsLong();
//            Instant instant = Instant.ofEpochMilli(value);
//
//            return LocalDate.of(instant, ZoneOffset.UTC);
//        }
        logger.info("parser.getText()....{}", parser.getText());

        return super.deserialize(parser, context);
    }

}
