package vo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonEntity {

    protected static ObjectMapper mapper = new ObjectMapper();

    public abstract JsonNode toJsonNode();

    public String toJson() {
        return toJsonNode().toString();
    }

}