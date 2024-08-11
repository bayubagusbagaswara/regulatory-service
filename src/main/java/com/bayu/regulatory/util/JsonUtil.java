package com.bayu.regulatory.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.Map;

@UtilityClass
public class JsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static String cleanedEntityDataFromApprovalData(String jsonDataFull) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonDataFull);

        ((ObjectNode) jsonNode).remove("approvalStatus");
        ((ObjectNode) jsonNode).remove("approveId");
        ((ObjectNode) jsonNode).remove("approveDate");
        ((ObjectNode) jsonNode).remove("approveIPAddress");
        ((ObjectNode) jsonNode).remove("inputId");
        ((ObjectNode) jsonNode).remove("inputDate");
        ((ObjectNode) jsonNode).remove("inputIPAddress");

        return objectMapper.writeValueAsString(jsonNode);
    }

    public static String cleanedId(String jsonDataFull) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonDataFull);

        ((ObjectNode) jsonNode).remove("id");
        return objectMapper.writeValueAsString(jsonNode);
    }

    public static String cleanedJsonData(String jsonDataFull) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonDataFull);

        ((ObjectNode) jsonNode).remove("dataChangeId");
        ((ObjectNode) jsonNode).remove("approvalStatus");
        ((ObjectNode) jsonNode).remove("inputerId");
        ((ObjectNode) jsonNode).remove("inputIPAddress");

        return objectMapper.writeValueAsString(jsonNode);
    }

    public static String cleanedJsonDataUpdate(String jsonDataFull) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(jsonDataFull);

        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = getEntryIterator((ObjectNode) jsonNode);
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                if (entry.getValue().isTextual() && entry.getValue().asText().isEmpty()) {
                    fields.remove();
                }
            }
        }
        return objectMapper.writeValueAsString(jsonNode);
    }

    private static Iterator<Map.Entry<String, JsonNode>> getEntryIterator(ObjectNode jsonNode) {
        // Remove the "id" property
        jsonNode.remove("id");

        // Remove properties with empty string values
        return jsonNode.fields();
    }


}
