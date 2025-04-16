package com.rrtyui.filestorage.util;

import lombok.experimental.UtilityClass;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@UtilityClass
public class Mapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
