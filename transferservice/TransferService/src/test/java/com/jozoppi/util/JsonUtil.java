package com.jozoppi.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    /**
     * Simply we map an object to a JSON string.
     * @param obj object to map
     * @return a JSON serialized string.
     */
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
