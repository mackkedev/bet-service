package com.mk.bettinggenerator.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapDeserializer extends JsonDeserializer<Map<Integer, String>> {

    @Override
    public Map<Integer, String> deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        Map<Integer, String> predictions = new HashMap<>();

        // Move to the start of the nested object since we'll stand on predictions at first
        jsonParser.nextToken();

        // Iterate through the nested object fields
        while (jsonParser.nextToken() != null && jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
            String key = jsonParser.getCurrentName();
            String value = jsonParser.getValueAsString();
            predictions.put(Integer.parseInt(key), value);
            jsonParser.nextToken(); // Move to the value
        }

        return predictions;
    }
}
