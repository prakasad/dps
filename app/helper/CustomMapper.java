package helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomMapper extends ObjectMapper{
    private static final long serialVersionUID = 8984109124860430227L;

    public static CustomMapper apiMapper = new CustomMapper(false);

    public CustomMapper(boolean failOnUnknown) {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,failOnUnknown );

    }

}
