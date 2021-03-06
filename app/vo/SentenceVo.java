package vo;

import com.fasterxml.jackson.databind.JsonNode;
import helper.CustomMapper;

public class SentenceVo extends JsonEntity {

    public SentenceVo(String sentence) {
        this.sentence = sentence;
    }

    public String sentence;

    public SentenceVo() {
    }


    public String getSentence() {
        return sentence;
    }

    @Override
    public JsonNode toJsonNode() {
        return CustomMapper.apiMapper.valueToTree(this);
    }
}
