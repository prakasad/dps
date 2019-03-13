package vo;

import com.fasterxml.jackson.databind.JsonNode;
import helper.CustomMapper;

import java.util.HashSet;
import java.util.Set;

public class WordSentenceIdReverseMapVo extends JsonEntity {

    // This entire process can be pushed to ES , where we can get the unique sentence where the words are mapped to.
    // As the assignment only suggest use of Nosql / Relationl DB this rudimentary method is being used.
    public String tokenizedWord;

    public Set<String> sentenceIds;

    public WordSentenceIdReverseMapVo() {
        sentenceIds = new HashSet<>();
    }

    public WordSentenceIdReverseMapVo(String tokenizedWord) {
        this.tokenizedWord = tokenizedWord;
        this.sentenceIds = new HashSet<>();
    }

    public Set<String> getSentenceIds() {
        return sentenceIds;
    }

    public void setSentenceIds(Set<String> sentenceIds) {
        this.sentenceIds = sentenceIds;
    }

    @Override
    public JsonNode toJsonNode() {
        return CustomMapper.apiMapper.valueToTree(this);
    }
}
