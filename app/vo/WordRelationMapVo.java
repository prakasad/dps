package vo;

import com.fasterxml.jackson.databind.JsonNode;
import helper.CustomMapper;

import java.util.HashMap;
import java.util.Map;

public class WordRelationMapVo extends  JsonEntity {

    public Map<String, WordRelationNode> wordRelationNodeMap;

    public WordRelationMapVo() {
        this.wordRelationNodeMap = new HashMap<>();
    }

    public WordRelationMapVo(Map<String, WordRelationNode> wordRelationNodeMap) {
        this.wordRelationNodeMap = wordRelationNodeMap;
    }

    public Map<String, WordRelationNode> getWordRelationNodeMap() {
        return wordRelationNodeMap;
    }

    @Override
    public JsonNode toJsonNode() {
        return CustomMapper.apiMapper.valueToTree(this);
    }
}
