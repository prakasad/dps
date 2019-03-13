package vo;

import com.fasterxml.jackson.databind.JsonNode;
import helper.CustomMapper;

public class ParentRelNode extends JsonEntity{

    // TODO can be ENUM but stansFord itself hasn't created on for the same.
    // short name of GrammerRelationClass
    public String parentRelation;

    public ParentRelNode() {
    }

    public ParentRelNode( String parentRelation) {
        this.parentRelation = parentRelation;
    }

    @Override
    public JsonNode toJsonNode() {

        return CustomMapper.apiMapper.valueToTree(this);
    }
}
