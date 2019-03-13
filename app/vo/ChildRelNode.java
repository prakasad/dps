package vo;

import com.fasterxml.jackson.databind.JsonNode;
import helper.CustomMapper;

public class ChildRelNode extends JsonEntity {
    public String actualWord;

    // This will be always lowercase.
    public String tokenziedWord;


    // TODO can be ENUM but stansFord itself hasn't created on for the same.
    // short name of GrammerRelationClass
    public String childRelation;

    public ChildRelNode() {
    }

    public ChildRelNode(String actualWord, String tokenziedWord, String childRelation) {
        this.actualWord = actualWord;
        this.tokenziedWord = tokenziedWord;
        this.childRelation = childRelation;
    }

    public ChildRelNode(String actualWord, String tokenziedWord) {
        this.actualWord = actualWord;
        this.tokenziedWord = tokenziedWord;
        this.childRelation = "**decendent**";
    }

    @Override
    public JsonNode toJsonNode() {

        return CustomMapper.apiMapper.valueToTree(this);
    }
}
